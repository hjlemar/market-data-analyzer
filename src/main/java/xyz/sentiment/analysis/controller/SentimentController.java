package xyz.sentiment.analysis.controller;

import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import xyz.sentiment.analysis.dto.IEXNews;
import xyz.sentiment.analysis.dto.SentimentSeries;
import xyz.sentiment.analysis.services.SentimentCache;
import xyz.sentiment.analysis.services.SentimentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;




@Api(description="Sentiment API")
@RestController
@RequestMapping(path="sentiment")
public class SentimentController { 

    @Autowired
    private SentimentCache cache;

    @Autowired
    private SentimentService service;

    @ApiOperation(value = "Get Series Data")
    @GetMapping(value="{ticker}")
    public ResponseEntity<SentimentSeries> getSeries(@PathVariable(name="ticker") final String ticker) {
        return new ResponseEntity<>(cache.getSeries(ticker),HttpStatus.ACCEPTED);

    }

    @ApiOperation(value = "Generate Sentiment")
    @PostMapping(value="{ticker}")
    public ResponseEntity<IEXNews> postMethodName(@RequestBody IEXNews entity, @PathVariable(name="ticker") final String ticker) {
        
        service.computeSentiment(ticker, entity);
        return ResponseEntity.accepted().build();
    }
    
}
