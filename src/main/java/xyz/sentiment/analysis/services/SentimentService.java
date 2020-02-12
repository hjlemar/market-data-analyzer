package xyz.sentiment.analysis.services;

import java.util.Arrays;
import java.util.List;
import java.util.OptionalDouble;
import java.util.Properties;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import xyz.sentiment.analysis.dto.IEXNews;
import xyz.sentiment.analysis.dto.SentimentTuple;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.apache.commons.codec.digest.DigestUtils;

@Service
public class SentimentService {

    @Autowired
    private SentimentCache cache;

    private final StanfordCoreNLP pipeline;
    
    public SentimentService(){
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
        this.pipeline = new StanfordCoreNLP(props);
    }

    @Async("asyncExecutor")
    public void computeSentiment(final String ticker, final IEXNews newsItem) {
        
        OptionalDouble mainSentiment = Arrays.asList(newsItem.getHeadline(), newsItem.getSummary())
            .stream()
            .mapToDouble((line) -> processLine(line))
            .max();            
       
        double sentiment = mainSentiment.isPresent() ? mainSentiment.getAsDouble() : -1;
        String id = md5(newsItem);
        if (id != null){
            cache.addItem(ticker, new SentimentTuple(id,newsItem.getDatetime(), sentiment));        
        }
    }

    private int processLine(final String line) {
        int mainSentiment = 0;
        if (line != null && line.length() > 0) {
            int longest = 0;
            final Annotation annotation = pipeline.process(line);
            for (final CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
                final Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
                final int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
                final String partText = sentence.toString();
                if (partText.length() > longest) {
                    mainSentiment = sentiment;
                    longest = partText.length();
                }

            }
        }
        return mainSentiment;
    }

    private String md5(final IEXNews newsItem){
        try{
            ObjectMapper mapper = new ObjectMapper();
            String hash = mapper.writeValueAsString(newsItem);
            return DigestUtils.md5Hex(hash);
        } catch(Exception ex) {
            System.out.println("Error trying to compute md5sum of "+newsItem.getHeadline());
            ex.printStackTrace();
        } finally {
            // nothing really to do here
        }

        return null;
    }
}