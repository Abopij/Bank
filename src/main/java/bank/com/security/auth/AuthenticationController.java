package bank.com.security.auth;

import bank.com.entities.BankAccount;
import bank.com.entities.BankAccountRedis;
import bank.com.entities.MoneyType;
import bank.com.repo.AccountDao;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Log
@Api("/api/v1")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    private final AccountDao accountDao;

    @ApiOperation(value = "User registration")
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
//        log.info("Adding a new user with money=" + request.getMoney() + request.getType());
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @ApiOperation(value = "Getting an authorized user's token")
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

}
