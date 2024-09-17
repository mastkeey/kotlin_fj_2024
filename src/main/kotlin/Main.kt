import client.NewsApiClient
import enums.SortDirection
import saver.NewsSaver
import service.NewsService
import java.time.LocalDate


fun main() {
    val client = NewsApiClient();
    val saver = NewsSaver();
    val newsService = NewsService(client, saver);

    val news = newsService.getNewsFromApi(sortDirection = SortDirection.DESC)

    val period = LocalDate.now().minusDays(100)..LocalDate.now()

    val mostRated = newsService.getMostRatedNews(news, 10, period)

    newsService.saveNews("news.csv" ,mostRated)
}