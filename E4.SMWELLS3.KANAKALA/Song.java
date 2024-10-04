public class Song implements SongInterface{
  private String title;
  private String artist;
  private String genre;
  private int year;
  private int bpm;
  private int energy;
  private int danceability;
  private int loudness;
  private int liveness;

  public Song (String row) {

    // Splits the row from the scanner by commas
    String[] features1 = row.split(",");

    // If the resulting array length is longer than expected, this means that there must be a comma
    // in the title
    if (features1.length > 14) {

      // Removes the quotes around the title, to a max of 5 double quotes
      String[] temp = row.split("\"", 5);

      // Then splits the remaining info
      String[] features = temp[2].split(",");

      // Updates all the fields
      this.title = temp[1].trim();
      this.artist = features[1].trim();
      this.genre = features[2].trim();
      this.year = Integer.parseInt(features[3].trim());
      this.bpm = Integer.parseInt(features[4].trim());
      this.energy = Integer.parseInt(features[5].trim());
      this.danceability = Integer.parseInt(features[6].trim());
      this.loudness = Integer.parseInt(features[7].trim());
      this.liveness = Integer.parseInt(features[8].trim());
    } else {

      // Since we now know there are no commas in the title, we can proceed as normal
      // and regularly split the string by comma
      this.title = features1[0].trim();
      this.artist = features1[1].trim();
      this.genre = features1[2].trim();
      this.year = Integer.parseInt(features1[3].trim());
      this.bpm = Integer.parseInt(features1[4].trim());
      this.energy = Integer.parseInt(features1[5].trim());
      this.danceability = Integer.parseInt(features1[6].trim());
      this.loudness = Integer.parseInt(features1[7].trim());
      this.liveness = Integer.parseInt(features1[8].trim());
    }
  }
  @Override
  public String getTitle() {

    return this.title;
  }

  @Override
  public String getArtist() {
    return this.artist;
  }

  @Override
  public String getGenres() {
    return this.genre;
  }

  @Override
  public int getYear() {
    return this.year;
  }

  @Override
  public int getBPM() {
    return this.bpm;
  }

  @Override
  public int getEnergy() {
    return this.energy;
  }

  @Override
  public int getDanceability() {
    return this.danceability;
  }

  @Override
  public int getLoudness() {
    return this.loudness;
  }

  @Override
  public int getLiveness() {
    return this.liveness;
  }

  /**
   * Compares this object with the specified object for order.  Returns a negative integer, zero, or
   * a positive integer as this object is less than, equal to, or greater than the specified
   * object.
   *
   * <p>The implementor must ensure {@link Integer#signum
   * signum}{@code (x.compareTo(y)) == -signum(y.compareTo(x))} for all {@code x} and {@code y}.
   * (This implies that {@code x.compareTo(y)} must throw an exception if and only if
   * {@code y.compareTo(x)} throws an exception.)
   *
   * <p>The implementor must also ensure that the relation is transitive:
   * {@code (x.compareTo(y) > 0 && y.compareTo(z) > 0)} implies {@code x.compareTo(z) > 0}.
   *
   * <p>Finally, the implementor must ensure that {@code
   * x.compareTo(y)==0} implies that {@code signum(x.compareTo(z)) == signum(y.compareTo(z))}, for
   * all {@code z}.
   *
   * @param o the object to be compared.
   * @return a negative integer, zero, or a positive integer as this object is less than, equal to,
   * or greater than the specified object.
   * @throws NullPointerException if the specified object is null
   * @throws ClassCastException   if the specified object's type prevents it from being compared to
   *                              this object.
   * @apiNote It is strongly recommended, but <i>not</i> strictly required that
   * {@code (x.compareTo(y)==0) == (x.equals(y))}.  Generally speaking, any class that implements
   * the {@code Comparable} interface and violates this condition should clearly indicate this fact.
   *  The recommended language is "Note: this class has a natural ordering that is inconsistent with
   * equals."
   */
  @Override
  public int compareTo(SongInterface o) {
    if (o == null) {
      throw new NullPointerException("Object is null");
    }
    if (!(o instanceof Song)) {
      throw new ClassCastException("Type prevents comparison");
    }
    if (o.getDanceability() > this.getDanceability()) {
      return -1;
    } else if (o.getDanceability() < this.getDanceability()) {
      return 1;
    }
    return 0;
  }
}
