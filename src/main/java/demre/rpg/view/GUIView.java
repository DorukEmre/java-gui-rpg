package demre.rpg.view;

import demre.rpg.controller.GameController;
import demre.rpg.model.GameEngine;

public class GUIView extends GameView {
  // extends javax.swing.JFrame

  public GUIView(GameEngine gameEngine, GameController controller) {
    super(gameEngine, controller);
    System.out.println("GUIView initialised with engine: " + gameEngine + " and controller: " + controller);
    initialiseGUIComponents();
  }

  private void initialiseGUIComponents() {
    // Logic to Initialise GUI components
    System.out.println("GUIView > Initialising GUI components...");
    // e.g., create windows, buttons, labels, etc.
  }

  @Override
  public void splashScreen() {
  }

  @Override
  public void selectHero() {
  }

  @Override
  public void createHero() {
  }

  @Override
  public void showHero() {
  }

  @Override
  public void updateView() {
    // Logic to update the GUI view based on game state
    System.out.println("GUIView > Updating GUI view...");
    // e.g., refresh hero stats, display messages, etc.
  }

  @Override
  public void showEnemyEncounter() {
  }

  @Override
  public void showEnemyRunFailure() {
  }

  @Override
  public void showItemFound() {
  }

  @Override
  public void showVictoryScreen() {
  }

  @Override
  public void showGameOver() {
  }

}
