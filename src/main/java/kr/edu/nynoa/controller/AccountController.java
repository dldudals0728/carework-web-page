package kr.edu.nynoa.controller;

import kr.edu.nynoa.dto.AccountFormDto;
import kr.edu.nynoa.dto.LoginFormDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/account")
public class AccountController {
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginFormDto loginFormDto, Model m) {
        System.out.println(loginFormDto.getId());
        return new ResponseEntity<>(loginFormDto, HttpStatus.OK);
    }
    @GetMapping("/json")
    public String test(LoginFormDto loginFormDto) {
        return loginFormDto.getId();
    }
}
