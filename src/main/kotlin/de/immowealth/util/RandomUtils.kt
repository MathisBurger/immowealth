package de.immowealth.util

/**
 * Utils handling random interactions
 */
class RandomUtils {

    companion object {
        /**
         * Generates a random string
         *
         * @param length The length of the string
         */
        fun getRandomString(length: Int) : String {
            val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
            return (1..length)
                .map { allowedChars.random() }
                .joinToString("")
        }
    }
}