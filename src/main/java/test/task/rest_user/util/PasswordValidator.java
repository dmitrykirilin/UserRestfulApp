package test.task.rest_user.util;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import test.task.rest_user.domain.dto.UserDto;

@Component
public class PasswordValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return UserDto.class.equals(aClass);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        UserDto user = (UserDto) obj;
        if (!user.getPassword().matches("^(?=.*[0-9])(?=.*[A-Z]).{2,}$")){
            errors.rejectValue("password", "","Your password must contains one uppercase letter and one number!");
        }
    }
}
