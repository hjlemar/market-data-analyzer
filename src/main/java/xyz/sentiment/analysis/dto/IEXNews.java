package xyz.sentiment.analysis.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;


@Data
//@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class IEXNews {
    private long datetime;
    private String source;
    private String url;
    private String headline;
    private String summary;
}