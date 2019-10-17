package continuous.stuff.experience

/**
 * This class is for researching. This text is an example of documentation.
 * @version 0.00.01
 * @since ContinuousStuffExperience:lib2 0.00.01
 */
class Lib2Class {
    /**
     * This method is for researching. This text is an example of documentation.
     * @param int1 first of two input integers
     * @param int2 second of two input integers
     * @return sum of two input integers
     * @version 0.00.01
     * @since ContinuousStuffExperience:lib2 0.00.01
     */
    fun sumOf(int1: Int, int2: Int): Int {
        return int1 + int2
    }

    /**
     * Returns the next element, or `null` if the iterable is empty.
     * @param T some of `Any`
     * @param iterable some `Iterable` of `T`
     * @see Iterable
     * @see Any
     * @return next element or `null`
     * @version 0.00.01
     * @since ContinuousStuffExperience:lib2 0.00.01
     */
    fun <T : Any> nextOrNull(iterable: Iterable<T>): T? {
        return iterable.iterator().run {
            if (hasNext()) next()
            else null
        }
    }

    /**
     * This method is for researching. This text is an example of documentation.
     * @return constant string with the name of the module, class and itself
     * @version 0.00.01
     * @since ContinuousStuffExperience:lib2 0.00.01
     */
    fun m2(): String {
        return "string from lib2: Lib2Class: m2"
    }
}
