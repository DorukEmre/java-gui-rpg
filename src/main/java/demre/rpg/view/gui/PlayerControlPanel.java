package demre.rpg.view.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import demre.rpg.Main;
import demre.rpg.controller.GameController;
import demre.rpg.model.GameEngine;
import demre.rpg.model.characters.Hero;
import demre.rpg.view.GUIView;

/**
 * Controls the character on the map.
 */
public class PlayerControlPanel extends JPanel {
  private final GameController controller;
  private final GameEngine gameEngine;
  private Hero hero;

  public PlayerControlPanel(GameController controller, GameEngine gameEngine) {
    this.controller = controller;
    this.gameEngine = gameEngine;
    this.hero = gameEngine.getHero();

    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    // Add components for controlling the map view
    add(createInstructionsPanel());
    add(Box.createVerticalStrut(10));
    add(createCharacterControlPanel());
  }

  private JPanel createInstructionsPanel() {
    JPanel instructionPanel = new JPanel();
    instructionPanel.setLayout(
        new BoxLayout(instructionPanel, BoxLayout.Y_AXIS));
    instructionPanel.setAlignmentX(CENTER_ALIGNMENT);

    GameEngine.Step step = gameEngine.getStep();
    if (step == GameEngine.Step.INVALID_ACTION) {
      JLabel invalidActionLabel = createLabel(
          "Invalid action. Please try again.");
      add(invalidActionLabel);

    } else if (step == GameEngine.Step.ENEMY_FIGHT_SUCCESS) {
      JLabel fightSuccessLabel = createLabel(
          "You defeated the enemy!");
      add(fightSuccessLabel);

    } else if (step == GameEngine.Step.LEVEL_UP) {
      JLabel levelUpLabel = createLabel(
          "You defeated the enemy and leveled up!");
      add(levelUpLabel);

      add(Box.createVerticalStrut(5));

      JLabel levelInfoLabel = createLabel(
          "You are now level "
              + hero.getLevel() + " with "
              + hero.getExperience() + " experience points.");
      add(levelInfoLabel);

    } else if (step == GameEngine.Step.ENEMY_RUN_SUCCESS) {
      JLabel runSuccessLabel = createLabel(
          "You successfully ran away from the enemy!");
      add(runSuccessLabel);
    }

    return instructionPanel;
  }

  private JLabel createLabel(String text) {
    JLabel label = new JLabel(text);

    label.setFont(new Font("Serif", Font.PLAIN, 16));
    label.setAlignmentX(CENTER_ALIGNMENT);

    return label;
  }

  private JPanel createCharacterControlPanel() {
    JPanel controlPanel = new JPanel();
    controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.X_AXIS));
    controlPanel.setAlignmentX(CENTER_ALIGNMENT);

    // Add basic hero info
    JPanel heroInfoPanel = createBasicHeroInfoPanel();
    controlPanel.add(heroInfoPanel);

    controlPanel.add(Box.createHorizontalStrut(100));

    // Add direction buttons
    JPanel directionPanel = createDirectionPanel();
    controlPanel.add(directionPanel);

    controlPanel.add(Box.createHorizontalStrut(100));

    // Add info button
    JButton continueButton = new JButton("Info");
    continueButton.addActionListener(e -> {
      try {
        controller.onMapInputContinue("Info");
      } catch (Exception ex) {
        GUIView.windowDispose(continueButton);
        Main.errorAndExit(ex, ex.getMessage());
      }
    });
    controlPanel.add(continueButton);

    return controlPanel;
  }

  private JPanel createBasicHeroInfoPanel() {
    JPanel heroInfoPanel = new JPanel();
    heroInfoPanel.setLayout(new BoxLayout(heroInfoPanel, BoxLayout.Y_AXIS));
    heroInfoPanel.setAlignmentX(CENTER_ALIGNMENT);

    {
      JLabel label = new JLabel(hero.getName());
      label.setFont(new Font("Serif", Font.BOLD, 16));
      label.setAlignmentX(CENTER_ALIGNMENT);
      heroInfoPanel.add(label);
    }
    {
      JLabel label = new JLabel(hero.getHeroClass());
      label.setFont(new Font("Arial", Font.BOLD, 14));
      label.setAlignmentX(CENTER_ALIGNMENT);
      heroInfoPanel.add(label);
    }
    {
      JLabel label = new JLabel(
          "Level: " + hero.getLevel() + ", Exp: " + hero.getExperience());
      label.setFont(new Font("Arial", Font.BOLD, 14));
      label.setAlignmentX(CENTER_ALIGNMENT);
      heroInfoPanel.add(label);
    }

    return heroInfoPanel;
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

    // Bind buttons to keys
    bindButtonToKey(
        directionPanel, northButton, KeyStroke.getKeyStroke("UP"));
    bindButtonToKey(
        directionPanel, southButton, KeyStroke.getKeyStroke("DOWN"));
    bindButtonToKey(
        directionPanel, westButton, KeyStroke.getKeyStroke("LEFT"));
    bindButtonToKey(
        directionPanel, eastButton, KeyStroke.getKeyStroke("RIGHT"));

    // Add buttons in a grid
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

  private void bindButtonToKey(
      JPanel panel, JButton button, KeyStroke keyStroke) {

    InputMap im = panel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW);
    ActionMap am = panel.getActionMap();

    String actionName = "move_" + keyStroke.toString();

    im.put(keyStroke, actionName);
    am.put(actionName, new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        button.doClick();
      }
    });

  }

}
