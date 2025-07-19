package demre.rpg;

import demre.rpg.model.GameEngine;
import demre.rpg.controller.GameController;
import demre.rpg.view.GameView;
import demre.rpg.view.ConsoleView;
import demre.rpg.view.GUIView;

public class Main {
  public static String gameMode = "console";

  public static void main(String[] args) {

    checkArgsAndGetGameMode(args);

    GameEngine gameEngine = new GameEngine();

    GameController gameController = new GameController(gameEngine);
    gameController.initialiseGame();

    GameView gameView;
    if (gameMode.equals("gui")) {
      System.out.println("Starting GUI mode...");
      // Initialise GUI components here
      gameView = new GUIView(gameEngine, gameController);
    } else {
      System.out.println("Starting Console mode...");
      // Initialise console components here
      gameView = new ConsoleView(gameEngine, gameController);
    }

    gameEngine.startGame(gameView);

    System.exit(0);
  }

  // Check command line arguments for game mode
  private static void checkArgsAndGetGameMode(String[] args) {
    if (args.length == 1 && args[0].equals("gui")) {
      gameMode = "gui";
    } else if (args.length == 1 && args[0].equals("console")) {
      gameMode = "console";
    } else {
      System.out.println("Usage: java -jar swingy.jar [gui|console]");
      System.exit(1);
    }
    System.out.println("Hello, swingy in mode: " + gameMode);
  }
}
