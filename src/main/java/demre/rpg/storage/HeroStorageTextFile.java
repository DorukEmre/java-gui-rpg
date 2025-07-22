package demre.rpg.storage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import demre.rpg.model.GameEngine;
import demre.rpg.model.characters.Hero;

public class HeroStorageTextFile {

  public static void saveToTextFile(GameEngine gameEngine)
      throws FileNotFoundException, IOException {

    String outputFile = "heroes.txt";
    Hero hero = gameEngine.getHero();
    Integer selectedHeroIndex = gameEngine.getSelectedHeroIndex();

    if (selectedHeroIndex != null
        && selectedHeroIndex >= 0
        && selectedHeroIndex < gameEngine.getHeroes().size()) {

      gameEngine.setHeroInHeroesAtIndex(selectedHeroIndex, hero);
      List<Hero> heroes = gameEngine.getHeroes();

      File file = new File(outputFile);

      if ((!file.exists() && !file.createNewFile())
          || !file.isFile() || !file.canWrite())
        throw new IOException("Failed to create output file.");
      try (
          FileWriter fileWriter = new FileWriter(file, false);
          PrintWriter writer = new PrintWriter(
              new BufferedWriter(fileWriter), true)) {
        writer.println(
            "# name, class, level, xp, att, def, hp, weapon, mod, armor, mod, helm, mod\n");
        for (Hero eachHero : heroes) {
          if (eachHero != null) {
            writer.println(eachHero.saveString());
          }
        }
      } catch (IOException e) {
        System.err.println("Error writing to file: " + e.getMessage());
      }
    } else {
      throw new IllegalArgumentException(
          "Invalid selected hero index: " + selectedHeroIndex);
    }
  }
}
