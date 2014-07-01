package org.marble.data.datastore;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class DbConnector {

    public DbConnector myInstance;

    public abstract PreparedStatement prepareStatement(String sql) throws SQLException;

}
