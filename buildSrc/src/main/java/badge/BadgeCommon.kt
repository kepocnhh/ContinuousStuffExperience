package badge

private const val BASE_URL = "https://img.shields.io/badge"
private const val DEFAULT_STYLE = "flat"

fun createBadgeUrl(urlTitle: String, urlValue: String, colorValue: String): String {
    return "$BASE_URL/$urlTitle-$urlValue-$colorValue.svg?style=$DEFAULT_STYLE"
}

fun createBadgeUrl(value: String, color: String): String {
    return "$BASE_URL/$value-$color.svg?style=$DEFAULT_STYLE"
}