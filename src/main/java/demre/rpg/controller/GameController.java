package demre.rpg.controller;

import demre.rpg.model.GameEngine;

public class GameController {
  private final GameEngine gameEngine;

  public GameController(GameEngine gameEngine) {
    this.gameEngine = gameEngine;
  }

  public void initialiseGame() {
    System.out.println("GameController initialised with engine: " + gameEngine);
  }

}
