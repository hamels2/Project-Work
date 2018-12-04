package group2.identisky;

/**
 * Class containing methods used for determining where an object currently is in the user's view
 * And, most importantly, whether or not it is currently visible
 *
 * TO-DO
 * deal with radians vs. degrees
 * learn the nature of the input format
 * 
 */
public class ObjectLocation {

     // J200 date format is the number of days from 1200 UTC on Jan 1 2000 AD
     // It is used for astronomical calculations
    public static double calculateJ2000(int year, int dayOfYear, int hour, int minute){
        hour = hour + minute/60;
        dayOfYear = dayOfYear + hour/24;
        double j2000 = (year - 2000)*365.25 - 1.5 + dayOfYear;
        return j2000;
    }

    // returns local siderial time in degrees
    public static double calculateLST(int year, int dayOfYear, int hour, int minute, double longitude){
        double j2000 = calculateJ2000(year, dayOfYear, hour, minute);
        double lst = 100.46 + 0.985647*j2000 + longitude + 15*(hour+minute/60);
        lst = lst + (lst/(-1))*Math.floor(lst/360)*360; //add or subtract 360 if 0<n<360
        return lst;
    }

    //returns hour angle in radians
    public static double calculateHourAngle(int year, int dayOfYear, int hour, int minute, double longitude, double rightAscension){
        double lst = calculateLST(year, dayOfYear, hour, minute, longitude);
        double hourAngle = lst - rightAscension;
        hourAngle = lst + (lst/(-1))*Math.floor(lst/360)*360; //add or subtract 360 if 0<n<360
        hourAngle = Math.toRadians(hourAngle);
        return hourAngle;
    }

    public static double calculateAltitude(double declination, double latitude, double hourAngle){
        double altitude = Math.sin(declination)*Math.sin(latitude) + Math.cos(declination)*Math.cos(latitude)*Math.cos(hourAngle);
        altitude = Math.toDegrees(Math.asin(altitude));
        return altitude;
    }

    public static double calculateAzimuth(double declination, double latitude, double altitude, double hourAngle){
        double azimuth = (Math.sin(declination) - Math.sin(altitude)*Math.sin(latitude))/(Math.cos(altitude)*Math.cos(latitude));
        azimuth = Math.toDegrees(Math.acos(azimuth));
        if (Math.sin(hourAngle)>=0){
            azimuth = 360-azimuth;
        }
        return azimuth;
    }

    //is this object visible in the sky at this time and location?
    public static boolean isVisible(int year, int dayOfYear, int hour, int minute, double latitude, double longitude, double declination, double rightAscension){
        double hourAngle = calculateHourAngle(year, dayOfYear, hour, minute, longitude, rightAscension);
        double altitude = calculateAltitude(declination, latitude, hourAngle);
        double azimuth = calculateAzimuth(declination, latitude, altitude, hourAngle);

        //for testing
        System.out.println("altitude: "+altitude);
        System.out.println("azimuth: "+azimuth);

        if (altitude>0){
            return true;
        }
        else{
            return false;
        }
    }

    public static void main(String[] args){
        double alt = calculateAltitude(Math.toRadians(36.466667), Math.toRadians(52.5), Math.toRadians(54.38));
        System.out.println("altitude: "+alt);
        System.out.println("azimuth: "+calculateAzimuth(Math.toRadians(36.466667), Math.toRadians(52.5),Math.toRadians(alt), Math.toRadians(54.382617)));
    }

}
