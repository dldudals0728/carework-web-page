package kr.edu.nynoa.dto;

import kr.edu.nynoa.constant.Role;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class BoardFormDto {

    private String category;

    private String section;

    @NotBlank(message = "게시글의 제목을 입력해주세요.")
    private String title;

    private String text;

    private String writer;

    private Role permission;
}
