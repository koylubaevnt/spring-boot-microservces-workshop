package io.javabrains.springbootconfig;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("db")
@Getter
@Setter
@ToString
public class DbSettings {

    private String connection;

    private String host;

    private int port;
}
