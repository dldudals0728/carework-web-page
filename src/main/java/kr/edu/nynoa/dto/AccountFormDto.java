package kr.edu.nynoa.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AccountFormDto {
    private String name;
    private String userRRN;
    private String phone;
    private String id;
    private String password;
    private String address;
    private String classNumber;
    private String classTime;
}
