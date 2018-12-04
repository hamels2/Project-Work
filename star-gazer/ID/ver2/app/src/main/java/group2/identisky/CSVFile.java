package group2.identisky;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;


public class CSVFile {
    public static ArrayList readStarCSV(InputStream s){
        Scanner input = new Scanner(s);
        ArrayList<String> list = new ArrayList<>();
        while (input.hasNextLine()) {//keep reading while there are more lines
            String line = input.nextLine();//read the next line in the file
            String[] splits = line.split(",");
            list.add(splits[0]);
            list.add(splits[1].substring(0,splits[1].length()-1));
            list.add(splits[2].substring(0,splits[2].length()-1));
            list.add(splits[3]);
        }
        input.close();//close the scanner
        ArrayList<SkyObject> skyObjectsList = new ArrayList<SkyObject>();
        for (int i = 0; i < list.size()/4; i++){
            String string = list.get(4 * i + 1);
            String[] splits = string.split("h");
            Double ra = Double.parseDouble(splits[0])+(Double.parseDouble(splits[1])/60);
            String string2 = list.get(4 * i + 2);
            String[] split2 = string2.split("d");
            Double dec = Double.parseDouble(split2[0])+(Double.parseDouble(split2[1])/60);
            skyObjectsList.add(new SkyObject(list.get(4*i),ra,dec,list.get(4*i+3)));
        }
        return skyObjectsList;
    }
}