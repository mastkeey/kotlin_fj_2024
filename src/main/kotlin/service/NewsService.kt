package service

import client.NewsApiClient
import enums.OrderType
import enums.SortDirection
import model.News
import saver.NewsSaver
import java.time.LocalDate

class NewsService(private val client: NewsApiClient, private val saver: NewsSaver) {

    fun getNewsFromApi(
        pageNumber: Int = 1,
        pageSize: Int = 100,
        location: String = "spb",
        sortDirection: SortDirection = SortDirection.ASC,
        orderType: OrderType = OrderType.PUBLICATION_DATE,
    ): List<News> {
        return client.getNews(pageNumber, pageSize, location, sortDirection, orderType)
    }

    fun saveNews(path: String = "news.csv", news: List<News>) {
        saver.saveNews(path, news)
    }

    fun getMostRatedNews(news: List<News>, count: Int = 10, period: ClosedRange<LocalDate>): List<News> {
        return news.mostRatedNews(count, period)
    }

    private fun List<News>.mostRatedNews(count: Int, period: ClosedRange<LocalDate>): List<News> {
        return this.asSequence()
            .filter { news ->
                news.publicationDate!! in period
            }
            .sortedBy {
                it.rating
            }
            .take(count)
            .toList()
    }
}