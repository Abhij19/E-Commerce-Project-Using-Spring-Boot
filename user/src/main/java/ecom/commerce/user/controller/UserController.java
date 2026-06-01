package ecom.commerce.user.controller;

import ecom.commerce.user.dto.UserRequest;
import ecom.commerce.user.dto.UserResponse;
import ecom.commerce.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return new ResponseEntity<>(userService.fetchAllUsers(),HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> createUsers(@RequestBody UserRequest userRequest) {
        userService.addUsers(userRequest);
        return ResponseEntity.ok("User added successfully");
    }


    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getSingleUser(@PathVariable String id) {
        log.info("Fetching user with id: {}", id);
        return userService.fetchUser(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable String id, @RequestBody UserRequest updatedUserRequest){
        boolean updated = userService.updateUser(id,updatedUserRequest);
        if(updated)
            return ResponseEntity.ok("User updated successfully");
        return ResponseEntity.notFound().build();
    }
}
