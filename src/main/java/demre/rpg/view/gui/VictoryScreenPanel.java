package demre.rpg.view.gui;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import demre.rpg.controller.GameController;
import demre.rpg.model.GameEngine;

public class VictoryScreenPanel extends JPanel {
  private JButton defaultButton;

  public VictoryScreenPanel(GameController controller, GameEngine gameEngine) {

    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    add(Box.createVerticalGlue());

    JLabel titleLabel = GUIUtils.createTitle(
        "Congratulations!", 24);
    add(titleLabel);

    add(Box.createVerticalStrut(20));

    JLabel label1 = GUIUtils.createLabel(
        "You have successfully completed the mission!", 18);
    add(label1);

    if (gameEngine.getStep() == GameEngine.Step.VICTORY_INVALID_ACTION) {
      JLabel label2 = GUIUtils.createInfo(
          "Invalid action. Please try again!", 18);
      add(label2);
    }

    add(Box.createVerticalStrut(20));

    // Add panel with choice buttons
    JPanel choice = new JPanel();
    choice.setLayout(new BoxLayout(choice, BoxLayout.X_AXIS));
    choice.setAlignmentX(CENTER_ALIGNMENT);

    {
      JButton button = new JButton("Next mission");
      button.setAlignmentX(CENTER_ALIGNMENT);
      button.addActionListener(
          e -> controller.onVictoryScreenContinue("next"));
      choice.add(button);
    }

    choice.add(Box.createHorizontalStrut(20));

    {
      JButton button = new JButton("Exit");
      button.setAlignmentX(CENTER_ALIGNMENT);
      button.addActionListener(
          e -> controller.onVictoryScreenContinue("exit"));
      choice.add(button);
    }

    add(choice);

    add(Box.createVerticalGlue());

    defaultButton = (JButton) choice.getComponent(0);
  }

  @Override
  public void addNotify() {
    super.addNotify();
    if (getRootPane() != null) {
      getRootPane().setDefaultButton(defaultButton);
    }
  }

}
