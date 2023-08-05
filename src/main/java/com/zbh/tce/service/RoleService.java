package com.zbh.tce.service;

import com.zbh.tce.entity.Role;
import com.zbh.tce.entity.criteria.RoleCriteria;
import com.zbh.tce.query.utils.QueryHelp;
import com.zbh.tce.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author ZinBhoneHtut
 */
@Service
@RequiredArgsConstructor
public class RoleService implements CrudService<Role, RoleCriteria> {

    private final RoleRepository roleRepository;

    @Override
    public Optional<Role> findById(long id) {
        return roleRepository.findById(id);
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public List<Role> findAll(RoleCriteria roleCriteria) {
        return roleRepository.findAll(((root, query, criteriaBuilder) ->
                QueryHelp.getPredicate(root, roleCriteria, query, criteriaBuilder)));
    }

    @Override
    public Page<Role> findAll(RoleCriteria roleCriteria, Pageable pageable) {
        return roleRepository.findAll(((root, query, criteriaBuilder) ->
                QueryHelp.getPredicate(root, roleCriteria, query, criteriaBuilder)), pageable);
    }

    @Override
    public Role save(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public void deleteById(long id) {
        roleRepository.deleteById(id);
    }

    @Override
    public long count() {
        return roleRepository.count();
    }

    public Role findByRoleName(String roleName) {
        return roleRepository.findByRoleName(roleName);
    }

    public List<Role> saveAll(Set<Role> roles) {
        return roleRepository.saveAll(roles);
    }

}
