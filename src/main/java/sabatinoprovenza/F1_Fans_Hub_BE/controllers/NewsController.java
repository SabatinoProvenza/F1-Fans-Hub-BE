package sabatinoprovenza.F1_Fans_Hub_BE.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sabatinoprovenza.F1_Fans_Hub_BE.dto.ArticleResponse;
import sabatinoprovenza.F1_Fans_Hub_BE.services.NewsService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class NewsController {

    private final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping("/news")
    public List<ArticleResponse> getNews() throws Exception {
        return newsService.getNews();
    }
}