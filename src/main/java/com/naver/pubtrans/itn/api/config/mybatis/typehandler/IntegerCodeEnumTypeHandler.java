package com.naver.pubtrans.itn.api.config.mybatis.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeException;
import org.apache.ibatis.type.TypeHandler;

import com.naver.pubtrans.itn.api.config.mybatis.IntegerCodeEnum;

/**
 * 숫자형 코드로 이루어진 Enum Type의 핸들러를 정의
 * @author adtec10
 *
 * @param <E>
 */
public class IntegerCodeEnumTypeHandler <E extends Enum <E>> implements TypeHandler <IntegerCodeEnum> {

	private Class <E> type;

	public IntegerCodeEnumTypeHandler(Class <E> type) {
        this.type = type;
    }

	@Override
	public void setParameter(PreparedStatement ps, int i, IntegerCodeEnum parameter, JdbcType jdbcType)
		throws SQLException {
		if(parameter == null) {
			ps.setNull(i, Types.INTEGER);
		} else {
			ps.setInt(i, parameter.getCode());
		}

	}

	@Override
	public IntegerCodeEnum getResult(ResultSet rs, String columnName) throws SQLException {
		return getCodeEnum(rs.getInt(columnName));
	}

	@Override
	public IntegerCodeEnum getResult(ResultSet rs, int columnIndex) throws SQLException {
		return getCodeEnum(rs.getInt(columnIndex));
	}

	@Override
	public IntegerCodeEnum getResult(CallableStatement cs, int columnIndex) throws SQLException {
		return getCodeEnum(cs.getInt(columnIndex));
	}

	private IntegerCodeEnum getCodeEnum(int code) {
		IntegerCodeEnum[] codeStringEnums = (IntegerCodeEnum[])type.getEnumConstants();

        for (IntegerCodeEnum e: codeStringEnums) {
            if (e.getCode() == code) {
                return e;
            }
        }
        return null;
    }

}
