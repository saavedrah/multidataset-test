package com.mono.multidatasourcetest.db;

import org.springframework.stereotype.Component;

import javax.sql.XADataSource;
import java.util.HashMap;
import java.util.Map;

@Component
public class DataSourceManager {

    static private Map<String, XADataSource> dataSourceMap = new HashMap<>();

    public XADataSource getDataSource(String dataSourceName) throws Exception {
        if (DataSourceManager.dataSourceMap.containsKey(dataSourceName)) {
            return DataSourceManager.dataSourceMap.get(dataSourceName);
        }

        throw new Exception("DataSource name: " + dataSourceName + " not found.");
    }

    public Map<String, XADataSource> getDataSourceMap() {
        return dataSourceMap;
    }

    public void setDataSource(String dataSourceName, org.apache.commons.dbcp.BasicDataSource dataSource) throws Exception {

        IDbConnector dbConnector;
        XADataSource xaDataSource = null;
        if (dataSource.getUrl().contains("postgres")) {
            dbConnector = new PostgreSqlDbConnector();
            Map<String,Object> parameters = new HashMap<>();
            parameters.put(PostgreSqlDbConnector.DB_URL, dataSource.getUrl());
            parameters.put(PostgreSqlDbConnector.DB_USER, dataSource.getUsername());
            parameters.put(PostgreSqlDbConnector.DB_PASSWORD, dataSource.getPassword());
            parameters.put(PostgreSqlDbConnector.DB_ID, dataSourceName);
            xaDataSource = dbConnector.getDataSource(parameters);
        } else if (dataSource.getUrl().contains("oracle")) {
            dbConnector = new OracleSqlConnector();
            Map<String,Object> parameters = new HashMap<>();
            parameters.put(OracleSqlConnector.DB_URL, dataSource.getUrl());
            parameters.put(OracleSqlConnector.DB_USER, dataSource.getUsername());
            parameters.put(OracleSqlConnector.DB_PASSWORD, dataSource.getPassword());
            parameters.put(OracleSqlConnector.DB_ID, dataSourceName);
            xaDataSource = dbConnector.getDataSource(parameters);
        } else {
            throw new Exception("DbConnector is not valid.");
        }

        if (xaDataSource == null) {
            throw new Exception("DataSource was not set.");
        }

        DataSourceManager.dataSourceMap.put(dataSourceName, xaDataSource);
    }
}

