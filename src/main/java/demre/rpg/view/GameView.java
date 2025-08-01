package demre.rpg.view;

public interface GameView {

  void splashScreen();

  void selectHero();

  void createHero();

  void showHeroInfo();

  void showMap();

  void showVictoryScreen();

  void showGameOver();

  void cleanup();
}
