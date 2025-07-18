package demre.rpg.view;

import demre.rpg.model.GameEngine;

public abstract class GameView {
  protected final GameEngine gameEngine;

  public GameView(GameEngine gameEngine) {
    this.gameEngine = gameEngine;
    System.out.println("GameView > Constructor initialised");
  }

  // Abstract methods to be implemented by subclasses
  public abstract void updateView();
}
