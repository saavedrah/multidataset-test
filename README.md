# multidataset-test
Spring boot test for multidataset using arjuna (Narayana) PostgreSql and Oracle

# Description
Using the configuration file config/applicationContext.xml multiple datasources could be added to the application.
The application uses Spring-Session(jdbc) and Arjuna to Insert and Select data from the table autotestcommodity.
Using the test class MultiDataSourceTestApplicationTest a login request is sent to set the session and after that a loop 
of select / insert requests are sent.

# Setup
It is required to create the session and the test table (autotestcommodity) in the database.

# Problem
The application stops after the fifth loop of transactions in PostgreSql and Oracle.

Following exception is thrown, 

<pre>
2019-10-05 22:48:20.724  INFO 27032 --- [o-auto-1-exec-4] c.m.m.db.PrepareStatementExec            : START select
2019-10-05 22:49:20.225  WARN 27032 --- [nsaction Reaper] com.arjuna.ats.arjuna                    : ARJUNA012117: TransactionReaper::check timeout for TX 0:ffffc0a82101:c116:5d989ef0:6e in state  RUN
2019-10-05 22:49:20.228  WARN 27032 --- [Reaper Worker 0] com.arjuna.ats.arjuna                    : ARJUNA012095: Abort of action id 0:ffffc0a82101:c116:5d989ef0:6e invoked while multiple threads active within it.
2019-10-05 22:49:20.234  WARN 27032 --- [Reaper Worker 0] com.arjuna.ats.arjuna                    : ARJUNA012381: Action id 0:ffffc0a82101:c116:5d989ef0:6e completed with multiple threads - thread http-nio-auto-1-exec-10 was in progress with java.lang.Object.wait(Native Method)
java.lang.Object.wait(Object.java:502)
com.arjuna.ats.internal.jdbc.ConnectionManager.create(ConnectionManager.java:134)
com.arjuna.ats.jdbc.TransactionalDriver.connect(TransactionalDriver.java:89)
java.sql.DriverManager.getConnection(DriverManager.java:664)
java.sql.DriverManager.getConnection(DriverManager.java:208)
com.mono.multidatasourcetest.db.PrepareStatementExec.executeUpdate(PrepareStatementExec.java:51)
</pre>
