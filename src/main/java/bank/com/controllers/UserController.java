package bank.com.controllers;

import bank.com.services.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
@Api(value = "/api/v1")
public class UserController {

    private final UserService userService;

    @ApiOperation(value = "Changing the phone")
    @PatchMapping("/change/phone")
    public String changePhone(@RequestBody String phone, @AuthenticationPrincipal Principal principal) {
        return userService.changePhone(userService.getUserByUsername(principal.getName()).getId(), phone) ? ResponseEntity.ok("Ok").toString() : ResponseEntity.status(403).toString();
    }

    @ApiOperation(value = "Changing the email")
    @PatchMapping("/change/email")
    public String changeEmail(@RequestBody String email, @AuthenticationPrincipal Principal principal) {
        return userService.changePhone(userService.getUserByUsername(principal.getName()).getId(), email) ? ResponseEntity.ok("Ok").toString() : ResponseEntity.status(403).toString();
    }

    @ApiOperation(value = "Removing the phone")
    @PostMapping("/remove/phone")
    public String removePhone(@AuthenticationPrincipal Principal principal) {
        return userService.removePhone(userService.getUserByUsername(principal.getName()).getPhone()) ? ResponseEntity.ok("Ok").toString() : ResponseEntity.status(403).toString();
    }

    @ApiOperation(value = "Changing the email")
    @PostMapping("/remove/email")
    public String removeEmail(@AuthenticationPrincipal Principal principal) {
        return userService.removeEmail(userService.getUserByUsername(principal.getName()).getEmail()) ? ResponseEntity.ok("Ok").toString() : ResponseEntity.status(403).toString();
    }
}
