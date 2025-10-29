package demre.rpg.view.console;

import java.util.NoSuchElementException;
import java.util.Scanner;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

public final class ConsoleHelper {

  // private static final Logger logger =
  // LoggerFactory.getLogger(ConsoleHelper.class);

  private ConsoleHelper() {
  }

  public static void println() {
    System.out.println();
    // logger.info("");
  }

  public static void println(String msg) {
    System.out.println(msg);
    // logger.info(msg);
  }

  public static void print(String msg) {
    System.out.print(msg);
    // logger.info(msg);
  }

  public static void flush() {
    System.out.flush();
    // logger.info(msg);
  }

  // Returns null on EOF (ctrl-d)
  public static String readLine(Scanner scanner) {
    try {
      return scanner.nextLine();
      // logger.info("INPUT: " + line);

    } catch (NoSuchElementException | IllegalStateException eof) {
      return null;
    }
  }
}