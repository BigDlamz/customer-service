package za.co.philani.customer.exception;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String INTERNAL_ERROR_REASON_PHRASE = HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase();
    private static final String NOT_FOUND_REASON_PHRASE = HttpStatus.NOT_FOUND.getReasonPhrase();
    private static final String BAD_REQUEST_REASON_PHRASE = HttpStatus.BAD_REQUEST.getReasonPhrase();

    @SneakyThrows
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException var, HttpHeaders headers, HttpStatus status, WebRequest req) {

        log.info("Invalid arguments provided {} {}", var.getMessage(), var);
        URI uri = new URI(((ServletWebRequest) req).getRequest().getRequestURI());
        val message = toFriendlyMsg(var.getBindingResult().getAllErrors());

        val problem = Problem.builder()
                .instance(uri)
                .title(BAD_REQUEST_REASON_PHRASE)
                .status(status.value())
                .detail(message)
                .build();

        return ResponseEntity.badRequest()
                .body(problem);

    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<Object> handleIllegalArgument(Exception var, WebRequest req)
            throws URISyntaxException {

        log.info("Illegal argument while processing request {} {}", var.getMessage(), var);
        URI uri = new URI(((ServletWebRequest) req).getRequest().getRequestURI());

        val problem = Problem
                .builder()
                .instance(uri)
                .title(BAD_REQUEST_REASON_PHRASE)
                .status(400)
                .detail(var.getMessage())
                .build();

        return ResponseEntity
                .badRequest()
                .body(problem);
    }

    @ExceptionHandler({CustomerNotFound.class})
    public ResponseEntity<Object> handleCustomerNotFound(Exception var, WebRequest req)
            throws URISyntaxException {

        log.info("Requested customer information not found {} {}", var.getMessage(), var);
        return reply(var, (ServletWebRequest) req, 404, NOT_FOUND_REASON_PHRASE);

    }

    @ExceptionHandler({CustomerServiceException.class})
    public ResponseEntity<Object> handleCustomerServiceException(Exception var, WebRequest req)
            throws URISyntaxException {

        log.info("There was an internal problem processing your request {} {}", var.getMessage(), var);
        return reply(var, (ServletWebRequest) req, 500, NOT_FOUND_REASON_PHRASE);

    }

    private static ResponseEntity<Object> reply(Exception var, ServletWebRequest req, int statusCode, String reason)
            throws URISyntaxException {

        URI uri = new URI(req.getRequest().getRequestURI());

        val problem = Problem.builder()
                .instance(uri)
                .title(reason)
                .status(statusCode)
                .detail(var.getMessage())
                .build();

        return ResponseEntity
                .status(statusCode)
                .body(problem);

    }

    private static String toFriendlyMsg(final List<ObjectError> errors) {

        final List<String> errorMessages = new ArrayList<>();

        for (ObjectError error : errors) {
            String msg = error.getDefaultMessage();
            if (msg != null) {
                if (!errorMessages.contains(msg)) {
                    errorMessages.add(msg);
                }
            }
        }
        val friendlyMessage = new StringBuilder();

        if (errorMessages.size() == 1) {
            friendlyMessage.append(errorMessages.get(0));
        } else {
            for (String error : errorMessages) {
                friendlyMessage.append(error).append("|");
            }
        }
        return StringUtils.removeEnd(friendlyMessage
                .toString(), "|");
    }

}
