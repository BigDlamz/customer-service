package za.co.philani.customer.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;
import za.co.philani.customer.service.Customer;
import za.co.philani.customer.service.CustomerService;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
@Slf4j
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private CustomerService customerService;

    @Test
    @WithMockUser(username = "philani", password = "password123")
    public void testGeOneCustomer() throws Exception {

        when(customerService.find(any())).thenReturn(Customer.builder().custNo("1").name("Philani").surname("Dlamini").build());

        mockMvc.perform(MockMvcRequestBuilders.get("/customers/{id}", String.class))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("Philani"))
                .andExpect(jsonPath("surname").value("Dlamini"));

    }

    @Test
    @WithMockUser(username = "philani", password = "password123")
    public void testGetAllCustomers() throws Exception {

        val firstCustomer = Customer.builder().custNo("1").name("Donald").surname("Trump").build();
        val secondCustomer = Customer.builder().custNo("2").name("David").surname("Beckham").build();
        List<Customer> customers = Stream.of(firstCustomer, secondCustomer).collect(Collectors.toList());

        when(customerService.findAll()).thenReturn(customers);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/customers"))
                .andExpect(status().isOk()).andReturn();

        val expected = objectMapper.writeValueAsString(customers);
        val actual = result.getResponse().getContentAsString();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @WithMockUser(username = "philani", password = "password123")
    public void testCreateCustomer() throws Exception {

        val custNo = "2";
        val customerToSave = Customer.builder().custNo(custNo).name("David").surname("Beckham").build();

        doNothing().when(customerService).saveOrUpdate(custNo, customerToSave);

        val result = mockMvc.perform(MockMvcRequestBuilders.put("/customers/{id}", custNo)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(customerToSave)))
                .andExpect(status().isOk()).andReturn();

        val expected = "Customer number 2 saved successfully";
        val actual = result.getResponse().getContentAsString();

        assertThat(expected).isEqualTo(actual);
    }
}