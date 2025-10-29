package demre.rpg.storage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import demre.rpg.model.GameEngine;
import demre.rpg.model.characters.Hero;

public class HeroStorageTextFile {

  private static final Logger logger = LoggerFactory.getLogger(HeroStorageTextFile.class);

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
            writer.println(saveString(eachHero));
          }
        }
      } catch (IOException e) {
        logger.error("Error writing to file: " + e.getMessage());
      }
    } else {
      throw new IllegalArgumentException(
          "Invalid selected hero index: " + selectedHeroIndex);
    }
  }

  // Returns a string representation of the hero's state to be saved to a file
  private static String saveString(Hero hero) {

    return String.format("%s,%s,%d,%d,%d,%d,%d,%s,%d,%s,%d,%s,%d",
        hero.getName(), hero.getHeroClass(),
        hero.getLevel(), hero.getExperience(),
        hero.getAttack(), hero.getDefense(), hero.getHitPoints(),
        hero.getWeapon().getName(), hero.getWeapon().getModifier(),
        hero.getArmor().getName(), hero.getArmor().getModifier(),
        hero.getHelm().getName(), hero.getHelm().getModifier());
  }
}
