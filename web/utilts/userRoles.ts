import {SimpleUserFragment} from "@/generated/graphql";
import {isUniqueArray} from "@/utilts/arrayUtils";

enum UserRoles {
    ADMIN='ROLE_ADMIN',
    TENANT_OWNER='ROLE_TENANT_OWNER',
    TENANT_ASSIGNED='ROLE_TENANT_ASSIGNED'
}

export function isGranted(user: SimpleUserFragment|null, roles: Array<UserRoles>) {
    return !isUniqueArray([...(user?.roles ?? []), ...roles]);
}

export default UserRoles;