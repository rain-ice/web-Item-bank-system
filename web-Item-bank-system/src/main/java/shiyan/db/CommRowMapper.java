package shiyan.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

/**
 * 通用的rowmapper
 * @author iceki
 *
 */
public class CommRowMapper<T> implements RowMapper<T>{
	
	private Class<T> cls;

	public CommRowMapper(Class<T> cls) {
		this.cls = cls;
	}

	@Override
	public T mapRow(ResultSet rs, int arg1) throws SQLException {
		try {
			// 根据类型new一个新实例对象
			T obj = cls.newInstance();
			// 将rs的一行，转换为对象
			GlobalUtils.oneRsToObj(rs, obj);
			return obj;
		} catch (Exception e) {
			throw new SQLException(e);
		}
	}

}
