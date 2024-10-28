package login.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
public class Member {

    private Long id;

    @NotEmpty
    private String email; //사용자 이름
    @NotEmpty
    private String password;
}
