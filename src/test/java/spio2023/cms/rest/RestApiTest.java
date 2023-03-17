package spio2023.cms.rest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Random;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RestApiTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void testDatabaseIsLoaded() throws Exception {
        mockMvc.perform(get("/procedures").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.procedures", hasSize(2)));
    }

    @Test
    public void testStepCalibration() throws Exception {
        var calibration = new JSONObject()
                .put("procedure", "/1")
                .toString();
        var requestPostCalibration = post("/calibrations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(calibration);
        mockMvc.perform(requestPostCalibration)
                .andExpect(status().isCreated());

        var measurementSeries = 5;
        for (int i = 0; i < measurementSeries; i++) {
            var instrumentValue = new JSONObject()
                    .put("value", getRandomNumber(-15.05, -14.95))
                    .toString();
            var deviceValue = new JSONObject()
                    .put("value", getRandomNumber(-16, -14))
                    .toString();
            var requestPostInstrumentValue = post("/inputValues")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(instrumentValue);
            var requestPostDeviceValue = post("/inputValues")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(deviceValue);
            mockMvc.perform(requestPostInstrumentValue)
                    .andExpect(status().isCreated());
            mockMvc.perform(requestPostDeviceValue)
                    .andExpect(status().isCreated());
        }

        var input = new JSONObject()
                .put("calibration", "/1")
                .put("step", "/4")
                .put("deviceValues",
                        new JSONArray().put("/1").put("/2").put("/3").put("/4").put("/5"))
                .put("instrumentValues",
                        new JSONArray().put("/6").put("/7").put("/8").put("/9").put("/10"))
                .toString();
        var requestPostInput = post("/inputs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(input);
        mockMvc.perform(requestPostInput)
                .andExpect(status().isCreated());

        mockMvc.perform(get("/results/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private double getRandomNumber(double rangeMin, double rangeMax) {
        return rangeMin + (rangeMax - rangeMin) * new Random().nextDouble();
    }
}
