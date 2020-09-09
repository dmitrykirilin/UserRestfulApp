package test.task.rest_user.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

        @NotBlank(message = "Name cannot be empty")
        private String name;

        @NotBlank(message = "Login cannot be empty")
        private String login;

        @NotBlank(message = "Password cannot be empty")
        private String password;
        private int [] roles;
}
