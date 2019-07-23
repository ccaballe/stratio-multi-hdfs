package com.stratio.architecture.multihdfs.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarathonContainer {


    @JsonProperty("portMappings")
    private List<MarathonPortMapping> portMappings;
}
