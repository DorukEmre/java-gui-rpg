package demre.rpg.view.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import demre.rpg.Main;
import demre.rpg.controller.GameController;
import demre.rpg.model.GameEngine;
import demre.rpg.view.GUIView;

/**
 * Controls the character on the map.
 */
public class MapControlPanel extends JPanel {
  private final GameController controller;
  private final GameEngine gameEngine;

  public MapControlPanel(GameController controller, GameEngine gameEngine) {
    this.controller = controller;
    this.gameEngine = gameEngine;

    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    // Add components for controlling the map view
    add(createInstructionsPanel());
    add(Box.createVerticalStrut(10));
    add(createDirectionPanel());
  }

  private JPanel createInstructionsPanel() {
    JPanel instructionPanel = new JPanel();
    instructionPanel.setLayout(
        new BoxLayout(instructionPanel, BoxLayout.Y_AXIS));
    instructionPanel.setAlignmentX(CENTER_ALIGNMENT);

    GameEngine.Step step = gameEngine.getStep();
    if (step == GameEngine.Step.INVALID_ACTION) {
      JLabel invalidActionLabel = new JLabel(
          "Invalid action. Please try again.");
      formatLabel(invalidActionLabel);
      add(invalidActionLabel);

    } else if (step == GameEngine.Step.ENEMY_FIGHT_SUCCESS) {
      JLabel fightSuccessLabel = new JLabel(
          "You defeated the enemy!");
      formatLabel(fightSuccessLabel);
      add(fightSuccessLabel);

    } else if (step == GameEngine.Step.LEVEL_UP) {
      JLabel levelUpLabel = new JLabel(
          "You defeated the enemy and leveled up!");
      formatLabel(levelUpLabel);
      add(levelUpLabel);

      add(Box.createVerticalStrut(5));

      JLabel levelInfoLabel = new JLabel(
          "You are now level "
              + gameEngine.getHero().getLevel() + " with "
              + gameEngine.getHero().getExperience() + " experience points.");
      formatLabel(levelInfoLabel);
      add(levelInfoLabel);

    } else if (step == GameEngine.Step.ENEMY_RUN_SUCCESS) {
      JLabel runSuccessLabel = new JLabel(
          "You successfully ran away from the enemy!");
      formatLabel(runSuccessLabel);
      add(runSuccessLabel);
    }

    // JLabel instructionsLabel = new JLabel(
    // "<html>Use the buttons to move your hero.<br>Click on the map to interact
    // with it.</html>");
    // instructionsLabel.setFont(new Font("Serif", Font.PLAIN, 16));
    // instructionsLabel.setAlignmentX(CENTER_ALIGNMENT);
    // instructionPanel.add(instructionsLabel);

    JLabel actionPromptLabel = new JLabel(
        "(N)orth, (S)outh, (E)ast, (W)est, (i)nfo or 'exit'.");
    actionPromptLabel.setFont(new Font("Serif", Font.PLAIN, 16));
    actionPromptLabel.setAlignmentX(CENTER_ALIGNMENT);
    add(actionPromptLabel);

    return instructionPanel;
  }

  private void formatLabel(JLabel label) {
    label.setFont(new Font("Serif", Font.PLAIN, 16));
    label.setAlignmentX(CENTER_ALIGNMENT);
    // label.setHorizontalAlignment(JLabel.CENTER);
    // label.setVerticalAlignment(JLabel.CENTER);
  }

  private JPanel createDirectionPanel() {
    JPanel directionPanel = new JPanel();
    directionPanel.setLayout(new GridLayout(3, 3));
    directionPanel.setMaximumSize(new Dimension(96, 96)); // 3*32
    directionPanel.setPreferredSize(new Dimension(96, 96));

    // Dummy buttons
    JButton dummy1 = newDummyButton();
    JButton dummy2 = newDummyButton();
    JButton dummy3 = newDummyButton();
    JButton dummy4 = newDummyButton();
    JButton dummy5 = newDummyButton();

    // Direction buttons
    JButton northButton = newDirectionButton("N");
    JButton southButton = newDirectionButton("S");
    JButton eastButton = newDirectionButton("E");
    JButton westButton = newDirectionButton("W");

    // Add buttons in compass arrangement
    directionPanel.add(dummy1); // (0,0)
    directionPanel.add(northButton); // (0,1)
    directionPanel.add(dummy2); // (0,2)
    directionPanel.add(westButton); // (1,0)
    directionPanel.add(dummy3); // (1,1) center
    directionPanel.add(eastButton); // (1,2)
    directionPanel.add(dummy4); // (2,0)
    directionPanel.add(southButton); // (2,1)
    directionPanel.add(dummy5); // (2,2)

    return directionPanel;
  }

  private JButton newDummyButton() {
    JButton button = new JButton();
    button.setPreferredSize(new Dimension(32, 32));
    button.setMargin(new Insets(0, 0, 0, 0));
    button.setEnabled(false);
    button.setBorderPainted(false);
    button.setContentAreaFilled(false);

    return button;
  }

  private JButton newDirectionButton(String direction) {
    JButton button = new JButton(direction);
    button.setPreferredSize(new Dimension(32, 32));
    button.setMinimumSize(new Dimension(32, 32));
    button.setMaximumSize(new Dimension(32, 32));
    button.setMargin(new Insets(0, 0, 0, 0));

    button.addActionListener(e -> {
      try {
        controller.onMapInputContinue(direction);
      } catch (Exception ex) {
        GUIView.windowDispose(button);
        Main.errorAndExit(ex, ex.getMessage());
      }
    });

    return button;
  }

}
