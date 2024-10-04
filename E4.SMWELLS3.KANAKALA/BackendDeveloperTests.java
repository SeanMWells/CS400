import static org.junit.Assert.*;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class BackendDeveloperTests{

  /**
   * This method testReadData tests that readData is correctly able to read the file without
   * producing an exception when it shouldn't and produces an exception when it should
   */
  @Test
  public void testReadData() {
    boolean testCase1;
    ISCPlaceholder<SongInterface> tree = new ISCPlaceholder<>();
    Backend backend = new Backend(tree);
    String file = "./songs.csv";
    try {
      backend.readData(file);
      testCase1 = true;
    } catch (IOException e) {
      testCase1 = false;
    }
    assertTrue(testCase1);
    file = "";
    try {
      backend.readData(file);
      testCase1 = true;
    } catch (IOException e) {
      testCase1 = false;
    }
    assertFalse(testCase1);
  }

  /**
   * This method verifies that the get range is correctly able to parse through and return
   * an expected number of song titles pertaining to the requirement
   */
  @Test
  public void testGetRange() throws IOException {
    ISCPlaceholder<SongInterface> tree = new ISCPlaceholder<>();

    Backend backend = new Backend(tree);
    backend.readData("./songs.csv");
    // Test case 1: Valid range
    List<String> songTitles = backend.getRange(40, 45);
    assertNotNull(songTitles);
    assertEquals(20, songTitles.size());
  }

  /**
   * This method verifies that the filter energetic songs method is correctly able to parse through and
   * return an expected number of song titles pertaining to the requirement
   */
  @Test
  public void testFilterEnergeticSongs() throws IOException {
    ISCPlaceholder<SongInterface> tree = new ISCPlaceholder<>();
    Backend backend = new Backend(tree);
    backend.readData("./songs.csv");
    backend.getRange(40,45);
    List<String> energeticSongs = backend.filterEnergeticSongs(75);
    assertNotNull(energeticSongs);
    assertEquals(10, energeticSongs.size());
  }

  /**
   * This method verifies that the five fastest method is correctly able to parse through and
   * return an expected number of song titles pertaining to the requirement
   */
  @Test
  public void testFiveFastest() throws IOException{
    ISCPlaceholder<SongInterface> tree = new ISCPlaceholder<>();
    Backend backend = new Backend(tree);
    backend.readData("./songs.csv");
    backend.getRange(40,45);
    List<String> fiveFast = backend.fiveFastest();
    assertNotNull(fiveFast);
    assertEquals(5, fiveFast.size());
  }
  /**
   * This method verifies that the five fastest method is correctly throwing an exception
   * and the filter energetic songs method returns an empty list without a get range call
   */
  @Test
  public void testWithNoGetRangeCall() {
    ISCPlaceholder<SongInterface> tree = new ISCPlaceholder<>();
    Backend backend = new Backend(tree);
    boolean testCase1 = false;
    boolean testCase2 = false;
    try {
      List<String> fiveFast = backend.fiveFastest();
    } catch (IllegalStateException e) {
      testCase1 = true;
    }
    List<String> energeticSongs = backend.filterEnergeticSongs(40);
    if (energeticSongs.isEmpty()) {
      testCase2 = true;
    }
    assertTrue(testCase1 && testCase2);
  }


}
