package com.fsb.digital_banking_backend.repositories;

import com.fsb.digital_banking_backend.entities.AccountOperation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountOperationRepository extends JpaRepository<AccountOperation, Long> {
}
