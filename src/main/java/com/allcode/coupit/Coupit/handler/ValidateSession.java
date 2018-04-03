package com.allcode.coupit.Coupit.handler;

import com.allcode.coupit.Coupit.model.Session;
import com.allcode.coupit.Coupit.service.SessionService;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.filter.GenericFilterBean;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@EnableJpaRepositories("model")
@EnableTransactionManagement
public class ValidateSession extends GenericFilterBean {

    public SessionService sessionService;

    public ValidateSession(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    void redirectError(ServletRequest request, ServletResponse response) throws IOException{
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        httpResponse.sendRedirect(httpRequest.getContextPath() + "/invalid_token");
    }

    @Override
    @Transactional
    public void doFilter(
            ServletRequest request, ServletResponse response, FilterChain chain
    ) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());
        Boolean validPath = path.matches("^/api/.*");
        if(validPath){
            String token = request.getParameter("token");
            if(token != null){
                Session session = sessionService.findByToken(token);
                if(session == null) {
                    this.redirectError(request, response);
                    return;
                }
            }else{
                this.redirectError(request, response);
                return;
            }
        }

        chain.doFilter(request, response);
    }
}
