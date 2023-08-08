package com.zbh.tce.service;

import com.zbh.tce.common.query.QueryHelp;
import com.zbh.tce.entity.User;
import com.zbh.tce.entity.criteria.UserCriteria;
import com.zbh.tce.exception.ResourceNotFoundException;
import com.zbh.tce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author ZinBhoneHtut
 */
@Service
@RequiredArgsConstructor
public class UserService implements CrudService<User, UserCriteria> {

    private final UserRepository userRepository;

    @Override
    public Optional<User> findById(long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Page<User> findAll(UserCriteria userCriteria, Pageable pageable) {
        return userRepository.findAll(((root, query, criteriaBuilder) ->
                QueryHelp.getPredicate(root, userCriteria, query, criteriaBuilder)), pageable);
    }

    @Override
    public List<User> findAll(UserCriteria userCriteria) {
        return userRepository.findAll(((root, query, criteriaBuilder) ->
                QueryHelp.getPredicate(root, userCriteria, query, criteriaBuilder)));
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void update(long id, User user) throws ResourceNotFoundException {
       User _user = this.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        _user.setName(user.getName());
        _user.setEmail(user.getEmail());
        _user.setTelephone(user.getTelephone());
        _user.setGender(user.getGender());
        _user.setRoles(user.getRoles());
        this.save(_user);
    }

    @Override
    public void deleteById(long id) {
        userRepository.deleteById(id);
    }

    @Override
    public long count() {
        return userRepository.count();
    }

    public boolean isUserExists(String userName) {
        return userRepository.existsByName(userName);
    }

}
