package demre.rpg.view;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import java.awt.BorderLayout;
import java.awt.Font;

import demre.rpg.controller.GameController;
import demre.rpg.model.GameEngine;
import demre.rpg.model.GameEngineListener;
import demre.rpg.model.GameEngine.Step;

public class GUIView
    extends javax.swing.JFrame
    implements GameView, GameEngineListener {

  private final GameEngine gameEngine;
  private final GameController controller;
  private JPanel mainPanel;

  public GUIView(GameEngine gameEngine, GameController controller) {
    this.gameEngine = gameEngine;
    this.controller = controller;
    System.out.println("GUIView initialised with engine: " + gameEngine + " and controller: " + controller);
    initialiseGUIComponents();
  }

  private void initialiseGUIComponents() {
    System.out.println("GUIView > Initialising GUI components...");

    // Create main window
    setTitle("RPG Game");
    setSize(800, 600);
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    // e.g., set up panels, buttons, labels, etc.
    this.mainPanel = new JPanel();

    mainPanel.setLayout(new BorderLayout());

    JLabel titleLabel = new JLabel(
        "Welcome to the RPG Game", SwingConstants.CENTER);
    titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
    mainPanel.add(titleLabel, BorderLayout.NORTH);

    JButton consoleButton = new JButton("Switch to Console View");
    mainPanel.add(consoleButton, BorderLayout.SOUTH);

    // Logic to wait for user input (e.g., button click) to proceed
    consoleButton.addActionListener(e -> {
      System.out.println("GUIView > Switching view to console...");
      // mainPanel.remove(consoleButton);
      // mainPanel.revalidate();
      // mainPanel.repaint();
      controller.switchToConsole();
    });

    setContentPane(mainPanel);
    setVisible(true);
    System.out.println("GUIView > GUI components initialised.");
  }

  @Override
  public void onStepChanged(GameEngine.Step newStep) {
    switch (newStep) {
      case SPLASH_SCREEN -> splashScreen();
      case SELECT_HERO, INVALID_HERO_SELECTION -> selectHero();
      case CREATE_HERO, INVALID_HERO_CREATION -> createHero();
      case INFO, NEW_MISSION -> showHero();
      case PLAYING, INVALID_ACTION, ENEMY_FIGHT_SUCCESS, LEVEL_UP, ENEMY_RUN_SUCCESS -> updateView();
      case ENEMY_ENCOUNTER, ENEMY_INVALID_ACTION -> showEnemyEncounter();
      case ENEMY_RUN_FAILURE -> showEnemyRunFailure();
      case ITEM_FOUND, ITEM_FOUND_AND_LEVEL_UP, ITEM_INVALID_ACTION -> showItemFound();
      case VICTORY_MISSION, VICTORY_INVALID_ACTION -> showVictoryScreen();
      case GAME_OVER, GAME_OVER_INVALID_ACTION -> showGameOver();
      case EXIT_GAME -> cleanup();
      default -> {
      }
    }
  }

  @Override
  public void splashScreen() {
    System.out.println("GUIView > Displaying splash screen...");
    JButton startButton = new JButton("Start Game");
    mainPanel.add(startButton, BorderLayout.CENTER);

    // Logic to wait for user input (e.g., button click) to proceed
    startButton.addActionListener(e -> {
      System.out.println("GUIView > Start button clicked, proceeding to game...");
      mainPanel.remove(startButton);
      mainPanel.revalidate();
      mainPanel.repaint();
      controller.onSplashScreenContinue("continue");
    });

    return;
  }

  @Override
  public void selectHero() {
    System.out.println("GUIView > Displaying hero selection screen...");
    // gameEngine.setCurrentStep(Step.EXIT_GAME);
  }

  @Override
  public void createHero() {
    System.out.println("GUIView > Displaying hero creation screen...");
  }

  @Override
  public void showHero() {
    System.out.println("GUIView > Displaying hero information...");

  }

  @Override
  public void updateView() {
    System.out.println("GUIView > Updating GUI view...");
  }

  @Override
  public void showEnemyEncounter() {
    System.out.println("GUIView > Displaying enemy encounter...");
  }

  @Override
  public void showEnemyRunFailure() {
    System.out.println("GUIView > Displaying enemy run failure...");
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

}
