package com.mono.multidatasourcetest.db;

import org.springframework.beans.factory.annotation.Autowired;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.XADataSource;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Map;

public class GeneralDbConnector implements IDbConnector {
    public final static String DB_URL = "URL";
    public final static String DB_HOST = "DB_HOST";
    public final static String DB_DATABASE_NAME = "DB_DATABASE_NAME";
    public final static String DB_PORT = "DB_PORT";
    public final static String DB_ID = "DB_ID";
    public final static String DB_USER = "DB_USER";
    public final static String DB_PASSWORD = "DB_PASSWORD";

    @Autowired
    DataSourceManager dataSourceManager;

    @Override
    public XADataSource getDataSource(Map<String, Object> parameters) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NamingException, NoSuchMethodException, InvocationTargetException, SQLException {
        return null;
    }

    protected static InitialContext prepareInitialContext()
            throws NamingException {
        final InitialContext initialContext = new InitialContext();

        try {
            initialContext.lookup("java:/comp/env/jdbc");
        } catch (NamingException ne) {
            initialContext.createSubcontext("java:");
            initialContext.createSubcontext("java:/comp");
            initialContext.createSubcontext("java:/comp/env");
            initialContext.createSubcontext("java:/comp/env/jdbc");
        }

        return initialContext;
    }
}

