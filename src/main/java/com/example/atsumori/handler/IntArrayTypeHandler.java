package com.example.atsumori.handler;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * PostgreSQL の int[] を Java の int[] にマッピングする TypeHandler
 */
public class IntArrayTypeHandler extends BaseTypeHandler<int[]> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, int[] parameter, JdbcType jdbcType)
            throws SQLException {
        // PostgreSQLのint[]型に変換
        Array array = ps.getConnection().createArrayOf("integer", toObjectArray(parameter));
        ps.setArray(i, array);
    }

    @Override
    public int[] getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Array array = rs.getArray(columnName);
        return toIntArray(array);
    }

    @Override
    public int[] getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Array array = rs.getArray(columnIndex);
        return toIntArray(array);
    }

    @Override
    public int[] getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Array array = cs.getArray(columnIndex);
        return toIntArray(array);
    }

    private int[] toIntArray(Array array) throws SQLException {
        if (array == null) {
            return null;
        }
        Object[] objs = (Object[]) array.getArray();
        int[] ints = new int[objs.length];
        for (int i = 0; i < objs.length; i++) {
            ints[i] = ((Number) objs[i]).intValue();
        }
        return ints;
    }

    private Object[] toObjectArray(int[] ints) {
        if (ints == null) return null;
        Object[] objs = new Object[ints.length];
        for (int i = 0; i < ints.length; i++) {
            objs[i] = ints[i];
        }
        return objs;
    }
}
