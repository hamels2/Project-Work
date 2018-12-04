package group2.identisky;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.GregorianCalendar;

public class StarAndPlanetExpert{

    //ensures that duplicate objects are not returned
    private boolean returnedStars;
    private boolean returnedPlanets;

    public StarAndPlanetExpert(){
        this.returnedStars = false;
        this.returnedPlanets = false;
    }

    //assesses given info and recommends potential answers
    public void assess(GregorianCalendar dateTime, double longitude, double latitude, Dictionary<String, Double> info, ArrayList<SkyObject> potentialAnswers,
                       ArrayList<SkyObject> starDB, ArrayList<SkyObject> planetDB) {

        //did the user pick the "stationary object" category?
        if ((info.get("stationary")!=0.0)&&(info.get("constellation")==0.0)) {
            if (!returnedStars && (info.get("twinkling")!=0.0)) {

                for (SkyObject obj: starDB){
                    AstroCalc.setCoords(obj, dateTime, longitude, latitude);
                    if (AstroCalc.isVisible(obj, info.get("horizon"),0,360)){
                        potentialAnswers.add(obj);
                    }
                }

                returnedStars = true;
            }
            if (!returnedPlanets && (info.get("twinkling")==0.0)) {

                for (SkyObject obj: planetDB){
                    AstroCalc.setCoords(obj, dateTime, longitude, latitude);
                    if (AstroCalc.isVisible(obj, info.get("horizon"),0,360)){
                        potentialAnswers.add(obj);
                    }
                }

                returnedPlanets = true;
            }
        }
    }
}