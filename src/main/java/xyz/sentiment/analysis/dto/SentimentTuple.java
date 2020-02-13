package xyz.sentiment.analysis.dto;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Measurement(name = "sentiment")
public class SentimentTuple{


    @Column(name = "time")
    private Instant datetime;

    @Column(name = "value")
    private Double sentiment;

    @Column(name = "ticker" )
    private String ticker;

    public SentimentTuple(){        
    }
    
}