package worker

import client.KtorNewsApiClient
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import model.News
import mu.KotlinLogging

val logger = KotlinLogging.logger {}

suspend fun worker(
    newsClient: KtorNewsApiClient,
    channel: Channel<List<News>>,
    numberOfPages: Int,
) {
    val news = newsClient.getNews(pageNumber = numberOfPages)
    if (news.isNotEmpty()) channel.send(news)
}

fun createWorkers(
    newsClient: KtorNewsApiClient,
    channel: Channel<List<News>>,
    countOfThreads: Int,
    maxPages: Int,
) = (1..countOfThreads).map { i ->
    kotlinx.coroutines.GlobalScope.launch {
        try {
            for (page in 1..maxPages) {
                if (page % countOfThreads == i - 1) {
                    worker(newsClient = newsClient, channel = channel, numberOfPages = page)
                }
            }
        } catch (e: Exception) {
            logger.error("Error: ${e.message}")
        }
    }
}