package bank.com.controllers;

import bank.com.entities.User;
import bank.com.services.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
@Api(value = "/api/v1")
public class UserController {

    final UserService userService;

    @ApiOperation(value = "Changing the phone")
    @PostMapping("/change/phone")
    public String changePhone(@RequestBody Long idClient, @RequestBody String phone) {
        return userService.changePhone(idClient, phone) ? ResponseEntity.ok("Ok").toString() : ResponseEntity.status(400).toString();
    }

    @ApiOperation(value = "Changing the email")
    @PostMapping("/change/email")
    public String changeEmail(@RequestBody Long idClient, @RequestBody String email) {
        return userService.changePhone(idClient, email) ? ResponseEntity.ok("Ok").toString() : ResponseEntity.status(400).toString();
    }

    @ApiOperation(value = "Removing the phone")
    @PostMapping("/remove/phone")
    public String removePhone(@RequestBody User user) {
        return userService.removePhone(user.getPhone()) ? ResponseEntity.ok("Ok").toString() : ResponseEntity.status(400).toString();
    }

    @ApiOperation(value = "Changing the email")
    @PostMapping("/remove/email")
    public String removeEmail(@RequestBody User user) {
        return userService.removeEmail(user.getPhone()) ? ResponseEntity.ok("Ok").toString() : ResponseEntity.status(400).toString();
    }
}
