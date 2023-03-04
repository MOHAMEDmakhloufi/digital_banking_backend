package com.fsb.digital_banking_backend.dtos;

import com.fsb.digital_banking_backend.entities.AccountOperation;
import com.fsb.digital_banking_backend.entities.Customer;
import com.fsb.digital_banking_backend.enums.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;



@Data
public class SavingBankAccountDTO extends BankAccountDTO{

    private String id;
    private double balance;
    private Date createAt;
    private AccountStatus status;
    private CustomerDTO customerDTO;
    private double interestRate;
}
