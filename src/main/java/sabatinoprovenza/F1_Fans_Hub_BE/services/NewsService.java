package sabatinoprovenza.F1_Fans_Hub_BE.services;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.springframework.stereotype.Service;
import sabatinoprovenza.F1_Fans_Hub_BE.dto.ArticleResponse;
import sabatinoprovenza.F1_Fans_Hub_BE.dto.Rss2JsonResponse;
import sabatinoprovenza.F1_Fans_Hub_BE.exceptions.NotFoundException;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Service
public class NewsService {

    private final ObjectMapper mapper = new ObjectMapper();

    public List<ArticleResponse> getNews() {

        try {

            String url = "https://api.rss2json.com/v1/api.json?rss_url=https://it.motorsport.com/rss/f1/news/";

            HttpResponse<String> response = Unirest.get(url).asString();

            if (response.getStatus() != 200) {
                throw new RuntimeException("Errore nella chiamata all'API RSS");
            }

            if (response.getBody() == null) {
                throw new RuntimeException("Risposta vuota dall'API RSS");
            }

            Rss2JsonResponse rss = mapper.readValue(response.getBody(), Rss2JsonResponse.class);

            if (rss.items() == null || rss.items().isEmpty()) {
                throw new RuntimeException("Nessun articolo trovato");
            }

            String source = rss.feed().title().split(" - ")[0];

            return rss.items()
                    .stream()
                    .map(item -> new ArticleResponse(
                            item.guid() != null ? item.guid() : item.link(),
                            item.title(),
                            stripHtml(item.description()),
                            stripHtml(item.content()),
                            extractImage(item),
                            item.link(),
                            item.pubDate(),
                            source
                    ))
                    .toList();

        } catch (Exception e) {

            System.out.println("Errore nel recupero delle news: " + e.getMessage());

            return List.of();
        }
    }

    private String extractImage(Rss2JsonResponse.Item item) {
        if (item.enclosure() != null && item.enclosure().link() != null && !item.enclosure().link().isBlank()) {
            return item.enclosure().link();
        }
        if (item.thumbnail() != null && !item.thumbnail().isBlank()) {
            return item.thumbnail();
        }
        return "";
    }

    private String stripHtml(String html) {
        if (html == null) return "";

        // rimuove HTML
        String text = html.replaceAll("<[^>]*>", "");

        // rimuove "Continua a leggere"
        int index = text.indexOf("Continua a leggere");
        if (index != -1) {
            text = text.substring(0, index);
        }

        text = text.trim();


        return text;
    }

    public ArticleResponse getNewsById(String id) {
        try {
            return getNews().stream()
                    .filter(a -> a.id().equals(id))
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException("Articolo non trovato"));
        } catch (Exception e) {
            throw new NotFoundException("L'articolo non è stato trovato");
        }
    }
}
