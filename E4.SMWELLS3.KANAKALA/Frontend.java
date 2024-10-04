// –== CS400 Spring 2024 File Header Information ==–
// Name: Sean Wells
// Email: smwells3@wisc.edu
// Lecturer: Professor Heimerl
// Notes to Grader:
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Frontend implements FrontendInterface {
  
  private String min = "min";
  private String max = "max";
  private String energy = "none";
  private BackendInterface back;
  private Scanner in;

  public Frontend(Scanner scan, BackendInterface backend) {
    in = scan;
    back = backend;
  }

  /**
     * Repeated gives the user an opportunity to issue new commands until
     * they select Q to quit.
     */ 
  @Override
  public void runCommandLoop() {
    displayMainMenu();
    if (in.hasNextLine()) {
      String s = in.nextLine().trim();
      if(s.equals("R")) { readFile(); runCommandLoop();} else {
      if(s.equals("G")) { getValues(); runCommandLoop();} else {
      if(s.equals("F")) { setFilter(); runCommandLoop();} else {
      if(s.equals("D")) { topFive(); runCommandLoop();} else {
      if(s.equals("Q")) { in.close(); } else {
        System.out.println("Please choose one of the provided options.");
        runCommandLoop();
    } } } } } }
  }

  /**
     * Displays the menu of command options to the user.
     */
  @Override
  public void displayMainMenu() {
    String menu = """
        
        ~~~ Command Menu ~~~
            [R]ead Data
            [G]et Songs by Danceability [min - max]
            [F]ilter New Songs (by Min Energy: none)
            [D]isplay Five Fastest
            [Q]uit
        Choose command:""";
    menu=menu.replace("min",min).replace("max",max).replace("none",energy);
    System.out.println(menu + " ");
  }

  /**
     * Provides text-based user interface and error handling for the 
     * [R]ead Data command.
     */
  @Override
  public void readFile() {
    System.out.println("Enter path to csv file to load: ");
    String file = in.nextLine();
    try {
      back.readData(file);
      System.out.println("Done reading file.");
    } catch (IOException e) {
      System.out.println("Please provide the path to a valid file.");
    }
  }

  /**
     * Provides text-based user interface and error handling for the 
     * [G]et Songs by Danceability command.
     */
  @Override
  public void getValues() {
    String oldMin = min;
    String oldMax = max;
    System.out.print("Enter range of values (MIN - MAX): ");
    String input = in.nextLine();
    String[] inputs = input.split("-");
    if (inputs.length != 2) {
      System.out.println("Invalid number of inputs. "
          + "Please provide two integers in the format MIN - MAX");
      return;
    }
    min = inputs[0].strip();
    max = inputs[1].strip();
    try {
      if (Integer.parseInt(max) < Integer.parseInt(min)) {
        System.out.println("Please enter a maximum value that is greater than the minimum.");
        return;
      }
      List<String> range = back.getRange(Integer.parseInt(min), Integer.parseInt(max));
      String output = String.valueOf(range.size()) + " songs found between " + min + 
          " - " + max + ":";
      for (String song : range) {
        output += "\n" + song;
      }
      System.out.println(output);
    } catch (NumberFormatException e) {
      System.out.println("Please input integers in the format MIN - MAX");
      min = oldMin;
      max = oldMax;
    }
    
  }

  /**
     * Provides text-based user interface and error handling for the 
     * [F]ilter Energetic Songs (by Min Energy) command.
     */
  @Override
  public void setFilter() {
    String oldEnergy = energy;
    System.out.print("Enter minimum energy: ");
    String input = in.nextLine();
    energy = input.strip();
    try {
      List<String> filter = back.filterEnergeticSongs(Integer.parseInt(energy));
      String output = String.valueOf(filter.size()) + " songs found between " + min + " - " + max
          + " with energy >= " + energy + ":";
      for (String song : filter) {
        output += "\n" + song;
      }
      System.out.println(output);
    } catch (NumberFormatException e) {
      energy = oldEnergy;
      System.out.println("Please input an integer for the energy");
      setFilter();
    }
  }

  /**
     * Provides text-based user interface and error handling for the 
     * [D]isplay Five Fastest command.
     */
  @Override
  public void topFive() {
    boolean error = false;
    String output = "Top five songs found between " + min + " - " + max + 
          " with energy >= " + energy + ":";
    try {
	List<String> five = back.fiveFastest();
	for (String song : five) {
        output += "\n" + song;
        }
        System.out.println(output);
    } catch (IllegalStateException e) {
    error = true;
    System.out.println("Please call [G] before this command.");
    }
  }
}
