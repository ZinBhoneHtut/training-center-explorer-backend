package com.zbh.tce.entity.criteria;

import com.zbh.tce.common.query.Query;
import com.zbh.tce.common.query.Query.Type;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class UserCriteria {

    @Query(blurry = "name,email,telephone")
    private String blurry;

    @Query(type = Type.IN_LONG, propName = "id")
    private List<Long> id;

    @Query(type = Type.IN_STRING, propName = "name")
    private List<String> userNameIn;

    @Query(type = Type.EQUAL)
    private String email;

    @Query(type = Type.BETWEEN, subAttName = "audit.createdDate")
    private List<String> createdDate = new ArrayList<>();

}
