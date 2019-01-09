package dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class userdao {
	@Bean
	public String namedao() {
		return "尝试";
	}

}
