package demre.rpg.view;

public class GUIView {
  public void displayMessage(String message) {
    System.out.println("GUI Message: " + message);
  }

  public void displayError(String error) {
    System.err.println("GUI Error: " + error);
  }

  public void displayGameStart() {
    System.out.println("Welcome to the RPG Game in GUI mode!");
  }

  public void displayGameEnd() {
    System.out.println("Thank you for playing in GUI mode!");
  }
}
