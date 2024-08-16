package de.immowealth.controller

import jakarta.annotation.security.PermitAll
import jakarta.ws.rs.Path

@Path("/graphql")
@PermitAll
class GraphQLController {
}