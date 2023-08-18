package com.zbh.tce.config;

import com.zbh.tce.entity.Role;
import com.zbh.tce.entity.User;
import com.zbh.tce.entity.enums.RoleEnum;
import com.zbh.tce.service.RoleService;
import com.zbh.tce.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
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

    @Value("${user.default.password}")
    private String defaultPassword;

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
            roles.add(new Role(RoleEnum.ADMIN, "For admin who can manage the core operations."));
            roles.add(new Role(RoleEnum.MODERATOR, "For moderator who can manage the contents."));
            roles.add(new Role(RoleEnum.USER, "For general user"));
            roleService.saveAll(roles);
            log.info("Role has been initialized");
        }
    }

    private void initUser() {
        if (userService.count() == 0) {
            List<Role> roleList = roleService.findAll();
            User user = User.builder().name("Zin Bhone Htut").email("zinbhonehtutt@gmail.com")
                    .telephone("0123456789").password(encoder.encode(defaultPassword))
                    .roles(new HashSet<>(roleList)).build();
            userService.save(user);
            log.info("User has been initialized");
        }
    }

}
