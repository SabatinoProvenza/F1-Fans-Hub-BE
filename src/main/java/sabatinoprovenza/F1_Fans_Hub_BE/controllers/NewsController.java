package sabatinoprovenza.F1_Fans_Hub_BE.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sabatinoprovenza.F1_Fans_Hub_BE.dto.ArticleResponse;
import sabatinoprovenza.F1_Fans_Hub_BE.services.FavoriteService;
import sabatinoprovenza.F1_Fans_Hub_BE.services.NewsService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class NewsController {

    private final NewsService newsService;
    private final FavoriteService favoriteService;

    public NewsController(NewsService newsService, FavoriteService favoriteService) {
        this.newsService = newsService;
        this.favoriteService = favoriteService;
    }

    @GetMapping("/news")
    public List<ArticleResponse> getNews() {
        return newsService.getNews();
    }

    @GetMapping("/news/{id}")
    public ArticleResponse getNewsDetail(@PathVariable String id) {
        return newsService.getNewsById(id);
    }

}