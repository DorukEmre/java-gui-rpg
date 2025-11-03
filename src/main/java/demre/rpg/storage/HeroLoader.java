package demre.rpg.storage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import demre.rpg.Main;
import demre.rpg.model.GameEngine;
import demre.rpg.model.characters.Hero;
import demre.rpg.model.factories.CharacterFactory;

public class HeroLoader {

  private static final Logger logger = LoggerFactory.getLogger(HeroLoader.class);

  private static String url = "jdbc:sqlite:" + Main.databaseName;

  public static void loadHeroesFromDatabase(GameEngine gameEngine)
      throws IOException {

    try (Connection conn = DriverManager.getConnection(url)) {
      logger.info("loadHeroesFromDatabase > Connected to database.");

      if (conn == null) {
        throw new IOException("Failed to connect to the database.");
      }

      // Create heroes table if it does not exist
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

      // Select all heroes and load them into the game engine
      String sql = "SELECT * FROM heroes";
      try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        var rs = pstmt.executeQuery();

        while (rs.next()) {
          CharacterFactory factory = CharacterFactory.getInstance();

          Hero hero = factory.newHero(
              rs.getString("class"),
              rs.getString("name"),
              rs.getInt("level"),
              rs.getInt("exp"),
              rs.getInt("att"),
              rs.getInt("def"),
              rs.getInt("hp"),
              rs.getString("weapon"),
              rs.getInt("weapon_mod"),
              rs.getString("armor"),
              rs.getInt("armor_mod"),
              rs.getString("helm"),
              rs.getInt("helm_mod"));

          gameEngine.addHero(hero);

          logger.info("loadHeroesFromDatabase > Hero loaded: " + hero);
        }
      }

    } catch (Exception e) {
      throw new IOException("Error loading from database: " + e.getMessage());
    }
  }

}
