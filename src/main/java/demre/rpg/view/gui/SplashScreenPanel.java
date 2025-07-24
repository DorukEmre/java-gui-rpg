package demre.rpg.view.gui;

import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import demre.rpg.controller.GameController;

public class SplashScreenPanel extends JPanel {

  public SplashScreenPanel(GameController controller) {
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    JLabel welcomeLabel = new JLabel("Welcome to the Game!", JLabel.CENTER);
    welcomeLabel.setFont(new Font("Serif", Font.BOLD, 24));
    welcomeLabel.setAlignmentX(CENTER_ALIGNMENT);

    JButton startButton = new JButton("Start Game");
    startButton.setAlignmentX(CENTER_ALIGNMENT);
    startButton.addActionListener(
        e -> controller.onSplashScreenContinue("continue"));

    add(Box.createVerticalGlue());
    add(welcomeLabel);
    add(Box.createVerticalStrut(20));
    add(startButton);
    add(Box.createVerticalGlue());
  }
}
