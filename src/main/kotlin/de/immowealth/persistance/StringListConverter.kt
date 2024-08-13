package de.immowealth.persistance

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter


@Converter
class StringListConverter : AttributeConverter<MutableSet<String>, String> {
    override fun convertToDatabaseColumn(stringList: MutableSet<String>): String {
        return stringList.joinToString(SPLIT_CHAR)
    }

    override fun convertToEntityAttribute(string: String): MutableSet<String> {
        return string.split(SPLIT_CHAR).toMutableSet()
    }

    companion object {
        private const val SPLIT_CHAR = ";"
    }
}