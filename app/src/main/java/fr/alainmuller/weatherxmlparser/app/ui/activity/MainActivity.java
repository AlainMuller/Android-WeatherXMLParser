package fr.alainmuller.weatherxmlparser.app.ui.activity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

import fr.alainmuller.weatherxmlparser.app.R;


public class MainActivity extends ActionBarActivity {

    public static final String DATA_KEY = "data";
    private static final String DATA_REP = "xml";

    private TextView tvInfos, tvContent;

    private String[] files;     // Liste des fichiers
    private byte[] dataContent; // Contenu XML à exploiter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Init des textview
        tvInfos = (TextView) findViewById(R.id.tv_activity_main_infoXML);
        tvContent = (TextView) findViewById(R.id.tv_activity_main_content);

        // Tests : on fait ça dans l'Activity pour simplifier, mais c'est sale de faire ce genre
        // d'opération via le thread d'UI! =/

        // On récupère l'AssetManager pour accéder au fichier source XML (à remplacer par un appel HTTP...)
        AssetManager assetManager = getAssets();

        // On va lister les fichiers contenus dans le sous répertoire "xml" du répertoire assets
        try {
            files = assetManager.list(DATA_REP);
            //Toast.makeText(getApplicationContext(), "Nombre de fichiers : " + files.length, Toast.LENGTH_SHORT).show();

            for (int i = 0; i < files.length; i++) {
                tvInfos.append(i > 0 ? "\n" : "" + "File :" + i + " Name => " + files[i]);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // On va charger le fichier San_Francisco.xml
        InputStream input;
        try {
            // Récupération de l'inputStream directement depuis le fichier via l'AssetManager
            input = assetManager.open(DATA_REP + "/" + files[0]);

            int size = input.available();
            dataContent = new byte[size];
            input.read(dataContent);
            input.close();

            // On transforme le byte buffer en String pour l'afficher
            tvContent.setText(new String(dataContent));

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * Gestion du clic sur le bouton de l'UI : chargement de l'Activity de parsing de l'XML
     *
     * @param view le bouton en question
     */
    public void goParsing(View view) {
        // On dépile l'activity de la BackStack
        //finish();
        Intent parseIntent = new Intent(getApplicationContext(), ParseActivity.class);
        // On lui fournit les données du fichier XML pour parsing
        parseIntent.putExtra(DATA_KEY, dataContent);
        startActivity(parseIntent);
    }
}
