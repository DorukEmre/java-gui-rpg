package demre.rpg.storage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import demre.rpg.Main;
import demre.rpg.model.GameEngine;
import demre.rpg.model.characters.Hero;
import demre.rpg.util.CharacterFactory;

public class HeroLoader {
  private static String url = "jdbc:sqlite:" + Main.databaseName;

  public static void loadHeroesFromDatabase(GameEngine gameEngine) {

    try (Connection conn = DriverManager.getConnection(url)) {
      System.out.println("loadHeroesFromDatabase > Connected to database.");
      if (conn == null) {
        throw new IOException("Failed to connect to the database.");
      }

      String sql = "SELECT * FROM heroes";
      try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        var rs = pstmt.executeQuery();
        while (rs.next()) {
          CharacterFactory factory = CharacterFactory.getInstance();
          Hero hero = factory.newHero(
              rs.getString("class"),
              rs.getString("name"),
              rs.getInt("level"),
              rs.getInt("xp"),
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
          System.out.println("loadHeroesFromDatabase > Hero loaded: " + hero);
        }
      }
    } catch (Exception e) {
      System.err.println("Error loading from database: " + e.getMessage());
    }
  }

}
