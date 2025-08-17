package sco.carlukesoftware.composeextensions.utils

import java.nio.file.Path
import kotlin.io.path.readText
import kotlin.io.path.toPath

/**
 * Utility object for accessing and reading resources from the classpath.
 *
 * This object provides convenient functions to retrieve the content of resource files
 * as text or to get their [Path]. It simplifies common resource handling tasks.
 */
object ResourceUtils {

    /**
     * Reads the text content of a resource file.
     *
     * This function locates a resource file by its name, reads its entire content as a String,
     * and normalizes line endings to '\n'.
     *
     * @param name The name of the resource file (e.g., "my_text_file.txt").
     *             The resource is expected to be found in the classpath.
     * @return The text content of the resource file as a String.
     * @throws IllegalStateException if the resource with the given name is not found.
     */
    fun getResourceText(name: String): String = getResourcePath(name)
        .readText()
        .replace(System.lineSeparator(), "\n")

    /**
     * Retrieves the [Path] for a resource located in the classpath.
     *
     * This function uses the class loader of [ResourceUtils] to find the specified resource.
     *
     * @param name The name of the resource to locate. This should be the path to the resource
     *             relative to the classpath root (e.g., "my_file.txt" or "some/folder/my_file.txt").
     * @return The [Path] object representing the resource.
     * @throws IllegalStateException if the resource with the given name is not found.
     */
    fun getResourcePath(name: String): Path {
        val resource = ResourceUtils::class.java.classLoader?.getResource(name) ?: error("Resource $name not found")
        return resource
            .toURI()
            .toPath()
    }

}
