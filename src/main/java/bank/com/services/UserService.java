package bank.com.services;

import bank.com.entities.User;
import bank.com.repo.UserRepo;
import bank.com.repo.specifications.UserSpecs;
import lombok.extern.java.Log;
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
@Log
public class UserService {

    private final UserRepo userRepo;

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

        Optional<User> user = userRepo.findOne(filter);
        if (user.isEmpty()) {
            return false;
        }

        if (user.get().getEmail() != null) {
            user.get().setPhone(null);
            update(user.get());
            return true;
        }
        return false;
    }

    public boolean removeEmail(String email) {
        Specification<User> filter = Specification.where(null);
        filter = filter.and(UserSpecs.equalsToEmail(email));

        Optional<User> user = userRepo.findOne(filter);
        if (user.isEmpty()) {
            return false;
        }
        if (user.get().getPhone() != null) {
            user.get().setEmail(null);
            update(user.get());
            return true;
        }
        return false;
    }

    public boolean changePhone(Long id, String phone) {
        Specification<User> filter = Specification.where(null);
        filter = filter.and(UserSpecs.equalsToPhone(phone));

        if (userRepo.findOne(filter).isPresent()) {
            return false;
        }
        User user = userRepo.getReferenceById(id);
        user.setPhone(phone);

        update(user);

        return true;
    }

    public boolean changeEmail(Long id, String email) {
        Specification<User> filter = Specification.where(null);
        filter = filter.and(UserSpecs.equalsToEmail(email));

        if (userRepo.findOne(filter).isPresent()) {
            return false;
        }

        User user = userRepo.getReferenceById(id);
        user.setEmail(email);

        update(user);

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

        return userRepo.findOne(filter).isPresent() ? userRepo.findOne(filter).get() : null;
    }

    public User getUserByEmail(String email) {
        Specification<User> filter = Specification.where(null);
        filter = filter.and(UserSpecs.equalsToEmail(email));

        return userRepo.findOne(filter).isPresent() ? userRepo.findOne(filter).get() : null;
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


    public boolean existsById(Long id) {
        return userRepo.existsById(id);
    }

}
