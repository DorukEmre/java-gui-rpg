package demre.rpg.view.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Insets;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import demre.rpg.Main;
import demre.rpg.controller.GameController;
import demre.rpg.model.GameEngine;
import demre.rpg.model.characters.Hero;
import demre.rpg.view.GUIView;

public class SelectHeroPanel extends JPanel {
  private final GameController controller;
  private final GameEngine gameEngine;
  private JButton defaultButton;

  public SelectHeroPanel(GameController controller, GameEngine gameEngine) {
    this.controller = controller;
    this.gameEngine = gameEngine;

    setLayout(new BorderLayout());

    // Hero selection panel
    JPanel contentPanel = createContentPanel();

    // Delete/populate heroes in database panel
    JPanel databasePanel = createDatabasePanel();

    add(contentPanel, BorderLayout.CENTER);
    add(databasePanel, BorderLayout.SOUTH);

  }

  private JPanel createContentPanel() {
    List<Hero> heroes = gameEngine.getHeroes();

    JPanel contentPanel = new JPanel();
    contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

    contentPanel.add(Box.createVerticalGlue());
    contentPanel.add(Box.createVerticalStrut(10));

    // Title label
    JLabel selectHeroTitle = new JLabel("Select Your Hero");
    selectHeroTitle.setFont(new Font("Serif", Font.BOLD, 24));
    // selectHeroTitle.setAlignmentX(CENTER_ALIGNMENT);
    contentPanel.add(selectHeroTitle);

    contentPanel.add(Box.createVerticalStrut(20));

    JPanel heroesList = new JPanel();
    heroesList.setLayout(new BoxLayout(heroesList, BoxLayout.Y_AXIS));
    // heroesList.setAlignmentX(LEFT_ALIGNMENT);

    // List heroes as buttons
    for (int i = 0; i < heroes.size(); i++) {
      Hero hero = heroes.get(i);

      // Create button (left)
      JButton heroButton = new JButton(i + 1 + ". ");
      heroButton.setMargin(new Insets(5, 8, 5, 6));
      String indexStr = i + 1 + "";
      heroButton.addActionListener(e -> {
        try {
          controller.onSelectHeroContinue(indexStr);
        } catch (Exception ex) {
          GUIView.windowDispose(heroButton);
          Main.errorAndExit(ex, ex.getMessage());
        }
      });

      // Create label (right)
      JLabel heroStats = new JLabel(
          hero.getName() + " - " + hero.getHeroClass()
              + ". Level: " + hero.getLevel() + ", XP: " + hero.getExperience()
              + ", ATT: " + hero.getAttack() + ", DEF: " + hero.getDefense()
              + ", HP: " + hero.getHitPoints());
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

    // Make the list scrollable
    JScrollPane scrollPane = new JScrollPane(heroesList);
    // scrollPane.setAlignmentX(LEFT_ALIGNMENT);
    contentPanel.add(scrollPane);

    contentPanel.add(Box.createVerticalStrut(20));

    JLabel newHeroTitle;
    if (heroes == null || heroes.isEmpty()) {
      newHeroTitle = new JLabel(
          "No heroes available. Create a new hero.");
    } else {
      newHeroTitle = new JLabel(
          "Or create a new hero:");
    }
    newHeroTitle.setFont(new Font("Serif", Font.BOLD, 24));
    // newHeroTitle.setAlignmentX(CENTER_ALIGNMENT);
    contentPanel.add(newHeroTitle);

    contentPanel.add(Box.createVerticalStrut(20));

    JButton newHeroButton = new JButton("New Hero");
    // newHeroButton.setAlignmentX(CENTER_ALIGNMENT);
    newHeroButton.addActionListener(e -> {
      try {
        controller.onSelectHeroContinue("new");
      } catch (Exception ex) {
        GUIView.windowDispose(newHeroButton);
        Main.errorAndExit(ex, ex.getMessage());
      }
    });
    contentPanel.add(newHeroButton);

    contentPanel.add(Box.createVerticalStrut(10));
    contentPanel.add(Box.createVerticalGlue());

    defaultButton = newHeroButton;

    return contentPanel;
  }

  private JPanel createDatabasePanel() {
    JPanel databasePanel = new JPanel();
    databasePanel.setLayout(new BoxLayout(databasePanel, BoxLayout.X_AXIS));

    JButton deleteHeroesButton = new JButton("Delete All Heroes");
    deleteHeroesButton.addActionListener(e -> {
      try {
        controller.onDatabaseAction("deleteAll");
      } catch (Exception ex) {
        GUIView.windowDispose(deleteHeroesButton);
        Main.errorAndExit(ex, ex.getMessage());
      }
    });

    JButton generateHeroesButton = new JButton("Generate Heroes (lvl 1-10)");
    generateHeroesButton.addActionListener(e -> {
      try {
        controller.onDatabaseAction("generate");
      } catch (Exception ex) {
        GUIView.windowDispose(generateHeroesButton);
        Main.errorAndExit(ex, ex.getMessage());
      }
    });

    databasePanel.add(deleteHeroesButton);

    databasePanel.add(Box.createHorizontalStrut(10));

    databasePanel.add(generateHeroesButton);

    databasePanel.add(Box.createHorizontalGlue());

    return databasePanel;
  }

  @Override
  public void addNotify() {
    super.addNotify();
    if (getRootPane() != null) {
      getRootPane().setDefaultButton(defaultButton);
    }
  }

}
