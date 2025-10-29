package demre.rpg;

import demre.rpg.model.GameEngine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import demre.rpg.controller.GameController;
import demre.rpg.view.GameView;
import demre.rpg.view.ConsoleView;
import demre.rpg.view.GUIView;

public class Main {

  private static final Logger logger = LoggerFactory.getLogger(Main.class);

  public static String gameMode;
  public static final String databaseName = "heroes.db";

  public static void main(String[] args) {

    try {
      // Check command line arguments for game mode
      checkArgsAndGetGameMode(args);

      GameEngine gameEngine = new GameEngine();

      GameController gameController = new GameController(gameEngine);

      GameView gameView;
      if (gameMode.equals("gui")) {
        gameView = new GUIView(gameEngine, gameController);
      } else {
        gameView = new ConsoleView(gameEngine, gameController);
      }

      gameEngine.initialise(gameView, gameController);
      gameEngine.startGame();

    } catch (Exception e) {
      errorAndExit(e, e.getMessage());

    }
  }

  // Check command line arguments for game mode
  private static void checkArgsAndGetGameMode(String[] args) {
    if (args.length == 1 && args[0].equals("gui")) {
      gameMode = "gui";
    } else if (args.length == 1 && args[0].equals("console")) {
      gameMode = "console";
    } else {
      throw new IllegalArgumentException(
          "Usage: java -jar swingy.jar <gui|console>");
    }
  }

  /**
   * Prints an error message and exits the program with a non-zero status.
   * 
   * @param message the error message to print
   */
  public static void errorAndExit(Exception e, String message) {
    e.printStackTrace();
    logger.error("Error: " + message);
    System.exit(1);
  }
}
