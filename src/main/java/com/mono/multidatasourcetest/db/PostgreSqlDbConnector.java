package com.mono.multidatasourcetest.db;

import com.mono.multidatasourcetest.db.GeneralDbConnector;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.XADataSource;
import java.lang.reflect.InvocationTargetException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

public class PostgreSqlDbConnector extends GeneralDbConnector {

    @Override
    public XADataSource getDataSource(Map<String,Object> parameters)
            throws ClassNotFoundException, IllegalAccessException, InstantiationException, NamingException, NoSuchMethodException, InvocationTargetException, SQLException {

        String schema = (String)parameters.get(DB_ID);

        InitialContext initialContext = prepareInitialContext();

        Class clazz = Class.forName("org.postgresql.xa.PGXADataSource");
        XADataSource xaDataSource = (XADataSource) clazz.newInstance();

        if (parameters.containsKey(DB_URL)) {
            clazz.getMethod("setUrl",
                    new Class[]{String.class}).invoke(xaDataSource, new Object[]{(String)parameters.get(DB_URL)});
        } else {
            clazz.getMethod("setServerName",
                    new Class[]{String.class}).invoke(xaDataSource, new Object[]{(String) parameters.get(DB_HOST)});
            clazz.getMethod("setDatabaseName",
                    new Class[]{String.class}).invoke(xaDataSource, new Object[]{(String) parameters.get(DB_DATABASE_NAME)});
            clazz.getMethod("setPortNumber",
                    new Class[]{int.class}).invoke(xaDataSource, new Object[]{(int) parameters.get(DB_PORT)});
            clazz.getMethod("setCurrentSchema",
                    new Class[]{String.class}).invoke(xaDataSource, new Object[]{schema});
        }

        clazz.getMethod("setUser",
                new Class[]{String.class}).invoke(xaDataSource, new Object[]{(String)parameters.get(DB_USER)});
        clazz.getMethod("setPassword",
                new Class[]{String.class}).invoke(xaDataSource, new Object[]{(String)parameters.get(DB_PASSWORD)});

        final String name = "java:/comp/env/jdbc/" + schema;
        initialContext.bind(name, xaDataSource);

        DriverManager.registerDriver(new com.arjuna.ats.jdbc.TransactionalDriver());

//        DriverManagerDataSource dataSource = new DriverManagerDataSource("jdbc:arjuna:" + name);
//        dataSource.setDriverClassName("com.arjuna.ats.jdbc.TransactionalDriver");

        return xaDataSource;
    }
}

