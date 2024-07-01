package de.immowealth.graphql

import io.smallrye.graphql.api.Adapter

/**
 * Adapter built to only provide empty passwords to the users
 */
class PasswordAdapter : Adapter<String, String?> {


    override fun from(a: String?): String {
        return a ?: ""
    }

    override fun to(o: String?): String? {
        return null;
    }
}