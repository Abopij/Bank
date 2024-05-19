package bank.com.security.auth;

import bank.com.entities.MoneyType;
import bank.com.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String username;

    private String firstName;

    private String secondName;

    private String patronymic;

    private String email;

    private String phone;

    private String password;

    private double money;

    private MoneyType type;

    private Role role;

    private Date date;
}
