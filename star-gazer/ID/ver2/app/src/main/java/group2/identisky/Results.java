package group2.identisky;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Results extends Activity {

    private SkyObject[] Results;
    private ArrayList<String> answerNames;
    private ArrayList<String> answerAzimuths;
    private ArrayList<String> answerAltitudes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        ListView listResults = (ListView) findViewById(R.id.listResults);
        ArrayList<SkyObject> answers = Identisky.getAnswers();
        answerNames = new ArrayList<String>();
        answerAltitudes = new ArrayList<String>();
        answerAzimuths = new ArrayList<String>();
        DecimalFormat df = new DecimalFormat("##.####");
        for(int i = 0; i < answers.size(); i++){
            answerNames.add(answers.get(i).getName());
            answerAltitudes.add("Altitude: " + df.format(answers.get(i).getAltitude()));
            answerAzimuths.add("Azimuth: " + df.format(answers.get(i).getAzimuth()));
        }

        ArrayAdapter<String> adapter = new CustomList(this,answerNames,answerAzimuths,answerAltitudes);

        listResults.setAdapter(adapter);

        listResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goToUrl("https://en.wikipedia.org/wiki/" + answerNames.get((int) id));
            }
        });
    }

    private void goToUrl (String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }

    public class CustomList extends ArrayAdapter<String>{

        private final Activity context;
        private final ArrayList<String> names;
        private final ArrayList<String> azimuths;
        private final ArrayList<String> altitudes;
        public CustomList(Activity context, ArrayList<String> names, ArrayList<String> azimuths,
                          ArrayList<String> altitudes) {
            super(context, R.layout.results_row_layout, names);
            this.context = context;
            this.names = names;
            this.azimuths = azimuths;
            this.altitudes = altitudes;
        }
        @Override
        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.results_row_layout, null, true);
            TextView title = (TextView) rowView.findViewById(R.id.objectName);
            TextView azimuth = (TextView) rowView.findViewById(R.id.azimuth);
            TextView altitude = (TextView) rowView.findViewById(R.id.altitude);
            title.setText(names.get(position));
            azimuth.setText(azimuths.get(position));
            altitude.setText(altitudes.get(position));
            return rowView;
        }
    }

}
