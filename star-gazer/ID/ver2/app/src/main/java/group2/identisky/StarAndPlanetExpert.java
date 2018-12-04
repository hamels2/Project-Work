package group2.identisky;

import android.content.Context;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.GregorianCalendar;
import java.util.Map;

public class StarAndPlanetExpert{

    //ensures that duplicate objects are not returned
    private boolean returnedStars;
    private boolean returnedPlanets;
    private ArrayList<SkyObject> stars;

    public StarAndPlanetExpert(Context con){
        this.returnedStars = false;
        this.returnedPlanets = false;
        DBHandler db = new DBHandler(con);
        this.stars = db.getAllStars();
    }

    //assesses given info and recommends potential answers
    public void assess(GregorianCalendar dateTime, Map<String, Double> info, String astrology, ArrayList<SkyObject> potentialAnswers,
                       ArrayList<SkyObject> starsDB){

        //did the user pick the "stationary object" category?
        if (info.get(Encryption.encrypt("category"))==3){
            if (!returnedStars && (info.get(Encryption.encrypt("twinkling"))!=0.0)) {

                for (SkyObject obj: starsDB){
                    AstroCalc.setCoords(obj, dateTime, info.get(Encryption.encrypt("longitude")), info.get(Encryption.encrypt("latitude")));
                    if (AstroCalc.isVisible(obj, info.get(Encryption.encrypt("skyVisibility")))){
                        potentialAnswers.add(obj);
                    }
                }

                returnedStars = true;
            }
            if (!returnedPlanets && (info.get(Encryption.encrypt("twinkling"))==0.0)) {

                SkyObject planet = new SkyObject("A planet", 0, 0, "planet");
                potentialAnswers.add(planet);
                returnedPlanets = true;
            }
        }
    }
}