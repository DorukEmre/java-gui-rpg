package demre.rpg.model;

import demre.rpg.controller.GameController.Direction;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import demre.rpg.controller.GameController.Action;
import demre.rpg.model.characters.Hero;
import demre.rpg.model.characters.Mage;
import demre.rpg.model.characters.Rogue;
import demre.rpg.model.characters.Warrior;
import demre.rpg.model.items.Armor;
import demre.rpg.model.items.Helm;
import demre.rpg.model.items.Weapon;
import demre.rpg.view.GameView;
import jakarta.validation.constraints.NotNull;

public class GameEngine {
  public enum Step {
    SPLASH_SCREEN,
    SELECT_HERO, INVALID_HERO_SELECTION,
    CREATE_HERO, INVALID_HERO_CREATION,
    PLAYING, EXIT_GAME
  }

  private Step currentStep;
  private Hero hero;
  private List<Hero> heroes;

  // Constructor

  public GameEngine()
      throws FileNotFoundException, IOException {
    this.currentStep = Step.SPLASH_SCREEN;
    this.heroes = HeroLoader.loadHeroes();
    this.hero = null;
    System.out.println("GameEngine initialised.");
  }

  // Getters

  public Step getCurrentStep() {
    return currentStep;
  }

  public Hero getHero() {
    return hero;
  }

  public List<Hero> getHeroes() {
    return heroes;
  }

  // Setters

  public void setCurrentStep(Step step) {
    this.currentStep = step;
  }

  public void setHero(Hero hero) {
    this.hero = hero;
  }

  public void setHeroes(List<Hero> heroes) {
    this.heroes = heroes;
  }

  // Methods

  public void startGame(@NotNull GameView gameView) {
    System.out.println("GameEngine > Starting game...");

    while (currentStep != Step.EXIT_GAME) {

      if (currentStep == Step.SPLASH_SCREEN) {
        // Show splash screen
        gameView.splashScreen();
      } else if (currentStep == Step.SELECT_HERO
          || currentStep == Step.INVALID_HERO_SELECTION) {
        // Show hero selection screen
        gameView.selectHero(currentStep);
      } else if (currentStep == Step.CREATE_HERO
          || currentStep == Step.INVALID_HERO_CREATION) {
        // Show hero creation screen
        gameView.createHero(currentStep);
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
    if (heroes == null || heroes.size() == 0) {
      return false;
    }

    // Convert selection to int and check if it exists in the heroes array
    try {
      int index = Integer.parseInt(selection);
      if (index >= 0 && index < heroes.size()) {
        return true;
      }
    } catch (NumberFormatException e) {
      // Ignore, will return false below
    }
    return false;
  }

  public boolean isValidHeroName(String name) {
    if (name == null || name.trim().isEmpty()
        || name.length() < 3 || name.length() > 20
        || !name.matches("[a-zA-Z0-9 ]+")) {
      return false;
    }
    return true;
  }

  public boolean isValidHeroClass(String heroClass) {
    return (heroClass.equalsIgnoreCase("Mage")
        || heroClass.equalsIgnoreCase("Warrior")
        || heroClass.equalsIgnoreCase("Rogue"));
  }

  public void selectHero(@NotNull String selection) {
    System.out.println("GameEngine > Selecting hero: " + selection);

    int index = Integer.parseInt(selection);
    this.hero = heroes.get(index);
    System.out.println("Hero selected: " + hero.getName());
  }

  public void createHero(@NotNull String name, @NotNull String heroClass) {
    System.out.println("GameEngine > Creating hero: " + name);

    if (heroClass.equalsIgnoreCase("Mage")) {
      this.hero = new Mage(
          name, 1, 0, 5, 5, 10,
          new Weapon("Wooden stick"), new Armor("Cloth armor"), new Helm("Paper hat"));
    } else if (heroClass.equalsIgnoreCase("Warrior")) {
      this.hero = new Warrior(
          name, 1, 0, 5, 5, 10,
          new Weapon("Wooden stick"), new Armor("Cloth armor"), new Helm("Paper hat"));
    } else if (heroClass.equalsIgnoreCase("Rogue")) {
      this.hero = new Rogue(
          name, 1, 0, 5, 5, 10,
          new Weapon("Wooden stick"), new Armor("Cloth armor"), new Helm("Paper hat"));
    }

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
