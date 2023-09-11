package com.cblandon.inversiones;

import com.cblandon.inversiones.Roles.Role;
import com.cblandon.inversiones.Roles.Roles;
import com.cblandon.inversiones.User.UserEntity;
import com.cblandon.inversiones.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@SpringBootApplication
public class InversionesApplication {

	public static void main(String[] args) {
		SpringApplication.run(InversionesApplication.class, args);
	}

}
