package bank.com.services;

import bank.com.entities.BankAccount;
import bank.com.entities.BankAccountRedis;
import bank.com.repo.AccountDao;
import bank.com.repo.BankAccountRepo;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log
public class BankAccountService {

    @Value("${bank.account.transfer.commission}")
    private double commission;

    final BankAccountRepo bankAccountRepo;

    final AccountDao accountDao;

    @Autowired
    public BankAccountService(BankAccountRepo bankAccountRepo, AccountDao accountDao) {
        this.bankAccountRepo = bankAccountRepo;
        this.accountDao = accountDao;
    }

    public boolean save(BankAccount account) {
        if (account.getMoney() >= 0.0) {
            bankAccountRepo.save(account);
            accountDao.save(new BankAccountRedis(account.getId(), account.getMoney(), account.getType(), account.isActive()));
            return true;
        }
        return false;
    }

    public boolean saveWithoutCash(BankAccount account) {
        if (account.getMoney() >= 0.0) {
            bankAccountRepo.save(account);
            return true;
        }
        return false;
    }

    public void delete(BankAccount account) {
        bankAccountRepo.delete(account);
        accountDao.deleteAccount(account.getId());
    }

    public void deleteWithoutCash(BankAccount account) {
        bankAccountRepo.delete(account);
    }

    public List<BankAccount> getAllUsers() {
        return bankAccountRepo.findAll();
    }

    public void delete(Long id) {
        bankAccountRepo.deleteById(id);
        accountDao.deleteAccount(id);
    }

    public BankAccount getUserById(Long id) {
        return bankAccountRepo.getReferenceById(id);
    }

    public boolean update(BankAccount account) {
        if (account.getMoney() >= 0.0) {
            bankAccountRepo.updateWithTransactional(account.getId(), account.getMoney(), account.getType(), account.isActive());
            return true;
        }
        return false;
    }

    public boolean updateWithCash(BankAccount account) {
        if (account.getMoney() >= 0) {
            bankAccountRepo.saveAndFlush(account);
            accountDao.save(new BankAccountRedis(account.getId(), account.getMoney(), account.getType(), account.isActive()));
            return true;
        }
        return false;
    }

    public boolean transferMoney(BankAccount sender, BankAccount recipient, double amount) {
        if (sender.getType().equals(recipient.getType())) {
            if ((sender.getMoney() - amount) < 0.0) {
                return false;
            }
            if (bankAccountRepo.existsById(sender.getId()) || bankAccountRepo.existsById(recipient.getId())) {
                return false;
            }
            sender.setMoney(sender.getMoney() - amount);
            recipient.setMoney((recipient.getMoney() + amount) * (1d - commission));

            return update(sender) && update(recipient);
        }

        return false;
    }

    public List<BankAccount> findAll() {
        return bankAccountRepo.findAllWithLock();
    }

    public void increaseAllDeposits(double percent) {
        long currentTime = System.currentTimeMillis();
        List<BankAccount> accountsWeird = findAll();
        for (BankAccount account : accountsWeird) {
            double newMoney = account.getMoney() * (1 + (percent / 100));
            if (account.isActive() && (accountDao.getAccountById(account.getId()).getMoney() * 2.07) >= newMoney) {
                account.setMoney(newMoney);
                update(account);
                log.info("For user with id=" + account.getId() + " his deposit is increased by 5%");
            } else {
                if (account.isActive()) {
                    log.info("User with id=" + account.getId() + " deposit is not active due barrier");
                    account.setActive(false);
                    update(account);
                }
            }
        }
    }

}
