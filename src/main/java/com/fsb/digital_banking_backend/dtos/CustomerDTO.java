package com.fsb.digital_banking_backend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fsb.digital_banking_backend.entities.BankAccount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;


@Data
public class CustomerDTO {
    private Long id;
    private String name;
    private String email;
}
