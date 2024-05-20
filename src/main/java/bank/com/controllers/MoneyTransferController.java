package bank.com.controllers;

import bank.com.entities.User;
import bank.com.entities.UserPrincipal;
import bank.com.services.BankAccountService;
import bank.com.services.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@RestController
@RequestMapping("/api/v1/transfer")
@RequiredArgsConstructor
@Api(value = "api/v1/")
public class MoneyTransferController {

    final BankAccountService bankAccountService;

    final UserService userService;

    @ApiOperation(value = "Transfer money between Clients")
    @PostMapping
    public String moneyTransfer(@RequestBody Long idRecipientClient, @RequestBody double money) {
        UserPrincipal user = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (idRecipientClient != null && user != null && !userService.getUserByUsername(user.getUsername()).equals(idRecipientClient)) {
            if (!userService.existsById(idRecipientClient)) {
                boolean response = bankAccountService.transferMoney(bankAccountService.getUserById(userService.getUserByUsername(user.getUsername()).getId()), bankAccountService.getUserById(idRecipientClient), money);
                return response ? ResponseEntity.ok("Ok").toString() : ResponseEntity.status(400).toString();
            }
        }
        return ResponseEntity.status(400).toString();
    }

}
