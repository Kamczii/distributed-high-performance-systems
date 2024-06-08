package pl.rsww.active_mq;

import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.jmx.ManagementContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@Slf4j
@SpringBootApplication
@EnableDiscoveryClient
public class ActiveMqApplication {

    @Value("${broker.host}")
    private String brokerHost;

    public static void main(String[] args) {
        SpringApplication.run(ActiveMqApplication.class, args);
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public BrokerService broker() throws Exception {
        final BrokerService broker = new BrokerService();
        final String uri = String.format("stomp://%s:61613", brokerHost);
        log.info("Adding connector: {}", uri);
        broker.addConnector(uri);

        broker.setPersistent(false);
        final ManagementContext managementContext = new ManagementContext();
        managementContext.setCreateConnector(true);
        broker.setManagementContext(managementContext);

        return broker;
    }
}
