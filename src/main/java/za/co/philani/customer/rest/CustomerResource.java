package za.co.philani.customer.rest;

import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import za.co.philani.customer.model.Customer;

import javax.validation.Valid;
import java.util.List;

public interface CustomerResource {

    @ApiOperation("Find a specific customer")
    ResponseEntity<Customer> findOneCustomer(String id);

    @ApiOperation("Find all customers")
    ResponseEntity<List<Customer>> findCustomers();

    @ApiOperation("Save or update a customer")
    ResponseEntity<String> saveCustomer(String id, @Valid Customer customer);

    @ApiOperation("Delete a customer")
    ResponseEntity<String> deleteCustomer(String id);

}
