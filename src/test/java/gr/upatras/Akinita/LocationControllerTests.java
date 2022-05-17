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
public class LocationControllerTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    /**
     * Test get All functionality
     */
    @Test
    public void locationsGetAll() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/locations/", HttpMethod.GET, requestEntity, String.class);
        Type listType = new TypeToken<ArrayList<Location>>() {
        }.getType();
        List<Location> locations = new Gson().fromJson(response.getBody(), listType);

        assert (locations != null);
        assertEquals(String.valueOf(11), String.valueOf(locations.size()), " locations Get All different size");
        assertThat(response.getBody()).contains("{\"city\":\"Αθήνα\",\"area\":\"ΜΑΡΟΥΣΙ\",\"county\":\"Ελλάδα\",\"postCode\":15125}");
    }

    /**
     * Test get by id when id exist and when it doesn't
     */
    @Test
    public void locationsGetId() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        Type listType = new TypeToken<ArrayList<Location>>() {
        }.getType();

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/locations/15125", HttpMethod.GET, requestEntity, String.class);
        List<Location> locations = new Gson().fromJson(response.getBody(), listType);

        assert (locations != null);
        assertEquals(String.valueOf(1), String.valueOf(locations.size()), "locations Get By id different size");
        assertThat(response.getBody()).isEqualTo("[{\"city\":\"Αθήνα\",\"area\":\"ΜΑΡΟΥΣΙ\",\"county\":\"Ελλάδα\",\"postCode\":15125}]");


        response = restTemplate.exchange("http://localhost:" + port + "/locations/-1", HttpMethod.GET, requestEntity, String.class);
        locations = new Gson().fromJson(response.getBody(), listType);

        assert (locations != null);
        assertEquals(String.valueOf(0), String.valueOf(locations.size()), "locations Get By id different size");
        assertThat(response.getBody()).isEqualTo("[]");
    }

    /**
     * Test Add item And delete
     */
    @Test
    public void locationsAddItem() {
        Location oldLoc = new Location("city", "area", "county", 69420);
        HttpEntity<Location> requestEntity = new HttpEntity<>(oldLoc);

        // Check with item that doesn't exist in db
        ResponseEntity<Location> location = restTemplate.exchange("http://localhost:" + port + "/locations/", HttpMethod.POST, requestEntity, Location.class);
        assert (location != null);
        assertEquals(HttpStatus.OK, location.getStatusCode(), "Different Status Code");
        assertEquals(oldLoc.getPostCode(), Objects.requireNonNull(location.getBody()).getPostCode(), "Different Post Code");


        // delete new entry
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<Void> requestEntityHeader = new HttpEntity<>(headers);
        ResponseEntity<Void> response = restTemplate.exchange("http://localhost:" + port + "/locations/69420", HttpMethod.DELETE, requestEntityHeader, Void.class);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Delete Different Status Code");

        // With id that exist in db
        oldLoc = new Location("city", "area", "county", 15125);
        requestEntity = new HttpEntity<>(oldLoc);
        location = restTemplate.exchange("http://localhost:" + port + "/locations/", HttpMethod.POST, requestEntity, Location.class);
        assert (location != null);
        assertEquals(HttpStatus.BAD_REQUEST, location.getStatusCode(), "Different Status Code");
        assertEquals(oldLoc.getPostCode(), Objects.requireNonNull(location.getBody()).getPostCode(), "Different Post Code");
    }

    /**
     * Test search functionality
     */
    @Test
    public void locationsSearch() {
        Location oldLoc = new Location("Αθήνα", null, null, null);


        HttpEntity<Location> requestEntity = new HttpEntity<>(oldLoc);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/locations/search", HttpMethod.POST, requestEntity, String.class);
        Type listType = new TypeToken<ArrayList<Location>>() {
        }.getType();
        List<Location> locations = new Gson().fromJson(response.getBody(), listType);

        assert (locations != null);
        assertEquals(String.valueOf(5), String.valueOf(locations.size()), "Custom search different size");
        assertThat(response.getBody()).contains("{\"city\":\"Αθήνα\",\"area\":\"ΜΑΡΟΥΣΙ\",\"county\":\"Ελλάδα\",\"postCode\":15125}");
    }

    /**
     * Test update functionality
     */
    @Test
    public void locationsUpdate() throws URISyntaxException, IOException, InterruptedException {
        Location oldLoc = new Location("Αθήνα", "ΜΑΡΟΥΣΙ", "county", 69420);
        String locStr = new Gson().toJson(oldLoc);

        HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();
        java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                .uri(new URI("http://localhost:" + port + "/locations/15125"))
                .method("PATCH", java.net.http.HttpRequest.BodyPublishers.ofString(locStr))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Update Status Code");

        oldLoc = new Location("Αθήνα", "ΜΑΡΟΥΣΙ", "county", 15125);
        locStr = new Gson().toJson(oldLoc);


        request = java.net.http.HttpRequest.newBuilder()
                .uri(new URI("http://localhost:" + port + "/locations/69420"))
                .method("PATCH", java.net.http.HttpRequest.BodyPublishers.ofString(locStr))
                .header("Content-Type", "application/json")
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());
        assertEquals(200, response.statusCode(), "Update Status Code");
    }
}