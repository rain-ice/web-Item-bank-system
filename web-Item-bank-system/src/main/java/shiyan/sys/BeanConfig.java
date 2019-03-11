package shiyan.sys;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import shiyan.db.DbHelper;




@Configuration
public class BeanConfig {
	@Bean
	public DbHelper dbHelper(JdbcTemplate jdbcTemplate) {
		DbHelper helper = new DbHelper(jdbcTemplate);
		return helper;
	}

}
