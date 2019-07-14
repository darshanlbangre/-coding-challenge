package com.coding.challenge;

import com.coding.challenge.entity.Subscriber;
import com.coding.challenge.repository.HealthRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.assertj.core.api.Assertions;

import java.util.ArrayList;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest()
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:test.application.properties")
public class HealthMonitorServiceApplicationTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private HealthRepository repository;

    @Test
    public void contextLoads() {
    }

    @Test
    public void postSubscriber_thenStatus200()
            throws Exception {
        Subscriber subscriberTest = new Subscriber("John", "john@xyz.com");
        ObjectMapper mapper = new ObjectMapper();
        mvc.perform(MockMvcRequestBuilders.post("/api/service/status/subscribe").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(subscriberTest)));
        List<Subscriber> found = new ArrayList<>();
        repository.findAll().forEach(found::add);
        Assertions.assertThat(found).extracting(Subscriber::getName).contains("John");
    }
}
