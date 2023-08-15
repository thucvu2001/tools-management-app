package com.lifesup.toolsmanagement;

import com.lifesup.toolsmanagement.user.dto.UserDTO;
import com.lifesup.toolsmanagement.user.model.User;
import com.lifesup.toolsmanagement.user.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableWebSecurity
@EnableJpaRepositories
public class ToolsManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(ToolsManagementApplication.class, args);
    }

//    @Bean
//    CommandLineRunner commandLineRunner(UserService userService, PasswordEncoder passwordEncoder) {
//        UserDTO userDTO = UserDTO.builder()
//                .username("thucvu2001")
//                .password(passwordEncoder.encode("310501Asd@"))
//                .email("vuthuc3152001@gmail.com")
//                .phone("0942718565")
//                .build();
//        return args -> {
//            userService.saveWithDTO(userDTO, User.class, UserDTO.class);
//        };
//    }
}
