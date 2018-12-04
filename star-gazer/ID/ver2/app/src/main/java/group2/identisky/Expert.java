package group2.identisky;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Map;

//abstract class for all experts
//takes in relevant info from forum, including the potentialAnswers ArrayList that it updates with new answers (if possible)
public abstract class Expert {

    public abstract void assess(GregorianCalendar dateTime, Map<String, Double> info, String astrology, ArrayList<SkyObject> potentialAnswers);

}
