package demre.rpg.view;

public class ConsoleView {
  public void displayMessage(String message) {
    System.out.println(message);
  }

  public void displayError(String error) {
    System.err.println("Error: " + error);
  }

  public void displayGameStart() {
    System.out.println("Welcome to the RPG Game in console mode!");
  }

  public void displayGameEnd() {
    System.out.println("Thank you for playing in console mode!");
  }
}
