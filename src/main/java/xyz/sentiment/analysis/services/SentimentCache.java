package xyz.sentiment.analysis.services;

import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.impl.InfluxDBResultMapper;
import org.springframework.stereotype.Service;

import xyz.sentiment.analysis.dto.SentimentSeries;
import xyz.sentiment.analysis.dto.SentimentTuple;

import java.util.Collections;
import java.util.List;
import java.util.Map;


@Service
public class SentimentCache{
 
    private final InfluxDB influxDB;
    private final String db = "analyser_data";


    public SentimentCache(){        
        influxDB = InfluxDBFactory.connect("http://localhost:8086", "analyser_service", "Say$omethingN!ce");
        influxDB.setLogLevel(InfluxDB.LogLevel.BASIC);
        influxDB.setDatabase(db);
        influxDB.createRetentionPolicy("defaultPolicy", "sentiment", "30d", 1, true);
    }

    public void addItem(String ticker, SentimentTuple tuple){
        Point p = Point.measurement("sentiment")
            .time(tuple.getDatetime(),TimeUnit.MILLISECONDS)
            .addField("ticker",ticker)
            .addField("value",tuple.getSentiment())
            .build();

        influxDB.write(p);

    }

    public SentimentSeries getSeries(String ticker){
        Query q = new Query("Select * from sentiment", db);
        QueryResult queryResult = influxDB.query(q); 
        InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
        List<SentimentTuple> tuples = resultMapper
              .toPOJO(queryResult, SentimentTuple.class);            
        SentimentSeries ss = new SentimentSeries();
        ss.setData(tuples);
        return ss;
    }
}