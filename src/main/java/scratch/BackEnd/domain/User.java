package scratch.BackEnd.domain;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long kakaoid;

    @Column
    private String nickname ;

    @Column
    private String email;

    @Builder
    public User(Long kakaoid, String nickname, String email){
        this.kakaoid = kakaoid;
        this.nickname = nickname;
        this.email = email;
    }

}
