package continuous.stuff.experience.analysis

import org.junit.jupiter.api.Test
import java.lang.reflect.Method
import kotlin.reflect.KClass

internal class PotentialBugsTest {
    @Test
    fun deprecationSampleTest() {
        classByName("PotentialBugsKt").declaredMethods.invokeByName("deprecationSample")
    }

    @Test
    fun colorTest() {
        classByName("Color").enumConstants.first().invokeDeclaredMethodByName("privateTest")
    }
}

private fun Array<Method>.singleByName(name: String) = single { it.name == name }.apply {
    check(!isAccessible) { "method $name must be not accessible" }
    isAccessible = true
}

private fun Array<Method>.invokeByName(name: String) = singleByName(name).call()

private fun Method.call() {
    check(parameterCount == 0) { "parameter count of method $name must equals to 0" }
    invoke(null)
}
private fun Method.call(any: Any) {
    check(parameterCount == 0) { "parameter count of method $name must equals to 0" }
    check(declaringClass == any::class.java) { "declaringClass of method $name must equals to (any::class.java)" }
    invoke(any)
}

private fun Any.invokeDeclaredMethodByName(name: String) {
    this::class.java.declaredMethods.singleByName(name).call(this)
}

private fun classBy(name: String, packageName: String) = Class.forName("$packageName.$name")
private fun classBy(name: String, packageOf: KClass<*>) = classBy(name, packageOf.java.`package`.name)
private fun Any.classByName(name: String) = classBy(name, this::class)
