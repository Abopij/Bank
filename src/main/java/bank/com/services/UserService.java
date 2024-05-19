package bank.com.services;

import bank.com.entities.User;
import bank.com.repo.UserRepo;
import bank.com.repo.specifications.UserSpecs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    UserRepo userRepo;

    @Autowired
    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public void save(User user) {
        userRepo.save(user);
    }

    public void delete(User user) {
        userRepo.delete(user);
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public void delete(Long id) {
        userRepo.deleteById(id);
    }

    public boolean removePhone(String phone) {
        Specification<User> filter = Specification.where(null);
        filter = filter.and(UserSpecs.equalsToPhone(phone));

        User user = userRepo.findOne(filter).get();
        if (user.getEmail() != null) {
            user.setPhone(null);
            update(user);
            return true;
        }
        return false;
    }

    public boolean removeEmail(String email) {
        Specification<User> filter = Specification.where(null);
        filter = filter.and(UserSpecs.equalsToEmail(email));

        User user = userRepo.findOne(filter).get();
        if (user.getPhone() != null) {
            user.setEmail(null);
            update(user);
            return true;
        }
        return false;
    }

    public boolean changePhone(Long id, String phone) {
        Specification<User> filter = Specification.where(null);
        filter = filter.and(UserSpecs.equalsToPhone(phone));

        if (userRepo.findOne(filter).get() != null) {
            return false;
        }

        User user = userRepo.getReferenceById(id);
        user.setPhone(phone);

        save(user);

        return true;
    }

    public User getUserById(Long id) {
        return userRepo.getReferenceById(id);
    }

    public void update(User user) {
        userRepo.saveAndFlush(user);
    }

    public User getUserByPhone(String phone) {
        Specification<User> filter = Specification.where(null);
        filter = filter.and(UserSpecs.equalsToPhone(phone));

        return userRepo.findOne(filter)
                .get();
    }

    public User getUserByEmail(String email) {
        Specification<User> filter = Specification.where(null);
        filter = filter.and(UserSpecs.equalsToEmail(email));

        return userRepo.findOne(filter)
                .get();
    }
    public Page<User> getUsersByDateGreater(Date date, int numberPage, int countMaxInPage) {
        Specification<User> filter = Specification.where(null);
        filter = filter.and(UserSpecs.dateOfBirthGreaterThanOrEq(date));

        return userRepo.findAll(filter, PageRequest.of(numberPage, countMaxInPage));
    }

    public Page<User> getUsersByFullName(String firstName, String secondName, String patronymic, int numberPage, int countMaxInPage) {
        Specification<User> filter = Specification.where(null);
        filter = filter.and(UserSpecs.equalsToFullName(firstName, secondName, patronymic));

        return userRepo.findAll(filter, PageRequest.of(numberPage, countMaxInPage));
    }

    public User getUserByUsername(String username) {
        return userRepo.getUserByUsername(username);
    }

    public Optional<User> findByUsername(String username) {
        return userRepo.findByUsername(username);
    }
}
