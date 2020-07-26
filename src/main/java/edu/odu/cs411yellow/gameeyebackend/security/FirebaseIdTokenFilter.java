package edu.odu.cs411yellow.gameeyebackend.security;

import com.google.api.client.util.Strings;
import com.google.api.core.ApiFuture;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@Component
public class FirebaseIdTokenFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String idToken = resolveToken(request);

        if (Strings.isNullOrEmpty(idToken)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            Authentication authentication = getAndValidateAuthentication(idToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (!Strings.isNullOrEmpty(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }

    private FirebaseToken authenticateFirebaseToken(String authToken) throws Exception {
        ApiFuture<FirebaseToken> app = FirebaseAuth.getInstance().verifyIdTokenAsync(authToken);
        return app.get();
    }

    private Authentication getAndValidateAuthentication(String authToken) throws Exception {
        Authentication authentication;

        FirebaseToken firebaseToken = authenticateFirebaseToken(authToken);
        authentication = new UsernamePasswordAuthenticationToken(firebaseToken, authToken, new ArrayList<>());

        return authentication;
    }
}
