package bank.com.services;

import bank.com.entities.BankAccount;
import bank.com.entities.MoneyType;
import bank.com.repo.AccountDao;
import bank.com.repo.BankAccountRepo;
import bank.com.repo.UserRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@ExtendWith(MockitoExtension.class)
class BankAccountServiceTest {
    @InjectMocks
    private BankAccountService bankAccountService;

    @Mock
    private BankAccountRepo bankAccountRepo;
    @Mock
    private AccountDao accountDao;
    @Mock
    private RedisTemplate redisTemplate;
    private BankAccount sender;
    private BankAccount recipient;

    @BeforeEach
    void setUp() {
        sender = BankAccount.builder()
                .id(1L)
                .money(100.0)
                .type(MoneyType.RUB)
                .isActive(true)
                .build();
        recipient = BankAccount.builder()
                .id(2L)
                .money(100.0)
                .type(MoneyType.RUB)
                .isActive(true)
                .build();
    }


    @Test
    void transferMoneyOkTest() {
        bankAccountService.saveWithoutCash(sender);
        bankAccountService.saveWithoutCash(recipient);
        for (int i = 0; i < 1000; i++) {
            boolean response = bankAccountService.transferMoney(sender, recipient, Math.random() * 100);
            List<BankAccount> list = bankAccountService.findAll();
            Assertions.assertTrue(response);
            sender.setMoney(100.0);
            recipient.setMoney(100.0);
        }
        bankAccountService.deleteWithoutCash(sender);
        bankAccountService.deleteWithoutCash(recipient);
    }

    @Test
    void transferMoneyValidTest() {
        bankAccountService.saveWithoutCash(sender);
        bankAccountService.saveWithoutCash(recipient);
        for (int i = 0; i < 1000; i++) {
            boolean response = bankAccountService.transferMoney(sender, recipient, Math.random() * 100000 + 100);
            Assertions.assertFalse(response);
            sender.setMoney(100.0);
            recipient.setMoney(100.0);
        }
        bankAccountService.deleteWithoutCash(sender);
        bankAccountService.deleteWithoutCash(recipient);
    }

    @Test
    void transferMoneyMultipleOkTest() throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(100);
        CountDownLatch countDownLatch = new CountDownLatch(1000 * 2);

        for (int i = 0; i < 1000; i++) {
            service.execute(new Thread(() -> {
                BankAccount senderM = BankAccount.builder()
                        .id(countDownLatch.getCount())
                        .money(100.0)
                        .type(MoneyType.RUB)
                        .isActive(true)
                        .build();
                countDownLatch.countDown();
                BankAccount recipientM = BankAccount.builder()
                        .id(countDownLatch.getCount())
                        .money(100.0)
                        .type(MoneyType.RUB)
                        .isActive(true)
                        .build();
                bankAccountService.saveWithoutCash(senderM);
                bankAccountService.saveWithoutCash(recipientM);


                boolean response = bankAccountService.transferMoney(senderM, recipientM, Math.random() * 100);
                Assertions.assertTrue(response);
                countDownLatch.countDown();

                bankAccountService.deleteWithoutCash(senderM);
                bankAccountService.deleteWithoutCash(recipientM);
            }));
        }
        countDownLatch.await();
    }

    @Test
    void transferMoneyMultipleValidTest() throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(100);
        CountDownLatch countDownLatch = new CountDownLatch(1000 * 2);
        for (int i = 0; i < 1000; i++) {
            service.execute(new Thread(() -> {
                BankAccount senderM = BankAccount.builder()
                        .id(countDownLatch.getCount())
                        .money(100.0)
                        .type(MoneyType.RUB)
                        .isActive(true)
                        .build();
                countDownLatch.countDown();
                BankAccount recipientM = BankAccount.builder()
                        .id(countDownLatch.getCount())
                        .money(100.0)
                        .type(MoneyType.RUB)
                        .isActive(true)
                        .build();
                bankAccountService.saveWithoutCash(senderM);
                bankAccountService.saveWithoutCash(recipientM);

                boolean response = bankAccountService.transferMoney(sender, recipient, Math.random() * 10000000 + 120);
                Assertions.assertFalse(response);
                countDownLatch.countDown();

                bankAccountService.deleteWithoutCash(senderM);
                bankAccountService.deleteWithoutCash(recipientM);
            }));
        }
        countDownLatch.await();
    }

}
