package demre.rpg.model;

import demre.rpg.model.characters.Hero;

public class GameEngine {
  private Hero hero;

  public GameEngine() {
    // Initialize the game engine, load resources, etc.
    loadGameDataFromFile();
    System.out.println("GameEngine initialised.");
  }

  private void loadGameDataFromFile() {
    // Logic to load game data from a file
    System.out.println("GameEngine > Loading game data from file...");
    // For example, you might read hero data, map data, etc.
    // This is a placeholder for actual file reading logic
  }

}
