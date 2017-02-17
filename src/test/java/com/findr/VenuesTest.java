package com.findr;

import com.findr.controller.VenuesController;
import com.findr.model.Response;
import com.findr.model.Venue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by harshsetia on 17/02/2017.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(VenuesController.class)
public class VenuesTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void testVenuesControllerSetup() throws Exception {
        this.mvc.perform(get("/venues/someLocation").accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }


}
