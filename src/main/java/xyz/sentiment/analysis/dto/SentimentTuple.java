package xyz.sentiment.analysis.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Measurement(name = "memory")
public class SentimentTuple{
    private String id;

    @Column(name = "time")
    private long datetime;

    @Column(name = "sentiment")
    private Double sentiment;
    
}