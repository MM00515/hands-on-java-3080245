package bank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataSource {
  public static Connection connect() {
    String db_file = "jdbc:sqlite:resources/bank.db";
    Connection connection = null;
    try {
      connection = DriverManager.getConnection(db_file);
      System.out.println("We\'re connected");
    } catch (SQLException err) {
      err.printStackTrace();
    }
    return connection;
  }

  public static Customer getCustomer(String username) {
    String sql = "SELECT * from Customers WHERE username = ?";
    Customer customer = null;
    try (Connection connection = connect()) {
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setString(1, username);
      try (ResultSet resultSet = statement.executeQuery()) {
        customer = new Customer(
            resultSet.getInt("id"),
            resultSet.getString("name"),
            resultSet.getString("username"),
            resultSet.getString("password"),
            resultSet.getInt("account_id"));
      }
    } catch (SQLException err) {
      err.printStackTrace();
    }
    return customer;
  }

  public static Account getAccount(int accountId) {
    String sql = "SELECT * from Accounts WHERE id = ?";
    Account account = null;
    try (Connection connection = connect()) {
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setInt(1, accountId);
      try (ResultSet resultSet = statement.executeQuery()) {
        account = new Account(
            resultSet.getInt("id"),
            resultSet.getString("type"),
            resultSet.getDouble("balance"));
      } catch (SQLException err) {
        err.printStackTrace();
      }
    } catch (SQLException err) {
      err.printStackTrace();
    }
    return account;
  }

  public Account updateAmount(Account account, double updatedAmount){
    String sql = "UPDATE Accounts set balance = ? where id = "+account.getId();
    try (Connection connection = connect()) {
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setDouble(1, updatedAmount);
      if(statement.executeUpdate() > 0){
        System.out.println("Balance was updated!");
        account = getAccount(account.getId());
        System.out.println("Account ID: "+account.getId());
        System.out.println("Current Balance is: " + account.getBalance());
      }
    }catch(SQLException err){
      err.printStackTrace();
    }
    return account;
  }

  public static void main(String[] args) {
    Customer customer = getCustomer("ojamblinbx@ycombinator.com");
    System.out.println(customer.getName());
    Account account = getAccount(customer.getAccountId());
    System.out.println(account.getId());
    System.out.println(account.getType());
    System.out.println(account.getBalance());
  }
}
