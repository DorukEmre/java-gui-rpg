package demre.rpg.view;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.Box;
import javax.swing.BoxLayout;
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
import demre.rpg.view.gui.ButtonsMap;
import demre.rpg.view.gui.CreateHeroPanel;
import demre.rpg.view.gui.GUIUtils;
import demre.rpg.view.gui.GameOverPanel;
import demre.rpg.view.gui.HeroInfoPanel;
import demre.rpg.view.gui.MapViewPanel;
import demre.rpg.view.gui.SelectHeroPanel;
import demre.rpg.view.gui.SplashScreenPanel;
import demre.rpg.view.gui.VictoryScreenPanel;

public class GUIView
    extends javax.swing.JFrame
    implements GameView, GameEngineListener {

  private static final Logger logger = LoggerFactory.getLogger(GUIView.class);

  private final GameEngine gameEngine;
  private final GameController controller;
  private JPanel contentPanel;
  private CardLayout cardLayout;
  private JPanel mapViewPanel;

  public ButtonsMap tileButtonsMap;

  public GUIView(GameEngine gameEngine, GameController controller) {
    this.gameEngine = gameEngine;
    this.controller = controller;
    this.tileButtonsMap = new ButtonsMap(gameEngine, controller); // empty array
    this.mapViewPanel = null;

    logger.info("GUIView initialised with engine: " + gameEngine + " and controller: " + controller);
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
    logger.info("GUIView > Initialising GUI components...");

    // Create main window
    setTitle("Java RPG with Swing GUI");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());
    setSize(800, 600);
    setMinimumSize(new Dimension(600, 400));

    // Make window full screen
    setExtendedState(JFrame.MAXIMIZED_BOTH);

    // Create content panel to display different views
    cardLayout = new CardLayout();
    contentPanel = new JPanel(cardLayout);
    contentPanel.setPreferredSize(getSize());

    // Add button to switch to console view
    JPanel bottomPanel = new JPanel();
    bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));

    bottomPanel.add(Box.createHorizontalGlue());

    JButton switchButton = GUIUtils.createButton("Switch to Console View");
    switchButton.addActionListener(
        e -> controller.switchView("console"));

    JButton exitButton = GUIUtils.createButton("Save and exit");
    exitButton.addActionListener(e -> {
      try {
        controller.exitGame();
      } catch (Exception ex) {
        Main.errorAndExit(ex, ex.getMessage());
      }
    });

    bottomPanel.add(switchButton);
    bottomPanel.add(Box.createHorizontalStrut(10));
    bottomPanel.add(exitButton);

    add(contentPanel, BorderLayout.CENTER);
    add(bottomPanel, BorderLayout.SOUTH);
    setVisible(true);

    logger.info("GUIView > GUI components initialised.");
  }

  public void showStage(String stageName, JPanel stagePanel) {
    contentPanel.add(stagePanel, stageName);
    cardLayout.show(contentPanel, stageName);
    contentPanel.revalidate();
    contentPanel.repaint();
  }

  @Override
  public void splashScreen() {
    logger.info("GUIView > Displaying splash screen...");

    showStage("splash", new SplashScreenPanel(controller));
  }

  @Override
  public void selectHero() {
    logger.info("GUIView > Displaying hero selection screen...");

    showStage(
        "selectHero", new SelectHeroPanel(controller, gameEngine));
  }

  @Override
  public void createHero() {
    logger.info("GUIView > Displaying hero creation screen...");

    showStage(
        "createHero", new CreateHeroPanel(controller, gameEngine));
  }

  @Override
  public void showHeroInfo() {
    logger.info("GUIView > Displaying hero information...");

    showStage("heroInfo", new HeroInfoPanel(controller, gameEngine));

  }

  @Override
  public void showMap() {
    logger.info("GUIView > Showing game map...");

    if (gameEngine.isMoving()) {
      // Use existing mapViewPanel if player is moving
      ((MapViewPanel) mapViewPanel).updateHeroPosition();
    } else {
      mapViewPanel = new MapViewPanel(this, controller, gameEngine);
    }

    showStage("map", mapViewPanel);
  }

  @Override
  public void showVictoryScreen() {
    logger.info("GUIView > Displaying victory screen...");

    showStage(
        "victory", new VictoryScreenPanel(controller, gameEngine));
  }

  @Override
  public void showGameOver() {
    logger.info("GUIView > Displaying game over screen...");

    showStage("gameOver", new GameOverPanel(controller, gameEngine));
  }

  @Override
  public void cleanup() {
    logger.info("GUIView > Cleaning up resources...");
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
