package de.immowealth.mock

import java.security.Principal

class MockedPrincipal : Principal {

    var principalName: String = ""

    override fun getName(): String {
        return principalName;
    }
}