package bank.com.repo;

import bank.com.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long>, JpaSpecificationExecutor<User>, PagingAndSortingRepository<User, Long> {
    User getUserByUsername(String username);
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);
}
