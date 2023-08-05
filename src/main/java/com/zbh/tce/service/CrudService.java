package com.zbh.tce.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * 
 * @author ZinBhoneHtut
 *
 * @param <Entity>
 */
public interface CrudService<E, Q> {
	Optional<E> findById(long id);
	List<E> findAll();
	E save(E entity);
	void deleteById(long id);
	long count();
	List<E> findAll(Q queryCriteria);
	Page<E> findAll(Q queryCriteria, Pageable pageable);
}
