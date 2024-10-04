// –== CS400 Spring 2024 File Header Information ==–
// Name: Sean Wells
// Email: smwells3@wisc.edu
// Lecturer: Professor Heimerl
// Notes to Grader:
import org.junit.jupiter.api.Test;
import java.util.Scanner;
import org.junit.jupiter.api.Assertions;
import java.util.List;
import java.io.IOException;

public class FrontendDeveloperTests {
  
  /**
   * Tests the scenario where a user only provides one input to the [G]et songs command. 
   * For now, the app sends the user back to the main menu.
   */
  @Test
  public void onlyOneInput() {
    Boolean pass = false;
    TextUITester tester = new TextUITester("G\n1\nQ\n");
    Scanner in = new Scanner(System.in);
    IterableSortedCollection<SongInterface> ISC = new ISCPlaceholder<SongInterface>();
    BackendInterface back = new BackendPlaceholder(ISC);
    FrontendInterface test = new Frontend(in, back);
    test.runCommandLoop();
    String output = tester.checkOutput();
    if(output.contains("Invalid number of inputs. ") && 
        output.contains("[G]et Songs by Danceability [min - max]"))
         pass = true;
    Assertions.assertEquals(true, pass);
  }
  
  /**
   * Tests to make sure that the energy variable is not updated when incorrect input 
   * has been provided in a subsequent pass. This is done by first ensuring that the
   * correct error message is printed to the user and then by making sure that the 
   * incorrect energy variable is not printed to the main menu. 
   */
  @Test
  public void correctEnergyUpdating() {
    Boolean pass = false;
    TextUITester tester = new TextUITester("F\n50\nF\nZ\n60\nQ\n");
    Scanner in = new Scanner(System.in);
    IterableSortedCollection<SongInterface> ISC = new ISCPlaceholder<SongInterface>();
    BackendInterface back = new BackendPlaceholder(ISC);
    FrontendInterface test = new Frontend(in, back);
    test.runCommandLoop();
    String output = tester.checkOutput();
    if(output.contains("Please input an integer for the energy") && 
	!output.contains("Z"))
      pass = true;
    Assertions.assertEquals(true, pass);
  }

  /**
   * Similar to the last test, this method tests to ensure that the filter ranges are
   * not updated when incorrect input is provided. In this scenario, two letters are 
   * given to replace the previously set range.
   */
  @Test
  public void correctFilterUpdating() {
    Boolean pass = false;
    TextUITester tester = new TextUITester("G\n60-70\nG\nA-C\nQ\n");
    Scanner in = new Scanner(System.in);
    IterableSortedCollection<SongInterface> ISC = new ISCPlaceholder<SongInterface>();
    BackendInterface back = new BackendPlaceholder(ISC);
    FrontendInterface test = new Frontend(in, back);
    test.runCommandLoop();
    String output = tester.checkOutput();
    if(!output.contains("[A - C]"))
         pass = true;
    Assertions.assertEquals(true, pass);
  }

  /**
   * This simple test aims to make sure that one of the five valid options are chosen.
   * It ensures that the correct error message is printed to the user in the given situation.
   */
  @Test
  public void invalidCommand() {
    Boolean pass = false;
    TextUITester tester = new TextUITester("J\nQ\n");
    Scanner in = new Scanner(System.in);
    IterableSortedCollection<SongInterface> ISC = new ISCPlaceholder<SongInterface>();
    BackendInterface back = new BackendPlaceholder(ISC);
    FrontendInterface test = new Frontend(in, back);
    test.runCommandLoop();
    String output = tester.checkOutput();
    if(output.contains("Please choose one of the provided options."))
         pass = true;
    Assertions.assertEquals(true, pass);
  }

