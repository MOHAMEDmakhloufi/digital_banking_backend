package com.fsb.digital_banking_backend.repositories;

import com.fsb.digital_banking_backend.entities.AccountOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountOperationRepository extends JpaRepository<AccountOperation, Long> {
    public List<AccountOperation> findAccountOperationByBankAccountId(String bankAccountId);
    Page<AccountOperation> findAccountOperationByBankAccountId(String accountId, Pageable pageable);
}
