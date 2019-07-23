package com.stratio.architecture.multihdfs.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "configuration")
@JacksonXmlRootElement(localName = "configuration")
public class ConfigurationDTOResponse {

    @JacksonXmlElementWrapper(useWrapping = false)
    private List<HDFSProperty> property;


}
