package demre.rpg.storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import java.io.IOException;

import demre.rpg.Main;
import demre.rpg.model.GameEngine;
import demre.rpg.model.characters.Hero;
import demre.rpg.model.factories.CharacterFactory;

public class HeroStorage {

  private static String url = "jdbc:sqlite:" + Main.databaseName;

  public static void saveToDatabase(GameEngine gameEngine)
      throws IOException {

    try (Connection conn = DriverManager.getConnection(url)) {
      System.out.println("saveToDatabase > Connection to database: " + conn);
      if (conn == null) {
        throw new IOException("Failed to connect to the database.");
      }

      // Create table if it does not exist
      String createTableSQL = "CREATE TABLE IF NOT EXISTS heroes ("
          + "id INTEGER PRIMARY KEY,"
          + "name TEXT NOT NULL,"
          + "class TEXT NOT NULL,"
          + "level INTEGER NOT NULL,"
          + "exp INTEGER NOT NULL,"
          + "att INTEGER NOT NULL,"
          + "def INTEGER NOT NULL,"
          + "hp INTEGER NOT NULL,"
          + "weapon TEXT NOT NULL,"
          + "weapon_mod INTEGER NOT NULL,"
          + "armor TEXT NOT NULL,"
          + "armor_mod INTEGER NOT NULL,"
          + "helm TEXT NOT NULL,"
          + "helm_mod INTEGER NOT NULL"
          + ")";
      try (PreparedStatement pstmt = conn.prepareStatement(createTableSQL)) {
        pstmt.executeUpdate();
      }

      String sql = "INSERT OR REPLACE INTO heroes (id, name, class, level, exp, att, def, hp, weapon, weapon_mod, armor, armor_mod, helm, helm_mod) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

      Hero hero = gameEngine.getHero();
      int selectedHeroIndex = gameEngine.getSelectedHeroIndex();
      gameEngine.setHeroInHeroesAtIndex(selectedHeroIndex, hero);

      try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setInt(1, selectedHeroIndex + 1);
        pstmt.setString(2, hero.getName());
        pstmt.setString(3, hero.getHeroClass());
        pstmt.setInt(4, hero.getLevel());
        pstmt.setInt(5, hero.getExperience());
        pstmt.setInt(6, hero.getAttack());
        pstmt.setInt(7, hero.getDefense());
        pstmt.setInt(8, hero.getHitPoints());
        pstmt.setString(9, hero.getWeapon().getName());
        pstmt.setInt(10, hero.getWeapon().getModifier());
        pstmt.setString(11, hero.getArmor().getName());
        pstmt.setInt(12, hero.getArmor().getModifier());
        pstmt.setString(13, hero.getHelm().getName());
        pstmt.setInt(14, hero.getHelm().getModifier());
        pstmt.executeUpdate();
      }

    } catch (Exception e) {
      throw new IOException("Database error: " + e.getMessage(), e);
    }
  }

  public static void deleteAllHeroes()
      throws IOException {

    try (Connection conn = DriverManager.getConnection(url)) {
      if (conn == null) {
        throw new IOException("Failed to connect to the database.");
      }

      String sql = "DELETE FROM heroes";
      try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.executeUpdate();
      }

    } catch (Exception e) {
      throw new IOException("Database error: " + e.getMessage(), e);
    }
  }

  public static void generateHeroes(GameEngine gameEngine, int nHeroes)
      throws IOException {

    try (Connection conn = DriverManager.getConnection(url)) {
      if (conn == null) {
        throw new IOException("Failed to connect to the database.");
      }

      CharacterFactory factory = CharacterFactory.getInstance();

      // Check last index to avoid duplicates
      String sql = "SELECT COUNT(*) FROM heroes";
      int lastIndex = 0;
      try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        var rs = pstmt.executeQuery();
        if (rs.next()) {
          lastIndex = rs.getInt(1);
        }
      }

      for (int level = 1; level <= nHeroes; level++) { // Generate n heroes
        lastIndex++;

        Hero hero = factory.generateHero(level);

        sql = "INSERT INTO heroes (id, name, class, level, exp, att, def, hp, weapon, weapon_mod, armor, armor_mod, helm, helm_mod) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
          pstmt.setInt(1, lastIndex);
          pstmt.setString(2, hero.getName());
          pstmt.setString(3, hero.getHeroClass());
          pstmt.setInt(4, hero.getLevel());
          pstmt.setInt(5, hero.getExperience());
          pstmt.setInt(6, hero.getAttack());
          pstmt.setInt(7, hero.getDefense());
          pstmt.setInt(8, hero.getHitPoints());
          pstmt.setString(9, hero.getWeapon().getName());
          pstmt.setInt(10, hero.getWeapon().getModifier());
          pstmt.setString(11, hero.getArmor().getName());
          pstmt.setInt(12, hero.getArmor().getModifier());
          pstmt.setString(13, hero.getHelm().getName());
          pstmt.setInt(14, hero.getHelm().getModifier());
          pstmt.executeUpdate();
        }

        System.out.println("generateHeroes > Generated hero: " + hero);

      }

    } catch (Exception e) {
      throw new IOException("Database error: " + e.getMessage(), e);
    }

  }
}
