package com.example.demo.bootstrap;

import com.example.demo.dto.UserDTO;
import com.example.demo.model.RoleEntity;
import com.example.demo.model.RoleEnum;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AuthenticationService;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;

/**
 * Creates default users who can log-in to the system, one with role admin and other with role editor.
 */
@Component
public class UserSeeder implements ApplicationListener<ContextRefreshedEvent> {
    private final AuthenticationService authenticationService;
    private final RoleRepository roleRepository;


    public UserSeeder(AuthenticationService authenticationService, RoleRepository roleRepository) {
        this.authenticationService = authenticationService;
        this.roleRepository = roleRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        createRoles();
        createUsers();
    }

    private void createRoles() {
        RoleEnum[] roleNames = new RoleEnum[]{RoleEnum.ADMIN, RoleEnum.EDITOR, RoleEnum.VIEWER};
        Map<RoleEnum, String> roleDesc = Map.of(
                RoleEnum.VIEWER, "Default user role",
                RoleEnum.EDITOR, "Editor role",
                RoleEnum.ADMIN, "Administrator role"
        );

        Arrays.stream(roleNames).forEach((name) -> {
            final RoleEntity roleToCreate = new RoleEntity(name, roleDesc.get(name));
            roleRepository.save(roleToCreate);
        });
    }

    private void createUsers(){
        authenticationService.signup(new UserDTO("admin", "admin"), RoleEnum.ADMIN);
        authenticationService.signup(new UserDTO("editor", "editor"), RoleEnum.EDITOR);
    }


}