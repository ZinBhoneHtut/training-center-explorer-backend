package com.zbh.tce.common.query;

import com.zbh.tce.common.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

import javax.persistence.criteria.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @credit Zheng Jie
 */
@Slf4j
@SuppressWarnings({"unchecked", "all"})
public class QueryHelp {

    public static <R, Q> Predicate getPredicate(Root<R> root, Q query, CriteriaQuery cq, CriteriaBuilder cb) {
        List<Predicate> list = new ArrayList<>();
        if (query == null) {
            return cb.and(list.toArray(new Predicate[0]));
        }
        try {
            List<Field> fields = getAllFields(query.getClass(), new ArrayList<>());
            //fields.stream().forEach(myPojo -> log.info("fields :: {} ", myPojo.toString()));
            for (Field field : fields) {
                boolean accessible = field.isAccessible();
                // Set the access rights of the object to ensure access to private attributes
                field.setAccessible(true);
                Query q = field.getAnnotation(Query.class);
                if (q != null) {
                    String propName = q.propName();
                    String joinName = q.joinName();
                    String blurry = q.blurry();
                    String attributeName = isBlank(propName) ? field.getName() : propName;
                    String subAtt = q.subAttName();
                    Query.Fetch fetchType = q.fetchType();

                    Class<?> fieldType = field.getType();
                    Object val = field.get(query);

                    if (ObjectUtils.isEmpty(val) || "".equals(val)) {
                        continue;
                    }

                    Join join = null;
                    // Fuzzy multi-field
                    if (ObjectUtils.isNotEmpty(blurry)) {
                        String[] blurrys = blurry.split(",");
                        List<Predicate> orPredicate = new ArrayList<>();
                        for (String s : blurrys) {
                            orPredicate.add(cb.like(root.get(s)
                                    .as(String.class), "%" + val.toString() + "%"));
                        }
                        System.out.println("or predicate : " + orPredicate);
                        Predicate[] p = new Predicate[orPredicate.size()];
                        list.add(cb.or(orPredicate.toArray(p)));
                        continue;
                    }

                    if (ObjectUtils.isNotEmpty(joinName)) {
                        log.info("joinName is not empty");
                        String[] joinNames = joinName.split(">");
                        for (String name : joinNames) {
                            switch (q.join()) {
                                case LEFT:
                                    if (ObjectUtils.isNotEmpty(join) && ObjectUtils.isNotEmpty(val)) {
                                        join = join.join(name, JoinType.LEFT);
                                    } else {
                                        join = root.join(name, JoinType.LEFT);
                                    }
                                    break;
                                case RIGHT:
                                    if (ObjectUtils.isNotEmpty(join) && ObjectUtils.isNotEmpty(val)) {
                                        join = join.join(name, JoinType.RIGHT);
                                    } else {
                                        join = root.join(name, JoinType.RIGHT);
                                    }
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                    if ((q.fetchType() == fetchType.LEFT || q.fetchType() == fetchType.RIGHT) && ((cq.getResultType().equals(Long.class) || cq.getResultType().equals(long.class)))) {
                        return null;
                    }
                    // Handle for lazy fetch
                    switch (q.fetchType()) {
                        case LEFT:
                            root.fetch(propName, JoinType.LEFT);
                            cq.distinct(true);
                            break;
                        case RIGHT:
                            root.fetch(propName, JoinType.RIGHT);
                            cq.distinct(true);
                            break;
                        default:
                            break;
                    }

                    switch (q.type()) {
                        case EQUAL:
                            list.add(cb.equal(getExpression(attributeName, join, root)
                                    .as((Class<? extends Comparable>) fieldType), val));
                            break;
                        case GREATER_THAN:
                            list.add(cb.greaterThanOrEqualTo(getExpression(attributeName, join, root)
                                    .as((Class<? extends Comparable>) fieldType), (Comparable) val));
                            break;
                        case LESS_THAN:
                            list.add(cb.lessThanOrEqualTo(getExpression(attributeName, join, root)
                                    .as((Class<? extends Comparable>) fieldType), (Comparable) val));
                            break;
                        case LESS_THAN_NQ:
                            list.add(cb.lessThan(getExpression(attributeName, join, root)
                                    .as((Class<? extends Comparable>) fieldType), (Comparable) val));
                            break;
                        case INNER_LIKE:
                            list.add(cb.like(getExpression(attributeName, join, root)
                                    .as(String.class), "%" + val.toString() + "%"));
                            break;
                        case LEFT_LIKE:
                            list.add(cb.like(getExpression(attributeName, join, root)
                                    .as(String.class), "%" + val.toString()));
                            break;
                        case RIGHT_LIKE:
                            list.add(cb.like(getExpression(attributeName, join, root)
                                    .as(String.class), val.toString() + "%"));
                            break;
                        case IN_STRING:
                            if (ObjectUtils.isNotEmpty((List<String>) val)) {
                                list.add(getExpression(attributeName, join, root).in((List<String>) val));
                            }
                            break;
                        case IN_LONG:
                            if (ObjectUtils.isNotEmpty((Collection<Long>) val)) {
                                list.add(getExpression(attributeName, join, root).in((Collection<Long>) val));
                            }
                            break;
                        case NOT_EQUAL:
                            list.add(cb.notEqual(getExpression(attributeName, join, root), val));
                            break;
                        case NOT_NULL:
                            list.add(cb.isNotNull(getExpression(attributeName, join, root)));
                            break;
                        case IS_NULL:
                            list.add(cb.isNull(getExpression(attributeName, join, root)));
                            break;
                        case BETWEEN:
                            List<Object> betweenObj = new ArrayList<>((List<Object>) val);
                            log.info("Value of date from client: {}", val);
                            List<String> between = new ArrayList<>();
                            String DATE_PATTERN = "yyyy-MM-dd";
                            if (betweenObj != null && betweenObj.size() == 2) {
                                if (betweenObj.get(0) instanceof java.util.Date) {
                                    between.add(DateUtils.format((java.util.Date) betweenObj.get(0), DATE_PATTERN).concat(" 00:00"));
                                }
                                if (betweenObj.get(1) instanceof java.util.Date) {
                                    between.add(DateUtils.format((java.util.Date) betweenObj.get(1), DATE_PATTERN).concat(" 23:59"));
                                }
                            }

                            if (between.size() == 2) {
                                if (subAtt == null || subAtt.isEmpty()) {
                                    list.add(cb.between(getExpression(attributeName, join, root).as((Class<? extends Comparable>) between.get(0).getClass()),
                                            (Comparable) between.get(0), (Comparable) between.get(1)));
                                } else {
                                    list.add(cb.between(getExpression(attributeName, subAtt, join, root).as((Class<? extends Comparable>) between.get(0).getClass()),
                                            (Comparable) between.get(0), (Comparable) between.get(1)));
                                }
                                break;
                            } else {
                                break;
                            }

                        case REG_EXP:
                            list.add(cb.like(getExpression(attributeName, join, root)
                                    .as(String.class), "%" + val + "%"));
                            log.info("List in query help REG_EXP: {}", list);
                            break;
                        default:
                            break;
                    }


                }
                field.setAccessible(accessible);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        int size = list.size();

        return cb.and(list.toArray(new Predicate[size]));
    }

    @SuppressWarnings("unchecked")
    private static <T, R> Expression<T> getExpression(String attributeName, Join join, Root<R> root) {
        if (ObjectUtils.isNotEmpty(join)) {
            return join.get(attributeName);
        } else {
            return root.get(attributeName);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T, R> Expression<T> getExpression(String attributeName, String subAtt, Join join, Root<R> root) {
        if (ObjectUtils.isNotEmpty(join)) {
            return join.get(attributeName);
        } else {
            return root.get(attributeName).get(subAtt);
        }
    }

    private static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static List<Field> getAllFields(Class clazz, List<Field> fields) {
        if (clazz != null) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            getAllFields(clazz.getSuperclass(), fields);
        }
        return fields;
    }
}
