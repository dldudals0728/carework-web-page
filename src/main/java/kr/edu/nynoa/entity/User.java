package kr.edu.nynoa.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "user")
@Getter
@Setter
@ToString
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false, length = 10)
    private String name;

    @Column(nullable = false, length = 14)
    private String RRN;

    @Column(nullable = false, length = 13)
    private String phone;

    @Column(nullable = false, name = "user_id")
    private String userId;

    @Column(nullable = false, length = 16)
    private String password;

    @Column(nullable = false)
    private String address;

    @Column(name = "class_number")
    private String classNumber;

    @Column(name = "class_time")
    private String classTime;
}
