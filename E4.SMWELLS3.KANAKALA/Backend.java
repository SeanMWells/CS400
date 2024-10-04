
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class Backend implements BackendInterface {

  private IterableRedBlackTree<SongInterface> tree;
  private boolean getRangeCalled;
  private List<SongInterface> getRangeSongs;

  public Backend(IterableRedBlackTree<SongInterface> tree) {
    this.tree = tree;
    this.getRangeCalled = false;
    getRangeSongs = new ArrayList<>();
  }
  /**
   * Loads data from the .csv file referenced by filename.
   *
   * @param filename is the name of the csv file to load data from
   * @throws IOException when there is trouble finding/reading file
   */
  @Override
  public void readData(String filename) throws IOException {
    File file = new File(filename);

    if (!file.exists() || !file.isFile()) {
      throw new IOException("Cannot find/read file.");
    }
    Scanner s = new Scanner(file, "utf-8");
    s.nextLine();

    // Loops through songs and inserts into the tree
    while (s.hasNext()) {
      Song song = new Song(s.nextLine());
      tree.insert(song);
    }

  }

  /**
   * Retrieves a list of song titles for songs that have a Danceability within the specified range
   * (sorted by Danceability in ascending order). If a minEnergy filter has been set using
   * filterEnergeticSongs(), then only songs with an energy rating greater than or equal to this
   * minEnergy should be included in the list that is returned by this method.
   *
   * Note that either this danceability range, or the resulting unfiltered list of songs should be
   * saved for later use by the other methods defined in this class.
   *
   * @param low  is the minimum Danceability of songs in the returned list
   * @param high
   * @return List of titles for all songs in specified range
   */
  @Override
  public List<String> getRange(int low, int high) {
    Iterator<SongInterface> iter = this.tree.iterator();
    List<String> songs = new ArrayList<>();

    // Iterates through tree and adds songs within range to return list
    while (iter.hasNext()) {
      SongInterface song = iter.next();
      if (song.getDanceability() <= high && song.getDanceability() >= low) {
        songs.add(song.getTitle());
        this.getRangeSongs.add(song);
      }
    }
    getRangeCalled = true;
    return songs;
  }

  /**
   * Filters the list of songs returned by future calls of getRange() and fiveFastest() to only
   * include energetic songs.  If getRange() was previously called, then this method will return a
   * list of song titles (sorted in ascending order by Danceability) that only includes songs on
   * with the specified minEnergy or higher.  If getRange() was not previously called, then this
   * method should return an empty list.
   *
   * Note that this minEnergy threshold should be saved for later use by the other methods defined
   * in this class.
   *
   * @param minEnergy is the minimum energy of songs that are returned
   * @return List of song titles, empty if getRange was not previously called
   */
  @Override
  public List<String> filterEnergeticSongs(int minEnergy) {
    List<String> energeticSongs = new ArrayList<>();
    List<SongInterface> updateGetRangeSongs = new ArrayList<>();

    if (getRangeCalled) {
      getRangeSongs.sort(Comparator.comparingInt(SongInterface::getDanceability));
      for (SongInterface song : getRangeSongs) {
        if (song.getEnergy() >= minEnergy) {
          energeticSongs.add(song.getTitle());
          updateGetRangeSongs.add(song);
        }
      }
      getRangeSongs = updateGetRangeSongs;
    }
    return energeticSongs;
  }

  /**
   * This method makes use of the attribute range specified by the most recent call to getRange().
   * If a minEnergy threshold has been set by filterEnergeticSongs() then that will also be utilized
   * by this method. Of those songs that match these criteria, the five fastest will be returned by
   * this method as a List of Strings in increasing order of danceability.  Each string contains the
   * speed in BPM followed by a colon, a space, and then the song's title. If fewer than five such
   * songs exist, return all of them.
   *
   * @return List of five fastest song titles and their respective speeds
   * @throws IllegalStateException when getRange() was not previously called.
   */
  @Override
  public List<String> fiveFastest() {
    if (!getRangeCalled) {
      throw new IllegalStateException("getRange not previously called");
    }
    int count = 0;
    List<SongInterface> temp = getRangeSongs;
    temp.sort(Comparator.comparingInt(SongInterface::getBPM).reversed());
    List<SongInterface> sortByDanceability = new ArrayList<>();
    List<String> fastest = new ArrayList<>();
    for (SongInterface song : getRangeSongs) {
      sortByDanceability.add(song);
      count++;
      if (count >= 5) {
        break;
      }
    }
    sortByDanceability.sort(Comparator.comparingInt(SongInterface::getDanceability));
    for (SongInterface song : sortByDanceability) {
      fastest.add(song.getBPM() + ": " + song.getTitle());
    }
    return fastest;
  }
}
