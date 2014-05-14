package fr.alainmuller.weatherxmlparser.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import fr.alainmuller.weatherxmlparser.app.R;
import fr.alainmuller.weatherxmlparser.app.model.ForecastDay;

/**
 * Created by Alain MULLER on 14/05/2014.
 */
public class ForecastAdapter extends ArrayAdapter<ForecastDay> {
    private final LayoutInflater mInflater;

    public ForecastAdapter(Context context) {
        // On utilise un adapter sur le style android simple_list_item_2
        super(context, android.R.layout.simple_list_item_2);
        // On récupère le Layout inflater de l'activity pour manipuler la view
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(List<ForecastDay> data) {
        clear();
        if (data != null) {
            for (ForecastDay forecastEntry : data) {
                add(forecastEntry);
            }
        }
    }

    /**
     * Chargement des données dans la vue
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView == null) {
            view = mInflater.inflate(R.layout.single_item, parent, false);
        } else {
            view = convertView;
        }

        ForecastDay item = getItem(position);
        // On va récupérer le jour et le temps (nom de l'icone) pour les afficher dans la liste
        ((TextView) view.findViewById(R.id.tv_single_item_label)).setText(String.valueOf(item.getPeriod()));
        ((TextView) view.findViewById(R.id.tv_single_item_forecast)).setText(item.getIcon());

        return view;
    }
}
