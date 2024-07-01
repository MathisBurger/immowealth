package de.immowealth.exception

/**
 * Is thrown if a user already exists
 */
class UserExistsException(string: String) : Exception(string) {
}