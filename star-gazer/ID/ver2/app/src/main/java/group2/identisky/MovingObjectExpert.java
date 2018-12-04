package group2.identisky;
import android.content.Context;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.GregorianCalendar;
import java.util.Map;

public class MovingObjectExpert{

    //ensures that duplicate objects are not added to the forum
    private boolean returnedObjects;

    public MovingObjectExpert(Context context){
        this.returnedObjects = false;
    }

    //assesses given info and recommends potential answers
    public void assess(GregorianCalendar dateTime, Map<String, Double> info, String astrology, ArrayList<SkyObject> potentialAnswers,
                       ArrayList<SkyObject> objectsDB){

        //did the user pick the "moving object" category?
        //did we already do this check and return the answer?
        if (!returnedObjects&&(info.get(Encryption.encrypt("category"))==2.0)){
            //could it be a satellite?
            SkyObject sat = new SkyObject("A satellite", 0, 0, "other");
            SkyObject met = new SkyObject("A meteor", 0, 0, "other");
            SkyObject plane = new SkyObject("An airplane", 0, 0, "other");
            if ((info.get(Encryption.encrypt("blinking"))==0)&&(info.get(Encryption.encrypt("speed"))==1.0)){

                potentialAnswers.add(sat);
            }
            //could it be a meteor/shooting star?
            else if ((info.get(Encryption.encrypt("blinking"))==0)&&(info.get(Encryption.encrypt("speed"))==2.0)){

                potentialAnswers.add(met);
            }
            //could it be an airplane?
            else if ((info.get(Encryption.encrypt("blinking"))==1.0)&&(info.get(Encryption.encrypt("speed"))==1.0)){

                potentialAnswers.add(plane);
            }
            else{
                potentialAnswers.add(met);
                potentialAnswers.add(sat);
                potentialAnswers.add(plane);
            }
            returnedObjects = true;
        }


    }

}
