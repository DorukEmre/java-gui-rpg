package demre.rpg.view.gui;

import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import demre.rpg.controller.GameController;
import demre.rpg.model.GameEngine;

public class VictoryScreenPanel extends JPanel {

  public VictoryScreenPanel(GameController controller, GameEngine gameEngine) {

    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    add(Box.createVerticalGlue());

    JLabel titleLabel = new JLabel("Congratulations!");
    titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
    titleLabel.setAlignmentX(CENTER_ALIGNMENT);
    add(titleLabel);
    add(Box.createVerticalStrut(20));

    JLabel label1 = new JLabel("You have successfully completed the mission!");
    label1.setFont(new Font("Serif", Font.PLAIN, 18));
    label1.setAlignmentX(CENTER_ALIGNMENT);
    add(label1);

    if (gameEngine.getStep() == GameEngine.Step.VICTORY_INVALID_ACTION) {
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
      JButton button = new JButton("Next mission");
      button.setAlignmentX(CENTER_ALIGNMENT);
      button.addActionListener(
          e -> controller.onVictoryScreenContinue("try"));
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
  }

}
