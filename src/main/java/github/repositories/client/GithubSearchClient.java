package github.repositories.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import github.repositories.dto.RepositoryItem;
import github.repositories.dto.RepositoryItemsList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GithubSearchClient {

  private static final String GITHUB_API_HOST = "https://api.github.com";
  private static final String RESOURCE_PATH = "/search/repositories";
  private static final String SEARCH_TERM = "?sort=stars&order=desc&q=stars:>=40000";
  private static final String ITEMS_SIZE = "&per_page={itemsSize}";

  private final RestTemplate restTemplate;

  public GithubSearchClient(RestTemplateBuilder restTemplateBuilder) {

    ObjectMapper mapper = new ObjectMapper()
        .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);

    this.restTemplate = restTemplateBuilder
        .messageConverters(new MappingJackson2HttpMessageConverter(mapper)).build();

  }

  public List<RepositoryItem> getRepositoriesFromGithub(int itemsSize) {

    String requestUri =
        GITHUB_API_HOST + RESOURCE_PATH + SEARCH_TERM + ITEMS_SIZE ;

    Map<String, String> urlParameters = new HashMap<>();
    urlParameters.put("itemsSize", Long.toString(itemsSize));

    ResponseEntity<RepositoryItemsList> response = restTemplate
        .getForEntity(requestUri, RepositoryItemsList.class, urlParameters);

    return Objects.requireNonNull(response.getBody()).getItems();
  }

}
