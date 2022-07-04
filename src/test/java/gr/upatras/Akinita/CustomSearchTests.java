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
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomSearchTests {
	@LocalServerPort
		private int port;

	@Autowired
		private TestRestTemplate restTemplate;
	
	@Test
	public void customSearch() throws URISyntaxException, IOException, InterruptedException{
		
		String srStr = "{\"property\":  {\"tm\":  90},\"location\": {\"postCode\":  15127},\"modifiers\":  {\"propertyTm\":\">\"}}";
		
		HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();
        java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                .uri(new URI("http://localhost:" + port + "/search/"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(srStr))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertThat(response.body()).contains("\"property\":{\"id\":7,\"listedPrice\":730,\"tm\":90,\"type\":\"ΔΙΑΜΕΡΙΣΜΑ\",\"road\":\"ΔΙΑΓΟΡΑ\",\"addressNum\":6,\"floor\":2,\"availability\":false,\"ownerAfm\":9,\"areaCode\":15127}");
        
	}
	 
}
