package bank.com.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Entity
@Table(name = "bank_accounts")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BankAccount implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "money", columnDefinition = "float(53) CHECK (money >= 0)")
    private double money;

    @Column(name = "is_active")
    private boolean isActive; // <= 207% от изначального

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private MoneyType type;

    @OneToOne(mappedBy = "account", fetch = FetchType.LAZY)
    private User user;

    public BankAccount(double money, MoneyType type) {
        this.money = money;
        this.type = type;
    }
}