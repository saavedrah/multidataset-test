package com.mono.multidatasourcetest.services;

import com.mono.multidatasourcetest.db.StatementExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

@Service
public class LogicService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogicService.class);


    private final String selectStmt = "SELECT commodity_id, commodity_name from autotestcommodity";

    volatile Collection<Map> returnCollection = new ArrayList<>();

    public Collection<Map> selectTransaction(String dataSourceName) throws NotSupportedException, SQLException, SystemException {
        StatementExecutor statementExecutor = new StatementExecutor();
        Collection<Map> ret = statementExecutor.executeSelect(dataSourceName, selectStmt);

        return ret;
    }

    public int registerTransaction(String dataSourceName) throws Exception {
        final String updateStmt = String.format("INSERT INTO autotestcommodity (commodity_id, commodity_name) values ('%s', 'commodityName')", UUID.randomUUID().toString());

        StatementExecutor statementExecutor = new StatementExecutor();
        int rowCount = statementExecutor.executeUpdate(dataSourceName, updateStmt, 10000);

        return rowCount;
    }
    public Collection execThreadTransaction(final String dataSourceName) throws InterruptedException {

        final CountDownLatch latch = new CountDownLatch(3);
        final String updateStmt = String.format("INSERT INTO autotestcommodity (commodity_id, commodity_name) values ('%s', 'commodityName')", UUID.randomUUID().toString());

        Thread thread1 = new Thread(new Runnable() {
            public void run() {
                try {
                    LOGGER.info("Start Thread 1");

                    StatementExecutor statementExecutor = new StatementExecutor();
                    Collection<Map> ret = statementExecutor.executeSelect(dataSourceName, selectStmt);

                    LOGGER.info("Thread 1: " + ret);

                } catch (RuntimeException e) {
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                } finally {
                    latch.countDown();
                }
            }
        }, "Thread 1");

        Thread thread2 = new Thread(new Runnable() {
            public void run() {
                try {
                    LOGGER.info("Start Thread 2");

                    StatementExecutor statementExecutor = new StatementExecutor();
                    int rowCnt = statementExecutor.executeUpdate(dataSourceName, updateStmt, 5000);

                    LOGGER.info("Thread 2 - Insert: " + rowCnt);
                } catch (RuntimeException e) {
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                } finally {
                    latch.countDown();
                }
            }
        }, "Thread 2");

        Thread thread3 = new Thread(new Runnable() {
            public void run() {
                try {
                    LOGGER.info("Start Thread 3");

                    Thread.currentThread().sleep(1000);

                    StatementExecutor statementExecutor = new StatementExecutor();
                    returnCollection = statementExecutor.executeSelect("ds1", selectStmt);

                    LOGGER.info("Thread 3: " + returnCollection);
                } catch (RuntimeException e) {
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                } finally {
                    latch.countDown();
                }
            }
        }, "Thread 3");

        thread1.start();
        Thread.sleep(1000);
        thread2.start();
        Thread.sleep(1000);
        thread3.start();

        latch.await();

        return returnCollection;
    }
}
