package com.stratio.architecture.multihdfs.web;

import com.stratio.architecture.multihdfs.dto.*;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;
import javax.servlet.Filter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"TEST"})
public abstract class AbstractIT {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Resource(name = AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME)
    private Filter securityFilter;

    @MockBean
    private RestTemplate restTemplate;

    private static MockMvc mockMvc;

    @Before
    public void setup() throws Exception {

        ClassLoader classLoader = ClassLoader.getSystemClassLoader();

        if (this.mockMvc == null) {
            this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        }

        MarathonAppsDTOResponse marathonAppsDTOResponse = new MarathonAppsDTOResponse(Arrays.asList(
                new MarathonApp("/hdfs-test-1",
                        new MarathonLabel("hdfs", "http"), new MarathonContainer(Arrays.asList(new MarathonPortMapping(8443)))),
                new MarathonApp("/hdfs-test-2",
                        new MarathonLabel("hdfs", "http"), new MarathonContainer(Arrays.asList(new MarathonPortMapping(8443)))
                )));

        ResponseEntity<MarathonAppsDTOResponse> response = ResponseEntity.ok(marathonAppsDTOResponse);

        Mockito.when(restTemplate.exchange(Matchers.eq("https://marathon.mesos:8443/v2/apps"), Matchers.eq(HttpMethod.GET),
                Matchers.<HttpEntity<?>>any(),
                Matchers.eq(MarathonAppsDTOResponse.class))).thenReturn(response);

        Mockito.when(restTemplate.getForEntity("http://hdfs-test-1.marathon.mesos:8443/v1/config/core-site.xml", String.class))
                .thenReturn(ResponseEntity.ok(new String(Files.readAllBytes(new File(classLoader.getResource("core-site.xml").getFile()).toPath()))));

        Mockito.when(restTemplate.getForEntity("http://hdfs-test-1.marathon.mesos:8443/v1/config/krb5.conf", String.class))
                .thenReturn(ResponseEntity.ok(new String(Files.readAllBytes(new File(classLoader.getResource("krb5.conf").getFile()).toPath()))));

        File hdfs_site_1 = new File(classLoader.getResource("hdfs-test-1-site.xml").getFile());
        File hdfs_site_2 = new File(classLoader.getResource("hdfs-test-2-site.xml").getFile());

        JAXBContext jaxbContext;
        ConfigurationDTOResponse hdfs_site_1_response = null;
        ConfigurationDTOResponse hdfs_site_2_response = null;

        try {
            jaxbContext = JAXBContext.newInstance(ConfigurationDTOResponse.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            hdfs_site_1_response = (ConfigurationDTOResponse) jaxbUnmarshaller.unmarshal(hdfs_site_1);
            hdfs_site_2_response = (ConfigurationDTOResponse) jaxbUnmarshaller.unmarshal(hdfs_site_2);
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        Mockito.when(restTemplate.getForObject("http://hdfs-test-1.marathon.mesos:8443/v1/config/hdfs-site.xml", ConfigurationDTOResponse.class))
                .thenReturn(hdfs_site_1_response);

        Mockito.when(restTemplate.getForObject("http://hdfs-test-2.marathon.mesos:8443/v1/config/hdfs-site.xml", ConfigurationDTOResponse.class))
                .thenReturn(hdfs_site_2_response);
    }

    protected MockMvc getMockMvc() {
        return this.mockMvc;
    }


}
