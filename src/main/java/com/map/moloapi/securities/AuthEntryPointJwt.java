package com.map.moloapi.securities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;

/**
 * @author DIOMANDE Souleymane
 * @Date 16/10/2022 18:44
 */
@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        logger.error("## Authentification requis, Status code : {}, HTTP [{}] : {}", HttpServletResponse.SC_UNAUTHORIZED, request.getMethod(), request.getRequestURI());
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Echec d'authentification");
    }

    public static String bufferedToString(BufferedReader stringReader) throws SQLException, IOException {
        String singleLine = null;
        StringBuffer strBuff = new StringBuffer();
        while ((singleLine = stringReader.readLine()) != null) {
            strBuff.append(singleLine);
        }
        return strBuff.toString();
    }

}
