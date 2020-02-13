package xyz.sentiment.analysis.services;

import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.impl.InfluxDBResultMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import xyz.sentiment.analysis.dto.SentimentSeries;
import xyz.sentiment.analysis.dto.SentimentTuple;

import java.util.Collections;
import java.util.List;
import java.util.Map;


@Service
public class SentimentCache{
 
    private static final Logger LOG = LoggerFactory.getLogger(SentimentCache.class);

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
            .time(tuple.getDatetime().toEpochMilli(),TimeUnit.MILLISECONDS)
            .addField("ticker",ticker.toLowerCase())
            .addField("value",tuple.getSentiment())
            .build();

        influxDB.write(p);

    }

    public SentimentSeries getSeries(String ticker){
        String cmd = String.format("Select * from sentiment where ticker = '%s'" , ticker.toLowerCase());
        Query q = new Query(cmd, db);
        QueryResult queryResult = influxDB.query(q); 
        LOG.info(queryResult.toString());
        InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
        
        List<SentimentTuple> tuples = resultMapper
              .toPOJO(queryResult, SentimentTuple.class);            
        SentimentSeries ss = new SentimentSeries();
        ss.setData(tuples);
        return ss;
    }

    public boolean hasBeenProcessed(String ticker, long time){
        Query q = new Query(String.format("select * from sentiment where ticker = '%s' and time = %d",ticker,time),db);
        QueryResult result = influxDB.query(q);
        LOG.info(result.getResults().toString());

        return result.getResults().size() > 0 && 
            (result.getResults().get(0).getSeries() != null && result.getResults().get(0).getSeries().size() > 0);
        
    }
}