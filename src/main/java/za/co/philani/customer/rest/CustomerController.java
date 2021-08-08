package za.co.philani.customer.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import za.co.philani.customer.service.Customer;
import za.co.philani.customer.service.CustomerService;

import java.text.MessageFormat;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/customers")
@Validated
public class CustomerController implements CustomerResource {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping(value = "/{id}", produces = "application/json")
       public ResponseEntity<Customer> findOneCustomer(@PathVariable("id") String id) {
        return ResponseEntity.ok(customerService.find(id));
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<Customer>> findCustomers() {
        return ResponseEntity.ok(customerService.findAll());
    }

    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> saveCustomer(@PathVariable("id") String id, @RequestBody Customer customer) {
        customerService.saveOrUpdate(id, customer);
        return ResponseEntity.ok(MessageFormat.format("Customer number {0} saved successfully", customer.getCustNo()));
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<String> deleteCustomer(@PathVariable("id") String id) {
        customerService.delete(id);
        return ResponseEntity.ok(MessageFormat.format("Customer number {0} deleted successfully", id));
    }
}
