package bank.com.controllers;

import bank.com.services.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
@Api(value = "/api/v1")
@Log
public class UserController {

    private final UserService userService;

    @ApiOperation(value = "Changing the phone")
    @PatchMapping("/change/phone")
    public ResponseEntity<?> changePhone(@RequestBody String phone, @AuthenticationPrincipal UserDetails principal) {
        return userService.changePhone(userService.getUserByUsername(principal.getUsername()).getId(), phone) ? ResponseEntity.ok("Ok") : ResponseEntity.status(403).build();
    }

    @ApiOperation(value = "Changing the email")
    @PatchMapping("/change/email")
    public ResponseEntity<?> changeEmail(@RequestBody String email, @AuthenticationPrincipal UserDetails principal) {
        return userService.changeEmail(userService.getUserByUsername(principal.getUsername()).getId(), email) ? ResponseEntity.ok("Ok") : ResponseEntity.status(403).build();
    }

    @ApiOperation(value = "Removing the phone")
    @PatchMapping("/remove/phone")
    public ResponseEntity<?> removePhone(@AuthenticationPrincipal UserDetails principal) {
        return userService.removePhone(userService.getUserByUsername(principal.getUsername()).getPhone()) ? ResponseEntity.ok("Ok") : ResponseEntity.status(403).build();
    }

    @ApiOperation(value = "Changing the email")
    @PatchMapping("/remove/email")
    public ResponseEntity<?> removeEmail(@AuthenticationPrincipal UserDetails principal) {
        return userService.removeEmail(userService.getUserByUsername(principal.getUsername()).getEmail()) ? ResponseEntity.ok("Ok") : ResponseEntity.status(403).build();
    }
}
