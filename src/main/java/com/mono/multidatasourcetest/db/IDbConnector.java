package com.mono.multidatasourcetest.db;

import javax.naming.NamingException;
import javax.sql.XADataSource;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Map;

public interface IDbConnector {

    XADataSource getDataSource (Map<String,Object> parameters) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NamingException, NoSuchMethodException, InvocationTargetException, SQLException;
}
