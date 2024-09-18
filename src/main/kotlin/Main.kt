import client.NewsApiClient
import dsl.readme
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

    val result = readme {
        introduction {
            +"This is my first dsl on kotlin!!"
        }
        installation {
            +"1. Installation guide"
            +"2. Installation guide"
        }
        usage {
            +"Some usage info"
        }
        license {
            +"Fake licence:)"
        }
    }

    result.saveToFile("customReadmeFromDSL.md")
}