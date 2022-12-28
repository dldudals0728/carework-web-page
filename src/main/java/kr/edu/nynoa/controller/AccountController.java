package kr.edu.nynoa.controller;

import kr.edu.nynoa.dto.AccountFormDto;
import kr.edu.nynoa.dto.LoginFormDto;
import kr.edu.nynoa.entity.User;
import kr.edu.nynoa.manager.SessionManager;
import kr.edu.nynoa.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.CookieGenerator;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.HashMap;

@CrossOrigin
@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/sessionLogin")
    public ResponseEntity<Object> sessionLogin(@RequestBody LoginFormDto loginFormDto, HttpServletResponse response) {
        User user = userService.login(loginFormDto, passwordEncoder, response);
        HashMap<Object, Object> map = new HashMap<>();

        if (user == null) {
            map.put("status", 500);
        } else {
            map.put("status", 200);
            map.put("userName", user.getName());
            map.put("classNumber", user.getClassNumber());
            map.put("classTime", user.getClassTime());
            map.put("role", user.getRole());
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping("/homeLogin")
    public ResponseEntity<Object> homeLogin(HttpServletRequest request) {
        HashMap<String, Object> map = new HashMap<>();

        User user = userService.getUserSession(request);

        if (user == null) {
            map.put("status", 500);
            return new ResponseEntity<>(map, HttpStatus.OK);
        } else {
            map.put("status", 200);
            map.put("user", user);
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }

    @PostMapping("/cookieLogin")
    public ResponseEntity<Object> cookieLogin(HttpServletResponse response, @RequestBody LoginFormDto loginFormDto) {
        System.out.println("cookie login start");
        System.out.println(loginFormDto.toString());
        User user = userService.login(loginFormDto, passwordEncoder, response);
        HashMap<Object, Object> map = new HashMap<>();

        if (user == null) {
            map.put("status", 500);
        } else {
            map.put("status", 200);
            map.put("userName", user.getName());
            map.put("classNumber", user.getClassNumber());
            map.put("classTime", user.getClassTime());
            map.put("role", user.getRole());

            // Default Cookie settings
            Cookie cookie = new Cookie("userid", "dldudals");
            cookie.setPath("/");
            cookie.setMaxAge(30 * 60);
            cookie.setSecure(true);
            response.addCookie(cookie);
            response.addHeader("Set-Cookie", cookie.toString());

            // Response Cookie settings
            // ResponseCookie vs Cookie: Chrome 80 부터 sameSite의 default 값이 "Lax"로 변경되었다. 따라서 sameSite 값을 None으로 바꿔주어야 한다.
            // but, Cookie는 sameSite 값을 변경하는 api가 없다! ResponseCookie는 있다 ^^
//            ResponseCookie cookie = ResponseCookie.from("userid", "dldudals")
//                    .httpOnly(true)
//                    .secure(true)
//                    .path("/")
//                    .maxAge(60 * 60 * 60)
//                    .sameSite("None")
//                    .domain("localhost")
//                    .build();
//
//            response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
//            response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.SET_COOKIE);

            // Cookie Generator
//            CookieGenerator cg = new CookieGenerator();
//
//            cg.setCookieName("cookieName");
//            cg.addCookie(response, "cookieValue");
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping("/getUserSession")
    public ResponseEntity<Object> getUserSession(HttpServletRequest request) {
        HashMap<Object, Object> map = new HashMap<>();
        assert request != null;
        User user = userService.getUserSession(request);
        if (user == null) {
            System.out.println("there is no login info!");
            map.put("status", 500);
            return new ResponseEntity<>(map, HttpStatus.OK);
        } else {
            System.out.println("there is login info!");
            map.put("status", 200);
            map.put("login", user);
        }

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginFormDto loginFormDto, HttpServletResponse response) {
        User user = userService.login(loginFormDto, passwordEncoder, response);
        HashMap<Object, Object> map = new HashMap<>();

        if (user == null) {
            map.put("status", 500);
        } else {
            map.put("status", 200);
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request) {
        assert request != null;
        userService.userLogout(request);
    }

    @GetMapping("/idCheck")
    public ResponseEntity<Object> idCheck(@RequestParam(value = "id", required = true) String id) {
        User isUser = userService.findUser(id);
        HashMap<Object, Object> map = new HashMap<>();

        if (isUser == null) {
            map.put("status", 200);
        } else {
            map.put("status", 500);
        }

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping("/addUser")
    public ResponseEntity<Object> account(@Valid @RequestBody AccountFormDto accountFormDto, BindingResult bindingResult) {
        HashMap<Object, Object> map = new HashMap<>();
        if (bindingResult.hasErrors()) {
            map.put("status", 500);
            map.put("error code", bindingResult.getAllErrors());
            return new ResponseEntity<>(map, HttpStatus.OK);
        } else {
            map.put("status", 200);
        }
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
