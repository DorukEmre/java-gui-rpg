package demre.rpg.model;

import demre.rpg.controller.GameController.Direction;
import demre.rpg.controller.GameController.Action;
import demre.rpg.model.characters.Hero;
import demre.rpg.view.GameView;
import jakarta.validation.constraints.NotNull;

public class GameEngine {
  public enum Step {
    SPLASH_SCREEN, SELECT_HERO, INVALID_HERO_SELECTION, CREATE_HERO, PLAYING, EXIT_GAME
  }

  private Step currentStep;
  private Hero hero;
  private Hero[] heroes;

  // Constructor

  public GameEngine() {
    // Initialise the game engine, load resources, etc.
    loadGameDataFromFile();
    this.currentStep = Step.SPLASH_SCREEN;
    System.out.println("GameEngine initialised.");
  }

  // Getters

  public Step getCurrentStep() {
    return currentStep;
  }

  public Hero getHero() {
    return hero;
  }

  public Hero[] getHeroes() {
    return heroes;
  }

  // Setters

  public void setCurrentStep(Step step) {
    this.currentStep = step;
  }

  public void setHero(Hero hero) {
    this.hero = hero;
  }

  public void setHeroes(Hero[] heroes) {
    this.heroes = heroes;
  }

  // Methods

  private void loadGameDataFromFile() {
    // Logic to load game data from a file
    System.out.println("GameEngine > Loading game data from file...");

    // Load existing heroes from file
  }

  public void startGame(@NotNull GameView gameView) {
    System.out.println("GameEngine > Starting game...");

    while (currentStep != Step.EXIT_GAME) {

      if (currentStep == Step.SPLASH_SCREEN) {
        // Show splash screen
        gameView.splashScreen();
      } else if (currentStep == Step.SELECT_HERO) {
        // Show hero selection screen
        gameView.selectHero(currentStep);
      } else if (currentStep == Step.INVALID_HERO_SELECTION) {
        // Show hero selection screen
        gameView.selectHero(currentStep);
      } else if (currentStep == Step.CREATE_HERO) {
        // Show hero creation screen
        gameView.createHero();
      } else if (currentStep == Step.PLAYING) {
        // Update the view to reflect the current game state
        gameView.updateView();
      }
    }
    System.out.println("GameEngine > Ending game...");
  }

  public boolean isValidHeroSelection(@NotNull String selection) {
    if (selection.equalsIgnoreCase("valid"))
      return true;
    if (heroes == null || heroes.length == 0) {
      return false;
    }

    // Convert selection to int and check if it exists in the heroes array
    try {
      int index = Integer.parseInt(selection);
      if (index >= 0 && index < heroes.length) {
        return true;
      }
    } catch (NumberFormatException e) {
      // Ignore, will return false below
    }
    return false;
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
