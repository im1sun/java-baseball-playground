package login.domain;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class Member {

    private Long id;

    @NotEmpty
    private String email; //사용자 이름
    @NotEmpty
    private String password;
}
