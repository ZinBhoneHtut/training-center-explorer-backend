package com.zbh.tce.config;

import com.zbh.tce.common.enums.RoleEnum;
import com.zbh.tce.entity.Role;
import com.zbh.tce.entity.User;
import com.zbh.tce.service.RoleService;
import com.zbh.tce.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author ZinBhoneHtut
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ContextRefreshedListener implements ApplicationListener<ContextRefreshedEvent> {

    private final PasswordEncoder encoder;
    private final UserService userService;
    private final RoleService roleService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("======= Starting Initialize ========");
        initRole();
        initUser();
        log.info("======= Finished Initialize ========");
    }

    private void initRole() {
        if (roleService.count() == 0) {
            Set<Role> roles = new HashSet<>();
            roles.add(new Role(RoleEnum.ADMIN));
            roles.add(new Role(RoleEnum.MODERATOR));
            roles.add(new Role(RoleEnum.USER));
            roleService.saveAll(roles);
            log.info("Role has been initialized");
        }
    }

    private void initUser() {
        if (userService.count() == 0) {
            Role role = roleService.findByRoleName(RoleEnum.ADMIN.getRoleName());
            User user = new User("Zin Bhone Htut", "zinbhonehtutt@gmail.com", encoder.encode("password123"));
            user.setRoles(Collections.singleton(role));
            userService.save(user);
            log.info("User has been initialized");
        }
    }

}
