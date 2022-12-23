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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;

@CrossOrigin
@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    private final SessionManager sessionManager;

    @PostMapping("/sessionLogin")
    public ResponseEntity<Object> sessionLogin(@RequestBody LoginFormDto loginFormDto, HttpServletResponse response) {
        User user = userService.loginUser(loginFormDto, passwordEncoder);
        HashMap<Object, Object> map = new HashMap<>();

        if (user == null) {
            map.put("status", 500);
        } else {
            map.put("status", 200);
            map.put("userName", user.getName());
            map.put("classNumber", user.getClassNumber());
            map.put("classTime", user.getClassTime());
            map.put("role", user.getRole());
            sessionManager.createSession(user, response);
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping("/homeLogin")
    public Model homeLogin(HttpServletRequest request, Model model) {
        User user = (User) sessionManager.getSession(request);
        System.out.println(user);
        model.addAttribute("user", user);

        return model;
    }

    @PostMapping("/cookieLogin")
    public ResponseEntity<Object> cookieLogin(HttpServletResponse response, @RequestBody LoginFormDto loginFormDto) {
        System.out.println(loginFormDto.toString());
        User user = userService.loginUser(loginFormDto, passwordEncoder);
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
//            Cookie cookie = new Cookie("userid", "dldudals");
//            cookie.setPath("/");
//            cookie.setMaxAge(30 * 60);
//            cookie.setSecure(true);
//            response.addCookie(cookie);
//            response.addHeader("Set-Cookie", cookie.toString());

            // Response Cookie settings
            // ResponseCookie vs Cookie: Chrome 80 부터 sameSite의 default 값이 "Lax"로 변경되었다. 따라서 sameSite 값을 None으로 바꿔주어야 한다.
            // but, Cookie는 sameSite 값을 변경하는 api가 없다! ResponseCookie는 있다 ^^
            ResponseCookie cookie = ResponseCookie.from("userid", "dldudals")
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(60 * 60)
                    .sameSite("None")
                    .build();

            response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
            response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.SET_COOKIE);
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping("/getCookie")
    public ResponseEntity<Object> getCookie(HttpServletRequest request) {
        System.out.println("my request: " + request);
        if (request == null) {
            System.out.println("request is null!!");
        } else {
            System.out.println("request is not null!!");
        }
        assert request != null;
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            System.out.println("cookies is null!");
        } else {
            System.out.println("cookies is not null!");
        }
        System.out.println("cookies length: " + cookies.length);
        for (int i = 0; i < cookies.length; i++) {
            System.out.println(i + "번째 쿠키 이름: " + cookies[i].getName());
            System.out.println(i + "번째 쿠키 값: " + cookies[i].getValue());
        }

        return new ResponseEntity<>("cookie", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginFormDto loginFormDto) {
        User user = userService.loginUser(loginFormDto, passwordEncoder);
        HashMap map = new HashMap<>();

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

    @GetMapping("/idcheck")
    public ResponseEntity<Object> idcheck(@RequestParam(value = "id", required = true) String id) {
        User isUser = userService.findUser(id);
        HashMap map = new HashMap<>();

        if (isUser == null) {
            map.put("status", 200);
        } else {
            map.put("status", 500);
        }

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping("/addUser")
    public ResponseEntity<Object> account(@Valid @RequestBody AccountFormDto accountFormDto, BindingResult bindingResult) {
        HashMap map = new HashMap<>();
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
