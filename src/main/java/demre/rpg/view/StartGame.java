package demre.rpg.view;

import demre.rpg.model.characters.Hero;

public class StartGame extends javax.swing.JFrame implements WindowManager {

  private Hero hero;

  public StartGame(Hero hero) {
    this.hero = hero;
    initUI();
  }

  private void initUI() {
    setTitle("Start Game");
    setSize(400, 300);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    // Additional UI components and layout setup can be added here
  }

  @Override
  public void showSelectHero() {
    // Logic to display the hero selection screen
    System.out.println("Displaying hero selection screen...");
  }

  @Override
  public void showNewHero() {
    // Logic to display the new hero creation screen
    System.out.println("Displaying new hero creation screen...");
  }

  @Override
  public void showSelectMission(Hero hero) {
    // Logic to display the mission selection screen for the given hero
    System.out.println("Displaying mission selection screen for hero");
  }

}
