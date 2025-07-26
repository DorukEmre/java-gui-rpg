
package demre.rpg.view.gui;

import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import demre.rpg.controller.GameController;
import demre.rpg.model.GameEngine;

public class GameOverPanel extends JPanel {
  private JButton defaultButton;

  public GameOverPanel(GameController controller, GameEngine gameEngine) {

    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    add(Box.createVerticalGlue());

    JLabel titleLabel = new JLabel("You died!");
    titleLabel.setFont(new Font("Serif", Font.BOLD, 48));
    titleLabel.setAlignmentX(CENTER_ALIGNMENT);
    add(titleLabel);
    add(Box.createVerticalStrut(20));

    if (gameEngine.getStep() == GameEngine.Step.GAME_OVER_INVALID_ACTION) {
      JLabel label2 = new JLabel("Invalid action. Please try again!");
      label2.setFont(new Font("Serif", Font.PLAIN, 18));
      label2.setAlignmentX(CENTER_ALIGNMENT);
      add(label2);
    }

    add(Box.createVerticalStrut(20));

    // Add panel with choice buttons
    JPanel choice = new JPanel();
    choice.setLayout(new BoxLayout(choice, BoxLayout.X_AXIS));
    choice.setAlignmentX(CENTER_ALIGNMENT);

    {
      JButton button = new JButton("Try again");
      button.setAlignmentX(CENTER_ALIGNMENT);
      button.addActionListener(
          e -> controller.onGameOverContinue("try"));
      choice.add(button);
    }

    choice.add(Box.createHorizontalStrut(20));

    {
      JButton button = new JButton("Exit");
      button.setAlignmentX(CENTER_ALIGNMENT);
      button.addActionListener(
          e -> controller.onGameOverContinue("exit"));
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