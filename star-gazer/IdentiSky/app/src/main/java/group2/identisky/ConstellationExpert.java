package group2.identisky;

import android.content.Context;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.GregorianCalendar;

/**
 * Created by Home on 03-Apr-16.
 */
public class ConstellationExpert{

    private boolean returnedConsts;
    private ArrayList<SkyObject> constellations; //change to SkyObject after pulling Matt's changes

    public ConstellationExpert(Context con){
        this.returnedConsts = false;
        DBHandler db = new DBHandler(con);
        this.constellations = db.getAllConstellations();
    }

    //assesses given info and recommends potential answers
    public void assess(GregorianCalendar dateTime, double longitude, double latitude, Dictionary<String, Double> info, ArrayList<SkyObject> potentialAnswers){
        //did the user pick the constellations category?
        if (!returnedConsts&&(info.get("constellation")!=0.0)){

            for (SkyObject obj: constellations){
                AstroCalc.setCoords(obj, dateTime, longitude, latitude);
                if (AstroCalc.isVisible(obj, info.get("horizon"),0,360)){
                    potentialAnswers.add(obj);
                }
            }

            returnedConsts = true;

        }

    }

}