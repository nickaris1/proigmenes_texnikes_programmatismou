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
public class SaleControllerTests {
	

	@LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    /**
     * Test get All functionality
     */
    @Test
    public void salesGetAll() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/sales/", HttpMethod.GET, requestEntity, String.class);
        Type listType = new TypeToken<ArrayList<Sale>>() {
        }.getType();
        List<Sale> sales = new Gson().fromJson(response.getBody(), listType);

        assert (sales != null);
        assertEquals(String.valueOf(10), String.valueOf(sales.size()), " sales Get All different size");
        assertThat(response.getBody()).contains("{\"saleID\":1,\"date\":null,\"price\":\"125000.0\",\"tm\":\"104.0\",\"rental\":false,\"warranty\":null,\"startDate\":null,\"endDate\":null,\"propId\":1}");
    }

    /**
     * Test get by id when id exist and when it doesn't
     */
    @Test
    public void salesGetId() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        Type listType = new TypeToken<ArrayList<Sale>>() {
        }.getType();

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/sales/1", HttpMethod.GET, requestEntity, String.class);
        List<Sale> sales = new Gson().fromJson(response.getBody(), listType);

        assert (sales != null);
        assertEquals(String.valueOf(1), String.valueOf(sales.size()), "sales Get By id different size");
        assertThat(response.getBody()).isEqualTo("[{\"saleID\":1,\"date\":null,\"price\":\"125000.0\",\"tm\":\"104.0\",\"rental\":false,\"warranty\":null,\"startDate\":null,\"endDate\":null,\"propId\":1}]");


        response = restTemplate.exchange("http://localhost:" + port + "/sales/-1", HttpMethod.GET, requestEntity, String.class);
        sales = new Gson().fromJson(response.getBody(), listType);

        assert (sales != null);
        assertEquals(String.valueOf(0), String.valueOf(sales.size()), "sales Get By id different size");
        assertThat(response.getBody()).isEqualTo("[]");
    }

    /**
     * Test Add item And delete
     */
    @Test
    public void salesAddItem() {
        Sale oldSale = new Sale(11, "2021-03-23", "50000.0", "150.0", false, null, null, null, 4);
        HttpEntity<Sale> requestEntity = new HttpEntity<>(oldSale);

        // Check with item that doesn't exist in db
        ResponseEntity<Sale> sale = restTemplate.exchange("http://localhost:" + port + "/sales/", HttpMethod.POST, requestEntity, Sale.class);
        assert (sale != null);
        assertEquals(HttpStatus.OK, sale.getStatusCode(), "Different Status Code");
        assertEquals(oldSale.getSaleID(), Objects.requireNonNull(sale.getBody()).getSaleID(), "Different SaleID");


        // delete new entry
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<Void> requestEntityHeader = new HttpEntity<>(headers);
        ResponseEntity<Void> response = restTemplate.exchange("http://localhost:" + port + "/sales/11", HttpMethod.DELETE, requestEntityHeader, Void.class);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Delete Different Status Code");

        // With id that exist in db
        oldSale = new Sale(3, "2021-03-23", "50000.0", "150.0", false, null, null, null, 4);
        requestEntity = new HttpEntity<>(oldSale);
        sale = restTemplate.exchange("http://localhost:" + port + "/sales/", HttpMethod.POST, requestEntity, Sale.class);
        assert (sale != null);
        assertEquals(HttpStatus.BAD_REQUEST, sale.getStatusCode(), "Different Status Code");
        assertEquals(oldSale.getSaleID(), Objects.requireNonNull(sale.getBody()).getSaleID(), "Different SaleID");
    }

    /**
     * Test search functionality
     */
    @Test
    public void salesSearch() {
        Sale oldSale = new Sale(null, null, null, null, null, null, null, null, 7);


        HttpEntity<Sale> requestEntity = new HttpEntity<>(oldSale);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/sales/search", HttpMethod.POST, requestEntity, String.class);
        Type listType = new TypeToken<ArrayList<Sale>>() {
        }.getType();
        List<Sale> sales = new Gson().fromJson(response.getBody(), listType);

        assert (sales != null);
        assertEquals(String.valueOf(2), String.valueOf(sales.size()), "Custom search different size");
        assertThat(response.getBody()).contains("{\"saleID\":7,\"date\":\"2021-02-05\",\"price\":\"700.0\",\"tm\":\"60.0\",\"rental\":false,\"warranty\":700,\"startDate\":\"2021-02-05\",\"endDate\":\"2022-02-05\",\"propId\":7}");
    }

    /**
     * Test update functionality
     */
    @Test
    public void salesUpdate() throws URISyntaxException, IOException, InterruptedException {
        Sale oldSale = new Sale(69420, "2021-03-23", "130000.0", "467.0", false, null, null, null, 3);
        String saleStr = new Gson().toJson(oldSale);

        HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();
        java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                .uri(new URI("http://localhost:" + port + "/sales/3"))
                .method("PATCH", java.net.http.HttpRequest.BodyPublishers.ofString(saleStr))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Update Status Code");

        oldSale = new Sale(3, "2021-03-23", "117000.0", "467.0", false, null, null, null, 3);
        saleStr = new Gson().toJson(oldSale);


        request = java.net.http.HttpRequest.newBuilder()
                .uri(new URI("http://localhost:" + port + "/sales/69420"))
                .method("PATCH", java.net.http.HttpRequest.BodyPublishers.ofString(saleStr))
                .header("Content-Type", "application/json")
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());
        assertEquals(200, response.statusCode(), "Update Status Code");
    }


}
