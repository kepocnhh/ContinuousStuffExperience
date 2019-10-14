package badge

private const val BASE_URL = "https://img.shields.io"
private const val BASE_URL_BADGE = "$BASE_URL/badge"
private const val BASE_URL_V1 = "$BASE_URL/static/v1"
private const val DEFAULT_STYLE = "flat"
private const val COLOR_GRAY_DARK = "212121"
const val COLOR_RED = "d50000"
const val COLOR_GREEN = "00c853"
const val COLOR_BLUE = "2962ff"
const val COLOR_YELLOW = "00c853"
private const val DEFAULT_COLOR_LABEL = COLOR_GRAY_DARK

fun createBadgeUrl(
    label: String,
    message: String,
    colorLabel: String,
    colorMessage: String
): String {
    return BASE_URL_V1 +
        "?label=" + label +
        "&message=" + message +
        "&labelColor=" + colorLabel +
        "&color=" + colorMessage +
        "&style=" + DEFAULT_STYLE
}

fun createBadgeUrl(
    label: String,
    message: String,
    colorMessage: String
) = createBadgeUrl(
    label = label,
    message = message,
    colorMessage = colorMessage,
    colorLabel = DEFAULT_COLOR_LABEL
)

fun createBadgeUrl(message: String, color: String = DEFAULT_COLOR_LABEL): String {
    return "$BASE_URL_BADGE/$message-$color.svg?style=$DEFAULT_STYLE"
}
