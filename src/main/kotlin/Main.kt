import client.KtorNewsApiClient
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import model.News
import mu.KotlinLogging
import saver.NewsSaver
import worker.createWorkers

suspend fun sequentialExecution(newsClient: KtorNewsApiClient, numberOfPages: Int, saver: NewsSaver, savePath: String): Long {
    val logger = KotlinLogging.logger {}
    logger.info { "starting sequential execution" }
    val startTime = System.currentTimeMillis()
    val allNews = mutableListOf<News>()

    for (page in 1..numberOfPages) {
        val newsList = newsClient.getNews(page)
        allNews.addAll(newsList)
    }

    saver.saveNews(savePath, allNews)

    val endTime = System.currentTimeMillis()
    logger.info { "finished sequential execution after ${endTime - startTime} ms" }
    return endTime - startTime
}

fun main() = runBlocking {
    val newsClient = KtorNewsApiClient()
    val saver = NewsSaver()
    val logger = KotlinLogging.logger {}

    val countOfThreads = 6
    val maxPages = 20
    val maxConcurrentRequests = 5
    val channel = Channel<List<News>>()

    val startTime = System.currentTimeMillis()

    val workers = createWorkers(newsClient, channel, countOfThreads, maxPages)

    val readerJob = GlobalScope.launch {
        while (!channel.isClosedForReceive) {
            val newsBatch = channel.receiveCatching().getOrNull()
            if (newsBatch != null) {
                saver.saveNews("src/main/resources/save.csv", newsBatch)
            }
        }
    }

    workers.forEach { it.join() }
    channel.close()
    readerJob.join()

    val endTime = System.currentTimeMillis()
    logger.info("Finished all threads in ${endTime - startTime} ms")
}