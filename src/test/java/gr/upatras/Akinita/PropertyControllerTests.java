package gr.upatras.Akinita;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import org.springframework.http.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PropertyControllerTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    /**
     * Test get All functionality
     */
    @Test
    public void propertiesGetAll() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/properties/", HttpMethod.GET, requestEntity, String.class);
        Type listType = new TypeToken<ArrayList<Property>>() {
        }.getType();
        List<Property> properties = new Gson().fromJson(response.getBody(), listType);

        assert (properties != null);
        assertEquals(String.valueOf(15), String.valueOf(properties.size()), " properties Get All different size");
        assertThat(response.getBody()).contains("{\"id\":0,\"listedPrice\":125000,\"tm\":104,\"type\":\"ΔΙΑΜΕΡΙΣΜΑ\",\"road\":\"ΜΑΡΚΟΥ ΜΠΟΤΣΑΡΗ\",\"addressNum\":23,\"floor\":5,\"availability\":false,\"ownerAfm\":8,\"areaCode\":17562}");
    }

    /**
     * Test get by id when id exist and when it doesn't
     */
    @Test
    public void propertiesGetId() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        Type listType = new TypeToken<ArrayList<Property>>() {
        }.getType();

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/properties/0", HttpMethod.GET, requestEntity, String.class);
        List<Property> properties = new Gson().fromJson(response.getBody(), listType);

        assert (properties != null);
        assertEquals(String.valueOf(1), String.valueOf(properties.size()), "properties Get By id different size");
        assertThat(response.getBody()).isEqualTo("[{\"id\":0,\"listedPrice\":125000,\"tm\":104,\"type\":\"ΔΙΑΜΕΡΙΣΜΑ\",\"road\":\"ΜΑΡΚΟΥ ΜΠΟΤΣΑΡΗ\",\"addressNum\":23,\"floor\":5,\"availability\":false,\"ownerAfm\":8,\"areaCode\":17562}]");


        response = restTemplate.exchange("http://localhost:" + port + "/properties/-1", HttpMethod.GET, requestEntity, String.class);
        properties = new Gson().fromJson(response.getBody(), listType);

        assert (properties != null);
        assertEquals(String.valueOf(0), String.valueOf(properties.size()), "properties Get By id different size");
        assertThat(response.getBody()).isEqualTo("[]");
    }

    /**
     * Test Add item And delete
     */
    @Test
    public void propertiesAddItem() {
        Property oldProp = new Property(38, 1555, 23, "home", "thisisfine:)", 15, 4, true, 1, 15125);
        HttpEntity<Property> requestEntity = new HttpEntity<>(oldProp);

        // Check with item that doesn't exist in db
        ResponseEntity<Property> property = restTemplate.exchange("http://localhost:" + port + "/properties/", HttpMethod.POST, requestEntity, Property.class);
        assert (property != null);
        assertEquals(HttpStatus.OK, property.getStatusCode(), "Different Status Code");
        assertEquals(oldProp.getId(), Objects.requireNonNull(property.getBody()).getId(), "Different id");


        // delete new entry
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<Void> requestEntityHeader = new HttpEntity<>(headers);
        ResponseEntity<Void> response = restTemplate.exchange("http://localhost:" + port + "/properties/38", HttpMethod.DELETE, requestEntityHeader, Void.class);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Delete Different Status Code");

        // With id that exist in db
        oldProp = new Property(1, 1555, 23, "home", "thisisfine:)", 15, 4, true, 1, 15125);
        requestEntity = new HttpEntity<>(oldProp);
        property = restTemplate.exchange("http://localhost:" + port + "/properties/", HttpMethod.POST, requestEntity, Property.class);
        assert (property != null);
        assertEquals(HttpStatus.BAD_REQUEST, property.getStatusCode(), "Different Status Code");
        assertEquals(oldProp.getId(), Objects.requireNonNull(property.getBody()).getId(), "Different id");
    }

    /**
     * Test search functionality
     */
    @Test
    public void propertiesSearch() {
        Property oldProp = new Property(null,null, null, null,"ΑΝΔΡΟΥ",null,null,null,null,null);


        HttpEntity<Property> requestEntity = new HttpEntity<>(oldProp);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/properties/search", HttpMethod.POST, requestEntity, String.class);
        Type listType = new TypeToken<ArrayList<Property>>() {
        }.getType();
        List<Property> properties = new Gson().fromJson(response.getBody(), listType);

        assert (properties != null);
        assertEquals(String.valueOf(1), String.valueOf(properties.size()), "Custom search different size");
        assertThat(response.getBody()).contains("{\"id\":1,\"listedPrice\":103000,\"tm\":81,\"type\":\"ΔΙΑΜΕΡΙΣΜΑ\",\"road\":\"ΑΝΔΡΟΥ\",\"addressNum\":7,\"floor\":2,\"availability\":false,\"ownerAfm\":4,\"areaCode\":54351}");
    }

    /**
     * Test update functionality
     */
    @Test
    public void propertiesUpdate() throws URISyntaxException, IOException, InterruptedException {
        Property oldProp = new Property(69420, null, null, null, null, null, null, null, null,null);
        String locStr = new Gson().toJson(oldProp);

        HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();
        java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                .uri(new URI("http://localhost:" + port + "/properties/1"))
                .method("PATCH", java.net.http.HttpRequest.BodyPublishers.ofString(locStr))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Update Status Code");

        oldProp = new Property(1, null, null, null, null, null, null, null, null,null);
        locStr = new Gson().toJson(oldProp);


        request = java.net.http.HttpRequest.newBuilder()
                .uri(new URI("http://localhost:" + port + "/properties/69420"))
                .method("PATCH", java.net.http.HttpRequest.BodyPublishers.ofString(locStr))
                .header("Content-Type", "application/json")
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());
        assertEquals(200, response.statusCode(), "Update Status Code");
    }
}