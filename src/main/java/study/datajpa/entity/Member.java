package study.datajpa.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class Member {

    @Id
    @GeneratedValue
    private Long id;
    private String username;

    // private이 아닌 protected. JPA에서 활용할 때 프록시 객체 생성을 위해 열어두어야 한다.
    protected Member() {
    }

    public Member(String username) {
        this.username = username;
    }
}
