package test.task.rest_user.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import test.task.rest_user.domain.dto.UserDto;
import test.task.rest_user.domain.dto.UserErrorResponse;
import test.task.rest_user.services.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Sql("/Refresh_Users.sql")
class UserRestControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;

    private final static UserDto validUserDto = new UserDto("Dmitry", "dima", "55AA", new int[]{1, 2, 3});
    private final static UserDto invalidUserDto = new UserDto("", "", "aaa", new int[]{});
    private final static UserDto badRolesUserDto = new UserDto("Dmitry", "dima", "55AA", new int[]{1, 2, 3, 4, 5, 6});
    private final static String successString = "{\"Success\":true}";
    private final static String wrongPasswordMsg = "Your password must contains one uppercase letter and one number!";
    private final static String emptyNameMsg = "Name cannot be empty";
    private final static String emptyLoginMsg = "Login cannot be empty";
    private final static String badRolesMsg = "Invalid roles data in DtoUser!";

    @Test
    void shouldDeleteUserWhenGivenIdExists() throws Exception {
        this.mockMvc.perform(delete("/rest/v1/users/3/delete")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().string("User has removed successfully!"));

        assertEquals(2, userService.findAll().size());
    }

    @Test
    void shouldNotDeleteUserWhenGivenInvalidId() throws Exception {
        this.mockMvc.perform(delete("/rest/v1/users/4/delete")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Cannot found user with such id"));

        assertEquals(3, userService.findAll().size());
    }

    @Test
    void shouldUpdateUserAndItsRolesWhenAllDataIsValid() throws Exception {
        String toJson = new ObjectMapper().writeValueAsString(validUserDto);

        this.mockMvc.perform(put("/rest/v1/users/3/edit")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(toJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(successString));

        assertEquals(validUserDto.getName(), userService.findById(3).getName());
        assertEquals(validUserDto.getRoles().length, userService.findById(3).getRoles().size());
    }

    @Test
    void tryAddNonExistedRolesToExistsUserShouldReturnError() throws Exception {
        String toJson = new ObjectMapper().writeValueAsString(invalidUserDto);

        MockHttpServletResponse response = this.mockMvc.perform(put("/rest/v1/users/3/edit")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(toJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        UserErrorResponse errorResponse = new ObjectMapper().readValue(response.getContentAsString(), UserErrorResponse.class);

        assertEquals(3, errorResponse.getErrors().size());
        assertTrue(errorResponse.getErrors().stream().anyMatch(x -> x.equals(wrongPasswordMsg)));
        assertTrue(errorResponse.getErrors().stream().anyMatch(x -> x.equals(emptyLoginMsg)));
        assertTrue(errorResponse.getErrors().stream().anyMatch(x -> x.equals(emptyNameMsg)));
        assertEquals(3, userService.findAll().size());
    }

    @Test
    void shouldNotUpdateUserWhenGivenDataIsInvalid() throws Exception {
        String toJson = new ObjectMapper().writeValueAsString(badRolesUserDto);

        MockHttpServletResponse response = this.mockMvc.perform(put("/rest/v1/users/3/edit")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(toJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        UserErrorResponse errorResponse = new ObjectMapper().readValue(response.getContentAsString(), UserErrorResponse.class);

        assertTrue(errorResponse.getErrors().stream().anyMatch(x -> x.equals(badRolesMsg)));
        assertEquals(3, userService.findAll().size());
    }

    @Test
    void shouldCreateNewUserWhenAllDataIsValid() throws Exception {
        String toJson = new ObjectMapper().writeValueAsString(validUserDto);

        this.mockMvc.perform(post("/rest/v1/users/add")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(toJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(successString));

        assertTrue(userService.findByName(validUserDto.getName()).isPresent());
        assertEquals(validUserDto.getRoles().length,
                    userService.findByName(validUserDto.getName()).get().getRoles().size());
    }

    @Test
    void shouldNotCreateNewUserWhenGivenDataIsInvalid() throws Exception {
        String toJson = new ObjectMapper().writeValueAsString(invalidUserDto);

        MockHttpServletResponse response = this.mockMvc.perform(post("/rest/v1/users/add")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(toJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        UserErrorResponse errorResponse = new ObjectMapper().readValue(response.getContentAsString(), UserErrorResponse.class);

        assertEquals(3, errorResponse.getErrors().size());
        assertTrue(errorResponse.getErrors().stream().anyMatch(x -> x.equals(wrongPasswordMsg)));
        assertTrue(errorResponse.getErrors().stream().anyMatch(x -> x.equals(emptyLoginMsg)));
        assertTrue(errorResponse.getErrors().stream().anyMatch(x -> x.equals(emptyNameMsg)));
        assertEquals(3, userService.findAll().size());
    }
}
