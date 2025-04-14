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
    int input = 0;
    double amt = 0;
    Boolean isValid = false;
    do {
      System.out.println("Welcome, " + customer.getName() + "\n");
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
        System.out.println("Deposit successful!");
      } else if (input == 2) {
        System.out.println("---------------------------------------");
        do{
          System.out.println("Enter the amount to withdraw: ");
          amt = Double.parseDouble(scanner.next());
          if(amt > account.getBalance()){
            System.out.println("Low balance, try again.");       
          }
          else{
            isValid = true;
          }
        }while(isValid == false);
        double updatedBalance = account.getBalance() - amt;
        updatedBalance = Math.round(updatedBalance * 100.0) / 100.0;
        DataSource dataSource = new DataSource();
        account = dataSource.updateAmount(account, updatedBalance);
      } else if (input == 3) {
        System.out.println("Your current balance is: " + account.getBalance());
      }
    } while (input > 0 && input <= 3 && customer.isAuthenticated());
  }

  public static void main(String[] args) {
    System.out.print("\033[H\033[2J");
    System.out.println("Welcome to World Bank International!");

    Menu menu = new Menu();
    menu.scanner = new Scanner(System.in);

    Customer customer = menu.authenticateUser();
    if (customer != null) {
      System.out.println("Customer is authenticated, " + customer.isAuthenticated());
      Account account = DataSource.getAccount(customer.getAccountId());
      menu.showMenu(customer, account);
    }
    menu.scanner.close();
  }
}