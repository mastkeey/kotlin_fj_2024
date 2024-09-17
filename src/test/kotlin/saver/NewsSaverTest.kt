package saver

import model.News
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.io.IOException
import java.nio.file.Path
import java.time.LocalDate

class NewsSaverTest {

    @TempDir
    lateinit var tempDir: Path

    private lateinit var newsSaver: NewsSaver

    @BeforeEach
    fun setUp() {
        newsSaver = NewsSaver()
    }

    @Test
    fun `saveNews should create CSV file with correct content`() {
        val newsList = listOf(
            News(
                id = 1,
                publicationDate = LocalDate.of(2023, 1, 1),
                title = "Test Title",
                slug = "test-slug",
                description = "Test Description",
                siteUrl = "https://example.com",
                favoritesCount = 0,
                commentsCount = 0
            )
        )
        val filePath = tempDir.resolve("test.csv").toString()

        newsSaver.saveNews(filePath, newsList)

        val savedFile = File(filePath)
        assertTrue(savedFile.exists())
        val expectedContent = """
            id,publication_date,title,slug,place_id,description,site_url,favorites_count,comments_count,rating
            1,2023-01-01,Test Title,test-slug,null,Test Description,https://example.com,0,0,0.5
        """.trimIndent()

        assertEquals(expectedContent, savedFile.readText().trim())
    }

    @Test
    fun `saveNews should not overwrite existing file`() {
        val filePath = tempDir.resolve("existing.csv").toString()
        File(filePath).createNewFile() // Создаем файл заранее

        val newsList = listOf<News>()

        newsSaver.saveNews(filePath, newsList)

        assertTrue(File(filePath).exists())
    }
}
