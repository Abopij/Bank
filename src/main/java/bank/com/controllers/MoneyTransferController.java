package bank.com.controllers;

import bank.com.services.BankAccountService;
import bank.com.services.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@RestController
@RequestMapping("/api/v1/transfer")
@RequiredArgsConstructor
@Api(value = "api/v1/")
public class MoneyTransferController {

    private final BankAccountService bankAccountService;

    private final UserService userService;

    @ApiOperation(value = "Transfer money between Clients")
    @PostMapping
    public String moneyTransfer(@NonNull @RequestBody Long idRecipientClient, @NonNull @RequestBody double money, @AuthenticationPrincipal Principal user) {

        if (!userService.getUserByUsername(user.getName()).equals(idRecipientClient)) {
            if (!userService.existsById(idRecipientClient)) {
                boolean response = bankAccountService.transferMoney(bankAccountService.getUserById(userService.getUserByUsername(user.getName()).getId()), bankAccountService.getUserById(idRecipientClient), money);
                return response ? ResponseEntity.ok("Ok").toString() : ResponseEntity.status(400).toString();
            }
        }
        return ResponseEntity.status(400).toString();
    }

}
