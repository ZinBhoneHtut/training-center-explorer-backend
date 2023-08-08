package com.zbh.tce.mapper;

import com.zbh.tce.dto.UserDTO;
import com.zbh.tce.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.Date;

/**
 * @author ZinBhoneHtut
 */
@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = RoleMapper.class
)
public interface UserMapper extends BaseMapper<User, UserDTO> {

    User toEntity(UserDTO dto);

    @Mapping(source = "audit.createdDate", target = "createdDate", qualifiedByName = "formatDate")
    UserDTO toDTO(User user);

    @Named("formatDate")
    static String formatDate(Date date) {
        return BaseMapper.formatDate(date);
    }

}
