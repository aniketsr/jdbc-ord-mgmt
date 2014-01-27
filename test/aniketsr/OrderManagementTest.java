package aniketsr;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class OrderManagementTest {
    OrderManagement management;
    Connection connection;
    Statement statement;

    @Before
    public void setUp() throws Exception {
        management = new OrderManagement();
        connection = management.getConnection(connection);
        statement = connection.createStatement();
        statement.executeQuery("create table DDL(id int, name varchar(20),price int)");
    }

    @After
    public void tearDown() throws Exception {
        statement.executeQuery("drop table DDL");
        statement.close();
        management.closeConnection(connection);
    }

    @Test
    public void test_Insert_products() throws Exception {
        String query1 = "insert into DDL values(1,\"notebook\",20)";
        int expected = 1;
        int result = statement.executeUpdate(query1);
        assertEquals(expected, result);
    }

    @Test
    public void test_Insert_multiple_products() throws Exception {
        String query1 = "insert into DDL values(1,\"notebook\",20)";
        String query2 = "insert into DDL values(2,\"pen\",10)";
        statement.addBatch(query1);
        statement.addBatch(query2);
        int expected[] = {1, 1};
        int actual[] = statement.executeBatch();
        assert (Arrays.equals(expected, actual));
    }

    @Test(expected = java.lang.Exception.class)
    public void test_Insert_multiple_products_wrong_query() throws Exception {
        String query1 = "insert into DDL values(1,\"notebook\")";
        statement.executeUpdate(query1);
    }

    @Test(expected = java.lang.Exception.class)
    public void test_Insertion_fails_when_duplicate_data_is_entered_to_primary_key() throws Exception {
        String query1 = "insert into DDL values(1,\"notebook\",20)";
        String query2 = "alter table DDL add constraint primary key(id);";
        String query3 = "insert into DDL values(1,\"notebook\",20)";
        statement.executeUpdate(query1);
        statement.executeUpdate(query2);
        statement.executeUpdate(query3);
    }

    @Test
    public void test_SelectsFieldsFromOrderTable() throws Exception {
        String query1 = "insert into DDL values(1,\"notebook\",20)";
        String query2 = "SELECT id, name, price from DDL";

        statement.executeUpdate(query1);

        ResultSet resultSet = statement.executeQuery(query2);

        while (resultSet.next()) {
            assertEquals(1, resultSet.getInt(1));
            assertEquals("notebook", resultSet.getString(2));
            assertEquals(20, resultSet.getInt(3));
        }
        resultSet.close();
    }

    @Test
    public void test_SelectsFieldsFromOrderTableWithCondition() throws Exception {
        String query1 = "insert into DDL values(1,\"notebook\",20)";
        String query2 = "insert into DDL values(2,\"pen\",10)";
        String selectOrder = "SELECT id, name, price from DDL where price = 20;";

        statement.executeUpdate(query1);
        statement.executeUpdate(query2);

        ResultSet resultSet = statement.executeQuery(selectOrder);

        while (resultSet.next()) {
            assertEquals(1, resultSet.getInt(1));
            assertEquals("notebook", resultSet.getString(2));
            assertEquals(20, resultSet.getInt(3));
        }
        resultSet.close();
    }

    @Test
    public void test_UpdateDDL_updates_product_name() throws Exception {
        String query1 = "insert into DDL values(1,\"notebook\",20)";
        String query2 = "update DDL set name = \"pen\" where id = 1;";
        String selectOrder = "SELECT id, name, price from DDL where price = 20;";

        statement.executeUpdate(query1);
        statement.executeUpdate(query2);

        ResultSet resultSet = statement.executeQuery(selectOrder);

        while (resultSet.next()) {
            assertEquals(1, resultSet.getInt(1));
            assertEquals("pen", resultSet.getString(2));
            assertEquals(20, resultSet.getInt(3));
        }
        resultSet.close();
    }

    @Test
    public void test_delete_product_with_condition() throws Exception {
        String query1 = "insert into DDL values(1,\"notebook\",20)";
        String query2 = "delete from DDL where id = 1";

        statement.executeUpdate(query1);
        int affectedRows = statement.executeUpdate(query2);

        assertEquals(1, affectedRows);
    }
}