package github.repositories.service;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import github.repositories.client.GithubSearchClient;
import github.repositories.dto.License;
import github.repositories.dto.RepositoryItem;
import github.repositories.dto.RepositoryItemsList;
import java.util.List;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

@RestClientTest(GithubSearchClient.class)
class GithubSearchClientTest {

  @Autowired
  private GithubSearchClient githubSearchClient;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MockRestServiceServer mockRestServiceServer;

  @Before
  public void setUp() {
    objectMapper = new ObjectMapper()
        .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
  }

  @Test
  public void getRepositoriesFromGithub() throws Exception {

    RepositoryItem item1 = mockRepositoryItem("name1", "description1", 1);
    RepositoryItem item2 = mockRepositoryItem("name2", "description2", 2);
    RepositoryItem item3 = mockRepositoryItem("name3", "description3", 3);
    RepositoryItem item4 = mockRepositoryItem("name4", "description4", 4);

    RepositoryItemsList repositoryItemsList = RepositoryItemsList.builder()
        .items(List.of(item1, item2, item3, item4)).build();

    String json = this.objectMapper
        .writeValueAsString(repositoryItemsList);


    this.mockRestServiceServer
        .expect(requestTo("https://api.github.com/search/repositories?sort=stars&order=desc&q=stars:%3E%3D40000&per_page=4"))
        .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

    List<RepositoryItem> result = githubSearchClient.getRepositoriesFromGithub(4);

    Assertions.assertEquals(4, result.size());

  }

  private RepositoryItem mockRepositoryItem(String name, String description, Integer starCount) {
    return RepositoryItem.builder()
        .name(name)
        .description(description)
        .url("url")
        .stargazersCount(starCount)
        .license(License.builder().name("licenseName").build())
        .build();
  }

}
