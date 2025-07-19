package demre.rpg.view;

import demre.rpg.controller.GameController;
import demre.rpg.model.GameEngine;

public abstract class GameView {
  protected final GameEngine gameEngine;
  protected final GameController controller;

  protected GameView(GameEngine gameEngine, GameController controller) {
    this.gameEngine = gameEngine;
    this.controller = controller;
    System.out.println("GameView > Constructor initialised");
  }

  public abstract void splashScreen();

  public abstract void selectHero(GameEngine.Step step);

  public abstract void createHero();

  public abstract void updateView();
}
