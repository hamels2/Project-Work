package group2.identisky;
import java.util.ArrayList;
import java.util.Dictionary;

public class MovingObjectExpert {

    //ensures that duplicate objects are not added to the forum
    private boolean returnedObjects = false;

    //assesses given info and recommends potential answers
    public void assess(Dictionary<String, Boolean> info, ArrayList<SkyObject> potentialAnswers){

        //did the user pick the "moving object" category?
        //did we already do this check and return the answer?
        if (!returnedObjects&&!info.get("stationary")&&!info.get("constellation")){
            //could it be a satellite?
            if (!info.get("flashing")&&info.get("slowMoving")){
                SkyObject sat = new SkyObject("A satellite", 0, 0);
                potentialAnswers.add(sat);
            }
            //could it be a meteor/shooting star?
            else if (!info.get("flashing") && info.get("fastMoving")){
                SkyObject met = new SkyObject("A meteor", 0, 0);
                potentialAnswers.add(met);
            }
            //could it be an airplane?
            else if (info.get("flashing") && info.get("fastMoving")){
                SkyObject plane = new SkyObject("An airplane", 0, 0);
                potentialAnswers.add(plane);
            }
            returnedObjects = true;
        }


    }

}
