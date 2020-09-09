package test.task.rest_user;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RestUserApplication implements ApplicationRunner {

    public static void main(String[] args) {

        SpringApplication.run(RestUserApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments arg0) throws Exception {

//        userService.addStartedUsers();

    }
}
