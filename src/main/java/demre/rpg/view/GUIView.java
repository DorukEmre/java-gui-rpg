package demre.rpg.view;

import demre.rpg.model.GameEngine;

public class GUIView extends GameView {

  public GUIView(GameEngine gameEngine) {
    super(gameEngine);
    System.out.println("GUIView initialised with engine: " + gameEngine);
    initialiseGUIComponents();
  }

  private void initialiseGUIComponents() {
    // Logic to Initialise GUI components
    System.out.println("GUIView > Initialising GUI components...");
    // e.g., create windows, buttons, labels, etc.
  }

  @Override
  public void updateView() {
    // Logic to update the GUI view based on game state
    System.out.println("GUIView > Updating GUI view...");
    // e.g., refresh hero stats, display messages, etc.
  }

}
