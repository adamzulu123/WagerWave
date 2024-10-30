package com.ww.WagerWave.Security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

/*
klasa ta jest automatycznie wykrywana przez mechanizm skanowania komponentów
(component scanning) i dodawana do kontekstu aplikacji Springa,
co daje mozliwosc wskrzykniecia jej obiektow do innych klas zarządzanych przez Springa (dependency injection)
 */
@Component
public class CustomAuthenticationFailureHandler  implements AuthenticationFailureHandler {


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        //todo
        //request.getSession().setAttribute("error", "Invalid username or password");

        //response.sendRedirect(request.getContextPath());
    }
}
