package continuous.stuff.experience

/**
 * This class is for researching. This text is an example of documentation.
 * @version 0.00.01
 * @since ContinuousStuffExperience:lib 0.00.01
 */
class Lib1Class {

	/**
	 * This field is for researching. This text is an example of documentation.
	 * Always return `Type.TYPE_1`.
	 * @see Type.TYPE_1
	 * @version 0.00.01
	 * @since ContinuousStuffExperience:lib 0.00.01
	 */
	val paramType = Type.TYPE_1

	/**
	 * This internal class is for researching. This text is an example of documentation.
	 * @version 0.00.01
	 * @since ContinuousStuffExperience:lib 0.00.01
	 */
	enum class Type {
		/**
		 * This field of internal class is for researching. This text is an example of documentation.
		 * Single constant.
		 * @see Type
		 * @version 0.00.01
		 * @since ContinuousStuffExperience:lib 0.00.01
		 */
		TYPE_1,
	}

	/**
	 * This method is for researching. This text is an example of documentation.
	 * @return constant string with the name of the module, class and itself
	 * @version 0.00.01
	 * @since ContinuousStuffExperience:lib 0.00.01
	 */
	fun m1(): String {
		return "string from lib: Lib1Class: m1"
	}
}

/**
 * This data class is for researching. This text is an example of documentation.
 * @version 0.00.01
 * @since ContinuousStuffExperience:lib 0.00.01
 */
data class Lib1DataClass(
	/**
	 * This data class field is for researching. This text is an example of documentation.
	 * @version 0.00.01
	 * @since ContinuousStuffExperience:lib 0.00.01
	 */
	val paramString: String
)