package com.zbh.tce.entity.criteria;

import com.zbh.tce.common.query.Query;
import com.zbh.tce.common.query.Query.Type;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RoleCriteria {
    @Query(blurry = "roleName,description")
    private String blurry;

    @Query(type = Type.EQUAL, propName = "roleName")
    private String roleNameEqual;
}
