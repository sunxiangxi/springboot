package com.ahgj.community.canal.utils;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.*;

/**
 * 数据库操作语句
 * <br>🌹
 * <br>🌹author Hohn
 * <br>🌹model:
 */
public class DatabaseUtil {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseUtil.class);

    public static void rollBack(Connection con) {
        try {
            con.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 执行sql语句
     *
     * @param con
     * @param sql
     * @throws SQLException
     */
    public static void executeScript(Connection con, String sql) throws SQLException {
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
        } finally {
            closeRs(rs);
            closePst(pst);
        }
    }

    /**
     * 执行sql语句,返回List<Object>数据集合
     *
     * @param con
     * @param sql
     * @throws SQLException
     */
    public static List<Object> executeQuery4ParamsReturnList(Connection con, String sql, List<Object> paramList) throws SQLException {
        //创建预编译语句对象，一般都是用这个而不用Statement
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            //实例化预编译语句
            pst = con.prepareStatement(sql);
            //为sql语句设置参数
            setPrepareStatementParams(paramList, pst);
            // 执行查询，注意括号中不需要再加参数
            rs = pst.executeQuery();
           /*// 当结果集不为空时
            while (rs.next()){
                System.out.println("学号:" + rs.getInt("id") + "姓名:"
                        + rs.getString("name"));}*/

            return getConvertList(rs);
        } finally {
            closeRs(rs);
            closePst(pst);
        }
    }

    /**
     * 执行sql语句,返回List<Object>数据集合
     *
     * @param con
     * @param sql
     * @throws SQLException
     */
    public static List<Object[]> executeQuery4ParamsReturnArray(Connection con, String sql, List<Object> paramList) throws SQLException {
        //创建预编译语句对象，一般都是用这个而不用Statement
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            //实例化预编译语句
            pst = con.prepareStatement(sql);
            //为sql语句设置参数
            setPrepareStatementParams(paramList, pst);
            // 执行查询，注意括号中不需要再加参数
            rs = pst.executeQuery();
           /*// 当结果集不为空时
            while (rs.next()){
                System.out.println("学号:" + rs.getInt("id") + "姓名:"
                        + rs.getString("name"));}*/

            return getConvertArray(rs);
        } finally {
            closeRs(rs);
            closePst(pst);
        }
    }

    /**
     * 执行sql语句,返回List<Map<String, Object>>数据集合
     *
     * @param con
     * @param sql
     * @throws SQLException
     */
    public static List<Map<String, Object>> executeQuery4Map(Connection con, String sql) throws SQLException {
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            return getListFromRs(rs);
        } finally {
            closeRs(rs);
            closePst(pst);
        }
    }


    /**
     * 根据结果集返回List<Object>
     * 利用ResultSet的getMetaData的方法可以获得ResultSetMeta对象,而ResultSetMetaData存储了ResultSet的MetaData。
     * 所谓的MetaData在英文中的解释为"Data about Data",直译成中文则为"有关数据的数据"或者"描述数据的数据",
     * 实际上就是描述及解释含义的数据。以Result的MetaData为例,ResultSet是以表格的形式存在，所以getMetaData
     * 就包括了数据的 字段名称、类型以及数目等表格所必须具备的信息。在ResultSetMetaData类中主要有一下几个方法。
     * ResultSetMetaData rsmd=rs.getMetaData();
     * 1、getColumCount()方法
     * 返回所有字段的数目
     * 2、getColumName()方法
     * 根据字段的索引值取得字段的名称。
     * 3、getColumType()方法
     *
     * @param rs
     * @return
     * @throws SQLException
     */
    public static List<Object> getConvertList(ResultSet rs) throws SQLException {
        List<Object> list = new ArrayList();
        //获取元数据
        ResultSetMetaData md = rs.getMetaData();
        //获取所有字段的数目
        int columnCount = md.getColumnCount(); //Map rowData;
        while (rs.next()) { //rowData = new HashMap(columnCount);
            Map rowData = new HashMap();
            for (int i = 1; i <= columnCount; i++) {
                rowData.put(md.getColumnName(i), rs.getObject(i));

            }

            list.add(rowData);

        }
        return list;

    }

    /**
     * 根据结果集返回List<Object[]>
     * 利用ResultSet的getMetaData的方法可以获得ResultSetMeta对象,而ResultSetMetaData存储了ResultSet的MetaData。
     * 所谓的MetaData在英文中的解释为"Data about Data",直译成中文则为"有关数据的数据"或者"描述数据的数据",
     * 实际上就是描述及解释含义的数据。以Result的MetaData为例,ResultSet是以表格的形式存在，所以getMetaData
     * 就包括了数据的 字段名称、类型以及数目等表格所必须具备的信息。在ResultSetMetaData类中主要有一下几个方法。
     * ResultSetMetaData rsmd=rs.getMetaData();
     * 1、getColumCount()方法
     * 返回所有字段的数目
     * 2、getColumName()方法
     * 根据字段的索引值取得字段的名称。
     * 3、getColumType()方法
     *
     * @param rs
     * @return
     * @throws SQLException
     */
    public static List<Object[]> getConvertArray(ResultSet rs) throws SQLException {
        List<Object[]> list = new ArrayList();
        //获取元数据
        ResultSetMetaData md = rs.getMetaData();
        //获取所有字段的数目
        int columnCount = md.getColumnCount();
        while (rs.next()) {
            Object[] param = null;
            for (int i = 1; i <= columnCount; i++) {
                param = new Object[]{md.getColumnName(i), rs.getObject(i)};
                list.add(param);
            }

        }
        return list;

    }


    /**
     * 执行sql语句和参数,返回List<Map<String, Object>>数据集合
     *
     * @param con
     * @param sql
     * @param paramList
     * @return
     * @throws SQLException
     */
    public static List<Map<String, Object>> executeQuery4MapByParams(Connection con, String sql, List<Object> paramList) throws SQLException {
        //创建预编译语句对象，一般都是用这个而不用Statement
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            //实例化预编译语句
            pst = con.prepareStatement(sql);
            //为sql语句设置参数
            setPrepareStatementParams(paramList, pst);
            // 执行查询，注意括号中不需要再加参数
            rs = pst.executeQuery();
           /*// 当结果集不为空时
            while (rs.next()){
                System.out.println("学号:" + rs.getInt("id") + "姓名:"
                        + rs.getString("name"));}*/

            return getListFromRs(rs);
        } finally {
            closeRs(rs);
            closePst(pst);
        }
    }

    /**
     * 为sql语句设置参数
     *
     * @param paramList
     * @param pst
     * @throws SQLException
     */
    private static void setPrepareStatementParams(List<Object> paramList, PreparedStatement pst) throws SQLException {
        if (CollectionUtils.isNotEmpty(paramList)) {
            //为sql语句设置参数
            DBParamsUtil params = new DBParamsUtil();
            for (Object param : paramList) {
                params.addParam(param);
            }
            //设置查询参数,对应sql语句注入的参数顺序
            params.prepareStatement(pst);
        }
    }

    /**
     * 执行sql语句和对象,返回List<对象>数据集合
     *
     * @param con
     * @param sql
     * @param c
     * @return
     * @throws SQLException
     */
    public static List<Object> executeQuery(Connection con, String sql, Class<?> c) throws SQLException {
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            return getListFromRs(rs, c);
        } finally {
            closeRs(rs);
            closePst(pst);
        }
    }

    public static List<Map<String, Object>> getListFromRs(ResultSet rs) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();
        int i;
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        while (rs.next()) {
            Map<String, Object> map = new HashMap<String, Object>();
            for (i = 0; i < columns; i++) {
                map.put(md.getColumnName(i + 1), getValueByType(rs, md.getColumnType(i + 1), md.getColumnName(i + 1)));
            }
            list.add(map);
        }
        return list;
    }

    public static List<Map<String, Object>> getListFromRsLowerCase(ResultSet rs) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();
        int i;
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        while (rs.next()) {
            Map<String, Object> map = new HashMap<String, Object>();
            for (i = 0; i < columns; i++) {
                map.put(md.getColumnName(i + 1).toLowerCase(), getValueByType(rs, md.getColumnType(i + 1), md.getColumnName(i + 1)));
            }
            list.add(map);
        }
        return list;
    }

    public static List<Map<String, Object>> getListFromRsUpperCase(ResultSet rs) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();
        int i;
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        while (rs.next()) {
            Map<String, Object> map = new HashMap<String, Object>();
            for (i = 0; i < columns; i++) {
                map.put(md.getColumnName(i + 1).toUpperCase(), getValueByType(rs, md.getColumnType(i + 1), md.getColumnName(i + 1)));
            }
            list.add(map);
        }
        return list;
    }

    public static List<Object> getListFromRs(ResultSet rs, Class<?> c) throws SQLException {
        List<Object> list = new ArrayList<Object>();
        try {
            while (rs.next()) {
                Object o = initObjectFromRs(rs, c);
                list.add(o);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static Object getFirstObjectFromRs(ResultSet rs, Class<?> c) throws SQLException {
        Object o = null;
        try {
            o = initObjectFromRsIfExist(rs, c);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return o;
    }

    //根据类型获取值
    private static Object getValueByType(ResultSet rs, int type, String name) throws SQLException {
        switch (type) {
            case Types.NUMERIC:
                return rs.getLong(name);
            case Types.VARCHAR:
                return rs.getString(name);
            case Types.DATE:
                return rs.getDate(name);
            case Types.TIMESTAMP:
                return rs.getTimestamp(name);
            case Types.INTEGER:
                return rs.getInt(name);
            case Types.DOUBLE:
                return rs.getDouble(name);
            case Types.FLOAT:
                return rs.getFloat(name);
            case Types.BIGINT:
                return rs.getLong(name);
            default:
                return rs.getObject(name);
        }
    }

    private static boolean rsContainsFields(ResultSet rs, String fieldName) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        for (int i = 0; i < md.getColumnCount(); i++) {
            if (md.getColumnName(i + 1).equalsIgnoreCase(fieldName)) {
                return true;
            }
        }
        return false;
    }

    private static Object initObjectFromRs(ResultSet rs, Class<?> c) throws InstantiationException, SQLException, IllegalAccessException {
        Object o = c.newInstance();
        Method[] methods = o.getClass().getMethods();
        for (Method m : methods) {
            if (m.getName().startsWith("set")) {
                try {
                    m.invoke(o, getParamValueFromRs(rs, m));
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("IllegalArgumentException:" + e + "\nMethods:" + m.getName());
                } catch (InvocationTargetException e) {
                    throw new RuntimeException("InvocationTargetException:" + e + "\nMethods:" + m.getName());
                }
            }
        }
        return o;
    }

    private static Object initObjectFromRsIfExist(ResultSet rs, Class<?> c) throws SQLException, IllegalAccessException, InstantiationException {
        Object o = c.newInstance();
        Method[] methods = o.getClass().getMethods();
        String field;
        for (Method m : methods) {
            field = m.getName().substring(3);
            if (m.getName().startsWith("set") && rsContainsFields(rs, field)) {
                try {
                    m.invoke(o, getParamValueFromRs(rs, m));
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("IllegalArgumentException:" + e + "\nMethods:" + m.getName());
                } catch (InvocationTargetException e) {
                    throw new RuntimeException("InvocationTargetException:" + e + "\nMethods:" + m.getName());
                }
            }
        }
        return o;
    }

    private static Object getParamValueFromRs(ResultSet rs, Method m) throws SQLException {
        String fieldName = m.getName().substring(3);
        Type type = m.getGenericParameterTypes()[0];
        return getValueFromRs(rs, fieldName, type);
    }

    private static Object getValueFromRs(ResultSet rs, String fieldName, Type t) throws SQLException {
        String type = t.toString();
        try {
            if (type.equals("int") || type.equals("class java.lang.Integer")) {
                return rs.getInt(fieldName);
            } else if (type.equals("float") || type.equals("class java.lang.Float")) {
                return rs.getFloat(fieldName);
            } else if (type.equals("double") || type.equals("class java.lang.Double")) {
                return rs.getDouble(fieldName);
            } else if (type.equals("long") || type.equals("class java.lang.Long")) {
                return rs.getLong(fieldName);
            } else if (type.equals("class java.lang.String")) {
                return rs.getString(fieldName);
            } else if (type.equals("class java.sql.Timestamp")) {
                return rs.getTimestamp(fieldName);
            } else if (type.equals("class java.sql.Date")) {
                return rs.getDate(fieldName);
            } else if (type.equals("class java.sql.Time")) {
                return rs.getTime(fieldName);
            }
        } catch (SQLException e) {
            throw new SQLException("SQLException when get field:" + fieldName + "\n" + e);
        }
        throw new RuntimeException("getValueFromRsByField fail, field type is:" + type + ",field name is:" + fieldName);
    }

    public static void closeRs(ResultSet... rss) {
        for (ResultSet rs : rss) {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    public static void closePst(Statement... psts) {
        for (Statement pst : psts) {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    public static void closeCon(Connection... cons) {
        for (Connection con : cons) {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    /**
     * 执行数据库插入操作
     *
     * @param datas     插入数据表中key为列名和value为列对应的值的Map对象的List集合
     * @param tableName 要插入的数据库的表名
     * @param connection 连接对象
     * @return 影响的行数
     * @throws SQLException SQL异常
     */
    public static int insertAll(String tableName, List<Map<String, Object>> datas, Connection connection) throws SQLException {
        //影响的行数
        int affectRowCount = -1;

        PreparedStatement preparedStatement = null;
        try {
            Map<String, Object> valueMap = datas.get(0);
            //获取数据库插入的Map的键值对的值
            Set<String> keySet = valueMap.keySet();
            Iterator<String> iterator = keySet.iterator();
            //要插入的字段sql，其实就是用key拼起来的
            StringBuilder columnSql = new StringBuilder();
            //要插入的字段值，其实就是？
            StringBuilder unknownMarkSql = new StringBuilder();
            Object[] keys = new Object[valueMap.size()];
            int i = 0;
            while (iterator.hasNext()) {
                String key = iterator.next();
                keys[i] = key;
                columnSql.append(i == 0 ? "" : ",");
                columnSql.append(key);

                unknownMarkSql.append(i == 0 ? "" : ",");
                unknownMarkSql.append("?");
                i++;
            }
            //开始拼插入的sql语句
            StringBuilder sql = new StringBuilder();
            sql.append("INSERT INTO ");
            sql.append(tableName);
            sql.append(" (");
            sql.append(columnSql);
            sql.append(" )  VALUES (");
            sql.append(unknownMarkSql);
            sql.append(" )");

            //执行SQL预编译
            preparedStatement = connection.prepareStatement(sql.toString());
            //设置不自动提交，以便于在出现异常的时候数据库回滚
            connection.setAutoCommit(false);
            System.out.println(sql.toString());
            for (int j = 0; j < datas.size(); j++) {
                for (int k = 0; k < keys.length; k++) {
                    preparedStatement.setObject(k + 1, datas.get(j).get(keys[k]));
                }
                preparedStatement.addBatch();
            }
            int[] arr = preparedStatement.executeBatch();
            connection.commit();
            affectRowCount = arr.length;
            logger.info("===============成功了插入了" + affectRowCount + "行");
        } catch (Exception e) {
            if (connection != null) {
                connection.rollback();
            }
            e.printStackTrace();
            throw e;
        }
        return affectRowCount;
    }


    /**
     * 执行更新操作
     *
     * @param tableName  表名
     * @param valueMap   要更改的值
     * @param whereMap   条件
     * @param connection 连接对象
     * @return 影响的行数
     * @throws SQLException SQL异常
     */
    public static int update(String tableName, Map<String, Object> valueMap, Map<String, Object> whereMap, Connection connection) throws SQLException {
        /**获取数据库插入的Map的键值对的值**/
        Set<String> keySet = valueMap.keySet();
        Iterator<String> iterator = keySet.iterator();
        /**开始拼插入的sql语句**/
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE ");
        sql.append(tableName);
        sql.append(" SET ");

        /**要更改的的字段sql，其实就是用key拼起来的**/
        StringBuilder columnSql = new StringBuilder();
        int i = 0;
        List<Object> objects = new ArrayList<>();
        while (iterator.hasNext()) {
            String key = iterator.next();
            columnSql.append(i == 0 ? "" : ",");
            columnSql.append(key + " = ? ");
            objects.add(valueMap.get(key));
            i++;
        }
        sql.append(columnSql);

        /**更新的条件:要更改的的字段sql，其实就是用key拼起来的**/
        StringBuilder whereSql = new StringBuilder();
        int j = 0;
        if (whereMap != null && whereMap.size() > 0) {
            whereSql.append(" WHERE ");
            iterator = whereMap.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                whereSql.append(j == 0 ? "" : " AND ");
                whereSql.append(key + " = ? ");
                objects.add(whereMap.get(key));
                j++;
            }
            sql.append(whereSql);
        }
        return executeUpdate(sql.toString(), objects.toArray(), connection);
    }

    /**
     * 可以执行新增，修改，删除
     *
     * @param sql      sql语句
     * @param bindArgs 绑定参数
     * @return 影响的行数
     * @throws SQLException SQL异常
     */
    public static int executeUpdate(String sql, Object[] bindArgs, Connection connection) throws SQLException {
        /**影响的行数**/
        int affectRowCount = -1;

        PreparedStatement preparedStatement = null;
        try {

            /**执行SQL预编译**/
            preparedStatement = connection.prepareStatement(sql.toString());
            /**设置不自动提交，以便于在出现异常的时候数据库回滚**/
            connection.setAutoCommit(false);
            System.out.println(getExecSQL(sql, bindArgs));
            if (bindArgs != null) {
                /**绑定参数设置sql占位符中的值**/
                for (int i = 0; i < bindArgs.length; i++) {
                    preparedStatement.setObject(i + 1, bindArgs[i]);
                }
            }
            /**执行sql**/
            affectRowCount = preparedStatement.executeUpdate();
            connection.commit();
            String operate;
            if (sql.toUpperCase().indexOf("DELETE FROM") != -1) {
                operate = "删除";
            } else if (sql.toUpperCase().indexOf("INSERT INTO") != -1) {
                operate = "新增";
            } else {
                operate = "修改";
            }
            System.out.println("成功" + operate + "了" + affectRowCount + "行");
            System.out.println();
        } catch (Exception e) {
            if (connection != null) {
                connection.rollback();
            }
            e.printStackTrace();
            throw e;
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return affectRowCount;
    }

    /**
     * After the execution of the complete SQL statement, not necessarily the actual implementation of the SQL statement
     *
     * @param sql      SQL statement
     * @param bindArgs Binding parameters
     * @return Replace? SQL statement executed after the
     */
    private static String getExecSQL(String sql, Object[] bindArgs) {
        StringBuilder sb = new StringBuilder(sql);
        if (bindArgs != null && bindArgs.length > 0) {
            int index = 0;
            for (int i = 0; i < bindArgs.length; i++) {
                index = sb.indexOf("?", index);
                sb.replace(index, index + 1, String.valueOf(bindArgs[i]));
            }
        }
        return sb.toString();
    }

}
