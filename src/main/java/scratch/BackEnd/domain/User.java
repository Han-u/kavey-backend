package scratch.BackEnd.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class User extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @OneToMany(mappedBy= "user")
    private List<Survey> surveys = new ArrayList<>(); // 읽기만 가능
    private Long kakaoid;
    private String nickname ;
    private String email;

    @Builder
    public User(Long kakaoid, String nickname, String email){
        this.kakaoid = kakaoid;
        this.nickname = nickname;
        this.email = email;
    }

}
