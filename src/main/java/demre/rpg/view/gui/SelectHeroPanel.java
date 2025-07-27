package demre.rpg.view.gui;

import java.awt.Font;
import java.awt.Insets;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import demre.rpg.controller.GameController;
import demre.rpg.model.GameEngine;
import demre.rpg.model.characters.Hero;

public class SelectHeroPanel extends JPanel {
  private JButton defaultButton;

  public SelectHeroPanel(GameController controller, GameEngine gameEngine) {
    List<Hero> heroes = gameEngine.getHeroes();

    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    add(Box.createVerticalGlue());
    add(Box.createVerticalStrut(10));

    // Title label
    JLabel selectHeroTitle = new JLabel("Select Your Hero");
    selectHeroTitle.setFont(new Font("Serif", Font.BOLD, 24));
    selectHeroTitle.setAlignmentX(CENTER_ALIGNMENT);
    add(selectHeroTitle);

    add(Box.createVerticalStrut(20));

    // heroes = new ArrayList<>(); // for testing

    JPanel heroesList = new JPanel();
    heroesList.setLayout(new BoxLayout(heroesList, BoxLayout.Y_AXIS));
    heroesList.setAlignmentX(LEFT_ALIGNMENT);

    // List heroes as buttons
    for (int i = 0; i < heroes.size(); i++) {
      Hero hero = heroes.get(i);

      // Create button (left)
      JButton heroButton = new JButton(i + 1 + ". ");
      heroButton.setMargin(new Insets(5, 8, 5, 6));
      String indexStr = i + 1 + "";
      heroButton.addActionListener(
          e -> controller.onSelectHeroContinue(indexStr));

      // Create label (right)
      JLabel heroStats = new JLabel(
          hero.getName() + " (" + hero.getHeroClass() + ")"
              + " Level: " + hero.getLevel() + " XP: " + hero.getExperience());
      heroStats.setFont(new Font("Serif", Font.PLAIN, 16));

      // Create horizontal panel for button + label
      JPanel heroLine = new JPanel();
      heroLine.setLayout(new BoxLayout(heroLine, BoxLayout.X_AXIS));
      heroLine.setAlignmentX(LEFT_ALIGNMENT);
      heroLine.add(heroButton);
      heroLine.add(Box.createHorizontalStrut(20));
      heroLine.add(heroStats);

      heroesList.add(heroLine);
    }

    add(heroesList);

    add(Box.createVerticalStrut(20));

    JLabel newHeroTitle;
    if (heroes == null || heroes.isEmpty()) {
      newHeroTitle = new JLabel(
          "No heroes available. Create a new hero.");
    } else {
      newHeroTitle = new JLabel(
          "Or create a new hero:");
    }
    newHeroTitle.setFont(new Font("Serif", Font.BOLD, 24));
    newHeroTitle.setAlignmentX(CENTER_ALIGNMENT);
    add(newHeroTitle);

    add(Box.createVerticalStrut(20));

    JButton newHeroButton = new JButton("New Hero");
    newHeroButton.setAlignmentX(CENTER_ALIGNMENT);
    newHeroButton.addActionListener(
        e -> controller.onSelectHeroContinue("new"));
    add(newHeroButton);

    add(Box.createVerticalStrut(10));
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
