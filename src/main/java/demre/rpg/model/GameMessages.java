package demre.rpg.model;

import java.util.HashMap;
import java.util.Map;

import demre.rpg.model.GameEngine.Step;

public class GameMessages {
  private static final Map<Step, String> messages = new HashMap<>();

  static {
    messages.put(
        Step.INVALID_HERO_SELECTION,
        "Invalid hero selection. Please try again.");

    messages.put(
        Step.INVALID_HERO_CREATION,
        "Invalid hero creation. Please try again.\nHero name must be 3-20 characters long and can only contain alphanumeric characters and spaces.");
  }

  public static String getMessage(Step step) {
    return messages.getOrDefault(step, "");
  }

}