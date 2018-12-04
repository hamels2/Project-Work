package group2.identisky;

import android.content.Context;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Map;

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
        System.out.println("BKESID");
        for (SkyObject o: constellations){
            System.out.print("BLOOP");
        }
    }

    //assesses given info and recommends potential answers
    public void assess(GregorianCalendar dateTime, Map<String, Double> info, String astrology, ArrayList<SkyObject> potentialAnswers,
                       ArrayList<SkyObject> constellationsDB){
        //did the user pick the constellations category?
        if (!returnedConsts&&(info.get(Encryption.encrypt("category"))==1.0)){
            for (SkyObject obj: constellationsDB){
                AstroCalc.setCoords(obj, dateTime, info.get(Encryption.encrypt("longitude")), info.get(Encryption.encrypt("latitude")));
                if (AstroCalc.isVisible(obj, info.get(Encryption.encrypt("skyVisibility")))){
                    if (!astrology.equals(Encryption.encrypt("none"))){
                        if (obj.getName().equals(Encryption.encrypt(astrology))){
                            potentialAnswers.add(obj);
                        }
                    }
                    else {
                        potentialAnswers.add(obj);
                    }
                }
            }

            returnedConsts = true;

        }

    }

}