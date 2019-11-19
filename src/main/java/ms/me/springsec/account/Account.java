package ms.me.springsec.account;

import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@Table(name = "account", uniqueConstraints = @UniqueConstraint(name = "uni1_account", columnNames = "username"))
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String role;

    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }

    @Builder
    public Account(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
}
