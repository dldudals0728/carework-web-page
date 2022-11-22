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

## PasswordEncoder
org.springframework.security.crypto.password의 PasswordEncoder로 password를 암호화 했다.<br>
그리고 로그인 시에 입력받은 아이디와 비밀번호 중 비밀번호를 다시 암호화 하여 db를 검색했다.
```java
loginFormDto.getId();
passwordEncoder.encode(loginFormDto.getPassword());
```
이렇게 했는데 로그인이 도대체가 되질 않아...하

그래서 직접 회원가입을 하여 데이터를 넣고, 로그인 시 받은 비밀번호를 println 해보니 서로 다른 값이 출력되었다...!!<br>
지금 보니 해시값이 계속 달라지고, 그 해시값으로 암호화를 하다보니 매번 다른 암호화가 진행되는게 아닌가 싶다..!!<br>
그래서 찾아낸 방법은 PasswordEncoder.matches()이다!

```java
passwordEncoder.matches(rawPassword, encodedPassword);
```
이 함수를 통해서
```java
passwordEncoder.matches(loginFormDto.getPassword(), user.getPassword())
```
다음과 같이 처리하니까 true라는 값이 나왔다!

* 결론
> 로그인 시 받은 password를 암호화해서 검색하지 말고, 받은 password와 db에 저장된 암호화된 password를 matches 함수를 이용해 비교하자!

# AWS Server
나는 mysql로 서버를 구성하지 않고, mariadb를 사용했기 때문에 다른 부분만 작성!

### MobaXterm
[MobaXterm AWS 연결](https://minjii-ya.tistory.com/23)

1. 좌측 상단 Session 클릭
2. Remote host: ec2의 Elastic IP 주소 입력
3. Specify username 체크 후 이름: ubuntu(ec2-user 아님!)
4. 하단의 Advanced SSH settings에서 Use private key 체크 후 .pem 키 파일 선택
5. OK로 설정 완료 후 에러가 나오지 않는다면 설정 완료!

### RDS 설정 시
1. 데이터 베이스 생성 방식 -> mariadb 선택(프리티어)
2. 해당 데이터 베이스 생성 시 마스터 사용자 이름과 마스터 사용자 암호가 DB의 사용자 이름과 사용자 암호이다.
3. mariadb도 mysql과 같이 포트번호를 3306으로 해준다(3307 아님!)
4. RDS 보안 그룹 설정 시 유형을 MYSQL/Aurora으로 선택한다(mysql과 동일)
5. 워크밴치를 이용하여 테스트를 할 때는 mysql workbench를 사용한다. (처음에 버전 오류가 뜨는데 continue를 선택하면 다음 화면에 테스트 결과가 나옴)

### application.properties
처음에 github에 application.properties를 commit했었는데, RDS 데이터베이스 엔드포인트가 노출되기 때문에 삭제했다.
```
원격 저장소에서만 삭제 명령어
git rm --cached /main/resources/application.properties

원격 & 로컬 저장소 모두 삭제 명령어
git rm /main/resources/application.properties
```

그리고 mysql과 다른 application.properties를 구성해준다.
portNumber 뒤에 db이름을 붙히면 안된다!!<br>
(이유를 예측해 보자면 db 이름이 아니라 db 계정이 들어가는 곳인데, db계정이 아직 생성되지 않아 root 계정만 있으니까 넣으면 안되는거 아닐까?)
```properties
spring.datasource.driver-class-name=org.mariadb.jdbc.Driver
# spring.datasource.url=jdbc:mariadb://RDS데이터베이스엔드포인트:3306/db이름     # 이렇게 하면 안됨
spring.datasource.url=jdbc:mariadb://RDS데이터베이스엔드포인트:3306
spring.datasource.username=마스터이름
spring.datasource.password=패스워드
```

+ TIP
application.properties는 git clone으로 받아지지 않으니까, spring-backup/resources/application.properties 로 백업해 놓음!

### maven 명령어 test 유무
```
test가 없을 경우
$ mvn package
```

```
test가 있을 경우
$ mvn package -Dmaven.test.skip=true
```

여기서 테스트는, repository나 service의 테스트를 말하는게 아닌 것 같다.(test가 없는 명령어를 치니까 오류)

mvn package -Dmaven.test.skip=true 명령어를 사용했을 때만 된 걸 보니, test 디렉토리가 있냐 없냐로 나뉘는 것 같다. (나는 있음)

## Error Report
에러가 log에 나오긴 하나, 서버는 돌아가는 에러. 테스트중!
```
executing DDL "drop table if exists user" via JDBC Statement at org.hibernate.tool.schema.internal.exec.GenerationTargetToDatabase.accept
```
JPA에 User라는 entity가 table name이 user로 되어 있으면 에러가 나옴
```java
@Entity
@Table(name="users")
public class User {..}
```
이렇게 한번 해보자!

# CAFE24 domain port forwarding
카페24에서 도메인을 구매하면 1영업일이 소요된다! 주의!