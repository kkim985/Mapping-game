package nz.ac.auckland.se281;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/** This class is the main entry point. */
public class MapEngine {

  // initialise the static variables
  private static StringBuilder result;

  /**
   * this method is used to concatenate the list of strings to a single string.
   *
   * @param list the list of strings to be concatenated.
   * @return the concatenated string.
   */
  private static String stringOutput(List<String> list) {
    result = new StringBuilder();

    // iterate the list and concatenate the strings. If it is not the last element, add a comma and
    // a space.
    for (int i = 0; i < list.size(); i++) {
      if (i != list.size() - 1) {
        result.append(list.get(i));
        result.append(", ");
      } else {
        result.append(list.get(i));
      }
    }
    return result.toString();
  }

  // initialise the variables
  private Boolean countryExist;
  private Boolean countryValid;
  private Country country;
  private Country currentCountry;
  private Country node;
  private int totalTaxFee;
  private List<Country> shortestPathList;
  private List<Country> shortestPath;
  private List<Country> visited;
  private List<String> adjacencies;
  private List<String> countries;
  private List<String> shortestPathContinent;
  private List<String> shortestPathString;
  private Map<Country, Country> parentMap;
  private MapGraph<Country> adjCountryList = new MapGraph<>();
  private Queue<Country> queue;
  private Set<Country> countryList = new HashSet<>();
  private String[] adjacencyCountry;
  private String[] countryInfo;
  private String continentName;
  private String countryDestination;
  private String countryInput;
  private String countryName;
  private String countrySource;
  private String taxFee;

  public MapEngine() {
    loadMap();
  }

  /** invoked one time only when constracting the MapEngine class. */
  private void loadMap() {

    // read the files by using the method in Utils
    countries = Utils.readCountries();
    adjacencies = Utils.readAdjacencies();

    // iterate the countries
    for (String eachCountry : countries) {
      // split each country's information into 3 parts (country name, continent, taxfee)
      countryInfo = eachCountry.split(",");
      countryName = countryInfo[0];
      continentName = countryInfo[1];
      taxFee = countryInfo[2];
      // make a new Country instance for each country
      country = new Country(countryName, continentName, taxFee);
      // add the country to the HashSet
      countryList.add(country);
    }

    // iterate the adjacencies and add the country as a node to the graph
    for (Country eachCountryInList : countryList) {
      adjCountryList.addCountryNode(eachCountryInList);
    }

    // iterate the adjacencies and add the edges to the graph
    for (String adjCountry : adjacencies) {
      adjacencyCountry = adjCountry.split(",");
      for (int i = 1; i < adjacencyCountry.length; i++) {
        adjCountryList.addEdge(
            convertStringToCountry(adjacencyCountry[0]),
            convertStringToCountry(adjacencyCountry[i]));
      }
    }
  }

  /**
   * this method is used to convert the string to the Country object.
   *
   * @param country the country name in string format to be converted to the Country object.
   * @return the country name in Country object format.
   */
  public Country convertStringToCountry(String country) {

    // iterate the country list and return the country object if the country name is found.
    for (Country eachCountry : countryList) {
      if (country.equals(eachCountry.getCountry())) {
        return eachCountry;
      }
    }
    return null;
  }

  /** this method is invoked when the user run the command info-country. */
  public void showInfoCountry() {
    MessageCli.INSERT_COUNTRY.printMessage();

    // get the country name from the user input
    countryInput = Utils.scanner.nextLine();
    countryInput = Utils.capitalizeFirstLetterOfEachWord(countryInput);

    // check if the country is valid
    while (!isCountryValid(countryInput)) {
      try {
        countryFound(countryInput);
      } catch (CountryException e) {
        MessageCli.INVALID_COUNTRY.printMessage(countryInput);
        countryInput = Utils.capitalizeFirstLetterOfEachWord(Utils.scanner.nextLine());
      }
    }

    // print the country information
    for (Country country : countryList) {
      if (countryInput.equals(country.getCountry())) {
        MessageCli.COUNTRY_INFO.printMessage(
            country.getCountry(), country.getContinent(), country.getTaxFee());
      }
    }
  }

  /**
   * this method is used to check if the country is valid or not.
   *
   * @param countryInput the country name in string format to be checked.
   * @throws CountryException if the country is not found.
   */
  public void countryFound(String countryInput) throws CountryException {
    countryExist = false;

    // iterate the country list and check if the country is found.
    for (Country country : countryList) {
      if (countryInput.equals(country.getCountry())) {
        countryExist = true;
        return;
      }
    }

    // throw an exception if the country is not found.
    if (countryExist == false) {
      throw new CountryException();
    }
  }

