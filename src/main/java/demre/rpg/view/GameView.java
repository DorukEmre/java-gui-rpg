package demre.rpg.view;

public interface GameView {

  void splashScreen();

  void selectHero();

  void createHero();

  void showHero();

  void updateView();

  void showEnemyEncounter();

  void showEnemyRunFailure();

  void showItemFound();

  void showVictoryScreen();

  void showGameOver();

  void cleanup();
}
