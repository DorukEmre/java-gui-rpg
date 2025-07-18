package demre.rpg;

import demre.rpg.controller.GameController;
import demre.rpg.model.GameEngine;

public class Main {
  public static String gameMode = "console";

  public static void main(String[] args) {
    // Check command line arguments for game mode
    if (args.length == 1 && args[0].equals("gui")) {
      gameMode = "gui";
    } else if (args.length == 1 && args[0].equals("console")) {
      gameMode = "console";
    } else {
      System.out.println("Usage: java -jar swingy.jar [gui|console]");
      System.exit(1);
    }

    System.out.println("Hello, swingy in mode: " + gameMode);

    GameEngine gameEngine = new GameEngine();
    GameController gameController = new GameController(gameEngine);
    gameController.initialiseGame();

    System.exit(0);
  }
}
