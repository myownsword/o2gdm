package com.tp.o2gdm.o2gdmcore.config;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.*;


/**
 * 解决保存数据字段超过4000 char 问题
 */
public class OracleClobTypeHandler implements TypeHandler<Object> {

    @Override
    public void setParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException {
        Clob clob = ps.getConnection().createClob();
        clob.setString(1, (String) parameter);
        ps.setClob(i, clob);
    }

    @Override
    public Object getResult(ResultSet rs, String columnName) throws SQLException {
        Clob clob =  rs.getClob(columnName);
        return (clob == null || clob.length() == 0) ? null : clob.getSubString((long) 1, (int) clob.length());
    }

    @Override
    public Object getResult(ResultSet rs, int columnIndex) throws SQLException {
        return null;
    }

    @Override
    public Object getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return null;
    }
}
