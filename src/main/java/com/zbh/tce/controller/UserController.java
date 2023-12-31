package com.zbh.tce.controller;

import com.zbh.tce.common.utils.SecurityUtils;
import com.zbh.tce.dto.DataTablesOutput;
import com.zbh.tce.dto.UserDTO;
import com.zbh.tce.entity.User;
import com.zbh.tce.entity.criteria.UserCriteria;
import com.zbh.tce.exception.ResourceNotFoundException;
import com.zbh.tce.mapper.UserMapper;
import com.zbh.tce.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.zbh.tce.common.constant.UrlConstant.USER_API;

/**
 * @author ZinBhoneHtut
 */
@Slf4j
@RestController
@RequestMapping(USER_API)
@RequiredArgsConstructor
class UserController extends BaseController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN_READ')")
    public ResponseEntity<DataTablesOutput<UserDTO>> getUsersWithCriteria(UserCriteria userCriteria, Pageable pageable) {
        log.trace("Inside getUsersWithCriteria method");
        log.debug("User criteria: {} | Pageable: {}", userCriteria.toString(), pageable.toString());
        Page<User> userPage = userService.findAll(userCriteria, pageable);
        return new ResponseEntity<>(createDataTableOutput(userPage.map(userMapper::toDTO), userService.count(userCriteria)), HttpStatus.OK);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN_READ')")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        log.trace("Inside getAllUsers method");
        List<UserDTO> userDTOList = userMapper.toDTO(userService.findAll());
        return ResponseEntity.ok().body(userDTOList);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN_READ')")
    public ResponseEntity<UserDTO> getSpecificUser(@PathVariable("id") long id) {
        log.trace("Inside getSpecificUser method");
        if(id == 0) return ResponseEntity.ok().body(new UserDTO());
        User user = userService.findById(id).orElseThrow(() -> new ResourceNotFoundException("User doesn't exist."));
        UserDTO userDTO = userMapper.toDTO(user);
        return ResponseEntity.ok().body(userDTO);
    }

    /**
     * Note: This method will return false even the username is in database if the user check its own username
     */
    @GetMapping("/check-user-exists")
    @PreAuthorize("hasRole('ADMIN_READ')")
    public ResponseEntity<Boolean> checkUserAlreadyExists(@RequestParam(name = "userName", required = true) String userName) {
        log.trace("Inside check user already exists method");
        boolean isUserAlreadyExists = !StringUtils.equalsIgnoreCase(SecurityUtils.getCurrentUsername(), userName)
                && userService.isUserExists(userName);
        return new ResponseEntity<>(isUserAlreadyExists, HttpStatus.OK);
    }

    /**
     * Note: This method will return false even the email is in database if the user check its own email
     */
    @GetMapping("/check-email-exists")
    @PreAuthorize("hasRole('ADMIN_READ')")
    public ResponseEntity<Boolean> checkEmailAlreadyExists(@RequestParam(name = "email", required = true) String email) {
        log.trace("Inside check user already exists method");
        boolean isEmailAlreadyExists = !StringUtils.equalsIgnoreCase(SecurityUtils.getCurrentUsername(), email)
                && userService.isEmailExists(email);
        return new ResponseEntity<>(isEmailAlreadyExists, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN_WRITE')")
    public ResponseEntity<HttpStatus> createUser(@Valid @RequestBody UserDTO userDTO) {
        log.trace("Inside createUser method");
        try {
            User user = userMapper.toEntity(userDTO);
            user.setPassword(passwordEncoder.encode("changeme"));
            userService.save(user);
            log.info("User was created successfully");
        } catch (Exception e) {
            log.error("The error was caused by : {}", e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN_UPDATE')")
    public ResponseEntity<HttpStatus> updateUser(@RequestParam(required = true, value = "id") long id, @Valid @RequestBody UserDTO userDTO) {
        log.trace("Inside updateUser method");
        userService.update(id, userMapper.toEntity(userDTO));
        log.info("User was updated successfully");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN_DELETE')")
    public ResponseEntity<?> deleteUser(@PathVariable("id") long id) {
        log.trace("Inside deleteUser method");
        try {
            userService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (EmptyResultDataAccessException emptyResultException) {
            log.error("EmptyResultDataAccessException: {}", emptyResultException.getMessage());
            return new ResponseEntity<>("The entity with your requested id : " + id + " not found",
                    HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("The error was caused by : {}", e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
