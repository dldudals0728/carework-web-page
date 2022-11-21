package kr.edu.nynoa.controller;

import kr.edu.nynoa.dto.AccountFormDto;
import kr.edu.nynoa.dto.LoginFormDto;
import kr.edu.nynoa.entity.User;
import kr.edu.nynoa.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginFormDto loginFormDto, Model m) {

        return new ResponseEntity<>(loginFormDto, HttpStatus.OK);
    }

    @PostMapping("/addUser")
    public ResponseEntity<Object> account(@RequestBody AccountFormDto accountFormDto, Model m) {
        System.out.println(accountFormDto.toString());
        User user = User.createUser(accountFormDto, passwordEncoder);
        userService.saveUser(user);
        return new ResponseEntity<>(accountFormDto, HttpStatus.OK);
    }

    @GetMapping("/json")
    public String test(LoginFormDto loginFormDto) {
        return "HELLO!";
    }
}
