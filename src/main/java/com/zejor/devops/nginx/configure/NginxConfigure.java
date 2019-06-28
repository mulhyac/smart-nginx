package com.zejor.devops.nginx.configure;

import com.zejor.devops.nginx.spring.plugins.SpringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
public class NginxConfigure {

    @Bean
    public SpringUtils springUtils() {
        return new SpringUtils();
    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

}
