import java.util.Scanner;

/**
 * Main file of Project.
 *
 * @author Shane Broxson
 * @file Main.java
 */
public class Main {

  /**
   * Main function of project.
   *
   * @param args Default
   */
  public static void main(String[] args) {

    // Input for word to check
    Scanner input = new Scanner(System.in);
    System.out.println("Type your word ");
    String inputWord = input.nextLine();
    // Create new instance of BagOfWords
    BagOfWordsAnalysis b = new BagOfWordsAnalysis();
    b.loadFile();
    b.compare(inputWord);
  }
}
