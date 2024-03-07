package de.mathisburger.exception

import org.eclipse.microprofile.graphql.GraphQLException

/**
 * Exception is thrown if given date is not allowed in a
 * specific context
 */
class DateNotAllowedException(s: String) : GraphQLException() {
}