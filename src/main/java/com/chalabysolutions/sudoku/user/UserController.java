package com.chalabysolutions.sudoku.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping(path = UserLinks.LIST_USERS)
    public ResponseEntity<?> listUsers() {
        log.info("UsersController:  list users");
        List<UserEntity> resource = userService.getUsers();
        return ResponseEntity.ok(resource);
    }

    @PostMapping(path = UserLinks.ADD_USER)
    public ResponseEntity<?> saveUser(@RequestBody UserEntity user) {
        log.info("UsersController:  list users");
        UserEntity resource = userService.saveUser(user);
        return ResponseEntity.ok(resource);
    }
}
