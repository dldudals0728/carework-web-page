.README.md
# spring boot project - NYNOA web page
## initialize
1. spring initializer를 이용하여 spring boot project 생성
2. application.properties 한글 주석 깨짐 처리 방지
+ File > Settings > Editor > File Encodings > Transparent native-to-ascii conversion 체크
3. application.properties 작성


# NEED
1. github에 프로젝트를 올릴 건데, DB 계정 이름, password등을 어떻게 숨길 것인가?

# ERROR Report
### 첫번째 에러
```
***************************
APPLICATION FAILED TO START
***************************

Description:

Parameter 1 of constructor in kr.edu.nynoa.controller.AccountController required a bean of type 'org.springframework.security.crypto.password.PasswordEncoder' that could not be found.


Action:

Consider defining a bean of type 'org.springframework.security.crypto.password.PasswordEncoder' in your configuration.


Process finished with exit code 0
```
해결책
> SecurityConfig.java 추가!

## 길고 길었던 CORS 에러...
spring security를 사용하면서 CORS를 해결하려 하니 어려움이 많았다.

```java
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
        ...
```
처음에 CORS 허용 방법으로 사용했던 CrossOrigin 어노테이션이 작동하지 않았다.

구글링을 해보니, 대부분 deprecated된 WebSecurityConfigurerAdapter를 상속받아 구현한 security config를 사용하고 있었다.

내가 원하는 해결책은 SecurityFilterChain에서 CORS를 허용하는 방법이라는 것을 알았고, 결국 해결했다.

```java
import org.springframework.context.annotation.Bean;

@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.cors();
    http.csrf().disable();
    return http.build();
}
```
이렇게 SecurityFilterChain에서 CORS를 허용할 수 있게 됐다!

[출처](https://velog.io/@yeony402/Spring-Spring-Boot-Security-CORS-%ED%95%B4%EA%B2%B0) 감사합니다 복받으세요

## crazy error
accountDto를 만들 때 사용했던 필드 중 이런게 있다.
```java
private String RRN;
```
react에서 axios를 이용하여 값을 전달하고, spring에서는 accountFormDto로 받았다.

근데 계속 이런 오류가 떴다.
```
Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception [Request processing failed; nested exception is org.springframework.dao.DataIntegrityViolationException: not-null property references a null or transient value
```
이상함을 감지하고 Entity에 toString 어노테이션을 추가하여 확인한 결과
```
AccountFormDto(name=홍길동, RRN=null, phone=01012345678, id=dldudals, password=abc123, address=경기도, classNumber=1기, classTime=주간)
```
RRN에 null값이 들어가는 것을 확인했다!

처음에는 front에서 input tag의 name 값을 맞추지 않아서 그랬나 했더니 그거도 아니었다.

결국에 알아낸 이유는 Jackson 때문이었다!

[출처](https://bcp0109.tistory.com/309) 감사합니다 복받으세요

get 메서드의 변환에 따라 변수 명(?)이 달라지는 이유였다..
결론은
> DTO 의 필드명이 대문자로 시작하면 Request 요청 시 값이 제대로 들어오지 않는다!

이다. 필드명은 무조건 소문자로 시작하는 걸로...
