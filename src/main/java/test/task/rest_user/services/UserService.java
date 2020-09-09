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
import java.util.Optional;

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
        tryWriteNewRoles(userDto, user);
        return save(user);
    }

    @Transactional
    public User updateUserFromDto(User user, UserDto userDto){
        if(user.getRoles() != null) {
            user.getRoles().clear();
        }
        user.setName(userDto.getName());
        user.setLogin(userDto.getLogin());
        user.setPassword(userDto.getPassword());
        user.setRoles(new HashSet<>());
        tryWriteNewRoles(userDto, user);
        return save(user);
    }

    public User getUserByLogin(String login) {
        return userRepo.getUserByLogin(login);
    }

    public User findById(Integer id) {
        return userRepo.findById(id).orElse(null);
    }

    public Optional<User> findByName(String name) {
        return userRepo.findByName(name);
    }


    private void tryWriteNewRoles(UserDto userDto, User user) {
        try{
            for (int role : userDto.getRoles()) {
                user.getRoles().add(Role.values()[role - 1]);
            }
        }catch (Exception e){
            throw new IllegalArgumentException("Invalid roles data in DtoUser!");
        }
    }
}

