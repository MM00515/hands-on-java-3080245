package bank;

import java.io.Console;
import java.util.Scanner;

import javax.security.auth.login.LoginException;

public class Menu {
  private Scanner scanner;

  private Customer authenticateUser() {
    System.out.println("Please enter your username");
    String username = scanner.next();
    System.out.println("Please enter your password");
    Console console = System.console();
    char[] pwd = console.readPassword();
    String password = new String(pwd);

    Customer customer = null;

    try {
      customer = Authenticator.login(username, password);
    } catch (LoginException err) {
      System.out.println("There was an error: " + err.getMessage());
    }
    return customer;
  }

  private void showMenu(Customer customer, Account account) {
    System.out.println("---------------------------------------");
    System.out.println("Welcome, " + customer.getName() + "\n");
    int input = 0;
    double amt = 0;
    Boolean isValid = false;
    do {
      System.out.println("Please select the options from the following:");
      System.out.println("1. Deposit");
      System.out.println("2. Withdraw");
      System.out.println("3. Check Balance");
      System.out.println("4. Exit");
      input = Integer.parseInt(scanner.next());
      if (input == 1) {
        System.out.println("---------------------------------------");
        System.out.println("Current Balance is: " + account.getBalance() + "\n");
        System.out.println("Deposit\n");
        System.out.println("How much would you like to deposit?");

        amt = Double.parseDouble(scanner.next());
        double updatedBalance = amt + account.getBalance();
        updatedBalance = Math.round(updatedBalance * 100.0) / 100.0;
        DataSource dataSource = new DataSource();
        account = dataSource.updateAmount(account, updatedBalance);
      } else if (input == 2) {
        System.out.println("---------------------------------------");
        do{
          System.out.println("Enter the amount to withdraw: ");
          amt = Double.parseDouble(scanner.next());
          if(amt > account.getBalance()){
            System.out.println("Withdrawal amount larger than available, please try a smaller amount.\n");
          }
        }while(isValid==false);
      } else if (input == 3) {
        System.out.println("Your current balance is: " + account.getBalance());
      }
    } while (input > 0 && input <= 3);
  }

  public static void main(String[] args) {
    System.out.println("Welcome to World Bank International!");

    Menu menu = new Menu();
    menu.scanner = new Scanner(System.in);

    Customer customer = menu.authenticateUser();
    if (customer != null) {
      Account account = DataSource.getAccount(customer.getAccountId());
      menu.showMenu(customer, account);
    }
    menu.scanner.close();
  }
}