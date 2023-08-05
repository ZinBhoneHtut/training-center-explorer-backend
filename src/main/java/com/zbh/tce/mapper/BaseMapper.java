package com.zbh.tce.mapper;

import java.util.List;

/**
 * 
 * @author ZinBhoneHtut
 *
 */
public interface BaseMapper<E, D> {
	E toEntity(D dto);
	D toDTO(E entity);
	List<E> toEntity(List<D> dtoList);
	List<D> toDTO(List<E> entityList);
}
