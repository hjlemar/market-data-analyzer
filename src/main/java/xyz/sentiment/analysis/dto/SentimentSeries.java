package xyz.sentiment.analysis.dto;

import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SentimentSeries {

    private List<SentimentTuple> data = new ArrayList<>();

    public void add(SentimentTuple tuple){
        for (SentimentTuple item: data){
            if(tuple.getId().equals(item.getId())){
                return;
            }
        }
        data.add(tuple);
    }
}