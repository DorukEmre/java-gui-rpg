package demre.rpg.view.gui;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import demre.rpg.controller.GameController;

public class SplashScreenPanel extends JPanel {
  private JButton defaultButton;

  public SplashScreenPanel(GameController controller) {
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    JLabel welcomeLabel = GUIUtils.createTitle(
        "Welcome to the Game!", 24);

    JButton startButton = GUIUtils.createButton("Start Game");
    startButton.setAlignmentX(CENTER_ALIGNMENT);
    startButton.addActionListener(
        e -> controller.onSplashScreenContinue("continue"));

    add(Box.createVerticalGlue());
    add(welcomeLabel);
    add(Box.createVerticalStrut(20));
    add(startButton);
    add(Box.createVerticalGlue());

    defaultButton = startButton;
  }

  @Override
  public void addNotify() {
    super.addNotify();
    if (getRootPane() != null) {
      getRootPane().setDefaultButton(defaultButton);
    }
  }

}
