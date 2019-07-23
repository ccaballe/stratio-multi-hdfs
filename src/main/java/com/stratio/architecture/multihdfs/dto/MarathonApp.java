package com.stratio.architecture.multihdfs.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarathonApp {


    private String id;

    @JsonProperty("labels")
    private MarathonLabel labels;

    @JsonProperty("container")
    private MarathonContainer container;
}
