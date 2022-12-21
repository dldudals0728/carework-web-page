package kr.edu.nynoa.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table
@Getter
@Setter
@ToString
//@Builder
public class Img {
    @Id
    @Column(name = "img_idx")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false, unique = true)
    private String imgName;

    @Column(nullable = false)
    private String mappingBoard;
}
