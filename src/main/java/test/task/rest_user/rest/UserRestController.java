package test.task.rest_user.rest;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import test.task.rest_user.domain.Role;
import test.task.rest_user.domain.User;
import test.task.rest_user.domain.Views;
import test.task.rest_user.domain.dto.UserDto;
import test.task.rest_user.services.UserService;
import test.task.rest_user.util.PasswordValidator;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("rest/v1/users")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;
    private final PasswordValidator passwordValidator;

    //  Getting all users.
    @GetMapping(value = "list")
    @JsonView({Views.IdName.class})
    public ResponseEntity<List<User>> getAllUsers(){
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    //  Getting one user by id.
    @GetMapping(value = "{user}/get")
    @JsonView({Views.UserAndRoles.class})
    public ResponseEntity<Object> getUser(@PathVariable User user){
        if (user == null) {
            return new ResponseEntity<>("Cannot found user with such id", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(user);
    }

    //  Removing one user by id.
    @DeleteMapping(value = "{user}/delete")
    public ResponseEntity<?> deleteUser(@PathVariable User user) {
        if(user == null){
            return new ResponseEntity<>("Cannot found user with such id", HttpStatus.NOT_FOUND);
        }
        userService.delete(user);
        return ResponseEntity.ok("User has removed successfully!");
    }

    //  User updating.
    @PutMapping(value = "{user}/edit")
    public ResponseEntity<Object> updateUser(@PathVariable("user") User userFromDB,
                                            @RequestBody @Valid UserDto user,
                                             BindingResult bindingResult) throws JsonProcessingException {
        if (userFromDB == null || user == null) {
            return new ResponseEntity<>("Cannot found user with such id", HttpStatus.NOT_FOUND);
        }

        //        Validation.
        passwordValidator.validate(user, bindingResult);
        if(bindingResult.hasErrors()){
            String jsonResponse = getJSONResponse(bindingResult);
            return new ResponseEntity<>(jsonResponse, HttpStatus.BAD_REQUEST);
        }

        //  Filling new data.
        if(userFromDB.getRoles() != null) {
            userFromDB.getRoles().clear();
        }
        userFromDB.setName(user.getName());
        userFromDB.setLogin(user.getLogin());
        userFromDB.setPassword(user.getPassword());
        userFromDB.setRoles(new HashSet<>());
        for (int role : user.getRoles()) {
            userFromDB.getRoles().add(Role.values()[role - 1]);
        }

        //   Saving to DB.
        userService.save(userFromDB);
        return ResponseEntity.ok(new HashMap<String, Boolean>(){{
            put("Success", true);
        }});
    }


    //  User adding.
    @PostMapping(value = "add")
    public ResponseEntity<Object> addNewUser(@RequestBody @Valid UserDto userDto,
                                             BindingResult bindingResult) throws JsonProcessingException {
        if (userDto == null) {
            return new ResponseEntity<>("Request body is absent", HttpStatus.NOT_FOUND);
        }

        //   Checking that added user is unique.
        boolean isExists = userService.getUserByLogin(userDto.getLogin()) != null;
        if(isExists){
            String jsonResponse = mapToJson(new HashMap<String, Object>(){{
                put("success", "false");
                put("errors", "User with such login is already exists!");
            }});
            return new ResponseEntity<>(jsonResponse, HttpStatus.BAD_REQUEST);
        }
        passwordValidator.validate(userDto, bindingResult);
        if(bindingResult.hasErrors()){
            String jsonResponse = getJSONResponse(bindingResult);
            return new ResponseEntity<>(jsonResponse, HttpStatus.BAD_REQUEST);
        }

        //  Adding new user.
        try {
            userService.createNewUserFromDto(userDto);
        }catch (IllegalArgumentException ex){
            String jsonResponse = mapToJson(new HashMap<String, Object>(){{
                put("success", "false");
                put("errors", ex.getMessage());
            }});
            return new ResponseEntity<>(jsonResponse, HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(new HashMap<String, Boolean>(){{
            put("Success", true);
        }});
    }



    protected String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }


    private String getJSONResponse(BindingResult bindingResult) throws JsonProcessingException {
        List<String> errors = getErrors(bindingResult);
        return mapToJson(new HashMap<String, Object>(){{
            put("success", "false");
            put("errors", errors);
        }});
    }

    public static List<String> getErrors(BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
    }
}
