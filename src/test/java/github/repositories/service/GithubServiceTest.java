package github.repositories.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import github.repositories.client.GithubSearchClient;
import github.repositories.dto.License;
import github.repositories.dto.RepositoryItem;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class GithubServiceTest {

  @InjectMocks
  GithubService githubService;

  @Mock
  GithubSearchClient githubSearchClient = mock(GithubSearchClient.class);

  @Test
  public void getRepositoriesTest() {

    RepositoryItem item1 = mockRepositoryItem("name1", "description1", "url",
        "license1", 1);
    RepositoryItem item2 = mockRepositoryItem("name2", "description2", "url",
        "license1", 2);
    RepositoryItem item3 = mockRepositoryItem("name3", "description3", "url",
        "license1", 3);
    RepositoryItem item4 = mockRepositoryItem("name4", "description4", "url",
        "license1", 4);

    when(githubSearchClient.getRepositoriesFromGithub(4))
        .thenReturn(List.of(item1, item2, item3, item4));

    Map<String, List<RepositoryItem>> repositories = githubService.getRepositories(4);

    assertEquals(repositories.get("license1").size(), 4);
    assertEquals(repositories.get("license1").get(0).getName(), "name4");
  }

  @Test
  public void getRepositoriesTest2() {

    RepositoryItem item1 = mockRepositoryItem("name1", "description1", "url1",
        "license1", 7);
    RepositoryItem item2 = mockRepositoryItem("name2", "description2", "url2",
        "license1", 2);
    RepositoryItem item3 = mockRepositoryItem("name3", "description3", "url3",
        "license1", 3);
    RepositoryItem item4 = mockRepositoryItem("name4", "description4", "url4",
        "license2", 2);
    RepositoryItem item5 = mockRepositoryItem("name5", "description5", "url5",
        "license2", 5);
    RepositoryItem item6 = mockRepositoryItem("name6", "description6", "url6",
        "license2", 1);

    when(githubSearchClient.getRepositoriesFromGithub(4))
        .thenReturn(List.of(item1, item2, item3, item4, item5, item6));

    Map<String, List<RepositoryItem>> repositories = githubService.getRepositories(4);

    assertEquals(repositories.get("license1").size(), 3);
    assertEquals(repositories.get("license1").get(0).getName(), "name1");

    assertEquals(repositories.get("license2").size(), 3);
    assertEquals(repositories.get("license2").get(0).getName(), "name5");
  }


  private RepositoryItem mockRepositoryItem(String name, String description, String url,
      String licenseName, Integer starCount) {
    return RepositoryItem.builder()
        .name(name)
        .description(description)
        .url(url)
        .stargazersCount(starCount)
        .license(License.builder().name(licenseName).build())
        .build();
  }

}
