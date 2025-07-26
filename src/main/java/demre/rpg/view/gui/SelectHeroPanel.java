package demre.rpg.view.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import demre.rpg.controller.GameController;
import demre.rpg.model.GameEngine;
import demre.rpg.model.characters.Hero;

public class SelectHeroPanel extends JPanel {
  private JButton defaultButton;

  public SelectHeroPanel(GameController controller, GameEngine gameEngine) {
    List<Hero> heroes = gameEngine.getHeroes();

    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    // Title label
    JLabel selectHeroLabel = new JLabel("Select Your Hero", JLabel.CENTER);
    selectHeroLabel.setFont(new Font("Serif", Font.BOLD, 24));
    selectHeroLabel.setAlignmentX(LEFT_ALIGNMENT);
    add(Box.createVerticalGlue());
    add(selectHeroLabel);
    add(Box.createVerticalStrut(20));

    // heroes = new ArrayList<>(); // for testing

    // List heroes as buttons
    for (int i = 0; i < heroes.size(); i++) {
      Hero hero = heroes.get(i);

      // Create button (left)
      JButton heroButton = new JButton(
          "<html>" + hero.getName() + "<br>" + hero.getHeroClass() + "</html>");
      heroButton.setAlignmentX(LEFT_ALIGNMENT);
      int buttonHeight = heroButton.getPreferredSize().height;
      heroButton.setPreferredSize(new Dimension(160, buttonHeight));
      heroButton.setMaximumSize(new Dimension(160, buttonHeight));
      heroButton.setMinimumSize(new Dimension(160, buttonHeight));
      heroButton.setHorizontalAlignment(SwingConstants.CENTER);
      heroButton.setVerticalAlignment(SwingConstants.CENTER);

      String indexStr = i + 1 + "";
      heroButton.addActionListener(
          e -> controller.onSelectHeroContinue(indexStr));

      // Create label (right)
      JLabel statsLabel = new JLabel(
          "Level: " + hero.getLevel() + "  XP: " + hero.getExperience());
      statsLabel.setFont(new Font("Serif", Font.PLAIN, 16));
      statsLabel.setAlignmentY(CENTER_ALIGNMENT);

      // Create horizontal panel for button + label
      JPanel heroPanel = new JPanel();
      heroPanel.setLayout(new BoxLayout(heroPanel, BoxLayout.X_AXIS));
      heroPanel.setAlignmentX(LEFT_ALIGNMENT);
      heroPanel.add(heroButton);
      heroPanel.add(Box.createHorizontalStrut(20));
      heroPanel.add(statsLabel);

      add(heroPanel);
    }

    add(Box.createVerticalStrut(20));

    JLabel newHeroLabel;
    if (heroes == null || heroes.isEmpty()) {
      newHeroLabel = new JLabel(
          "No heroes available. Create a new hero.", JLabel.CENTER);
    } else {
      newHeroLabel = new JLabel(
          "Or create a new hero:", JLabel.CENTER);
    }
    newHeroLabel.setAlignmentX(LEFT_ALIGNMENT);
    add(newHeroLabel);
    add(Box.createVerticalStrut(5));

    JButton newHeroButton = new JButton("New Hero");
    newHeroButton.setAlignmentX(LEFT_ALIGNMENT);
    newHeroButton.addActionListener(
        e -> controller.onSelectHeroContinue("new"));
    add(newHeroButton);
    add(Box.createVerticalGlue());

    defaultButton = newHeroButton;
  }

  @Override
  public void addNotify() {
    super.addNotify();
    if (getRootPane() != null) {
      getRootPane().setDefaultButton(defaultButton);
    }
  }

}
