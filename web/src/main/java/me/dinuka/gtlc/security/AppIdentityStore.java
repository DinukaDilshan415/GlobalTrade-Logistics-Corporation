package me.dinuka.gtlc.security;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.credential.Credential;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStore;
import me.dinuka.gtlc.ejb.AdminAuthService;
import me.dinuka.gtlc.ejb.UserAuthService;

import java.util.Set;

@ApplicationScoped
public class AppIdentityStore implements IdentityStore {

    @Inject
    private UserAuthService loginService;

    @Inject
    private AdminAuthService adminService;

    @Override
    public CredentialValidationResult validate(Credential credential) {
        if (credential instanceof UsernamePasswordCredential) {
            UsernamePasswordCredential upc = (UsernamePasswordCredential) credential;

            if (loginService.validate(upc.getCaller(), upc.getPasswordAsString())) {
                Set<String> roles = loginService.getRoles(upc.getCaller());

                return new CredentialValidationResult(upc.getCaller(), roles);
            } else if (adminService.validate(upc.getCaller(), upc.getPasswordAsString())) {
                Set<String> roles = adminService.getRoles(upc.getCaller());

                return new CredentialValidationResult(upc.getCaller(), roles);
            }
        }

        return CredentialValidationResult.INVALID_RESULT;
    }
}
