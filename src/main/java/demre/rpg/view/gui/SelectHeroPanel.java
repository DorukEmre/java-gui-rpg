package demre.rpg.view.gui;

import java.awt.BorderLayout;
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
    JLabel selectHeroTitle = GUIUtils.createTitle(
        "Select Your Hero", 24);
    contentPanel.add(selectHeroTitle);

    contentPanel.add(Box.createVerticalStrut(20));

    JPanel heroesList = new JPanel();
    heroesList.setLayout(new BoxLayout(heroesList, BoxLayout.Y_AXIS));

    if (heroes == null || heroes.isEmpty()) {
      JLabel noHeroLabel = GUIUtils.createLabel(
          "No heroes available.", 16);
      heroesList.add(noHeroLabel);

    } else {
      // List heroes as buttons
      for (int i = 0; i < heroes.size(); i++) {
        Hero hero = heroes.get(i);

        // Create button (left)
        JButton heroButton = GUIUtils.createButton(i + 1 + ". ");
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
        JLabel heroStats = GUIUtils.createLabel(
            hero.getName() + " - " + hero.getHeroClass()
                + ". Level: " + hero.getLevel() + ", XP: " + hero.getExperience()
                + ", ATT: " + hero.getAttack() + ", DEF: " + hero.getDefense()
                + ", HP: " + hero.getHitPoints(),
            16);

        // Create horizontal panel for button + label
        JPanel heroLine = new JPanel();
        heroLine.setLayout(new BoxLayout(heroLine, BoxLayout.X_AXIS));
        heroLine.setAlignmentX(LEFT_ALIGNMENT);

        heroLine.add(Box.createHorizontalStrut(20));
        heroLine.add(heroButton);
        heroLine.add(Box.createHorizontalStrut(20));
        heroLine.add(heroStats);

        heroesList.add(heroLine);
      }
    }

    // Make the list scrollable
    JScrollPane scrollPane = new JScrollPane(heroesList);
    contentPanel.add(scrollPane);

    contentPanel.add(Box.createVerticalStrut(20));

    JLabel newHeroTitle = (heroes == null || heroes.isEmpty())
        ? (newHeroTitle = GUIUtils.createTitle(
            "Create a new hero", 24))
        : (newHeroTitle = GUIUtils.createTitle(
            "Or create a new hero", 24));

    contentPanel.add(newHeroTitle);

    contentPanel.add(Box.createVerticalStrut(20));

    JButton newHeroButton = GUIUtils.createButton("New Hero");
    newHeroButton.setAlignmentX(CENTER_ALIGNMENT);
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

    JButton deleteHeroesButton = GUIUtils.createButton("Delete All Heroes");
    deleteHeroesButton.addActionListener(e -> {
      try {
        controller.onDatabaseAction("deleteAll");
      } catch (Exception ex) {
        GUIView.windowDispose(deleteHeroesButton);
        Main.errorAndExit(ex, ex.getMessage());
      }
    });

    JButton generateHeroesButton = GUIUtils.createButton("Generate Heroes (lvl 1-10)");
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
