package com.example.atsumori.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

public class MonthArrayTypeHandler extends BaseTypeHandler<int[]> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, int[] parameter, JdbcType jdbcType) throws SQLException {
        if (parameter != null && parameter.length > 0) {
            String str = Arrays.toString(parameter)
                    .replace("[", "")
                    .replace("]", "")
                    .replace(" ", "");
            ps.setString(i, str);
        } else {
            ps.setNull(i, Types.VARCHAR);
        }
    }

    @Override
    public int[] getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return parseStringToArray(rs.getString(columnName));
    }

    @Override
    public int[] getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return parseStringToArray(rs.getString(columnIndex));
    }

    @Override
    public int[] getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return parseStringToArray(cs.getString(columnIndex));
    }

    private int[] parseStringToArray(String value) {
        if (value == null || value.isEmpty()) return new int[0];
        value = value.replaceAll("[{}\\[\\]\\s]", ""); // {11,12} みたいな形式もOK
        if (value.isEmpty()) return new int[0];
        return Arrays.stream(value.split(","))
                .mapToInt(Integer::parseInt)
                .toArray();
    }
}
