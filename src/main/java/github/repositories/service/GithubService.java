package github.repositories.service;


import static java.util.stream.Collectors.groupingBy;

import github.repositories.client.GithubSearchClient;
import github.repositories.dto.License;
import github.repositories.dto.RepositoryItem;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;


@Service
public class GithubService {

  private final GithubSearchClient githubSearchClient;

  public GithubService(GithubSearchClient githubSearchClient) {
    this.githubSearchClient = githubSearchClient;
  }

  public Map<String, List<RepositoryItem>> getRepositories(int pageSize) {
    List<RepositoryItem> repositoryItems = githubSearchClient.getRepositoriesFromGithub(pageSize);
    repositoryItems.forEach(item -> {
      if (item.getLicense() == null) {
        item.setLicense(License.builder().name("NULL License").build());
      }
    });
    return repositoryItems.stream()
        .sorted(Comparator.comparingLong(RepositoryItem::getStargazersCount).reversed())
        .collect(groupingBy(item -> item.getLicense().getName()));
  }

}
