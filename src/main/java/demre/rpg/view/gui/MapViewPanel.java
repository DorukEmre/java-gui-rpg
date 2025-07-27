package demre.rpg.view.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import demre.rpg.controller.GameController;
import demre.rpg.model.GameEngine;

public class MapViewPanel extends JPanel {

  public MapViewPanel(GameController controller, GameEngine gameEngine) {

    GameEngine.Step step = gameEngine.getStep();
    System.out.println("MapViewPanel > Game step: " + step);

    setLayout(new BorderLayout(0, 20));

    JScrollPane scrollableMap = new ScrollMapPanel(controller, gameEngine);

    JPanel instructionsPanel;
    if (step == GameEngine.Step.PLAYING
        || step == GameEngine.Step.INVALID_ACTION
        || step == GameEngine.Step.ENEMY_FIGHT_SUCCESS
        || step == GameEngine.Step.LEVEL_UP
        || step == GameEngine.Step.ENEMY_RUN_SUCCESS) {
      instructionsPanel = new PlayerControlPanel(controller, gameEngine);
    } else if (step == GameEngine.Step.ENEMY_ENCOUNTER
        || step == GameEngine.Step.ENEMY_INVALID_ACTION
        || step == GameEngine.Step.ENEMY_RUN_FAILURE) {
      instructionsPanel = new EnemyEncounterPanel(controller, gameEngine);
    } else if (step == GameEngine.Step.ITEM_FOUND
        || step == GameEngine.Step.ITEM_FOUND_AND_LEVEL_UP
        || step == GameEngine.Step.ITEM_INVALID_ACTION) {
      instructionsPanel = new ItemFoundPanel(controller, gameEngine);
    } else {
      instructionsPanel = new JPanel(); // Fallback empty panel
    }

    // add(scrollableMap);
    // add(Box.createVerticalStrut(10));
    // add(instructionsPanel);

    // Set fixed height for instructionsPanel
    instructionsPanel.setPreferredSize(new Dimension(0, 150));

    add(scrollableMap, BorderLayout.CENTER);
    add(instructionsPanel, BorderLayout.SOUTH);

    // Center the map on the hero
    SwingUtilities.invokeLater(
        () -> ((ScrollMapPanel) scrollableMap).centerMapOnHero());

    // Add label with mapDrawPanel size, scrollPane size, and directionPanel size
    // JLabel sizeLabel = new JLabel();
    // sizeLabel.setFont(new Font("Serif", Font.PLAIN, 16));
    // sizeLabel.setAlignmentX(CENTER_ALIGNMENT);
    // // add(sizeLabel);
    // add(sizeLabel, java.awt.BorderLayout.NORTH);

    // // Helper to update the label
    // Runnable updateSizeLabel = () -> sizeLabel.setText(
    // "ScrollPane size: " + scrollableMap.getWidth()
    // + "x" + scrollableMap.getHeight()
    // + ", Instructions size: " + instructionsPanel.getWidth()
    // + "x" + instructionsPanel.getHeight());

    // // Add listeners to update the label on resize
    // scrollableMap.addComponentListener(new java.awt.event.ComponentAdapter() {
    // public void componentResized(java.awt.event.ComponentEvent e) {
    // updateSizeLabel.run();
    // }
    // });
    // instructionsPanel.addComponentListener(new java.awt.event.ComponentAdapter()
    // {
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
