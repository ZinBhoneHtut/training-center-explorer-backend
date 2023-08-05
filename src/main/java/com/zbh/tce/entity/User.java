package com.zbh.tce.entity;

import com.zbh.tce.entity.audit.Audit;
import com.zbh.tce.entity.audit.AuditListener;
import com.zbh.tce.entity.audit.IAudit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

/**
 * @author ZinBhoneHtut
 */
@Getter
@Setter
@Entity
@Table(name = "user_profile")
@NoArgsConstructor
@EntityListeners(AuditListener.class)
public class User implements Serializable, IAudit {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "name", length = 60, nullable = false, unique = true)
    private String name;
    @Column(name = "email", length = 80, unique = true)
    private String email;
    @Column(name = "password", length = 255)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    private Set<Role> roles;

    @Embedded
    private Audit audit;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(name, user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

}
