package com.yunmel.db.common;

import java.sql.SQLException;

import javax.sql.DataSource;

public interface IDataSourceCreator {

	/**
	 * create data source
	 * @param datasourcecfg
	 * @return
	 * @throws SQLException
	 */
	public DataSource createDatasource() throws SQLException;
	
}
