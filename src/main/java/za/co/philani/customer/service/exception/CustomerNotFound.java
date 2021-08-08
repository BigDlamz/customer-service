package za.co.philani.customer.service.exception;

public class CustomerNotFound extends CustomerServiceException {

    private static final String CUSTOMER_NOT_FOUND = "Customer not found";

    public CustomerNotFound() {
        super(CUSTOMER_NOT_FOUND);
    }
}
