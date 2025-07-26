package demre.rpg.view.gui;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import demre.rpg.controller.GameController;
import demre.rpg.model.GameEngine;

public class MapViewPanel extends JPanel {
  // private final GameController controller;
  // private final GameEngine gameEngine;

  public MapViewPanel(GameController controller, GameEngine gameEngine) {
    // this.controller = controller;
    // this.gameEngine = gameEngine;
    GameEngine.Step step = gameEngine.getStep();

    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    JScrollPane scrollableMap = new ScrollMapPanel(controller, gameEngine);

    add(scrollableMap);
    add(Box.createVerticalStrut(10));

    JPanel instructionsPanel;
    if (step == GameEngine.Step.PLAYING
        || step == GameEngine.Step.INVALID_ACTION
        || step == GameEngine.Step.ENEMY_FIGHT_SUCCESS
        || step == GameEngine.Step.LEVEL_UP
        || step == GameEngine.Step.ENEMY_RUN_SUCCESS) {
      instructionsPanel = new MapControlPanel(controller, gameEngine);
    } else if (step == GameEngine.Step.ENEMY_ENCOUNTER
        || step == GameEngine.Step.ENEMY_INVALID_ACTION
        || step == GameEngine.Step.ENEMY_RUN_FAILURE) {
      instructionsPanel = new EnemyEncounterPanel(controller, gameEngine);
      // } else if (step == GameEngine.Step.ITEM_FOUND
      // || step == GameEngine.Step.ITEM_FOUND_AND_LEVEL_UP
      // || step == GameEngine.Step.ITEM_INVALID_ACTION) {
      // instructionsPanel = new ItemFoundPanel(controller, gameEngine);
    } else {
      instructionsPanel = new JPanel(); // Fallback empty panel
    }
    add(instructionsPanel);

    SwingUtilities.invokeLater(
        () -> ((ScrollMapPanel) scrollableMap).centerMapOnHero());

    // Add label with mapDrawPanel size, scrollPane size, and directionPanel size
    // JLabel sizeLabel = new JLabel();
    // sizeLabel.setFont(new Font("Serif", Font.PLAIN, 16));
    // sizeLabel.setAlignmentX(CENTER_ALIGNMENT);
    // add(sizeLabel);

    // // Helper to update the label
    // Runnable updateSizeLabel = () -> sizeLabel.setText(
    // "Map size: " + mapDrawPanel.getWidth() + "x" + mapDrawPanel.getHeight() +
    // ", ScrollPane size: " + scrollPane.getWidth() + "x" + scrollPane.getHeight()
    // +
    // ", DirectionPanel size: " + directionPanel.getWidth() + "x" +
    // directionPanel.getHeight());
    // // Add listeners to update the label on resize
    // scrollPane.addComponentListener(new java.awt.event.ComponentAdapter() {
    // public void componentResized(java.awt.event.ComponentEvent e) {
    // updateSizeLabel.run();
    // }
    // });
    // mapDrawPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
    // public void componentResized(java.awt.event.ComponentEvent e) {
    // updateSizeLabel.run();
    // }
    // });
    // directionPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
    // public void componentResized(java.awt.event.ComponentEvent e) {
    // updateSizeLabel.run();
    // }
    // });
    // this.addComponentListener(new java.awt.event.ComponentAdapter() {
    // public void componentResized(java.awt.event.ComponentEvent e) {
    // updateSizeLabel.run();
    // }
    // });

    // // Set initial text
    // updateSizeLabel.run();

  }

}
