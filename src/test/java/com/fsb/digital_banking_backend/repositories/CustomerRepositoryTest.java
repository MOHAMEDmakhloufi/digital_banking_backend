package com.fsb.digital_banking_backend.repositories;

import com.fsb.digital_banking_backend.entities.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest(
        properties = {
                "spring.jpa.properties.javax.persistence.validation.mode=none"
        }
)
class CustomerRepositoryTest {
    @Autowired
    private CustomerRepository underTest;

    @Test
    void itShouldSaveCustomer() {
        //GIVEN
        Customer customer = new Customer();
        customer.setName("Hassan");
        customer.setEmail("Hassan@gmail.com");

        //WHEN
        Customer customerSaved = underTest.save(customer);
        //THEN
        assertThat(customerSaved)
                .isEqualToIgnoringGivenFields(customer, "id", "bankAccounts");
        assertThat(customerSaved.getId()).isNotNull();
    }
}