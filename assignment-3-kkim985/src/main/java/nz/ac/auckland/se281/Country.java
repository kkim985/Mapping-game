package nz.ac.auckland.se281;

/** This class represents a country with its continent and tax fee. */
public class Country {

  // initialise the variables
  private String country;
  private String continent;
  private String taxFee;

  // constructor which takes in the country, continent and tax fee
  public Country(String country, String continent, String taxFee) {
    this.country = country;
    this.continent = continent;
    this.taxFee = taxFee;
  }

  // getter methods for the country, continent and tax fee
  public String getCountry() {
    return country;
  }

  public String getContinent() {
    return continent;
  }

  public String getTaxFee() {
    return taxFee;
  }

  @Override
  public String toString() {
    return country;
  }
}
