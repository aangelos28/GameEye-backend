package edu.odu.cs411yellow.gameeyebackend.security;

import com.google.api.client.util.Strings;
import com.google.api.core.ApiFuture;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import edu.odu.cs411yellow.gameeyebackend.services.ClaimService;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Request filter that authorizes requests by validating the Bearer token.
 */
@Component
public class FirebaseIdTokenFilter extends OncePerRequestFilter {

    private ClaimService claimService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        // Lazy inject claim service since we cannot autowire it
        if (claimService == null) {
            ServletContext servletContext = request.getServletContext();
            WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
            claimService = webApplicationContext.getBean(ClaimService.class);
        }

        String idToken = resolveToken(request);

        if (Strings.isNullOrEmpty(idToken)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            Authentication authentication = validateBearerAndGetAuthentication(idToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Gets the Bearer token from a request.
     * @param request Request object.
     * @return The Bearer token in string format.
     */
    private static String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (!Strings.isNullOrEmpty(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    /**
     * Verifies the passed ID token and returns an instance of FirebaseToken if
     * it is successfully verified.
     * @param authToken ID token in string format.
     * @return FirebaseToken object if the token is verified.
     * @throws Exception If the token cannot be verified.
     */
    private static FirebaseToken authenticateFirebaseToken(String authToken) throws Exception {
        ApiFuture<FirebaseToken> app = FirebaseAuth.getInstance().verifyIdTokenAsync(authToken);
        return app.get();
    }

    /**
     * Validates the passed authentication token and creates a Spring Authentication object
     * with authorities (i.e. user roles).
     * @param authToken Authentication token in string format.
     * @return Spring Authentication object with authorities
     * @throws Exception If the token cannot be verified
     */
    private Authentication validateBearerAndGetAuthentication(String authToken) throws Exception {
        Authentication authentication;

        FirebaseToken idToken = authenticateFirebaseToken(authToken);

        if (!claimService.userEmailVerified(idToken)) {
            throw new Exception("User email not verified.");
        }

        // User is valid. Get their roles as Spring authorities.
        List<SimpleGrantedAuthority> authorities = claimService.getUserRolesAsAuthorities(idToken);

        authentication = new UsernamePasswordAuthenticationToken(idToken, authToken, authorities);

        return authentication;
    }
}
