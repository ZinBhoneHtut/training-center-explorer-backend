package com.zbh.tce.entity;

import com.zbh.tce.common.enums.RoleEnum;
import com.zbh.tce.entity.audit.Audit;
import com.zbh.tce.entity.audit.AuditListener;
import com.zbh.tce.entity.audit.IAudit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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

    @Column(name = "role_name", length = 30, unique = true)
    private String roleName;

    @Column(name = "description", length = 255)
    private String description;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    @Embedded
    private Audit audit;

    public Role(RoleEnum roleEnum) {
        this.roleName = roleEnum.getRoleName();
        this.description = roleEnum.getDescription();
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
