package com.fsb.digital_banking_backend.web;

import com.fsb.digital_banking_backend.dtos.AccountHistoryDTO;
import com.fsb.digital_banking_backend.dtos.AccountOperationDTO;
import com.fsb.digital_banking_backend.dtos.BankAccountDTO;
import com.fsb.digital_banking_backend.entities.BankAccount;
import com.fsb.digital_banking_backend.exceptions.AmountNotVallidException;
import com.fsb.digital_banking_backend.exceptions.BalanceNotSufficientException;
import com.fsb.digital_banking_backend.exceptions.BankAccountNotFoundException;
import com.fsb.digital_banking_backend.exceptions.CustomerNotFoundException;
import com.fsb.digital_banking_backend.services.BankAccountService;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("accounts")

public class BankAccountRestController {

    private final BankAccountService bankAccountService;

    public BankAccountRestController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }
    @PostMapping("/save/{customerId}")
    @PostAuthorize("hasAuthority('ADMIN')")
    public BankAccountDTO saveBankAccount(@RequestParam double initialBalance,
                                @RequestParam String type,
                                @RequestParam double money,
                                @PathVariable Long customerId) throws CustomerNotFoundException {
        BankAccountDTO bankAccountDTO = bankAccountService.saveBankAccount(initialBalance, type, money, customerId);
        return bankAccountDTO;
    }
    @GetMapping("/{accountId}")
    @PostAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public BankAccountDTO getBankAccount(@PathVariable(name = "accountId") String accountId) throws BankAccountNotFoundException {
        return bankAccountService.getBankAccount(accountId);
    }

    @GetMapping()
    @PostAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public List<BankAccountDTO> listAccounts() {
        return bankAccountService.bankAccountList();
    }

    @GetMapping("/{accountId}/operations")
    @PostAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public List<AccountOperationDTO> getHistory(@PathVariable String accountId) {

        return bankAccountService.accountHistory(accountId);

    }
    @GetMapping("/{accountId}/pageOperations")
    @PostAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public AccountHistoryDTO getAccountHistory(@PathVariable String accountId,
                                               @RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "5") int size) throws BankAccountNotFoundException {

        return bankAccountService.getAccountHistory(accountId, page, size);

    }

    @PostMapping("/transfer/{from}/{to}")
    @PostAuthorize("hasAuthority('ADMIN')")
    public void transfer(@PathVariable("from") String accountIdSource,
                         @PathVariable("to") String accountIdDestination,
                         @RequestParam double amount)
            throws BankAccountNotFoundException, BalanceNotSufficientException, AmountNotVallidException {
        bankAccountService.transfer(accountIdSource, accountIdDestination, amount);
    }
    @PostMapping("/debit/{accountId}")
    @PostAuthorize("hasAuthority('ADMIN')")
    public void debit(@PathVariable String accountId,
                         @RequestParam double amount,
                         @RequestParam String description)
            throws BankAccountNotFoundException, BalanceNotSufficientException, AmountNotVallidException {
        bankAccountService.debit(accountId, amount, description);
    }
    @PostMapping("credit")
    @PostAuthorize("hasAuthority('ADMIN')")
    public void credit(@RequestParam String accountId,
                      @RequestParam double amount,
                      @RequestParam String description)
            throws BankAccountNotFoundException {
        bankAccountService.credit(accountId, amount, description);
    }
}
