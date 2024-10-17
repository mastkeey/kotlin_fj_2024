package saver

import model.News
import mu.KotlinLogging
import java.io.File
import java.io.FileWriter
import java.io.IOException

class NewsSaver {
    private val logger = KotlinLogging.logger {}

    fun saveNews(path: String = "news.csv", news: Collection<News>) {
        logger.info { "Start saving news..." }
        val validatedPath = validateAndPreparePath(path) ?: return

        val file = File(validatedPath)

        try {
            val fileExists = file.exists()
            FileWriter(file, true).use { writer ->
                if (!fileExists) {
                    writer.appendLine("id,publication_date,title,slug,place_id,description,site_url,favorites_count,comments_count,rating")
                }

                news.forEach { newsItem ->
                    writer.appendLine(
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
            logger.error { "Error saving news: ${e.message}" }
        } finally {
            logger.info { "Finished saving news..." }
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