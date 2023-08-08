package com.zbh.tce.common.query;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @credit Zheng Jie
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Query {

    String propName() default "";

    String subAttName() default "";

    Type type() default Type.EQUAL;

    /**
     * The attribute name of the connection query, such as userRole in the User class
     */
    String joinName() default "";

    /**
     * Default left connection
     */
    Join join() default Join.LEFT;

    /**
     * The attribute is to fecth lazy object
     */
    Fetch fetchType() default Fetch.NONE;

    /**
     * Multi-field fuzzy search, only supports String type fields, multiple separated by commas, such as @Query (blurry = "email, username")
     */
    String blurry() default "";


    enum Type {
        EQUAL
        , GREATER_THAN
        , LESS_THAN
        , INNER_LIKE
        , LEFT_LIKE
        , RIGHT_LIKE
        , LESS_THAN_NQ
        , IN_STRING
        , IN_LONG
        , NOT_EQUAL
        , BETWEEN
        , NOT_NULL
        , IS_NULL
        , FETCH
        , REG_EXP
    }

    enum Join {
        LEFT, RIGHT
    }

    enum Fetch {
        LEFT, RIGHT, NONE
    }
}

