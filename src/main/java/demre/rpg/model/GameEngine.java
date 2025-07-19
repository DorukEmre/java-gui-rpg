package demre.rpg.model;

import demre.rpg.controller.GameController.Direction;
import demre.rpg.controller.GameController.Action;
import demre.rpg.model.characters.Hero;
import demre.rpg.view.GameView;

public class GameEngine {
  private Hero hero;
  private Hero[] heroes;

  public GameEngine() {
    // Initialise the game engine, load resources, etc.
    loadGameDataFromFile();
    System.out.println("GameEngine initialised.");
  }

  private void loadGameDataFromFile() {
    // Logic to load game data from a file
    System.out.println("GameEngine > Loading game data from file...");

    // Load existing heroes from file
  }

  public void startGame(GameView gameView) {

    System.out.println("GameEngine > Starting game...");

    // Show splash screen
    gameView.splashScreen();

    // Select a previously created hero
    gameView.selectHero();

    // Create a hero
    gameView.createHero();

    // Update the view to reflect the current game state
    gameView.updateView();
  }

  public void movePlayer(Direction direction) {
    // Logic to move the player in the specified direction
    System.out.println("GameEngine > Moving player in direction: " + direction.name());
    // e.g., update hero's position based on direction
  }

  public void doPlayerAction(Action action) {
    // Logic to perform an action with the player
    System.out.println("GameEngine > Performing action: " + action.name());
    // e.g., handle fight, run, keep, or drop actions
  }
}
