package test.task.rest_user.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
public class UserDto {

        @NotBlank(message = "Name cannot be empty")
        private String name;

        @NotBlank(message = "Login cannot be empty")
        private String login;

        @NotBlank(message = "Password cannot be empty")
        private String password;
        private int [] roles;
}
