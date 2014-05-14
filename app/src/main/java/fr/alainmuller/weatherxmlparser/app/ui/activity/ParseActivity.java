package fr.alainmuller.weatherxmlparser.app.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import fr.alainmuller.weatherxmlparser.app.R;


public class ParseActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
         Pas besoin de ça, car on a déclaré le fragment dans le XML

        // getFragmentManager pour anciennes versions
        FragmentManager fm = getSupportFragmentManager();

        // On crée le ListFragment et on l'ajoute en content du layout de l'activity
        if (fm.findFragmentById(android.R.id.content) == null) {
            ForecastFragment list = new ForecastFragment();
            fm.beginTransaction().add(android.R.id.content, list).commit();
        }
        */

        setContentView(R.layout.activity_parse);
    }
}
