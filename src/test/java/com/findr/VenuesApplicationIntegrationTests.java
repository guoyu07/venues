package com.findr;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.findr.model.Response;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Assertions.assertThat;
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class VenuesApplicationIntegrationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void testInvalidLocationForVenuesEndPoint() throws Exception{
		String body = this.restTemplate.getForObject("/venues/doesNotExist", String.class);
		ObjectMapper objectMapper = new ObjectMapper();
		Response response = objectMapper.readValue(body, Response.class);
		assertThat(response.getResponseCode()).isEqualTo(400);
	}

	@Test
	public void testValidLocationForVenuesEndPoint() throws Exception{
		String body = this.restTemplate.getForObject("/venues/london", String.class);
		ObjectMapper objectMapper = new ObjectMapper();
		Response response = objectMapper.readValue(body, Response.class);
		assertThat(response.getResponseCode()).isEqualTo(200);
	}

	@Test
	public void testErrorCodeAbsentForValidLocationForVenuesEndPoint() throws Exception{
		String body = this.restTemplate.getForObject("/venues/london", String.class);
		ObjectMapper objectMapper = new ObjectMapper();
		Response response = objectMapper.readValue(body, Response.class);
		assertThat(response.getResponseMessage()).isEqualTo(null);
	}

	@Test
	public void testErrorCodePresentWhenLocationNotFound() throws Exception{
		String body = this.restTemplate.getForObject("/venues/doesNotExist", String.class);
		ObjectMapper objectMapper = new ObjectMapper();
		Response response = objectMapper.readValue(body, Response.class);
		assertThat(response.getResponseMessage()).isEqualTo("Couldn't geocode param near: doesNotExist");
	}

}
