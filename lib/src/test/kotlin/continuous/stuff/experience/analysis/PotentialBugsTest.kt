package continuous.stuff.experience.analysis

import java.lang.reflect.Method
import kotlin.reflect.KClass
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class PotentialBugsTest {
    private val packageName = this::class.java.`package`.name

    @Test
    fun deprecationSampleTest() {
        invokePrivateStatic(
            className = "$packageName.PotentialBugsKt",
            methodName = "deprecationSample"
        )
    }

    @Test
    fun equalsVerifyEqualsHashCodeTest() {
        val any = invokePrivateConstructor("$packageName.VerifyEqualsHashCode")
        assertFalse(any == "blabla")
        val other = invokePrivateConstructor("$packageName.VerifyEqualsHashCode")
        assertTrue(any == other)
    }
    @Test
    fun hashCodeVerifyEqualsHashCodeTest() {
        invokePrivateConstructor("$packageName.VerifyEqualsHashCode").hashCode()
    }

    @Test
    fun explicitGarbageCollectionCallTest() {
        invokePrivateStatic(
            className = "$packageName.PotentialBugsKt",
            methodName = "explicitGarbageCollectionCall"
        )
    }

    @Test
    fun nextSomeIteratorTest() {
        val iterator = invokePrivateConstructor("$packageName.SomeIterator") as Iterator<*>
        assertNotNull(iterator.next())
    }
    @Test
    fun nextTwiceSomeIteratorTest() {
        val iterator = invokePrivateConstructor("$packageName.SomeIterator") as Iterator<*>
        assertNotNull(iterator.next())
        assertThrows(NoSuchElementException::class.java) {
            iterator.next()
        }
    }
    @Test
    fun hasNextSomeIteratorTest() {
        val iterator = invokePrivateConstructor("$packageName.SomeIterator") as Iterator<*>
        assertTrue(iterator.hasNext())
        iterator.next()
        assertFalse(iterator.hasNext())
    }

    @Test
    fun unconditionalJumpStatementInLoopTest() {
        invokePrivateStatic(
            className = "$packageName.PotentialBugsKt",
            methodName = "unconditionalJumpStatementInLoop"
        )
    }

    @Test
    fun unreachableCodeTest() {
        invokePrivateStatic(
            className = "$packageName.PotentialBugsKt",
            methodName = "unreachableCode"
        )
    }

    @Test
    fun uselessPostfixExpressionTest() {
        invokePrivateStatic(
            className = "$packageName.PotentialBugsKt",
            methodName = "uselessPostfixExpression"
        )
    }
}

private fun invokePrivateStatic(
    className: String,
    methodName: String
) {
    val c = Class.forName(className)
    val method = c.declaredMethods.single { it.name == methodName }
    check(!method.isAccessible) { "method $methodName must be not accessible" }
    method.isAccessible = true
    method.invoke(null)
}

private fun invokePrivate(
    any: Any,
    methodName: String
) {
    val method = any::class.java.declaredMethods.single { it.name == methodName }
    check(!method.isAccessible) { "method $methodName must be not accessible" }
    method.isAccessible = true
    method.invoke(any)
}

private fun invokePrivateConstructor(
    className: String
): Any {
    val c = Class.forName(className)
    val constructor = c.declaredConstructors.single { it.parameterCount == 0 }
    check(!constructor.isAccessible) { "constructor must be not accessible" }
    constructor.isAccessible = true
    return constructor.newInstance()
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
