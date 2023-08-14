package com.zbh.tce.repository;

import com.zbh.tce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 
 * @author ZinBhoneHtut
 *
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
	boolean existsByName(String name);

	boolean existsByEmail(String email);

	Optional<User> findByName(String userName);
}
