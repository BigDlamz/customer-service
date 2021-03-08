package za.co.philani.customer.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import za.co.philani.customer.exception.CustomerNotFound;
import za.co.philani.customer.model.Customer;
import za.co.philani.customer.repository.CustomerRepository;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;

    @Autowired
    protected CustomerServiceImpl(CustomerRepository repository) {
        this.repository = repository;
    }

    @Override
    @Cacheable(value = "customers", key = "#id")
    public Customer find(String id) {
        return Optional.of(repository.findById(id))
                .orElseThrow(CustomerNotFound::new)
                .orElse(null);
    }

    @Override
    public List<Customer> findAll() {
        return repository.findAll();
    }

    @Override
    @CachePut(value = "customers", key = "#id")
    public void saveOrUpdate(String id,Customer customer) {
        log.info("Saving or updating customer number {}", customer.getCustNo());
        repository.save(customer);

    }

    @Override
    @CacheEvict(value = "customers", key = "#id")
    public void delete(String id) {
        log.info("Deleting customer number {}", id);
        repository.deleteById(id);
    }
}
