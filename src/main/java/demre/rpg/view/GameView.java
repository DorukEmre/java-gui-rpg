package demre.rpg.view;

import demre.rpg.model.GameEngine;

public abstract class GameView {
  protected final GameEngine gameEngine;

  protected GameView(GameEngine gameEngine) {
    this.gameEngine = gameEngine;
    System.out.println("GameView > Constructor initialised");
  }

  public abstract void splashScreen();

  public abstract void selectHero();

  public abstract void createHero();

  public abstract void updateView();
}
