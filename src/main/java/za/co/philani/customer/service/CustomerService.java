package za.co.philani.customer.service;

import za.co.philani.customer.exception.CustomerNotFound;
import za.co.philani.customer.model.Customer;

import java.util.List;

public interface CustomerService {

    Customer find(String id) throws CustomerNotFound;

    List<Customer> findAll();

    void saveOrUpdate(String id, Customer entity);

    void delete(String id);

}
