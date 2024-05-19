package bank.com.controllers;

import bank.com.services.BankAccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/transfer")
@RequiredArgsConstructor
@Api(value = "api/v1/")
public class MoneyTransferController {

    final BankAccountService bankAccountService;

    @ApiOperation(value = "Transfer money between Clients")
    @PostMapping
    public String moneyTransfer(@RequestBody Long idSenderClient, @RequestBody Long idRecipientClient, @RequestBody double money) {
        if (idRecipientClient != null && idSenderClient != null && !idSenderClient.equals(idRecipientClient)) {
            boolean response = bankAccountService.transferMoney(bankAccountService.getUserById(idSenderClient), bankAccountService.getUserById(idRecipientClient), money);
            return response ? ResponseEntity.ok("Ok").toString() : ResponseEntity.status(400).toString();
        }
        return ResponseEntity.status(400).toString();
    }

}
