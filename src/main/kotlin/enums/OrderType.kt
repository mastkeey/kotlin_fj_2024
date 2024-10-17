package enums

enum class OrderType(val type: String) {
    PUBLICATION_DATE("publication_date"),
    FAVORITES_COUNT("favorites_count"),
    COMMENTS_COUNT("comments_count"),
    ID("id"),
    TITLE("title")
}