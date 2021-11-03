package github.repositories.controller;

import github.repositories.dto.RepositoryItem;
import github.repositories.service.GithubService;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/repositories")
@Validated
public class GithubController {

  private final GithubService githubService;

  public GithubController(GithubService githubService) {
    this.githubService = githubService;
  }

  @GetMapping
  public Map<String, List<RepositoryItem>> getRepositories(
      @RequestParam(name = "itemsSize")
      @Min(value = 1, message = "item size has to be more than or equal to 1")
      @Max(value = 100, message = "item size has to be less than or equal to 100") Integer itemsSize) {
    return githubService.getRepositories(itemsSize);
  }

}
