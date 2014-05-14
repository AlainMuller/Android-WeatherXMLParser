package fr.alainmuller.weatherxmlparser.app.ui.fragment;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import fr.alainmuller.weatherxmlparser.app.R;
import fr.alainmuller.weatherxmlparser.app.adapter.ForecastAdapter;
import fr.alainmuller.weatherxmlparser.app.model.ForecastDay;
import fr.alainmuller.weatherxmlparser.app.model.ForecastDays;
import fr.alainmuller.weatherxmlparser.app.model.ForecastReader;

/**
 * Created by Alain MULLER on 14/05/2014.
 */
public class ForecastFragment extends ListFragment implements LoaderManager.LoaderCallbacks<List<ForecastDay>> {

    private static final String LOG_TAG = ForecastFragment.class.getSimpleName();

    ForecastAdapter mAdapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Equivalent d'un textview d'id @android:id/empty sur un layout associé
        setEmptyText(getResources().getString(R.string.tv_fragment_forecast_empty_text));

        // Création d'un Adapter pour charger les données dans la liste
        mAdapter = new ForecastAdapter(getActivity());
        setListAdapter(mAdapter);

        // On va utiliser une progress bar pour le chargement de la liste
        setListShown(false);

        // Préparation du Loader => soit on se reconnecte à un existant, soit en en démarre un nouveau
        // On s'appelle en callback, cf. méthodes onCreateLoarder, onLoadFinished et onLoadReset ci-dessous
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Insert desired behavior here.
        Log.i(LOG_TAG, "Item clicked: " + id);
    }

    @Override
    public Loader<List<ForecastDay>> onCreateLoader(int arg0, Bundle arg1) {
        Log.i(LOG_TAG, "DataListFragment.onCreateLoader");
        return new DataListLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<ForecastDay>> arg0, List<ForecastDay> data) {
        mAdapter.setData(data);
        Log.i(LOG_TAG, "DataListFragment.onLoadFinished");
        // On réaffiche la liste
        if (isResumed()) {
            setListShown(true);
        } else {
            setListShownNoAnimation(true);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<ForecastDay>> arg0) {
        Log.i(LOG_TAG, "DataListFragment.onLoaderReset");
        mAdapter.setData(null);
    }

    public static class DataListLoader extends AsyncTaskLoader<List<ForecastDay>> {

        List<ForecastDay> mModels;

        public DataListLoader(Context context) {
            super(context);
        }

        @Override
        public List<ForecastDay> loadInBackground() {
            Log.i(LOG_TAG, "DataListLoader.loadInBackground");

            // Parsing XML en tâche de fond...

            // On récupère l'AssetManager pour accéder au fichier source XML (à remplacer par un appel HTTP...)
            AssetManager assetManager = getContext().getAssets();
            byte[] dataContent = null;

            // On va lister les fichiers contenus dans le sous répertoire "xml" du répertoire assets
            try {
                InputStream input = assetManager.open("xml/San_Francisco.xml");

                int size = input.available();
                dataContent = new byte[size];
                input.read(dataContent);
                input.close();

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            ForecastReader forecastReader = new ForecastReader();
            // Attention, ça pique les yeux! =/ #IrrelevantCode
            ForecastDays forecastDays = forecastReader.read(new BufferedReader(new InputStreamReader(new ByteArrayInputStream(dataContent))));

            Log.i(LOG_TAG, "DataListLoader.loadInBackground > Taille des prévisions : " + (forecastDays != null ? forecastDays.getForecastSize() : "0"));


            // TIMER pour voir s'afficher la barre de progression
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Idem, la flemme de faire ça propre #IrrelevantCode
            return forecastDays != null ? forecastDays.getForecastDays() : new ArrayList<ForecastDay>();
        }

        /**
         * Called when there is new data to deliver to the client.  The
         * super class will take care of delivering it; the implementation
         * here just adds a little more logic.
         */
        @Override
        public void deliverResult(List<ForecastDay> listOfData) {
            if (isReset()) {
                // An async query came in while the loader is stopped.  We
                // don't need the result.
                if (listOfData != null) {
                    onReleaseResources(listOfData);
                }
            }
            List<ForecastDay> oldForecasts = listOfData;
            mModels = listOfData;

            if (isStarted()) {
                // If the Loader is currently started, we can immediately
                // deliver its results.
                super.deliverResult(listOfData);
            }

            // At this point we can release the resources associated with
            // 'oldForecasts' if needed; now that the new result is delivered we
            // know that it is no longer in use.
            if (oldForecasts != null) {
                onReleaseResources(oldForecasts);
            }
        }

        /**
         * Handles a request to start the Loader.
         */
        @Override
        protected void onStartLoading() {
            if (mModels != null) {
                // If we currently have a result available, deliver it
                // immediately.
                deliverResult(mModels);
            }


            if (takeContentChanged() || mModels == null) {
                // If the data has changed since the last time it was loaded
                // or is not currently available, start a load.
                forceLoad();
            }
        }

        /**
         * Handles a request to stop the Loader.
         */
        @Override
        protected void onStopLoading() {
            // Attempt to cancel the current load task if possible.
            cancelLoad();
        }

        /**
         * Handles a request to cancel a load.
         */
        @Override
        public void onCanceled(List<ForecastDay> apps) {
            super.onCanceled(apps);

            // At this point we can release the resources associated with 'apps'
            // if needed.
            onReleaseResources(apps);
        }

        /**
         * Handles a request to completely reset the Loader.
         */
        @Override
        protected void onReset() {
            super.onReset();

            // Ensure the loader is stopped
            onStopLoading();

            // At this point we can release the resources associated with 'apps'
            // if needed.
            if (mModels != null) {
                onReleaseResources(mModels);
                mModels = null;
            }
        }

        /**
         * Helper function to take care of releasing resources associated
         * with an actively loaded data set.
         */
        protected void onReleaseResources(List<ForecastDay> apps) {
        }

    }


}
