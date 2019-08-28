package com.stratio.architecture.multihdfs.web;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@Slf4j
public class MultiHDFSControllerTest extends AbstractIT {

    private static final String BASE_URL = "/config";

    @Test
    public void getCoresite() throws Exception {
        MockHttpServletRequestBuilder httpServletRequestBuilder = MockMvcRequestBuilders.request(HttpMethod.GET,
                BASE_URL + "/hdfs-test-1/hdfs-test-2/core-site.xml");

        ResultActions resultActions = getMockMvc().perform(httpServletRequestBuilder);

        MvcResult result = resultActions
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andDo(MockMvcResultHandlers.log())
                .andReturn();

        log.debug("AAAAA", result.getResponse().getContentAsString());
    }

    @Test
    public void getKrb5Conf() throws Exception {
        MockHttpServletRequestBuilder httpServletRequestBuilder = MockMvcRequestBuilders.request(HttpMethod.GET,
                BASE_URL + "/hdfs-test-1/hdfs-test-2/krb5.conf");

        ResultActions resultActions = getMockMvc().perform(httpServletRequestBuilder);

        MvcResult result = resultActions
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andDo(MockMvcResultHandlers.log())
                .andReturn();
    }

    @Test
    public void getHDFSSite() throws Exception {
        MockHttpServletRequestBuilder httpServletRequestBuilder = MockMvcRequestBuilders.request(HttpMethod.GET,
                BASE_URL + "/hdfs-test-1/hdfs-test-2/hdfs-site.xml");

        ResultActions resultActions = getMockMvc().perform(httpServletRequestBuilder);

        MvcResult result = resultActions
                .andExpect(MockMvcResultMatchers.status().
                        is2xxSuccessful())
                .andDo(MockMvcResultHandlers.log())
                .andReturn();
    }

    @Test
    public void getHDFSSiteWithThree() throws Exception {
        MockHttpServletRequestBuilder httpServletRequestBuilder = MockMvcRequestBuilders.request(HttpMethod.GET,
                BASE_URL + "/hdfs-test-1/hdfs-test-2/hdfs-test-3/hdfs-site.xml");

        ResultActions resultActions = getMockMvc().perform(httpServletRequestBuilder);

        MvcResult result = resultActions
                .andExpect(MockMvcResultMatchers.status().
                        is2xxSuccessful())
                .andDo(MockMvcResultHandlers.log())
                .andReturn();
    }


}
