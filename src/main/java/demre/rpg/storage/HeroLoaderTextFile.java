package demre.rpg.storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import demre.rpg.model.characters.Hero;
import demre.rpg.model.factories.CharacterFactory;

public class HeroLoaderTextFile {

  public static List<Hero> loadHeroesFromTextFile()
      throws FileNotFoundException, IOException {
    System.out.println("Loading heroes from file...");
    List<Hero> heroes;
    String fileName = "heroes.txt";
    // open file heroes.txt
    checkFile(fileName);
    heroes = loadFileContent(fileName);
    // Read heroes from the file and populate the heroes array
    System.out.println("Heroes loaded: " + heroes.size());
    return heroes;
  }

  private static void checkFile(String fileName)
      throws FileNotFoundException, IOException {
    File file = new File(fileName);

    if (file.exists() && (!file.isFile() || !file.canRead() || !file.canWrite())) {
      throw new IOException("File '" + fileName + "' is not a valid file or is not readable/writable.");
    }
  }

  private static List<Hero> loadFileContent(String fileName)
      throws FileNotFoundException, IOException {
    List<Hero> heroes = new ArrayList<>();
    File file = new File(fileName);

    if (!file.exists() || file.length() == 0) {
      System.out.println("File '" + fileName + "' does not exist or is empty.");
      return heroes;
    }

    try (FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr)) {
      String line;

      while ((line = br.readLine()) != null) {

        System.out.println("Parsing line: " + line);
        // skip empty, whitespace only, or comment lines
        if (line.trim().isEmpty() || line.trim().startsWith("#")) {
          continue;
        }

        String[] components = line.trim().split(",");
        if (components.length != 13) {
          throw new IllegalArgumentException("Invalid hero data format: " + line);
        }

        // Parse hero attributes
        String name = components[0].trim();
        checkHeroName(name);
        String heroClass = components[1].trim();
        int level = Integer.parseInt(components[2].trim());
        int experience = Integer.parseInt(components[3].trim());
        checkExpAndLevel(experience, level);
        int attack = Integer.parseInt(components[4].trim());
        int defense = Integer.parseInt(components[5].trim());
        int hitPoints = Integer.parseInt(components[6].trim());
        String weaponName = components[7].trim();
        int weaponModifier = Integer.parseInt(components[8].trim());
        String armorName = components[9].trim();
        int armorModifier = Integer.parseInt(components[10].trim());
        String helmName = components[11].trim();
        int helmModifier = Integer.parseInt(components[12].trim());

        // Create hero using factory
        CharacterFactory factory = CharacterFactory.getInstance();
        Hero hero = factory.newHero(heroClass, name, level, experience,
            attack, defense, hitPoints, weaponName, weaponModifier, armorName, armorModifier, helmName, helmModifier);

        // Add the hero to the list
        heroes.add(hero);

        System.out.println(
            "Hero loaded: " + hero.getName() + " (" + heroClass + ")");
      }
    } catch (Exception e) {
      throw new RuntimeException(
          "Invalid content or format in heroes file: " + fileName, e);
    }

    return heroes;
  }

  public static void checkHeroName(String name) {
    if (name == null || name.trim().isEmpty()) {
      throw new IllegalArgumentException("Hero name cannot be null or empty.");
    }
    if (name.length() < 3 || name.length() > 20) {
      throw new IllegalArgumentException(
          "Hero name must be between 3 and 20 characters long.");
    }
    if (!name.matches("[a-zA-Z0-9 ]+")) {
      throw new IllegalArgumentException(
          "Hero name can only contain alphanumeric characters and spaces.");
    }
  }

  private static void checkExpAndLevel(int experience, int level) {
    if (experience < 0 || level < 1) {
      throw new IllegalArgumentException(
          "Experience must be non-negative and level must be at least 1.");
    }

    int threshold = level * 1000 + (level - 1) * (level - 1) * 450;
    int prevThreshold = (level == 1)
        ? 0
        : (level - 1) * 1000 + (level - 2) * (level - 2) * 450;

    if (experience < prevThreshold || experience >= threshold) {
      throw new IllegalArgumentException(
          "Experience not consistent with level.");
    }
  }

}
