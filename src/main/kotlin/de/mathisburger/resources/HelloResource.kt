package de.mathisburger.resources

import org.eclipse.microprofile.graphql.DefaultValue
import org.eclipse.microprofile.graphql.Description
import org.eclipse.microprofile.graphql.GraphQLApi
import org.eclipse.microprofile.graphql.Query

/**
 * Simple hello resource
 */
@GraphQLApi
class HelloResource {

    /**
     * Default query
     */
    @Query
    fun sayHello(@DefaultValue("World") name: String): String = "Hello $name"

}