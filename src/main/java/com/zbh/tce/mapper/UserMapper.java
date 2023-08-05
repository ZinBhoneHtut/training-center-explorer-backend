package com.zbh.tce.mapper;

import com.zbh.tce.dto.UserDTO;
import com.zbh.tce.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @author ZinBhoneHtut
 *
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper extends BaseMapper<User, UserDTO> {
	
	@Mapping(target = "audit", ignore = true)
	User toEntity(UserDTO dto);

	UserDTO toDTO(User user);
	
	@Named("formatDate")
	public static String formatDate(Date date) {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yy");
		if(date == null) {
			return "N/A";
		}
		return dateFormatter.format(date).toString();
	}
}
