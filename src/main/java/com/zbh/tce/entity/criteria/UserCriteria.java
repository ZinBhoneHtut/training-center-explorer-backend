package com.zbh.tce.entity.criteria;

import com.zbh.tce.query.annotation.Query;
import com.zbh.tce.query.annotation.Query.Type;
import lombok.Data;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserCriteria {

    @Query(blurry = "name,email")
    private String blurry;

    @Query(type = Type.IN_LONG, propName = "id")
    private List<Long> id;

    @Query(type = Type.IN_STRING, propName = "name")
    private List<String> userNameIn;

    @Query(type = Type.EQUAL)
    private String email;

    @Query(type = Type.BETWEEN, subAttName = "createdDate")
    private List<Date> audit = new ArrayList<>();

}
