import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.plugins.PluginAware
import org.gradle.api.tasks.SourceSet
import org.gradle.kotlin.dsl.apply

fun ExtensionAware.ext(firstPair: Pair<String, Any>, vararg pairs: Pair<String, Any>) {
    extensions.extraProperties.set(firstPair.first, firstPair.second)
    pairs.forEach { (key, value) ->
        extensions.extraProperties.set(key, value)
    }
}

fun PluginAware.applyFrom(firstScript: Any, vararg otherScripts: Any) {
    apply(from = firstScript)
    otherScripts.forEach {
        apply(from = it)
    }
}

fun Iterable<SourceSet>.allSource() = flatMap {
    it.allSource
}
fun Iterable<SourceSet>.srcDirs() = flatMap {
    it.allSource.srcDirs
}
fun Iterable<SourceSet>.output() = flatMap {
    it.output
}
