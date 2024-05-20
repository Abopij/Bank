package bank.com.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPrincipal {

    private Long id;

    private String username;

    private boolean isAdmin;
}
