package identisky.findgazinglocations;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;

public class GazingLocationsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    double [][] top3Elev = new double[3][3];

    double elevation;
    double  currentLatitude = 43.258511;
    double currentLongitude = -79.919389;
    /*
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gazing_locations);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this)
                .addApi(AppIndex.API)
                .build();

        new ElevationFinder().execute(Double.valueOf(currentLatitude) ,Double.valueOf(currentLongitude)); //-34, 151
    }

    //
    //* Manipulates the map once available.
    //* This callback is triggered when the map is ready to be used.
    //* This is where we can add markers or lines, add listeners or move the camera. In this case,
    //* we just add a marker near Sydney, Australia.
    //* If Google Play services is not installed on the device, the user will be prompted to install
    //* it inside the SupportMapFragment. This method will only be triggered once the user has
    //* installed Google Play services and returned to the app.
    //

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        long counter = 0;

        while(counter < 100000000)
        {
            //wait
            counter++;
        }

        LatLng elevMarkers[] = new LatLng[3];

        for (int i = 0; i < 3; i++) {
            elevMarkers[i] = new LatLng(top3Elev[i][0], top3Elev[i][1]);
            mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_CYAN)).position(elevMarkers[i]).title("Gazing Location #" + (i + 1))
                            .snippet("Elevation = " + String.valueOf(top3Elev[i][2])));
        }

        // Add a marker in Sydney and move the camera
        // LatLng sydney = new LatLng(-34, 151);
        // LatLng mcmaster = new LatLng(43.259495, -79.917470);
        LatLng current = new LatLng(currentLatitude, currentLongitude);
        mMap.addMarker(new MarkerOptions().position(current).icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_RED)).title("Current Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 14));
    }

    public class ElevationFinder extends AsyncTask<Double, Double, Double> {

        public void gridSearch(double latitude, double longitude) throws IOException
        {	    /*
                 * gridSearch(lat, long) takes in coordinates and creates
                 * a 3x3 grid of points. Using the lat and long of each
                 * point, it gets the elevation and returns
                 * an array of the 3 highest locations
                 */

            double grid[][] = new double[9][3]; // 9 points, containing (lat, long, elevation)
            double gridLengthKm = 2.0 / 111.0; // 2km = 2/111 line of lat/long

            double topLeftLat = latitude + (gridLengthKm / 2);
            double topLeftLong = longitude - (gridLengthKm / 2);

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

            top3Elev = findTop3(grid);
        }

        private double[][] findTop3(double[][] grid) {    // first sort grid by elevation
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

        private double getElevation(double latitude, double longitude) throws IOException
        {	/*
		     * getElevation(lat, long) takes in coordinates and returns
		     * the elevation at that point using google maps elevation API
		     *
		     * Code is based off:
		     * http://stackoverflow.com/questions/1359689/how-to-send-http-request-in-java#answer-1359702
		     */

            final String apiKey = "AIzaSyAMNRTex_aPcWbR6GrEJSraIzVV1SEVleE";

            URL googleElevation = new URL("https://maps.googleapis.com/maps/api/elevation/"
                    + "xml?locations=" + String.valueOf(latitude)
                    + "," + String.valueOf(longitude) + "&key=" + apiKey);

            URLConnection connection = googleElevation.openConnection();

            BufferedReader in = new BufferedReader(
                                    new InputStreamReader(
                                            connection.getInputStream()));

            String inputLine;
            String resultLine = "";

            while ((inputLine = in.readLine()) != null)
                resultLine += inputLine;
            in.close();

            resultLine = resultLine.substring(174, 181); // only store elevation from server response

            return Double.parseDouble(resultLine);
        }

        @Override
        protected Double doInBackground(Double...latlong)
        {
            DecimalFormat df = new DecimalFormat("#.#####");

            String lat = df.format(latlong[0]);
            String lng = df.format(latlong[1]);

            try
            {
                gridSearch(Double.parseDouble(lat), Double.parseDouble(lng));
                for (int i = 0; i < 3; i++)
                {
                    for (int j = 0; j < top3Elev[i].length; j++)
                    {
                        System.out.println("top3Elev["+i+"]["+j+"] = "+top3Elev[i][j]);
                    }
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            return null;
        }


        @Override
        protected void onPostExecute(Double result) {

        }
    }
}