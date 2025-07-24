package demre.rpg.view;

public interface GameView {

  void splashScreen();

  void selectHero();

  void createHero();

  void showHeroInfo();

  void showMap();

  void showEnemyEncounter();

  void showEnemyRunFailure();

  void showItemFound();

  void showVictoryScreen();

  void showGameOver();

  void cleanup();
}
