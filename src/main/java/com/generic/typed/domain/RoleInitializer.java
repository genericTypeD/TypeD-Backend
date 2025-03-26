package com.generic.typed.domain;

import com.generic.typed.repository.RoleRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class RoleInitializer implements ApplicationRunner {

    private final RoleRepository roleRepository;

    public RoleInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        // ROLE_USER가 없으면 생성
        if (!roleRepository.findByName("ROLE_USER").isPresent()) {
            Role userRole = new Role();
            userRole.setRoleId(1L);
            userRole.setName("ROLE_USER");
            roleRepository.save(userRole);
        }

        // 필요한 경우 ROLE_ADMIN도 생성
        if (!roleRepository.findByName("ROLE_ADMIN").isPresent()) {
            Role adminRole = new Role();
            adminRole.setRoleId(2L);
            adminRole.setName("ROLE_ADMIN");
            roleRepository.save(adminRole);
        }
    }
}