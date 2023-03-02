package com.fsb.digital_banking_backend.repositories;

import com.fsb.digital_banking_backend.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount, String> {
}
