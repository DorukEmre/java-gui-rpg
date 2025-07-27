package demre.rpg.view.gui;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import demre.rpg.controller.GameController;
import demre.rpg.model.GameEngine;

public class EnemyEncounterPanel extends JPanel {
  private final GameController controller;
  private final GameEngine gameEngine;
  private JButton defaultButton;

  public EnemyEncounterPanel(GameController controller, GameEngine gameEngine) {
    this.controller = controller;
    this.gameEngine = gameEngine;

    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    // Add components for controlling the map view
    add(createInstructionsPanel());
    add(createActionButtonsPanel());
  }

  private JPanel createInstructionsPanel() {
    JPanel instructionPanel = new JPanel();
    instructionPanel.setLayout(
        new BoxLayout(instructionPanel, BoxLayout.Y_AXIS));
    instructionPanel.setAlignmentX(CENTER_ALIGNMENT);

    GameEngine.Step step = gameEngine.getStep();

    if (step == GameEngine.Step.ENEMY_INVALID_ACTION) {
      JLabel label = GUIUtils.createInfo(
          "Invalid action. Please try again.", 16);
      instructionPanel.add(label);

    }
    if (step == GameEngine.Step.ENEMY_ENCOUNTER
        || step == GameEngine.Step.ENEMY_INVALID_ACTION) {
      JLabel label = GUIUtils.createLabel(
          "You encounter an enemy!", 16);
      instructionPanel.add(label);

    } else if (step == GameEngine.Step.ENEMY_RUN_FAILURE) {
      JLabel label1 = GUIUtils.createLabel(
          "You failed to run away from the enemy!", 16);
      instructionPanel.add(label1);
      JLabel label2 = GUIUtils.createLabel(
          "You have to fight.", 16);
      instructionPanel.add(label2);
    }

    return instructionPanel;
  }

  private JPanel createActionButtonsPanel() {
    JPanel actionPanel = new JPanel();
    actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.X_AXIS));
    actionPanel.setAlignmentX(CENTER_ALIGNMENT);

    GameEngine.Step step = gameEngine.getStep();

    {
      JButton button = new JButton("Fight");
      button.addActionListener(
          e -> controller.onEnemyEncounterContinue("Fight"));
      actionPanel.add(button);
    }

    actionPanel.add(Box.createHorizontalStrut(10));

    if (step == GameEngine.Step.ENEMY_ENCOUNTER
        || step == GameEngine.Step.ENEMY_INVALID_ACTION) {

      JButton button = new JButton("Run");
      button.addActionListener(
          e -> controller.onEnemyEncounterContinue("Run"));
      actionPanel.add(button);
    }

    if (step == GameEngine.Step.ENEMY_RUN_FAILURE)
      defaultButton = (JButton) actionPanel.getComponent(0); // Fight button
    else
      defaultButton = (JButton) actionPanel.getComponent(2); // Run button

    return actionPanel;
  }

  @Override
  public void addNotify() {
    super.addNotify();
    if (getRootPane() != null) {
      getRootPane().setDefaultButton(defaultButton);
    }
  }

}
