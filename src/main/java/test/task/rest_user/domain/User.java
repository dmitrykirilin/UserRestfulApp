package test.task.rest_user.domain;


import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter @Setter
@NoArgsConstructor @RequiredArgsConstructor
@EqualsAndHashCode(of = {"name", "username"})
@ToString(of = {"name", "username"})
@Table(name = "users")
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView({Views.Id.class})
    private Integer id;

    @NonNull
    @JsonView({Views.IdName.class})
    private String name;

    @NonNull
    @JsonView({Views.IdName.class})
    private String login;

    @NonNull
    @JsonView({Views.FullUser.class})
    private String password;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role_name")
    @JsonView({Views.UserAndRoles.class})
    private Set<Role> roles;

    public User(String name, String login, String password, Set<Role> roles) {
        this.name = name;
        this.login = login;
        this.password = password;
        this.roles = roles;
    }
}
