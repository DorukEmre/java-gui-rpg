package demre.rpg.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import demre.rpg.model.characters.Hero;
import demre.rpg.model.characters.Mage;
import demre.rpg.model.characters.Rogue;
import demre.rpg.model.characters.Warrior;
import demre.rpg.model.items.Armor;
import demre.rpg.model.items.Helm;
import demre.rpg.model.items.Weapon;

public class HeroLoader {

  public static List<Hero> loadHeroes()
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
        Hero hero;
        System.out.println("Parsing line: " + line);
        // skip empty, whitespace only, or comment lines
        if (line.trim().isEmpty() || line.trim().startsWith("#")) {
          continue;
        }

        String[] components = line.trim().split(",");
        if (components.length != 10) {
          throw new IllegalArgumentException("Invalid hero data format: " + line);
        }

        // Parse hero attributes
        String name = components[0].trim();
        checkHeroName(name);
        String heroClass = components[1].trim();
        int level = Integer.parseInt(components[2].trim());
        int experience = Integer.parseInt(components[3].trim());
        int attack = Integer.parseInt(components[4].trim());
        int defense = Integer.parseInt(components[5].trim());
        int hitPoints = Integer.parseInt(components[6].trim());
        String weaponName = components[7].trim();
        String armorName = components[8].trim();
        String helmName = components[9].trim();

        // Create a new Hero object based on the parsed data
        if (heroClass.equalsIgnoreCase("Mage")) {
          hero = new Mage(name, level, experience, attack, defense, hitPoints,
              new Weapon(weaponName), new Armor(armorName), new Helm(helmName));
        } else if (heroClass.equalsIgnoreCase("Warrior")) {
          hero = new Warrior(name, level, experience, attack, defense, hitPoints,
              new Weapon(weaponName), new Armor(armorName), new Helm(helmName));
        } else if (heroClass.equalsIgnoreCase("Rogue")) {
          hero = new Rogue(name, level, experience, attack, defense, hitPoints,
              new Weapon(weaponName), new Armor(armorName), new Helm(helmName));
        } else {
          throw new IllegalArgumentException("Unknown hero class: " + heroClass);
        }

        // Add the hero to the list
        heroes.add(hero);

        System.out.println(
            "Hero loaded: " + hero.getName() + " (" + heroClass + ")");
      }
    } catch (Exception e) {
      throw new RuntimeException("Invalid content or format in heroes file: " + fileName, e);
    }

    return heroes;
  }

  public static void checkHeroName(String name) {
    if (name == null || name.trim().isEmpty()) {
      throw new IllegalArgumentException("Hero name cannot be null or empty.");
    }
    if (name.length() < 3 || name.length() > 20) {
      throw new IllegalArgumentException("Hero name must be between 3 and 20 characters long.");
    }
    if (!name.matches("[a-zA-Z0-9 ]+")) {
      throw new IllegalArgumentException("Hero name can only contain alphanumeric characters and spaces.");
    }
  }

}
