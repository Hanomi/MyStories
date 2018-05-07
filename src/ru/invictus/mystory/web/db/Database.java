package ru.invictus.mystory.web.db;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {
    private static Connection connection;
    private static final Logger logger = Logger.getLogger(Database.class.getName());

    public Database() {
    }

    public static Connection getConnection() {
        if (connection == null) {
            try {
                Context context = new InitialContext();
                DataSource dataSource = (DataSource) context.lookup("jdbc/Mystory");
                connection = dataSource.getConnection();
            } catch (SQLException | NamingException e) {
                logger.log(Level.SEVERE, null, e);
            }
        }
        return connection;
    }
}
