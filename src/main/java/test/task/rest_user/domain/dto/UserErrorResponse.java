package test.task.rest_user.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserErrorResponse {

        private Boolean success;

        private List<String> errors;

}
