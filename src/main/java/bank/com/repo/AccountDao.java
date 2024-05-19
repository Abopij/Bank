package bank.com.repo;

import bank.com.entities.BankAccountRedis;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Log
public class AccountDao {

    private final String HASH_KEY = "accounts";
    private final RedisTemplate redisTemplate;

//    private static final AtomicInteger atomicInteger = new AtomicInteger(0);
    @PostConstruct
    private void clearCash() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    private volatile Long id = 1000000L; // дальняя ячейка в кэше, на случай отсутствия передаваемого id, но думаю, что лучше так не делать :)

    private synchronized Long increaseId() {
        return ++id;
    }

    public Long save(BankAccountRedis bankAccount) {
        Long id = bankAccount.getId() != null ? bankAccount.getId() : increaseId();
        redisTemplate.opsForHash().put(HASH_KEY, id, bankAccount);
        return id;
    }

    public List<BankAccountRedis> findAll() {
        return redisTemplate.opsForHash().values(HASH_KEY);
    }

    public BankAccountRedis getAccountById(Long id) {
        return (BankAccountRedis) redisTemplate.opsForHash().get(HASH_KEY, id);
    }

    public void deleteAccount(Long id) {
        redisTemplate.opsForHash().delete(HASH_KEY, id);
    }

}
