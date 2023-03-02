package com.fsb.digital_banking_backend.entities;

import com.fsb.digital_banking_backend.enums.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "BANK_ACCOUNTS")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE", length = 4, discriminatorType = DiscriminatorType.STRING)
@Data
@NoArgsConstructor @AllArgsConstructor
public class BankAccount {
    @Id
    protected String id;
    protected double balance;
    protected Date createAt;
    @Enumerated(EnumType.STRING)
    protected AccountStatus status;
    @ManyToOne
    protected Customer customer;
    @OneToMany(mappedBy = "bankAccount")
    protected List<AccountOperation> accountOperations;
}
