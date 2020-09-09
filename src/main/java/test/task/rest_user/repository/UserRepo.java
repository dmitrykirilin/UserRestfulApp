package test.task.rest_user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import test.task.rest_user.domain.User;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
    User getUserByLogin(String login);

    Optional<User> findByName(String name);
}
