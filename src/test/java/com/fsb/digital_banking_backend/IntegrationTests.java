package com.fsb.digital_banking_backend;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fsb.digital_banking_backend.dtos.CurrentBankAccountDTO;
import com.fsb.digital_banking_backend.dtos.CustomerDTO;

import com.fsb.digital_banking_backend.entities.CurrentAccount;
import com.fsb.digital_banking_backend.exceptions.BalanceNotSufficientException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void itShouldTransferAmountFromAccountToOther() throws Exception {
        //GIVEN
        Long customerId = 1L;
        //create customer DTO
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(customerId);
        customerDTO.setName("mohamed");
        customerDTO.setEmail("mohamed@gmail.com");
        //create bank account
        double initialBalance = 5000;
        String type = "current";
        double paramType = 500;
        //WHEN
            //create new customer
        ResultActions customerRegistrationAction = mockMvc.perform(post("/customers/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectToJson(customerDTO))));

            //create two account
        ResultActions currentAccountRegistrationAction = mockMvc.perform(
                post(String.format("/accounts/save/{customerId}?initialBalance=%s&type=%s&money=%s", initialBalance, type, paramType), customerId));
        CurrentBankAccountDTO currentAccount = jsonToCurrentAccount(currentAccountRegistrationAction
                .andReturn()
                .getResponse()
                .getContentAsString()
        );
        ResultActions currentAccount2RegistrationAction = mockMvc.perform(
                post(String.format("/accounts/save/{customerId}?initialBalance=%s&type=%s&money=%s", initialBalance, type, paramType), customerId));

        CurrentBankAccountDTO currentAccount2 = jsonToCurrentAccount(currentAccount2RegistrationAction
                .andReturn()
                .getResponse()
                .getContentAsString()
        );
            //create account operation
        ResultActions transferAction = mockMvc.perform(
                post(String.format("/accounts/transfer/{from}/{to}?amount=%s", 1000), currentAccount.getId(), currentAccount2.getId()));


        //THEN
        customerRegistrationAction.andExpect(status().isOk());
        currentAccountRegistrationAction.andExpect(status().isOk());
        currentAccount2RegistrationAction.andExpect(status().isOk());
        transferAction.andExpect(status().isOk());
    }
    @Test
    void itShouldThrowWhenNotSufficientMoneyWhenDoDebit() throws Exception {
        //GIVEN
        Long customerId = 1L;
        //create customer DTO
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(customerId);
        customerDTO.setName("mohamed");
        customerDTO.setEmail("mohamed@gmail.com");
        //create bank account
        double initialBalance = 800;
        String type = "current";
        double paramType = 500;
        //WHEN
        //create new customer
        ResultActions customerRegistrationAction = mockMvc.perform(post("/customers/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectToJson(customerDTO))));

        //create two account
        ResultActions currentAccountRegistrationAction = mockMvc.perform(
                post(String.format("/accounts/save/{customerId}?initialBalance=%s&type=%s&money=%s", initialBalance, type, paramType), customerId));
        CurrentBankAccountDTO currentAccount = jsonToCurrentAccount(currentAccountRegistrationAction
                .andReturn()
                .getResponse()
                .getContentAsString()
        );

        //create account operation

        ResultActions test_debit = mockMvc.perform(
                post(String.format("/accounts/debit/{accountId}?amount=%s&description=%s", 4000, "test debit"), currentAccount.getId()));


        //THEN
        customerRegistrationAction.andExpect(status().isOk());
        currentAccountRegistrationAction.andExpect(status().isOk());
        test_debit.andExpect(status().isNotAcceptable())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BalanceNotSufficientException))
                .andExpect(result -> assertEquals("Balance not sufficient", result.getResolvedException().getMessage()));
    }
    private String objectToJson(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            fail("Failed to convert object to json");
            return null;
        }

    }
    private CurrentBankAccountDTO jsonToCurrentAccount(String json) {
        try {
            CurrentBankAccountDTO currentAccount= new ObjectMapper().readValue(json, CurrentBankAccountDTO.class);
            return currentAccount;
        }catch (JsonProcessingException e){
            fail("Failed to convert json to CurrentAccount");
            return null;
        }

    }
}

