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
public class OwnerControllerTests {
	
	@LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    /**
     * Test get All functionality
     */
    @Test
    public void ownersGetAll() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/owners/", HttpMethod.GET, requestEntity, String.class);
        Type listType = new TypeToken<ArrayList<Owner>>() {
        }.getType();
        List<Owner> owners = new Gson().fromJson(response.getBody(), listType);

        assert (owners != null);
        assertEquals(String.valueOf(10), String.valueOf(owners.size()), " owners Get All different size");
        assertThat(response.getBody()).contains("{\"afm\":0,\"phone\":\"4200617621\",\"fname\":\"Sinus\",\"lname\":\"Lebastian\"}");
    }

    /**
     * Test get by id when id exist and when it doesn't
     */
    @Test
    public void ownersGetId() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        Type listType = new TypeToken<ArrayList<Owner>>() {
        }.getType();

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/owners/2", HttpMethod.GET, requestEntity, String.class);
        List<Owner> owners = new Gson().fromJson(response.getBody(), listType);

        assert (owners != null);
        assertEquals(String.valueOf(1), String.valueOf(owners.size()), "owners Get By id different size");
        assertThat(response.getBody()).isEqualTo("[{\"afm\":2,\"phone\":\"7330515533\",\"fname\":\"Anisha\",\"lname\":\"Macdonald\"}]");


        response = restTemplate.exchange("http://localhost:" + port + "/owners/-1", HttpMethod.GET, requestEntity, String.class);
        owners = new Gson().fromJson(response.getBody(), listType);

        assert (owners != null);
        assertEquals(String.valueOf(0), String.valueOf(owners.size()), "owners Get By id different size");
        assertThat(response.getBody()).isEqualTo("[]");
    }

    /**
     * Test Add item And delete
     */
    @Test
    public void ownersAddItem() {
        Owner oldOwner = new Owner(10, "Stefanos", "Tsitsipas", "6942069420");
        HttpEntity<Owner> requestEntity = new HttpEntity<>(oldOwner);

        // Check with item that doesn't exist in db
        ResponseEntity<Owner> owner = restTemplate.exchange("http://localhost:" + port + "/owners/", HttpMethod.POST, requestEntity, Owner.class);
        assert (owner != null);
        assertEquals(HttpStatus.OK, owner.getStatusCode(), "Different Status Code");
        assertEquals(oldOwner.getAFM(), Objects.requireNonNull(owner.getBody()).getAFM(), "Different AFM");


        // delete new entry
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<Void> requestEntityHeader = new HttpEntity<>(headers);
        ResponseEntity<Void> response = restTemplate.exchange("http://localhost:" + port + "/owners/10", HttpMethod.DELETE, requestEntityHeader, Void.class);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Delete Different Status Code");

        // With id that exist in db
        oldOwner = new Owner(4, "Dimitris", "Konstantinidis", "1542593863");
        requestEntity = new HttpEntity<>(oldOwner);
        owner = restTemplate.exchange("http://localhost:" + port + "/owners/", HttpMethod.POST, requestEntity, Owner.class);
        assert (owner != null);
        assertEquals(HttpStatus.BAD_REQUEST, owner.getStatusCode(), "Different Status Code");
        assertEquals(oldOwner.getAFM(), Objects.requireNonNull(owner.getBody()).getAFM(), "Different AFM");
    }

    /**
     * Test search functionality
     */
    @Test
    public void ownersSearch() {
        Owner oldOwner = new Owner(null, null, "Lees", null);


        HttpEntity<Owner> requestEntity = new HttpEntity<>(oldOwner);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/owners/search", HttpMethod.POST, requestEntity, String.class);
        Type listType = new TypeToken<ArrayList<Owner>>() {
        }.getType();
        List<Owner> owners = new Gson().fromJson(response.getBody(), listType);

        assert (owners != null);
        assertEquals(String.valueOf(1), String.valueOf(owners.size()), "Custom search different size");
        assertThat(response.getBody()).contains("{\"afm\":6,\"phone\":\"7360915355\",\"fname\":\"Eboni\",\"lname\":\"Lees\"}");
    }

    /**
     * Test update functionality
     */
    @Test
    public void ownersUpdate() throws URISyntaxException, IOException, InterruptedException {
        Owner oldOwner = new Owner(69420,"Pranav","Farley","6942069420");
        String ownerStr = new Gson().toJson(oldOwner);

        HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();
        java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                .uri(new URI("http://localhost:" + port + "/owners/1"))
                .method("PATCH", java.net.http.HttpRequest.BodyPublishers.ofString(ownerStr))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Update Status Code");

        oldOwner = new Owner(1,"Pranav","Farley","7830228455");
        ownerStr = new Gson().toJson(oldOwner);


        request = java.net.http.HttpRequest.newBuilder()
                .uri(new URI("http://localhost:" + port + "/owners/69420"))
                .method("PATCH", java.net.http.HttpRequest.BodyPublishers.ofString(ownerStr))
                .header("Content-Type", "application/json")
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());
        assertEquals(200, response.statusCode(), "Update Status Code");
    }

}
