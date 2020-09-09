package test.task.rest_user.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import test.task.rest_user.domain.Role;
import test.task.rest_user.domain.User;
import test.task.rest_user.domain.dto.UserDto;
import test.task.rest_user.repository.UserRepo;

import java.util.HashSet;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;

    public List<User> findAll() {
        return userRepo.findAll();
    }

    @Transactional
    public void delete(User user) {
        userRepo.delete(user);
    }

    @Transactional
    public User save(User user) {
        return userRepo.save(user);
    }

    @Transactional
    public User createNewUserFromDto(UserDto userDto){
        User user = new User(userDto.getName(), userDto.getLogin(), userDto.getPassword(), new HashSet<>());
        try{
            for (int role : userDto.getRoles()) {
                user.getRoles().add(Role.values()[role - 1]);
            }
        }catch (Exception e){
            throw new IllegalArgumentException("Invalid roles data in DtoUser!");
        }
        return save(user);
    }

    public User getUserByLogin(String login) {
        return userRepo.getUserByLogin(login);
    }
}

