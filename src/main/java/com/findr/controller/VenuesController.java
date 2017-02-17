package com.findr.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.findr.config.MyResponseErrorHandler;
import com.findr.model.Response;
import com.findr.model.Venue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

/**
 * Created by harshsetia on 14/02/2017.
 */
@RestController
@RequestMapping("/venues")
public class VenuesController {

    private RestTemplate template;

    @Value("${resturl}")
    private String REST_URL;

    @Value("${client_id}")
    private String CLIENTID;

    @Value("${client_secret}")
    private String CLIENSECRET;

    @Value("${version}")
    private String VERSION;


    @PostConstruct()
    public void controller() {
        template = new RestTemplate();
        template.setErrorHandler(new MyResponseErrorHandler());
    }


    @RequestMapping(method = RequestMethod.GET, value = "/{location}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody  String getVenuesForLocation(@PathVariable String location){
        String requestURL = getUrl(location);
        HttpEntity entity = getHttpEntity();
        ResponseEntity<String> response = template.exchange(requestURL, HttpMethod.GET, entity,String.class);
        return getJsonResponse(response);

    }

    private String getJsonResponse(ResponseEntity<String> response){

        Response findrResponse = new Response();
        populateResponseMetaData(findrResponse,response);

        if(HttpStatus.OK.equals(HttpStatus.valueOf(findrResponse.getResponseCode()))){

            List<Venue> venues = getVenues(response);

            findrResponse.setVenueList(venues);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        return gson.toJson(findrResponse);
    }

    private List<Venue> getVenues(ResponseEntity<String> response) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode wrapperNode;
        List<Venue> venues = new LinkedList<>();
        try {
            wrapperNode = objectMapper.readValue(response.getBody(), JsonNode.class);
            JsonNode responseNode = wrapperNode.get("response");
            ArrayNode groupsNode = (ArrayNode) responseNode.get("groups");
            JsonNode itemsWrapper = groupsNode.get(0);
            ArrayNode itemsNode = (ArrayNode)itemsWrapper.get("items");
            Iterator<JsonNode> elements = itemsNode.elements();
            while(elements.hasNext()){

                String url = "";
                String address = "";

                JsonNode venueNode = elements.next().get("venue");
                final String name = venueNode.get("name").asText();
                final JsonNode urlNode = venueNode.get("url");
                if(null!=urlNode){
                    url = urlNode.asText();
                }

                final JsonNode locationNode = venueNode.get("location");
                if(null!=locationNode){
                    final ArrayNode addressNode = (ArrayNode)locationNode.get("formattedAddress");
                    Iterator<JsonNode> addressElements = addressNode.elements();
                    StringBuffer buffer = new StringBuffer();
                    while(addressElements.hasNext()){
                        buffer.append(addressElements.next().asText());
                    }
                    address = buffer.toString();
                }

                Venue currentVenue = new Venue();
                currentVenue.setName(name);
                currentVenue.setFullAddress(address);
                currentVenue.setUrl(url);

                venues.add(currentVenue);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return venues;
    }

    private void populateResponseMetaData(Response findrResponse, ResponseEntity<String> fsApiResponse){
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode metaNode;
        try {
            metaNode = objectMapper.readValue(fsApiResponse.getBody(), JsonNode.class).get("meta");
            int statusCode = metaNode.get("code").asInt();
            if(!HttpStatus.OK.equals(HttpStatus.valueOf(statusCode))){
                findrResponse.setResponseMessage(metaNode.get("errorDetail").asText());
            }
            findrResponse.setResponseCode(statusCode);
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    private HttpEntity getHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

        return new HttpEntity(headers);
    }

    private String getUrl(@PathVariable String location) {
        StringBuffer sbf = new StringBuffer(REST_URL).append("?").append("near=").append(location).
                    append("&").append("client_id="+CLIENTID).append("&client_secret="+CLIENSECRET).append("&v="+VERSION);
        return sbf.toString();
    }

}
