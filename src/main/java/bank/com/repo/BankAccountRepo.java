package bank.com.repo;

import bank.com.entities.BankAccount;
import bank.com.entities.MoneyType;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankAccountRepo extends JpaRepository<BankAccount, Long> {

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Transactional
    @Query("SELECT ba FROM BankAccount ba")
    List<BankAccount> findAllWithLock();

    @Transactional
    @Modifying
    @Query("UPDATE BankAccount ba SET ba.money = :money, ba.type = :type, ba.isActive = :active WHERE ba.id = :id")
    void updateWithTransactional(@Param("id") Long id, @Param("money") double money, @Param("type") MoneyType type, @Param("active") boolean active);

}
