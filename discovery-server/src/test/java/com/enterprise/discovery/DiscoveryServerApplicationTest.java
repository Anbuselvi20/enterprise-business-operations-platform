package com.enterprise.discovery;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
        "server.port=0",
        "eureka.client.register-with-eureka=false",
        "eureka.client.fetch-registry=false"
})
class DiscoveryServerApplicationTest {

    @Test
    void shouldStartDiscoveryServer() {
        assertThat(DiscoveryServerApplication.class).isNotNull();
    }
}