  /**
   * this method is used to check if the country is valid or not which returns a boolean value.
   *
   * @param countryInput the country name in string format to be checked.
   * @return true if the country is valid, otherwise false.
   */
  public boolean isCountryValid(String countryInput) {
    countryValid = false;

    // iterate the country list and check if the country is found.
    for (Country country : countryList) {
      if (countryInput.equals(country.getCountry())) {
        countryValid = true;
      }
    }

    return countryValid;
  }

  /** this method is invoked when the user run the command route. */
  public void showRoute() {

    // initialise the variables
    shortestPathList = new ArrayList<>();
    shortestPathString = new ArrayList<>();
    shortestPathContinent = new ArrayList<>();
    totalTaxFee = 0;

    MessageCli.INSERT_SOURCE.printMessage();

    // get the source country from the user input and capitalise the first letter of each word
    countrySource = Utils.scanner.nextLine();
    countrySource = Utils.capitalizeFirstLetterOfEachWord(countrySource);

    // check if the country is valid. If not, throw an exception and keep asking the user to type
    // the country name.
    while (!isCountryValid(countrySource)) {
      try {
        countryFound(countrySource);
      } catch (CountryException e) {
        MessageCli.INVALID_COUNTRY.printMessage(countrySource);
        countrySource = Utils.capitalizeFirstLetterOfEachWord(Utils.scanner.nextLine());
      }
    }

    MessageCli.INSERT_DESTINATION.printMessage();

    // get the destination country from the user input and capitalise the first letter of each word
    countryDestination = Utils.scanner.nextLine();
    countryDestination = Utils.capitalizeFirstLetterOfEachWord(countryDestination);

    // check if the country is valid. If not, throw an exception and keep asking the user to type
    // the country name.
    while (!isCountryValid(countryDestination)) {
      try {
        countryFound(countryDestination);
      } catch (CountryException e) {
        MessageCli.INVALID_COUNTRY.printMessage(countryDestination);
        countryDestination = Utils.capitalizeFirstLetterOfEachWord(Utils.scanner.nextLine());
      }
    }

    // check if the source country is the same as the destination country. If so, print a message
    // and return.
    if (countrySource.equals(countryDestination)) {
      MessageCli.NO_CROSSBORDER_TRAVEL.printMessage();
      return;
    }

    // find the shortest path using the breadth-first search algorithm
    shortestPathList =
        findShortestPath(
            convertStringToCountry(countrySource), convertStringToCountry(countryDestination));

    // iterate through the shortestPathList. Add the country name to the shortestPathString list and
    // the continent name to the shortestPathContinent list.
    for (Country path : shortestPathList) {
      shortestPathString.add(path.getCountry());
      if (!shortestPathContinent.contains(path.getContinent())) {
        shortestPathContinent.add(path.getContinent());
      }
    }

    // calculate the total tax fee
    for (int i = 1; i < shortestPathList.size(); i++) {
      totalTaxFee += Integer.parseInt(shortestPathList.get(i).getTaxFee());
    }

    // print the output
    MessageCli.ROUTE_INFO.printMessage("[" + stringOutput(shortestPathString) + "]");
    MessageCli.CONTINENT_INFO.printMessage("[" + stringOutput(shortestPathContinent) + "]");
    MessageCli.TAX_INFO.printMessage(Integer.toString(totalTaxFee));
  }

  /**
   * this method is uses the breadth-first search algorithm to find the shortest path between two
   * countries.
   *
   * @param root the source country.
   * @param destination the destination country.
   * @return the shortest path between the source and destination countries.
   */
  public List<Country> findShortestPath(Country root, Country destination) {

    // initialise the variables
    visited = new LinkedList<>();
    queue = new LinkedList<>();
    parentMap = new HashMap<>();

    // add the source country to the visited list, queue, and parentMap
    visited.add(root);
    queue.add(root);
    parentMap.put(root, null);

    // iterate the queue until it is empty
    while (!queue.isEmpty()) {
      // get the current country from the queue
      currentCountry = queue.poll();

      // iterate the adjacent countries of the current country. If the adjacent country is not
      // visited, add it to the queue, visited list, and parentMap.
      for (Country adjacency : adjCountryList.get(currentCountry)) {
        if (!visited.contains(adjacency)) {
          queue.add(adjacency);
          visited.add(adjacency);
          parentMap.put(adjacency, currentCountry);
        }
      }

      // check if the current country is the destination country. If so, add the countries to the
      // shortestPath list and return it.
      if (currentCountry.equals(destination)) {
        shortestPath = new ArrayList<>();
        node = currentCountry;
        while (node != null) {
          shortestPath.add(node);
          node = parentMap.get(node);
        }
        Collections.reverse(shortestPath);
      }
    }

    return shortestPath;
  }
}
