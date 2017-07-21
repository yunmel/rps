package com.yunmel.rps.db;

import java.util.Map;

import com.yunmel.rps.utils.StringUtils;

public class SqlBuilder {
	
	public static final String buildSql(Map<String, Object> params,Object[] objs){
		StringBuilder sqlBuilder = new StringBuilder();
		int i = 0;
		for(Map.Entry<String, Object> entry : params.entrySet()){
		    sqlBuilder.append(" and ").append(StringUtils.getUnderlineName(entry.getKey())).append(" = ?");
			objs[i] = entry.getValue();
			i++;
		}
		return sqlBuilder.toString();
	}
}
