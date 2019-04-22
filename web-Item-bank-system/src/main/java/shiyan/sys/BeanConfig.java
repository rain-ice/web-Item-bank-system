package shiyan.sys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import shiyan.db.DbHelper;




@Configuration
public class BeanConfig {
	
	@Value("${dbhelper.showsql}")
	private boolean dbHelperShowSql;
	
	@Bean
	public DbHelper dbHelper(JdbcTemplate jdbcTemplate) {
		DbHelper helper = new DbHelper(jdbcTemplate);
		helper.setShowSql(dbHelperShowSql);
		return helper;
	}
	
	
	
	

}
