package BE;

public class Countries {

    private int CountryId;
    private String CountryName;

    public Countries(int id, String name) {
        this.CountryId = id;
        this.CountryName = name;
    }

    public Countries(String name) {
        this.CountryName = name;
    }

    public int getCountryId() {
        return CountryId;
    }

    public void setCountryId(int countryId) {
        CountryId = countryId;
    }

    public String getCountryName() {
        return CountryName;
    }

    public void setCountryName(String countryName) {
        CountryName = countryName;
    }

    @Override
    public String toString() {
        return CountryName;
    }
}