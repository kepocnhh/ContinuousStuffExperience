package continuous.stuff.experience

import java.util.Locale

import kotlin.reflect.KClass

fun <T: Enum<*>> KClass<T>.find(predicate: (T) -> Boolean) = java.enumConstants.find(predicate)

/**
 * this method is an example of using classes.
 * @see Lib1Class
 * @see OtherLib1Class
 * @see Lib2Class
 * @version 0.00.01
 * @since ContinuousStuffExperience:sample 0.00.01
 */

lateinit var lv: String

fun main() {
    listOf("123", "456").map { it.length } to 1
    ::lv.isInitialized
    val tmp = LinkedHashMap<String, String>()
    val l = Locale("")
    println("Hello continuous.stuff.experience")
    println("Lib1Class().m1() ${Lib1Class().m1()}")
    println("OtherLib1Class().m1() ${OtherLib1Class().m1()}")
    println("OtherLib1Class().m2() ${OtherLib1Class().m2()}")
    println("Lib2Class().sumOf(2, 3) ${Lib2Class().sumOf(2, 3)}")
}
