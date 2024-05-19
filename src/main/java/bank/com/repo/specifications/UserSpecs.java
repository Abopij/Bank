package bank.com.repo.specifications;

import bank.com.entities.User;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;

public class UserSpecs {
    // сравнение по сходству имени
    public static Specification<User> equalsToFullName(String firstName, String secondName, String patronymic) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.and(
                        criteriaBuilder.like(root.get("firstName"), "%" + firstName + "%"),
                        criteriaBuilder.like(root.get("secondName"), "%" + secondName + "%"),
                        criteriaBuilder.like(root.get("patronymic"), "%" + patronymic));
    }

    // сравнение по дата рождения (старше)
    public static Specification<User> dateOfBirthGreaterThanOrEq(Date date) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.greaterThan(root.get("dateOfBirth"), date);
    }

    // сравнение по номеру телефона
    public static Specification<User> equalsToPhone(String phone) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.equal(root.get("phone"), phone);
    }

    // сравнение по эл. почте
    public static Specification<User> equalsToEmail(String email) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("email"), email);
    }
}
