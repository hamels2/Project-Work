package group2.identisky;

/**
 * Created by Home on 03-Apr-16.
 */
public class SkyObject {

    private String name;
    private double rightAscension;
    private double declination;
    private double azimuth;
    private double altitude;
    private String type;

    public SkyObject(String name, double rightAscension, double declination, String type) {
        this.name = name;
        this.rightAscension = rightAscension;
        this.declination = declination;
        this.altitude = Double.POSITIVE_INFINITY;
        this.azimuth = Double.POSITIVE_INFINITY;
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setRA(double RA) {
        this.rightAscension = RA;
    }

    public double getRA() {
        return this.rightAscension;
    }

    public void setDeclination(double declination) {
        this.declination = declination;
    }

    public double getDeclination() {
        return this.declination;
    }

    public void setAzimuth(double azimuth) {
        this.azimuth = azimuth;
    }

    public double getAzimuth() {
        return this.azimuth;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public double getAltitude() {
        return this.altitude;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
