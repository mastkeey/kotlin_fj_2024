package saver

import model.News
import mu.KotlinLogging
import java.io.File
import java.io.IOException

class NewsSaver {
    private val logger = KotlinLogging.logger {}
    fun saveNews(path: String = "news.csv", news: Collection<News>) {
        logger.info { "start to save news..." }
        val validatedPath = validateAndPreparePath(path) ?: return

        val file = File(validatedPath)


        if (file.exists()) {
            logger.error { "File $validatedPath already exists." }
            return
        }

        try {
            file.printWriter().use { out ->
                out.println("id,publication_date,title,slug,place_id,description,site_url,favorites_count,comments_count,rating")

                news.forEach { newsItem ->
                    out.println(
                                "${newsItem.id}," +
                                "${newsItem.publicationDate}," +
                                "${newsItem.title}," +
                                "${newsItem.slug}," +
                                "${newsItem.place?.id}," +
                                "${newsItem.description}," +
                                "${newsItem.siteUrl}," +
                                "${newsItem.favoritesCount}," +
                                "${newsItem.commentsCount}," +
                                "${newsItem.rating}"
                    )
                }
            }
        } catch (e: IOException) {
            logger.error { "Error saving news: " + e.printStackTrace() }
        } finally {
            logger.info { "finish saving news..." }
        }
    }

    private fun validateAndPreparePath(path: String): String? {
        if (path.isBlank()) {
            logger.error { "Invalid file path: $path" }
            return null
        }

        return if (path.endsWith(".csv", ignoreCase = true)) {
            path
        } else {
            "$path.csv"
        }
    }
}