package com.ahgj.community.canal.utils;

import java.lang.reflect.Type;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据库查询sql自定义参数
 *
 * @author Hohn
 */
public class DBParamsUtil {
    /**
     * 数据库的基本类型，包含的jdbc的封装的基本类型
     */
    private static final int[] SQLTYPES = {Types.INTEGER, Types.BIGINT, Types.VARCHAR,
            Types.DATE, Types.TIMESTAMP, Types.DOUBLE, Types.TIME};
    /**
     * Java数据基本类型，SQLTYPES与CLASSTYPES类型是一一对应的
     */
    private static final Class<?>[] CLASSTYPES = {Integer.class, Long.class, String.class,
            Date.class, Timestamp.class, Double.class, Time.class};

    //访问数据库，更新、删除、修改、查询、添加的sql语句预先定义的语句参数
    private List<ParamEntity> paramList = new ArrayList<ParamEntity>();

    /**
     * 复制一份sql语句预定义参数的集合
     *
     * @return
     */
    public DBParamsUtil copy() {
        DBParamsUtil copy = new DBParamsUtil();
        for (ParamEntity entity : paramList) {
            copy.paramList.add(entity);
        }
        return copy;
    }

    /***
     * 为sql语句设置参数如：
     * select * from t_logistics_road t where t.roadid=?
     * @param o　添加的参数对象，此对象可以是任意的数据基本类型和类
     */
    public void addParam(Object o) {
        addParam(o, getSqlTypeByClassType(o.getClass()));
    }

    /**
     * @param type Java 编程语言中所有类型的公共高级接口。
     *             它们包括原始类型、参数化类型、数组类型、类型变量和基本类型
     * @return
     */
    private int getSqlTypeByClassType(Type type) {
        for (int i = 0; i < CLASSTYPES.length; i++) {
            if (type == CLASSTYPES[i]) {
                return SQLTYPES[i];
            }
        }
        throw new RuntimeException("unSupport Type type:" + type);
    }

    private int checkSupportType(int sqlType) {
        for (int i = 0; i < SQLTYPES.length; i++) {
            if (sqlType == SQLTYPES[i]) {
                return i;
            }
        }
        throw new RuntimeException("unsurpport sqltype:" + sqlType);
    }

    public void addNullParam(int sqlType) {
        checkSupportType(sqlType);
        addParam(null, sqlType);
    }

    public void addNullParam(Type t) {
        addParam(null, getSqlTypeByClassType(t));
    }

    /***
     *
     * @param o 添加参数设置对象
     * @param type jdbc数据库数据类型，如Types.INTEGER, Types.BIGINT, Types.VARCHAR等
     */
    private void addParam(Object o, int type) {
        ParamEntity entity = new ParamEntity(o, type);
        paramList.add(entity);
    }

//  public void removeParam(int index){
//      paramList.remove(index);
//  }
//
//  public void clearParams(){
//      paramList.clear();
//  }

    /***
     * @param pst
     * @param startIndex 由于sql语句设置的参数有多个，注意默认是从1开始
     */
    public void prepareStatement(PreparedStatement pst, int startIndex) throws SQLException {
        for (ParamEntity e : paramList) {
            int v = e.getValueType();
            Object o = e.getValue();
            if (o == null) {//如果为空就设置为null
                pst.setNull(startIndex++, v);
                continue;
            }
            //判断参数实体ParamEntity中Object对象是哪一种jdbc数据类型
            if (v == SQLTYPES[0]) {
                pst.setInt(startIndex++, (Integer) o);
            } else if (v == SQLTYPES[1]) {
                pst.setLong(startIndex++, (Long) o);
            } else if (v == SQLTYPES[2]) {
                pst.setString(startIndex++, (String) o);
            } else if (v == SQLTYPES[3]) {
                pst.setDate(startIndex++, (Date) o);
            } else if (v == SQLTYPES[4]) {
                pst.setTimestamp(startIndex++, (Timestamp) o);
            } else if (v == SQLTYPES[5]) {
                pst.setDouble(startIndex++, (Double) o);
            } else if (v == SQLTYPES[6]) {
                pst.setTime(startIndex++, (Time) o);
            }
        }
    }

    /***
     * 为执行数据库的sql语句设置参数值
     * @param pst
     * @throws SQLException
     */
    public void prepareStatement(PreparedStatement pst) throws SQLException {
        prepareStatement(pst, 1);
    }
}
