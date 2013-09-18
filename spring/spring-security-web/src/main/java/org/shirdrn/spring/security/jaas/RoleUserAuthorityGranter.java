package org.shirdrn.spring.security.jaas;

import java.security.Principal;
import java.util.Collections;
import java.util.Set;

import org.springframework.security.authentication.jaas.AuthorityGranter;

public class RoleUserAuthorityGranter implements AuthorityGranter {

    public Set<String> grant(Principal principal) {
        return Collections.singleton("ROLE_USER");
    }
}
