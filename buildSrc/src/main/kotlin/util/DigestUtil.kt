package util

import java.security.MessageDigest
import java.util.Locale

private const val HEX_CHARS = "0123456789ABCDEF"

enum class DigestType(val value: String) {
    SHA_512("SHA-512")
}

private val messageDigestSha512 = MessageDigest.getInstance(DigestType.SHA_512.value)!!

private fun ByteArray.toHexString(): String {
    val builder = StringBuilder(size * 2)
    forEach {
        val i = it.toInt()
        builder.append(HEX_CHARS[i shr 4 and 0x0f]).append(HEX_CHARS[i and 0x0f])
    }
    return builder.toString().toLowerCase(Locale.US)
}

fun String.digest(type: DigestType): String {
    val bytes = when (type) {
        DigestType.SHA_512 -> messageDigestSha512.digest(toByteArray())
    }
    return bytes.toHexString()
}

fun String.digestSha512() = messageDigestSha512.digest(toByteArray()).toHexString()
