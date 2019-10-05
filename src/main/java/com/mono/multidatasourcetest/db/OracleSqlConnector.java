package com.mono.multidatasourcetest.db;

import com.mono.multidatasourcetest.db.GeneralDbConnector;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.XADataSource;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class OracleSqlConnector extends GeneralDbConnector {
    @Override
    public XADataSource getDataSource(Map<String,Object> parameters)
            throws ClassNotFoundException, IllegalAccessException, InstantiationException, NamingException, NoSuchMethodException, InvocationTargetException {

        String dbId = (String) parameters.get(DB_ID);

        InitialContext initialContext = prepareInitialContext();

        Class clazz = Class.forName("oracle.jdbc.xa.client.OracleXADataSource");
        XADataSource xaDataSource = (XADataSource) clazz.newInstance();
        if (parameters.containsKey(DB_URL)) {
            clazz.getMethod("setURL",
                    new Class[]{String.class}).invoke(xaDataSource, new Object[]{(String)parameters.get(DB_URL)});
        } else {
            clazz.getMethod("setServerName",
                    new Class[]{String.class}).invoke(xaDataSource, new Object[]{(String) parameters.get(DB_HOST)});
            clazz.getMethod("setDatabaseName",
                    new Class[]{String.class}).invoke(xaDataSource, new Object[]{(String) parameters.get(DB_DATABASE_NAME)});
            clazz.getMethod("setPortNumber",
                    new Class[]{int.class}).invoke(xaDataSource, new Object[]{(int) parameters.get(DB_PORT)});
        }

        clazz.getMethod("setUser",
                new Class[]{String.class}).invoke(xaDataSource, new Object[]{(String)parameters.get(DB_USER)});
        clazz.getMethod("setPassword",
                new Class[]{String.class}).invoke(xaDataSource, new Object[]{(String)parameters.get(DB_PASSWORD)});

        clazz.getMethod("setImplicitCachingEnabled",
                new Class[]{boolean.class}).invoke(xaDataSource, new Object[]{true});
        clazz.getMethod("setFastConnectionFailoverEnabled",
                new Class[]{boolean.class}).invoke(xaDataSource, new Object[]{true});

        final String name = "java:/comp/env/jdbc/" + dbId;
        initialContext.bind(name, xaDataSource);

//        BasicDataSource dataSource = new BasicDataSource();
//        dataSource.setDriverClassName("com.arjuna.ats.jdbc.TransactionalDriver");
//        dataSource.set

        return xaDataSource;
    }
}
