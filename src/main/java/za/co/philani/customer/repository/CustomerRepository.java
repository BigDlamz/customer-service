package za.co.philani.customer.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import za.co.philani.customer.model.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository  extends MongoRepository<Customer, String> {

    @Override
    Optional<Customer> findById(String aLong);

    @Override
    List<Customer> findAll();

    @Override
    <S extends Customer> S save(S entity);

    @Override
    void deleteById(String aLong);


}
