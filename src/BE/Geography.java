package BE;

public class Geography {


    private int geographyId;
    private String geography;

    public Geography(int geographyId, String geography) {
        this.geographyId = geographyId;
        this.geography = geography;
    }



    private int getGeographyId() {
        return geographyId;
    }

    private void setGeographyId(int geographyId) {
        this.geographyId = geographyId;
    }

    private String getGeography() {
        return geography;
    }

    private void setGeography(String geography) {
        this.geography = geography;
    }


}
