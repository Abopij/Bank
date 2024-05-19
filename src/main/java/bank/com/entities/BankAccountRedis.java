package bank.com.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("accounts")
public class BankAccountRedis implements Serializable {
    @Id
    private Long id;

    private double money;

    private MoneyType type;

    private boolean isActive;

    public BankAccountRedis(double money, MoneyType type) {
        this.money = money;
        this.type = type;
    }

}
