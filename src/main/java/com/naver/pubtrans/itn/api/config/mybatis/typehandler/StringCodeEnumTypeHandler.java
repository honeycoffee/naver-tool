package com.naver.pubtrans.itn.api.config.mybatis.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeException;
import org.apache.ibatis.type.TypeHandler;

import com.naver.pubtrans.itn.api.config.mybatis.StringCodeEnum;
import com.naver.pubtrans.itn.api.consts.ResultCode;

/**
 * 문자형 코드로 이루어진 Enum Type의 핸들러를 정의
 * @author adtec10
 *
 * @param <E>
 */
public class StringCodeEnumTypeHandler <E extends Enum <E>> implements TypeHandler <StringCodeEnum> {

	private Class <E> type;

	public StringCodeEnumTypeHandler(Class <E> type) {
        this.type = type;
    }

	@Override
	public void setParameter(PreparedStatement ps, int i, StringCodeEnum parameter, JdbcType jdbcType)
		throws SQLException {
		if(parameter == null) {
			ps.setNull(i, Types.VARCHAR);
		} else {
			ps.setString(i, parameter.getCode());
		}

	}

	@Override
	public StringCodeEnum getResult(ResultSet rs, String columnName) throws SQLException {
		return getCodeEnum(rs.getString(columnName));
	}

	@Override
	public StringCodeEnum getResult(ResultSet rs, int columnIndex) throws SQLException {
		return getCodeEnum(rs.getString(columnIndex));
	}

	@Override
	public StringCodeEnum getResult(CallableStatement cs, int columnIndex) throws SQLException {
		return getCodeEnum(cs.getString(columnIndex));
	}

	private StringCodeEnum getCodeEnum(String code) {

		StringCodeEnum[] codeStringEnums = (StringCodeEnum[])type.getEnumConstants();

        for (StringCodeEnum e: codeStringEnums) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

}
