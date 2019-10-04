package com.mono.multidatasourcetest.db;

import com.arjuna.ats.jdbc.TransactionalDriver;
import com.sun.media.jfxmedia.logging.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.support.JdbcUtils;

import java.sql.*;
import java.util.*;

public class PrepareStatementExec {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PrepareStatementExec.class);

    private ResultSetMetaData _resultSetMD;
    private int _resultSetMD_ColCnt = 0;

    public Collection executeSelect (String dataSourceName, String sql) throws SQLException {
        LOGGER.info("START select");

        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            String dsJndiTestDb = TransactionalDriver.arjunaDriver + dataSourceName;
            conn = DriverManager.getConnection(dsJndiTestDb, new Properties());

            LOGGER.info("Connection - clientInfo: " + conn.getClientInfo());
//            LOGGER.info("Connection - schema :" + conn.getSchema());

            stmt = conn.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery();
            _resultSetMD = rs.getMetaData();
            _resultSetMD_ColCnt = _resultSetMD.getColumnCount();
            Collection returnClct = this.fetchData(rs);

            return returnClct;

        } finally {
            JdbcUtils.closeStatement(stmt);
        }
    }

    public int executeUpdate(String dataSourceName, String sql) throws SQLException {
        LOGGER.info("START update");

        Connection conn = null;
        PreparedStatement stmt = null;

        int rowCount = 0;
        try {
            String dsJndiTestDb = TransactionalDriver.arjunaDriver + dataSourceName;
            conn = DriverManager.getConnection(dsJndiTestDb, new Properties());

            LOGGER.info("Connection - clientInfo: " + conn.getClientInfo());
//            LOGGER.info("Connection - schema :" + conn.getSchema());

            stmt = conn.prepareStatement(sql);

            rowCount =  stmt.executeUpdate();

            LOGGER.info("Updated columns: " + rowCount);

        } finally {
            JdbcUtils.closeStatement(stmt);
        }

        return rowCount;
    }


    private Collection fetchData(ResultSet rs) throws SQLException {

        ArrayList<Map> data = new ArrayList<>();

        while (rs.next()) {
            Map<String,Object> rowMap = new HashMap<>();
            for (int i = 1; i <= _resultSetMD_ColCnt; i++) {
                String colDBName = _resultSetMD.getColumnName(i);
                rowMap.put(colDBName, rs.getObject(i));
            }
            data.add(rowMap);
        }

        return data;
    }

}
