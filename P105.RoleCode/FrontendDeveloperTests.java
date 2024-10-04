// –== CS400 Spring 2024 File Header Information ==–
// Name: Sean Wells
// Email: smwells3@wisc.edu
// Lecturer: Professor Heimerl
// Notes to Grader:
import org.junit.jupiter.api.Test;
import java.util.Scanner;
import org.junit.jupiter.api.Assertions;

public class FrontendDeveloperTests {
  
  /**
   * Tests the scenario where a user only provides one input to the [G]et songs command. 
   * For now, the app sends the user back to the main menu.
   */
  @Test
  public void test1() {
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
  public void test2() {
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
  public void test3() {
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
  public void test4() {
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
  public void test5() {
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
  
}
