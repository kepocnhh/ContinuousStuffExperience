import java.security.MessageDigest
import java.util.*

private enum class DigestType(val messageDigestValue: String) {
    SHA_512("SHA-512")
}
private val HEX_CHARS = "0123456789ABCDEF"
private val messageDigestSha512 = MessageDigest.getInstance(DigestType.SHA_512.messageDigestValue)!!

fun toHexString(array: ByteArray): String {
    val builder = StringBuilder(array.size * 2)
    array.forEach {
        val i = it.toInt()
        builder.append(HEX_CHARS[i shr 4 and 0x0f]).append(HEX_CHARS[i and 0x0f])
    }
    return builder.toString().toLowerCase(Locale.US)
}
fun digest(data: String, type: String): String {
    val bytes = when(DigestType.values().firstOrNull { it.messageDigestValue == type }) {
        DigestType.SHA_512 -> messageDigestSha512.digest(data.toByteArray())
        else -> throw UnsupportedOperationException("Type $type is not supported")
    }
    return toHexString(bytes)
}

extra.apply {
    set("toHexString", ::toHexString)
    set("digest", ::digest)
}