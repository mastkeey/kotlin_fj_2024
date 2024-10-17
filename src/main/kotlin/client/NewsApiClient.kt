package client

import enums.OrderType
import enums.SortDirection
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import model.News
import model.NewsResponse
import mu.KotlinLogging
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class NewsApiClient {
    private val logger = KotlinLogging.logger {}
    private val client = OkHttpClient()
    fun getNews(
        pageNumber: Int = 1,
        count: Int = 100,
        location: String = "spb",
        sortDirection: SortDirection = SortDirection.ASC,
        orderType: OrderType = OrderType.PUBLICATION_DATE
    ): List<News> {
        logger.info { "start fetching news..." }

        val url = HttpUrl.Builder()
            .scheme("https")
            .host("kudago.com")
            .addPathSegment("public-api")
            .addPathSegment("v1.4")
            .addPathSegment("news")
            .addQueryParameter("page_size", count.toString())
            .addQueryParameter("page", pageNumber.toString())
            .addQueryParameter("location", location)
            .addQueryParameter("order_by", sortDirection.direction + orderType.type)
            .addQueryParameter("fields", "publication_date,id,title,slug,place,description,site_url,favorites_count,comments_count")
            .build()

        val request = Request.Builder()
            .url(url)
            .build()

        try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) logger.error {"Unexpected code $response"}

                val responseBody = response.body?.string() ?: return emptyList()

                val json = Json { ignoreUnknownKeys = true }
                return json.decodeFromString<NewsResponse>(responseBody).results!!
            }
        } catch (e: IOException) {
            logger.error { "Error while fetching news from $url" }
            return emptyList();
        } finally {
            logger.info { "finish fetching news..." }
        }

    }
}