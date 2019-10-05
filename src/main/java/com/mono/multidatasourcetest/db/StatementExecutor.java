package com.mono.multidatasourcetest.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Component
public class StatementExecutor {
    private static final Logger LOGGER = LoggerFactory.getLogger(StatementExecutor.class);

    public Collection<Map> executeSelect(String dataSourceName, String sql) throws SQLException, SystemException, NotSupportedException {

        LOGGER.info("executeSelect - ThreadId:" + Thread.currentThread().getId());
        LOGGER.info ("executeSelect - ISOLATION LEVEL: " + com.arjuna.ats.jdbc.common.jdbcPropertyManager.getJDBCEnvironmentBean().getIsolationLevel());

        TransactionManager transactionManager = com.arjuna.ats.jta.TransactionManager.transactionManager();
        PrepareStatementExec sqlExec = new PrepareStatementExec();
        Collection<Map> result = new ArrayList<>();
        try {
            transactionManager.begin();

            result = sqlExec.executeSelect(dataSourceName, sql);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);

            throw e;
        } finally {
            LOGGER.info("Select rollback");
            transactionManager.rollback();

            LOGGER.info("Select result:" + result);
        }

        return result;
    }

    public int executeUpdate (String dataSourceName, String sql, int sleep) throws Exception {

        LOGGER.info("executeUpdate - ThreadId:" + Thread.currentThread().getId());

        TransactionManager transactionManager = com.arjuna.ats.jta.TransactionManager.transactionManager();
        PrepareStatementExec sqlExec = new PrepareStatementExec();
        int rowCount = 0;
        try {
            transactionManager.begin();

            rowCount = sqlExec.executeUpdate(dataSourceName, sql);

            // Simulate other transactions
            if (sleep > 0) {
                LOGGER.info("Start sleep");
                Thread.currentThread().sleep(sleep);
                LOGGER.info("End sleep");
            }

            transactionManager.commit();
            LOGGER.info("Update committed. rowCount:" + rowCount);
        } catch (SQLException sqlExc) {
            LOGGER.error(sqlExc.getMessage(), sqlExc);
            
            transactionManager.rollback();

            throw sqlExc;
        }

        return rowCount;
    }

}
