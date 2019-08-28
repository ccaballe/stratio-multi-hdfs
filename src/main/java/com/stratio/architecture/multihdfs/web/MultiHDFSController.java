/**
 * © 2019 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 * <p>
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal
 * en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or
 * sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written
 * authorization from Stratio Big Data Inc., Sucursal en España.
 */

package com.stratio.architecture.multihdfs.web;

import com.stratio.architecture.multihdfs.dto.ConfigurationDTOResponse;
import com.stratio.architecture.multihdfs.dto.HDFSProperty;
import com.stratio.architecture.multihdfs.dto.MarathonApp;
import com.stratio.architecture.multihdfs.dto.MarathonAppsDTOResponse;
import com.stratio.architecture.multihdfs.error.ErrorResponse;
import com.stratio.architecture.multihdfs.error.HDFSNotFoundException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/config")
public class MultiHDFSController {

    @Autowired
    RestTemplate restTemplate;

    // TODO include env var in yaml
    @Value("${MULTIHDFS_USER}")
    private String user;

    @Value("${MULTIHDFS_PASS}")
    private String pass;

    private String coreSiteFile = "core-site.xml";

    private String hdfsSiteFile = "hdfs-site.xml";

    private String krb5File = "krb5.conf";

    @ApiOperation(value = "Get config file", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = String.class),
            @ApiResponse(code = 404, message = "Not Found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Technical Error", response = ErrorResponse.class)})
    @RequestMapping(value = "/{config}/**", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> getConfig(@ApiParam(value = "Config file", required = true) @PathVariable String config, HttpServletRequest request) {


        final String path =
                request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString();
        final String bestMatchingPattern =
                request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE).toString();

        String arguments = new AntPathMatcher().extractPathWithinPattern(bestMatchingPattern, path);

        List<String> argumentsList = Arrays.asList(arguments.split("/"));

        String configFile = argumentsList.get(argumentsList.size() - 1);


        String hdfsMasterName = config;
        List<String> hdfsSecondaries = argumentsList.stream().limit(argumentsList.size() - 1).collect(Collectors.toList());

        MarathonAppsDTOResponse marathonAppsDTOResponse = getMarathonApps();
        String hdfsMaster = getHDFSURL(hdfsMasterName, marathonAppsDTOResponse);

        ResponseEntity<String> response;
        if (configFile.equals(coreSiteFile)) {
            response = restTemplate.getForEntity(URI.create(hdfsMaster + "/v1/config/" + coreSiteFile), String.class);
        } else if (configFile.equals(krb5File)) {
            response = restTemplate.getForEntity(hdfsMaster + "/v1/config/" + krb5File, String.class);
        } else if (configFile.equals(hdfsSiteFile)) {
            if (hdfsSecondaries == null || hdfsSecondaries.isEmpty())
                response = restTemplate.getForEntity(hdfsMaster + "/v1/config/" + hdfsSiteFile, String.class);
            else {
                response = concatenateHDFSSite(hdfsMaster, hdfsSecondaries.stream().map(x -> getHDFSURL(x, marathonAppsDTOResponse)).collect(Collectors.toList()));
            }
        } else {
            response = ResponseEntity.notFound().build();
        }
        return response;
    }

    public MarathonAppsDTOResponse getMarathonApps() {

        String marathonUrl = "https://marathon.mesos:8443/v2/apps";
        log.debug("Getting marathons apps from URL " + marathonUrl);

        MarathonAppsDTOResponse marathonAppsDTOResponse = restTemplate.exchange(marathonUrl,
                HttpMethod.GET,
                new HttpEntity<String>(createHeaders(user, pass)),
                MarathonAppsDTOResponse.class).getBody();
        return marathonAppsDTOResponse;
    }

    private String getHDFSURL(String hdfs, MarathonAppsDTOResponse marathonAppsDTOResponse) {
        log.debug("Getting HDFS URL for HDFS: " + hdfs);

        // filter HDFS apps
        List<MarathonApp> marathonAppsFiltered = marathonAppsDTOResponse.getApps().stream()
                .filter(app -> app.getLabels().getSTRATIO_SERVICE() != null && app.getLabels().getSTRATIO_SERVICE().equals("hdfs") && app.getId().split("/")[app.getId().split("/").length - 1].equals(hdfs))
                .collect(Collectors.toList());
        if (marathonAppsFiltered.size() <= 0)
            throw new HDFSNotFoundException("HDFS with name " + hdfs + " not found");

        MarathonApp marathonApp = marathonAppsFiltered.get(0);
        String id = marathonApp.getId();
        String protocol = marathonApp.getLabels().getDCOS_SERVICE_SCHEME();
        int containerPort = marathonApp.getContainer().getPortMappings().get(0).getContainerPort();
        List<String> aux = Arrays.asList(id.split("/"));
        Collections.reverse(aux);
        String url = protocol + "://" + StringUtils.join(aux, ".") + "marathon.mesos:" + containerPort;
        log.debug("HDFS URL is: " + url);
        return url;
    }

    private HttpHeaders createHeaders(String username, String password) {
        return new HttpHeaders() {{
            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.encodeBase64(
                    auth.getBytes(Charset.forName("US-ASCII")));
            String authHeader = "Basic " + new String(encodedAuth);
            set("Authorization", authHeader);
            setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            setContentType(MediaType.APPLICATION_JSON);
        }};
    }

    private ResponseEntity<String> concatenateHDFSSite(String hdfsMaster, List<String> hdfsSecondaries) {
        log.debug("Getting hdfs-site.xml from " + hdfsMaster);
        ConfigurationDTOResponse hdfsSiteMaster = restTemplate.getForObject(hdfsMaster + "/v1/config/hdfs-site.xml", ConfigurationDTOResponse.class);

        String dfsNameservicesProperty = "dfs.nameservices";
        String dfsNameservicesIdProperty = "dfs.nameservice.id";
        String hdfsMasterID = findProperty(hdfsSiteMaster, dfsNameservicesIdProperty).getValue();

        String hdfsNameservices = hdfsMasterID;

        for (String hdfsSecondary : hdfsSecondaries) {

            ConfigurationDTOResponse hdfsSiteSecondary = restTemplate.getForObject(hdfsSecondary + "/v1/config/hdfs-site.xml", ConfigurationDTOResponse.class);
            // concatenate dfs.nameservices
            String hdfsSecondaryID = findProperty(hdfsSiteSecondary, dfsNameservicesIdProperty).getValue();
            hdfsNameservices = hdfsNameservices + "," + hdfsSecondaryID;
            setProperty(hdfsSiteMaster, new HDFSProperty(dfsNameservicesProperty, hdfsNameservices));
            // include dfs.internal.nameservices with the hdfs master
            String dfsInternalNameservicesProperty = "dfs.internal.nameservices";
            setProperty(hdfsSiteMaster, new HDFSProperty(dfsInternalNameservicesProperty, hdfsMasterID));
            // include all custom properties from secondary hdfs to primary
            for (HDFSProperty hdfsProperty : findCustomProperties(hdfsSiteSecondary, hdfsSecondaryID)) {
                setProperty(hdfsSiteMaster, hdfsProperty);
            }

        }
        ResponseEntity responseEntity = ResponseEntity.ok(hdfsSiteMaster);
        return responseEntity;
    }

    private HDFSProperty findProperty(ConfigurationDTOResponse hdfsSite, String name) {
        return hdfsSite.getProperty().stream().filter(hdfsProperty -> name.equals(hdfsProperty.getName())).findFirst().orElse(null);
    }

    private void setProperty(ConfigurationDTOResponse hdfsSite, HDFSProperty hdfsProperty) {
        HDFSProperty oldProperty = findProperty(hdfsSite, hdfsProperty.getName());
        if (oldProperty == null) {
            hdfsSite.getProperty().add(hdfsProperty);
        } else {
            int index = hdfsSite.getProperty().indexOf(findProperty(hdfsSite, hdfsProperty.getName()));
            hdfsSite.getProperty().set(index, hdfsProperty);
        }

    }

    private List<HDFSProperty> findCustomProperties(ConfigurationDTOResponse hdfsSite, String id) {
        return hdfsSite.getProperty().stream().filter(hdfsProperty -> hdfsProperty.getName().contains("." + id)).collect(Collectors.toList());
    }


}
