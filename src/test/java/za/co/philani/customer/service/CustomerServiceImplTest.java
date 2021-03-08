package za.co.philani.customer.service;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import za.co.philani.customer.model.Customer;
import za.co.philani.customer.repository.CustomerRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;

//TODO add more tests

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @InjectMocks
    CustomerServiceImpl customerService;

    @Mock
    CustomerRepository customerRepository;

    @Test
    public void whenValidInputThenReturnOneCustomer() {
        Mockito.when(customerRepository.findById(anyString())).thenReturn(Optional.of(buildCustomer()));
        Customer expected = buildCustomer();
        Customer actual = customerService.find("1");
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void whenValidInputThenReturnAllCustomer() {

        val customerOne = Customer.builder()
                .custNo("1")
                .name("TestUser")
                .surname("TestSurname")
                .build();


        val customerTwo = Customer.builder()
                .custNo("2")
                .name("TestUser")
                .surname("TestSurname")
                .build();

        List<Customer> expected = Stream.of(customerOne, customerTwo).collect(Collectors.toList());
        Mockito.when(customerService.findAll()).thenReturn(expected);
        List<Customer> actual = customerService.findAll();
        assertThat(expected).isEqualTo(actual);

    }

    private Customer buildCustomer() {
        return Customer.builder()
                .custNo("1")
                .name("Philani")
                .surname("Dlamini")
                .build();
    }
}