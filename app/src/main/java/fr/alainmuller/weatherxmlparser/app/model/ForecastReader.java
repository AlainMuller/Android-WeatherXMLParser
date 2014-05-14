package fr.alainmuller.weatherxmlparser.app.model;

import com.sjl.dsl4xml.DocumentReader;

import java.io.Reader;

import static com.sjl.dsl4xml.SAXDocumentReader.mappingOf;
import static com.sjl.dsl4xml.SAXDocumentReader.tag;

/**
 * Created by Alain MULLER on 14/05/2014.
 * Lecture des prévisions météo via le parser DSL4XML
 */
public class ForecastReader {

    private DocumentReader<ForecastDays> mReader;

    public ForecastReader() {
        mReader = mappingOf("forecastdays", ForecastDays.class).to(
                tag("forecastday", ForecastDay.class).with(
                        tag("period"),
                        tag("icon"),
                        tag("icon_url"),
                        tag("title"),
                        tag("fcttext"),
                        tag("fcttext_metric"),
                        tag("pop")
                )
        );
    }

    public ForecastDays read(Reader reader) {
        return mReader.read(reader);
    }
}
