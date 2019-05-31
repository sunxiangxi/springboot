package com.ahgj.community.canal.utils;

/***
 * jdbc数据类型的种类和该数据类型的对应的值，
 * 相当于map中的 key和value,把这样的参数类型封装成一个实体对象
 * @author Hohn
 *
 */
public class ParamEntity {
    private Object value;//该jdbc数据类型的哪个类型
    private int sqlType;//jdbc数据类型的哪一种
    /***
     *
     * @param value
     * @param sqlType
     */
    public ParamEntity(Object value, int sqlType){
        this.value = value;
        this.sqlType = sqlType;
    }

    public Object getValue() {
        return value;
    }
    public void setValue(Object value) {
        this.value = value;
    }
    public int getValueType() {
        return sqlType;
    }
    public void setValueType(int sqlType) {
        this.sqlType = sqlType;

    }
}
