package test.task.rest_user.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Map;

@Data
@AllArgsConstructor
public class UserErrorResponse {

        private Boolean success;

        private Map<String, String> errors;

}
