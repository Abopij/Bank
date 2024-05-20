package bank.com.controllers;

import bank.com.entities.User;
import bank.com.services.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("api/v1/search")
@RequiredArgsConstructor
@Api(value = "/api/v1")
public class SearchByUserController {

    private final UserService userService;

    @ApiOperation(value = "Search users by different param (required false)")
    @GetMapping
    public Page<User> search(@RequestParam(required = false) Date date,
                             @RequestParam(required = false) String phone,
                             @RequestParam(required = false) String email,
                             @RequestParam(required = false) String fullName,
                             @RequestParam(required = false, defaultValue = "1") Integer page,
                             @RequestParam(required = false, defaultValue = "10") Integer count
                             ) {
        if (date != null) {
            return userService.getUsersByDateGreater(date, page, count);
        }
        if (phone != null) {
            return new PageImpl<>(List.of(userService.getUserByPhone(phone)));
        }
        if (email != null) {
            return new PageImpl<>(List.of(userService.getUserByEmail(email)));
        }
        if (fullName != null) {
            String[] names = fullName.split(" ");
            if (names.length == 3) {
                return userService.getUsersByFullName(names[0], names[1], names[2], page, count);
            }
        }
        return new PageImpl<>(userService.getAllUsers());
    }
}
