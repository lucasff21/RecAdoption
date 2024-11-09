package com.recsys.recPet.controller;

import com.recsys.recPet.dto.CreateUserDTO;
import com.recsys.recPet.dto.LoginUserDto;
import com.recsys.recPet.dto.RecoveryJwtTokenDto;
import com.recsys.recPet.dto.UserDTO;
import com.recsys.recPet.model.User;
import com.recsys.recPet.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<RecoveryJwtTokenDto> authenticateUser(@RequestBody LoginUserDto loginUserDto) {
        RecoveryJwtTokenDto token = userService.authenticateUser(loginUserDto);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createUser(@RequestBody CreateUserDTO createUserDto) {
        userService.createUser(createUserDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable Long id, @RequestBody UserDTO userDTO){
        User user  = new User();
        BeanUtils.copyProperties(userDTO, user);
        user.setId(id);
        User userUpdate = userService.update(user);

        return ResponseEntity.status(HttpStatus.OK).body(userUpdate);

    }

    @GetMapping("/findall")
    public ResponseEntity<List<User>> findAll(){
        List<User> userList = userService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(userList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id){
        User user = userService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/findbyemail/{email}")
    public ResponseEntity<?> findByEmail(@PathVariable String email){
        Optional<User> user = userService.findByEmail(email);

        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário não encontrado");
        }

        return ResponseEntity.status(HttpStatus.OK).body(user.get());

    }
}
