package demre.rpg.view;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.JButton;
import javax.swing.JFrame;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Window;

import demre.rpg.Main;
import demre.rpg.controller.GameController;
import demre.rpg.model.GameEngine;
import demre.rpg.model.GameEngineListener;
import demre.rpg.view.gui.HeroInfoPanel;
import demre.rpg.view.gui.MapViewPanel;
import demre.rpg.view.gui.SelectHeroPanel;
import demre.rpg.view.gui.SplashScreenPanel;

public class GUIView
    extends javax.swing.JFrame
    implements GameView, GameEngineListener {

  private final GameEngine gameEngine;
  private final GameController controller;
  private JPanel contentPanel;
  private CardLayout cardLayout;

  public GUIView(GameEngine gameEngine, GameController controller) {
    this.gameEngine = gameEngine;
    this.controller = controller;
    System.out.println("GUIView initialised with engine: " + gameEngine + " and controller: " + controller);
    initialiseGUIComponents();
  }

  @Override
  public void onStepChanged(GameEngine.Step newStep) {

    try {
      switch (newStep) {
        case SPLASH_SCREEN -> splashScreen();
        case SELECT_HERO, INVALID_HERO_SELECTION -> selectHero();
        case CREATE_HERO, INVALID_HERO_CREATION -> createHero();
        case INFO, NEW_MISSION -> showHeroInfo();
        case PLAYING, INVALID_ACTION, ENEMY_FIGHT_SUCCESS, LEVEL_UP,
            ENEMY_RUN_SUCCESS ->
          showMap();
        case ENEMY_ENCOUNTER, ENEMY_INVALID_ACTION, ENEMY_RUN_FAILURE ->
          showMap();
        case ITEM_FOUND, ITEM_FOUND_AND_LEVEL_UP, ITEM_INVALID_ACTION ->
          showMap();
        case VICTORY_MISSION, VICTORY_INVALID_ACTION -> showVictoryScreen();
        case GAME_OVER, GAME_OVER_INVALID_ACTION -> showGameOver();
        case EXIT_GAME -> cleanup();
        default -> {
        }
      }
    } catch (Exception e) {
      dispose();
      Main.errorAndExit(e, e.getMessage());
    }
  }

  private void initialiseGUIComponents() {
    System.out.println("GUIView > Initialising GUI components...");

    // Create main window
    setTitle("RPG Game");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());
    setSize(800, 600);
    setMinimumSize(new Dimension(600, 400));

    // Create content panel to display different views
    cardLayout = new CardLayout();
    contentPanel = new JPanel(cardLayout);
    contentPanel.setPreferredSize(getSize());

    // Add button to switch to console view
    JPanel bottomPanel = new JPanel();
    JButton switchButton = new JButton("Switch to Console View");
    bottomPanel.add(switchButton);

    // Add listeners
    switchButton.addActionListener(
        e -> controller.switchView("console"));

    add(contentPanel, BorderLayout.CENTER);
    add(bottomPanel, BorderLayout.SOUTH);
    setVisible(true);

    System.out.println("GUIView > GUI components initialised.");
  }

  public void showStage(String stageName, JPanel stagePanel) {
    contentPanel.add(stagePanel, stageName);
    cardLayout.show(contentPanel, stageName);
    contentPanel.revalidate();
    contentPanel.repaint();
  }

  @Override
  public void splashScreen() {
    System.out.println("GUIView > Displaying splash screen...");

    showStage("splash", new SplashScreenPanel(controller));
  }

  @Override
  public void selectHero() {
    System.out.println("GUIView > Displaying hero selection screen...");

    showStage(
        "selectHero", new SelectHeroPanel(controller, gameEngine));
  }

  @Override
  public void createHero() {
    System.out.println("GUIView > Displaying hero creation screen...");
  }

  @Override
  public void showHeroInfo() {
    System.out.println("GUIView > Displaying hero information...");

    showStage("heroInfo", new HeroInfoPanel(controller, gameEngine));

  }

  @Override
  public void showMap() {
    System.out.println("GUIView > Showing game map...");

    showStage("map", new MapViewPanel(controller, gameEngine));
  }

  @Override
  public void showEnemyEncounter() {
    System.out.println("GUIView > Displaying enemy encounter...");
  }

  @Override
  public void showItemFound() {
    System.out.println("GUIView > Displaying item found...");
  }

  @Override
  public void showVictoryScreen() {
    System.out.println("GUIView > Displaying victory screen...");
  }

  @Override
  public void showGameOver() {
    System.out.println("GUIView > Displaying game over screen...");
  }

  @Override
  public void cleanup() {
    System.out.println("GUIView > Cleaning up resources...");
    dispose();
  }

  public static void windowDispose(Component component) {
    if (component != null) {
      Window window = SwingUtilities.getWindowAncestor(component);
      if (window != null) {
        window.dispose();
      }
    }
  }

}
