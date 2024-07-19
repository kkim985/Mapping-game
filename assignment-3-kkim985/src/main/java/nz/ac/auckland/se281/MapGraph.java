package nz.ac.auckland.se281;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** this class represents a map graph with countries as nodes and edges between them. */
public class MapGraph<T> {

  // initialise the variables
  private Map<T, List<T>> adjCountries;

  // constructor which initialises the map
  public MapGraph() {
    this.adjCountries = new HashMap<>();
  }

  /**
   * method to add a country node to the graph.
   *
   * @param country the country to add to the graph
   */
  public void addCountryNode(T country) {
    adjCountries.putIfAbsent(country, new ArrayList<>());
  }

  /**
   * method to add an edge between two countries.
   *
   * @param country1 the country to add the edge from
   * @param country2 the country to add the edge to
   */
  public void addEdge(T country1, T country2) {
    addCountryNode(country1);
    addCountryNode(country2);
    adjCountries.get(country1).add(country2);
  }

  /**
   * method to get the adjacent countries of a given country.
   *
   * @param country the country to get the adjacent countries of
   * @return the list of adjacent countries
   */
  public List<T> get(T country) {
    return adjCountries.get(country);
  }
}
