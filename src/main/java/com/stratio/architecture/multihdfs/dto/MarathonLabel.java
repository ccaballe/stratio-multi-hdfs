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
public class MarathonLabel {


    @JsonProperty("STRATIO_SERVICE")
    private String STRATIO_SERVICE;

    @JsonProperty("DCOS_SERVICE_SCHEME")
    private String DCOS_SERVICE_SCHEME = "https";
}
