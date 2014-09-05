package org.marble.commons.config;

import javax.servlet.ServletContext;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

@Configuration
public class SecurityWebApplicationInitializer extends AbstractSecurityWebApplicationInitializer {
    
    @Override
    protected void beforeSpringSecurityFilterChain(ServletContext servletContext) {
        //insertFilters(servletContext, new MultipartFilter());
    }
}