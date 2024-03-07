package de.mathisburger.exception

import org.eclipse.microprofile.graphql.GraphQLException

/**
 * Thrown on invalid parameter
 */
class ParameterException(s: String) : GraphQLException() {
}