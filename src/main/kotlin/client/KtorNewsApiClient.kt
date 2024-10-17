package client

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import model.News
import model.NewsResponse
import mu.KotlinLogging
import worker.worker
import java.util.concurrent.Semaphore

class KtorNewsApiClient {
    private val client = HttpClient(CIO)
    private val logger = KotlinLogging.logger {}
    private val semaphore = Semaphore(8)
    suspend fun getNews(pageNumber: Int = 1, count: Int = 100): List<News> {
        if (semaphore.tryAcquire()) {
            try {
                val response: HttpResponse = client.get("https://kudago.com/public-api/v1.4/news") {
                    parameter("page_size", count)
                    parameter("page", pageNumber)
                    parameter(
                        "fields",
                        "publication_date,id,title,slug,place,description,site_url,favorites_count,comments_count"
                    )
                }
                if (!response.headers["Content-Type"]?.contains("application/json")!!) {
                    logger.info { "Skipped non-JSON response" }
                    return emptyList()
                }
                val responseBody = response.bodyAsText()
                return Json { ignoreUnknownKeys = true }.decodeFromString(
                    deserializer = NewsResponse.serializer(),
                    string = responseBody
                ).results ?: emptyList()
            } finally {
                semaphore.release()
            }
        } else {
            worker.logger.warn("API access locked, retrying in 10 seconds...")
            delay(10000)
            return emptyList()
        }
    }
}