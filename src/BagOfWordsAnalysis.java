import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Creates BOW and applies analysis for suggested words.
 *
 * @author Shane Broxson
 * @file BagOfWordsAnalysis.java
 */
class BagOfWordsAnalysis {

  private File dataFile = new File("src/messages.txt");

  private NavigableMap<String, Integer> allWordByKey = new TreeMap<>();

  void loadFile() {
    String line = null;

    try {
      BufferedReader reader = new BufferedReader(new FileReader(dataFile));
      while ((line = reader.readLine()) != null) {
        createTreeMap(line);
      }
      reader.close();
    } catch (Exception io) {
      io.printStackTrace();
    }
    buildBag();
  }

  /**
   * Build allWordsByKey Map based on line input from file.
   *
   * @param curLine Current line from file being read
   */
  private void createTreeMap(String curLine) {

    String[] alltokens = curLine.trim().split("\\s+");
    List<String> pairs = new ArrayList<String>();
    for (int i = 0; i < alltokens.length - 1; ++i) {
      pairs.add(alltokens[i] + ", " + alltokens[i + 1]);
    }
    for (String token : pairs) {
      allWordByKey.merge(token, 1, Integer::sum);
    }
  }

  // Builds BagOfWords
  private void buildBag() {
    // Sort from highest to lowest value using stream
    Map<String, Integer> bag =
        allWordByKey.entrySet().stream()
            .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
            .collect(
                Collectors.toMap(
                    Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
  }

  /**
   * Compares to all words and finds supportValue.
   *
   * @param input Word inputted by User
   */
  void compare(String input) {
    double totalOccourance = 0;
    boolean found = false;

    // Compares inputted word (input) to words on txt file
    for (Map.Entry<String, Integer> entry : allWordByKey.entrySet()) {
      String pairsOfWords = entry.getKey();

      int i = input.indexOf(' ');
      // Counting Occurances
      try {

        String initial = pairsOfWords.substring(0, input.length());

        if (initial.equals(input)) {

          int valueOfOccours = entry.getValue();
          totalOccourance += valueOfOccours;
        }

      } catch (StringIndexOutOfBoundsException e) {
      }
    }

    for (Map.Entry<String, Integer> entry : allWordByKey.entrySet()) {
      String pairsOfWords = entry.getKey();
      // Checking support values
      try {

        String initial = pairsOfWords.substring(0, input.length());

        if (initial.equals(input)) {
          double supportValue = entry.getValue() / totalOccourance;

          if (supportValue > .65) {
            System.out.println(
                "Your next word might be " + entry.getKey().substring(input.length() + 1));
            found = true;
          }
        }

      } catch (StringIndexOutOfBoundsException e) {
      }
    }

    // If confidence > %65
    if (found) {
      System.out.println("Your next word might be 'the' ");
      System.out.println("Your next word might be 'this' ");
    }
    // If confidence !> %65
    else {
      System.out.println("Your next word might be 'the'.");
      System.out.println("Your next word might be 'this' .");
      System.out.println("Your next word might be 'of'.");
    }
  }
}
