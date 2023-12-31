package com.zbh.tce.entity;

import com.zbh.tce.entity.audit.Audit;
import com.zbh.tce.entity.audit.AuditListener;
import com.zbh.tce.entity.audit.IAudit;
import com.zbh.tce.entity.enums.RoleEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author ZinBhoneHtut
 */
@Getter
@Setter
@Entity
@Table(name = "role")
@EntityListeners(AuditListener.class)
@NoArgsConstructor
public class Role implements IAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "role_name", length = 30, unique = true)
    private RoleEnum roleName;

    @Column(name = "description", length = 255)
    private String description;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    @Embedded
    private Audit audit;

    public Role(RoleEnum roleEnum, String description) {
        this.roleName = roleEnum;
        this.description = description;
    }

    public List<SimpleGrantedAuthority> getAuthorities() {
        return this.roleName.getAuthorities();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Role role = (Role) o;
        return Objects.equals(roleName, role.roleName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleName);
    }
}
