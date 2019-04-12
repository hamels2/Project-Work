package identisky.findgazinglocations;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class ElevationFinder extends AsyncTask<Void,Void,Void> {

    public static double[][] gridSearch(double latitude, double longitude) throws IOException {	/*
		 * gridSearch(lat, long) takes in coordinates and creates
		 * a 3x3 grid of points. Using the lat and long of each
		 * point, it gets the elevation and returns
		 * an array of the 3 highest locations
		 */

        double grid[][] = new double[9][3]; // 9 points, containing (lat, long, elevation)
        double gridLengthKm = 1.0 / 111.0; // 1km = 1/111 line of lat/long

        double topLeftLat = latitude + (gridLengthKm / 2);
        double topLeftLong = longitude + (gridLengthKm / 2);

        int count = 0;
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                grid[count][0] = topLeftLat - (i * gridLengthKm / 2);
                grid[count][1] = topLeftLong + ((j * gridLengthKm) / 2);
                grid[count][2] = getElevation(grid[count][0], grid[count][1]);

                count += 1;
            }
        }

        double top3Elev[][] = findTop3(grid);

        return grid;
    }

    private static double getElevation(double latitude, double longitude) throws IOException {	/*
		 * getElevation(lat, long) takes in coordinates and returns
		 * the elevation at that point using google maps elevation API
		 *
		 * Code is based off:
		 * http://stackoverflow.com/questions/1359689/how-to-send-http-request-in-java#answer-1359702
		 */

        final String apiKey = "AIzaSyAMNRTex_aPcWbR6GrEJSraIzVV1SEVleE";

        URL googleElevation = new URL("https://maps.googleapis.com/maps/api/elevation/"
                + "xml?locations=" + String.valueOf(longitude)
                + "," + String.valueOf(latitude) + "&key=" + apiKey);

        URLConnection connection = googleElevation.openConnection();
        BufferedReader in = new BufferedReader(
                                new InputStreamReader(
                                        connection.getInputStream()));

        String inputLine;
        String resultLine = "";

        while ((inputLine = in.readLine()) != null)
            resultLine += inputLine;
        in.close();

        resultLine = resultLine.substring(171, 181); // only store elevation from server response
        double elevation = Double.parseDouble(resultLine); // convert to double

        return elevation;
    }

    private static double[][] findTop3(double[][] grid) {    // first sort grid by elevation
        boolean flag = true;
        double tmp[] = {0.0, 0.0, 0.0};

        int i;
        int j;

        while (flag)
        {
            flag = false;  //set flag to false awaiting a possible swap
            for (i = 0; i < grid.length - 1; i++)
            {
                if (grid[i][2] < grid[i + 1][2])
                {
                    for (j = 0; j < grid[i].length; j++) //swap elements
                    {
                        tmp[j] = grid[i][j];
                        grid[i][j] = grid[i + 1][j];
                        grid[i + 1][j] = tmp[j];
                    }

                    flag = true;    // shows swapped occurred
                }
            }
        }

        // return top 3
        double top3Elev[][] = new double[3][grid[0].length];
        for (i = 0; i < 3; i++)
        {
            for (j = 0; j < grid[i].length; j++)
            {
                top3Elev[i][j] = grid[i][j];
            }
        }

        return top3Elev;
    }

    @Override
    protected Void doInBackground(Void... params) {


        return null;
    }
}
