package emlakcepte;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import emlakcepte.service.UserService;

@SpringBootApplication
@EnableFeignClients
@ImportAutoConfiguration({ FeignAutoConfiguration.class })
@EnableEurekaClient
public class EmlakcepteServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmlakcepteServiceApplication.class, args);
	}

	@Bean
	public UserService userService() {
		return new UserService();
	}

}
