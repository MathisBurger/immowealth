package de.immowealth.entity

/**
 * All user roles
 */
class UserRoles {

    companion object {
        /**
         * Admin role
         */
        final val ADMIN = "ROLE_ADMIN";

        /**
         * Tenant owner
         */
        final val TENANT_OWNER = "ROLE_TENANT_OWNER";

        /**
         * Tenant assigned
         */
        final val TENANT_ASSIGNED = "ROLE_TENANT_ASSIGNED"

        final val RENTER = "ROLE_RENTER"
    }
}