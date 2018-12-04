package group2.identisky;

import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * KEEP IN MIND
 * time for calcJulianDay must be in UTC (which is +5 hours from toronto)
 * westward longitude is POSITIVE+, eastward is NEGATIVE-
 * alt and az calculations require RADIAN input, but output DEGREES
 * everything else takes in DEGREES
 * ugh it works tho!!!!1
 */
public class AstroCalc {

    //testing
    public static void main(String[] args){
        GregorianCalendar now = new GregorianCalendar();
        now.set(GregorianCalendar.MONTH, 4);
        now.set(GregorianCalendar.YEAR, 2016);
        now.set(GregorianCalendar.DAY_OF_MONTH,5);
        now.set(GregorianCalendar.HOUR_OF_DAY, 19);
        now.set(GregorianCalendar.MINUTE, 44);
        SkyObject obj = new SkyObject("obj", 79.17,46.0,"other");
        setCoords(obj, now, 43.67, 79.4);
    }

    /* calculates azimuth and altitude of object as seen by observer at the given UTC time and location
    uses the objects right ascension and declination for calculations, given in decimal degrees
    TO-DO: update given object's alt and az accordingly
    for now, just prints alt and az */
    public static void setCoords(SkyObject obj, GregorianCalendar cal,  double lat, double lon){
        lon = -lon; //astronomical conventions are weird
        double RA = obj.getRA();
        double dec = Math.toRadians(obj.getDeclination());
        double yr = cal.get(GregorianCalendar.YEAR);
        double mo = cal.get(GregorianCalendar.MONTH);
        double day = cal.get(GregorianCalendar.DAY_OF_MONTH);
        double hr = cal.get(GregorianCalendar.HOUR_OF_DAY);
        double min = cal.get(GregorianCalendar.MINUTE);
        hr += 4; //conversion to UTC from EST;
        //check if converting time also changes day/month/year...
        if (hr>24){
            hr = hr%24;
            day++;
            if (day > cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH)){
                mo++;
                if (mo > 12){
                    yr++;
                }
            }
        }
        double jDay = calcJulianDay(yr, mo, day, hr, min);
        System.out.println("jDay: "+jDay);
        double gst = calcGST(jDay);
        System.out.println("GST: "+gst);
        double HA = calcHourAngle(gst, lon, RA);
        System.out.println("HA: "+HA);
        double altitude = calcAltitude(dec, Math.toRadians(lat), Math.toRadians(HA));
        double azimuth = calcAzimuth(dec, Math.toRadians(lat), Math.toRadians(HA));
        obj.setAltitude(altitude);
        obj.setAzimuth(azimuth);
        System.out.println("altitude: "+obj.getAltitude()+", azimuth: "+obj.getAzimuth());
    }

    //checks if the given object is visible
    //based on whether or not it is higher than the "visible horizon" of the user
    //as well as the direction they are facing (given here in lower and upper bounds, in a CW direction)
    //for example, 45-135 is east
    public static boolean isVisible(SkyObject obj, double horizon) {
        double alt = obj.getAltitude();
        double az = obj.getAzimuth();
        if (alt > horizon) {
                    return true;
        }
        return false;
    }

    /*calculates altitude of an object given its declination, the hr angle, and the observer's latitude
     assumes inputs are given in radians, outputs altitude in decimal degrees
     altitude is the height of the object in the sky as seen by an observer*/
    //DEFINITELY THE CORRECT FORMULA
    public static double calcAltitude(double dec, double lat, double hourAngle){
        double altitude = Math.asin(Math.sin(lat) * Math.sin(dec) + Math.cos(lat) * Math.cos(dec) * Math.cos(hourAngle));
        return Math.toDegrees(altitude);
    }

    /* calculates azimuth of object given its declination, the hr angle, and the observer's latitude
     assumes inputs are given in radians, outputs azimuth in decimal degrees
     azimuth is the ____ of an object in the sky as seen by an observer*/
    public static double calcAzimuth(double dec, double lat, double hourAngle){
        double az = Math.atan(Math.sin(hourAngle)/(Math.cos(hourAngle)*Math.sin(lat)-Math.tan(dec)*Math.cos(lat)));
        return normalize(Math.toDegrees(az));
    }

    //calculate hr angle in decimal degrees
    public static double calcHourAngle(double gst, double lon, double rightA){
        return normalize(gst - lon - rightA);
    }

    //calculate greenwhich sidereal time in decimal degrees
    public static double calcGST(double jDay){
        double t = (jDay - 2451545)/36525;
        double gst = 280.46061837 + 360.98564736629*(jDay-2451545) + 0.000387933*Math.pow(t,2) - Math.pow(t, 3)/39710000;
        return normalize(gst);
    }

    //convert current UTC day/time to julian day, useful for astronomical calculations
    public static double calcJulianDay(double yr, double mo, double day, double hr, double min){
        hr = hr + min/60;
        day = day + hr/24;
        if (mo <= 2){
            yr = yr - 1;
            mo = mo + 12;
        }
        double A = Math.floor(yr/100);
        double B = 2 - A + Math.floor(A/4);
        return Math.floor(365.25*(yr+4716)) + Math.floor(30.6001*(mo+1)) + day + B - 1524.5;
    }

    //convert from 0h0m0s format to 0.000deg format
    public static double timeToDegrees(double hr, double min, double sec){
        hr = hr + (min + sec/60)/60;
        double angle = hr*15;
        angle = normalize(angle);
        return angle;
    }

    //convert from 0deg0m0s format to 0.000deg format
    public static double decimalDegrees(double angle, double min, double sec){
        angle = angle + (min + sec/60)/60;
        angle = normalize(angle);
        return angle;
    }

    public static double normalize(double angle){
        double newAngle = angle;
        while (newAngle <= 0){
            newAngle += 360;
        }
        while (newAngle > 360){
            newAngle -= 360;
        }
        return newAngle;
    }
}