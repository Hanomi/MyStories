package test;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcTest {

    public void check() {

        try {
            Context context = new InitialContext();
            DataSource ds = (DataSource) context.lookup("jdbc/Mystory");
            Connection connection = ds.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from book");
            while (resultSet.next()) {
                System.out.println(resultSet.getString("name"));
            }

        } catch (NamingException | SQLException e) {
            e.printStackTrace();
        }
    }
}