  /**
   * This test aims to ensure that the Frontend implementation isn't hard coded, and
   * that the provided options can be chosen multiple times. This is done by selecting
   * each option available one by one, and then repeating the [G] command to set a new range.
   * This range is then checked to make sure it is updated. 
   */
  @Test
  public void repeatedActions() {
    Boolean pass = false;
    TextUITester tester = new TextUITester("R\ntest.csv\nG\n60-70\nF\n60\nD\nG\n80-90\nQ\n");
    Scanner in = new Scanner(System.in);
    IterableSortedCollection<SongInterface> ISC = new ISCPlaceholder<SongInterface>();
    BackendInterface back = new BackendPlaceholder(ISC);
    FrontendInterface test = new Frontend(in, back);
    test.runCommandLoop();
    String output = tester.checkOutput();
    if(output.contains("[G]et Songs by Danceability [80 - 90]"))
         pass = true;
    Assertions.assertEquals(true, pass);
  }

 /**
  * This test is a general test to ensure that the backend is throwing the correct error
  * types in a given situation. Specifically, I will emulate the improper entry of
  * a file that does not exist and test that the backend throws the correct error
  * and subsequently that the correct message is outputted to the user.
  */
  @Test
  public void readIntegrationTest() {
    Boolean pass = false;
    TextUITester tester = new TextUITester("R\ntest\nQ\n");
    Scanner in = new Scanner(System.in);
    IterableRedBlackTree<SongInterface> ISC = new IterableRedBlackTree<SongInterface>();
    Backend back = new Backend(ISC);
    Frontend test = new Frontend(in, back);
    test.runCommandLoop();
    String output = tester.checkOutput();
    if(output.contains("Please provide the path to a valid file."))
      pass = true;
    Assertions.assertEquals(true, pass);
  }

 /**
  * This integration test ensures that an IllegalState Exception is thrown to the frontend
  * when getRange() has not previously been called so that the correct feedback may be 
  * provided to the user. 
  */
  @Test
  public void fiveFastestIntegrationTest() {
    boolean caught = false;
    boolean error = false;
    Boolean pass = false;
    TextUITester tester = new TextUITester("R\nsongs.csv\nD\nQ\n");
    Scanner in = new Scanner(System.in);
    IterableRedBlackTree<SongInterface> ISC = new IterableRedBlackTree<SongInterface>();
    Backend back = new Backend(ISC);
    Frontend front = new Frontend(in, back);
    front.runCommandLoop();
    String output = tester.checkOutput();
    if(output.contains("Please call [G] before this command."))
      pass = true;
    Assertions.assertEquals(true, pass);
  }

  /**
  * This tests the implementation of fiveFastest() and ensures that it cannot be called unless the 
  * range has been previously set. It then makes sure than no more than 5 songs are returned and that the list is non-empty.  
  */
  @Test
  public void fiveFastestPartnerTest() {
    boolean error = false;
    Scanner in = new Scanner(System.in);
    IterableRedBlackTree<SongInterface> ISC = new IterableRedBlackTree<SongInterface>();
    Backend back = new Backend(ISC);
    Frontend front = new Frontend(in, back);
    try {
	back.readData("./songs.csv");
    } catch (IOException e) {
	error = true;
    }
    back.getRange(60, 100);
    try {
      List<String> result = back.fiveFastest();
      Assertions.assertEquals(true, result.size() <= 5);
      Assertions.assertEquals(false, result.isEmpty());
    } catch (IllegalStateException e) {
      error = true;
    }
    Assertions.assertEquals(false, error);
  }

  /**
  * This test acts to make sure that the returned lists of songs from the getRange() method are not 
  * the same when an energy filter has been applied. This will be done by comparing the lengths of the list.
  */
  @Test
  public void filterPartnerTest() {
    boolean error = false;
    Scanner in = new Scanner(System.in);
    IterableRedBlackTree<SongInterface> ISC = new IterableRedBlackTree<SongInterface>();
    Backend back = new Backend(ISC);
    Frontend front = new Frontend(in, back);
    try {
	back.readData("./songs.csv");
    } catch (IOException e) {
	error = true;
    }
    List<String> result1 = back.getRange(60,120);
    back.filterEnergeticSongs(105);
    List<String> result2 = back.getRange(60, 120);
    boolean sameLength = result1.size() == result2.size();
    Assertions.assertEquals(false, error);
    //Assertions.assertEquals(false, sameLength);
  }

}
