package me.smorenburg.api.security.controller;

import me.smorenburg.api.security.JwtAuthenticationRequest;
import me.smorenburg.api.security.JwtTokenUtil;
import me.smorenburg.api.security.JwtUser;
import me.smorenburg.api.security.service.JwtAuthenticationResponse;
import me.smorenburg.api.security.service.JwtUserDetailsServiceImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;
import java.util.Base64;

@RestController
@RequestMapping("${jwt.routes.apiendpoint}")
public class AuthenticationRestController {

    private final Log logger = LogFactory.getLog(this.getClass());


    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsServiceImpl userDetailsService;

    @RequestMapping(value = "${jwt.routes.authentication.authendpoint}", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(
            @RequestHeader(value = "Authorization") String userAgent
            /*@RequestHeader JwtAuthenticationRequest authenticationRequest*/,
            Device device) throws AuthenticationException {

        logger.info(userAgent);

        if (userAgent != null && userAgent.startsWith("Basic")) {
            // Authorization: Basic base64credentials
            String base64Credentials = userAgent.substring("Basic".length()).trim();
            String credentials = new String(Base64.getDecoder().decode(base64Credentials),
                    Charset.forName("UTF-8"));
            // credentials = username:password
            final String[] values = credentials.split(":", 2);

            JwtAuthenticationRequest authenticationRequest = new JwtAuthenticationRequest(values[0], values[1]);


            // Perform the security
            final Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getUsername(),
                            authenticationRequest.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Reload password post-security so we can generate token
            final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
            final String token = jwtTokenUtil.generateToken(userDetails, device);

            // Return the token
            return ResponseEntity.ok(new JwtAuthenticationResponse(token));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @RequestMapping(value = "${jwt.routes.authentication.authendpoint}/${jwt.routes.authentication.refreshendpoint}", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);

        if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())) {
            String refreshedToken = jwtTokenUtil.refreshToken(token);
            return ResponseEntity.ok(new JwtAuthenticationResponse(refreshedToken));
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

//    @ExceptionHandler(HttpMessageNotReadableException.class)
//    public ResponseEntity<Object> rulesForHttpMessageNotReadableException(HttpServletRequest req, HttpMessageNotReadableException e) {
//        return ResponseEntity.badRequest().body(new ResponseApiError(e, e.getMessage().substring(0, e.getMessage().indexOf(":")), req.getServletPath()));
//    }
//
//    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
//    public ResponseEntity<Object> httpRequestMethodNotSupportedException(HttpServletRequest req, HttpMessageNotReadableException e) {
//        return ResponseEntity.badRequest().body(new ResponseApiError(e, e.getMessage().substring(0, e.getMessage().indexOf(":")), req.getServletPath()));
//    }
}
