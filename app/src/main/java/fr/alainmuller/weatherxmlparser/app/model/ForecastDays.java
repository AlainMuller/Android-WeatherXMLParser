package fr.alainmuller.weatherxmlparser.app.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Alain MULLER on 14/05/2014.
 */
public class ForecastDays implements Iterable<ForecastDay> {

    private List<ForecastDay> forecastDays = new ArrayList<ForecastDay>();

    @Override
    public Iterator<ForecastDay> iterator() {
        return forecastDays.iterator();
    }

    public List<ForecastDay> getForecastDays() {
        return forecastDays;
    }

    // Déclaration de la méthode permettant au reader d'ajouter des Forecast à la liste
    public void addForecastDay(ForecastDay forecastDay) {
        forecastDays.add(forecastDay);
    }

    public int getForecastSize() {
        return forecastDays.size();
    }

}
