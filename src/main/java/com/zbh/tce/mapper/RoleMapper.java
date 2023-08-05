package com.zbh.tce.mapper;

import com.zbh.tce.dto.RoleDTO;
import com.zbh.tce.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * 
 * @author ZinBhoneHtut
 *
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleMapper extends BaseMapper<Role, RoleDTO> {

	@Mapping(target = "audit", ignore = true)
	Role toEntity(RoleDTO roleDTO);
}
