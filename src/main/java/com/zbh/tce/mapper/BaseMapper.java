package com.zbh.tce.mapper;

import com.zbh.tce.common.utils.DateUtils;

import java.util.Date;
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

	@DateToStringMapper
	public static String formatDate(Date date) {
		if(date == null) {
			return "N/A";
		}
		return DateUtils.format(date, "yyyy-MM-dd hh:mm:ss");
	}
}
