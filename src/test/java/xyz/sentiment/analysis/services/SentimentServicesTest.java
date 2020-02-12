package xyz.sentiment.analysis.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import xyz.sentiment.analysis.dto.IEXNews;
import xyz.sentiment.analysis.dto.SentimentSeries;
import xyz.sentiment.analysis.dto.SentimentTuple;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;


@Disabled
@SpringBootTest
public class SentimentServicesTest {

    @InjectMocks
    SentimentService service;

    @InjectMocks
    SentimentCache cache;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testComputeSentiment1() throws Exception{
     
        // IEXNews item = readItem("testComputeSentiment1.json");
        // assertThat(item.getHeadline()).isNotEmpty();
        // assertThat(item.getSummary()).isNotEmpty();

        // //String line = "Apple is planning to price its rumored low-cost iPhone at $399";
        
        // service.computeSentiment("aapl",item);
        // SentimentSeries series = cache.getSeries("aapl");
        // List<SentimentTuple> data = series.getData();
        // assertThat(data).isNotNull();
        // assertThat(data.size()).isEqualTo(1);
        // assertThat(data.get(1).getSentiment()).isCloseTo(2, within(.0001));
        
    }

    @Test
    public void testComputeSentiment2(){
        
        // IEXNews item = readItem("testComputeSentiment2.json");
        // assertThat(item.getHeadline()).isNotEmpty();
        // assertThat(item.getSummary()).isNotEmpty();

        // //String line = "Apple is planning to price its rumored low-cost iPhone at $399";
        // assertThat(service.computeSentiment("aapl",item)).isEqualTo(2);
    }    

    private IEXNews readItem(String filename){
        try {
            ObjectMapper mapper = new ObjectMapper();
            IEXNews item = mapper.readValue(SentimentServicesTest.class.getClassLoader().getResourceAsStream(filename),IEXNews.class);
            System.out.println(mapper.writeValueAsString(item));
            return item;
        } catch(Exception ex){
            System.out.println("Failed to read file: "+ filename);
            throw new RuntimeException(ex);            
        }

    }
}