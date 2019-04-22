package shiyan.db;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;


/**
 * 数据库操作的辅助类
 * 使用spring的jdbc template
 * @author iceki
 *
 */

public class DbHelper {
	/**
	 * 必须使用spring的jdbcTemplate进行构造
	 */
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 是否大于sql语句
	 */
	private boolean showSql = false;
	
	/**
	 * 存放某个类的 LinkForeignKey 属性名，有多个的话，用数组
	 */
	private ConcurrentMap<Class<?>,String[]> fkMap;
	public DbHelper(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		fkMap = new ConcurrentHashMap<>();
	}
	
	/**
	 * 返回某个类的外键，如果没有的话，则保存一下空
	 * null的话，表示第一次情况，还没有分析类
	 * @param cls
	 * @return
	 */
	private String[] getForeignKeyAttris(Class<?> cls) {
		String[] fks = fkMap.get(cls);
		if(fks != null)
			return fks;
		
		//需要进行类分析
		try {
			// 找到所有的私有属性
			Field[] allFields = cls.getDeclaredFields();
			//存在外键关系的属性名字
			List<String> fdlist = new ArrayList<>();
			for (Field fd : allFields) {
				//必须是私有属性
				if (fd.getModifiers() != 2)
					continue;
				//判断该属性是否有 LinkForeignKey 注解
				LinkForeignKey anno = fd.getAnnotation(LinkForeignKey.class);
				if(anno == null)
					continue;
				//蒋该属性的名字放入到列表中
				fdlist.add(fd.getName());
			}
			
			String[] empty = new String[] {};
			if(!fdlist.isEmpty()) {
				empty = fdlist.toArray(empty);
			}
			fkMap.put(cls, empty);
			return empty;
			
		} catch (Exception e) {
			//分析有错误了，则该类就当作没有fk
			String[] empty = new String[] {};
			fkMap.put(cls, empty);
			return empty;
		}
	}
	
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public boolean isShowSql() {
		return showSql;
	}

	public void setShowSql(boolean showSql) {
		this.showSql = showSql;
	}
	
	private void printSQL(String sql) {
		if(!showSql)
			return;
		System.out.println("sql->"+sql);
	}
	
	/**
	 * 返回某张数据库表的全部记录<br/>
	 * 
	 * @param cls
	 * @param tableName
	 * @return
	 */
	public <T> List<T> all(Class<T> cls, String tableName) throws Exception {
		List<T> list = new ArrayList<>();
		// 表名，如果未指定，则默认是java类名称
		String tableNm = tableName != null ? tableName : cls.getSimpleName();
		String sql = "select * from " + tableNm;
		printSQL(sql);
		
		list = jdbcTemplate.query(sql, new CommRowMapper<T>(cls));
		
		return list;
	}
	
	/**
	 * 返回某张数据库表的全部记录 <br/>
	 * 默认使用java对象的类名称作为表名 <br/>
	 * @param cls
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> all(Class<T> cls) throws Exception {
		return all(cls, null);
	}
	/**
	 * 根据sql语句找到一条相关信息
	 * @param cls
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public <T> T query(Class<T> cls,String sql)throws Exception{
		List<T> list = jdbcTemplate.query(sql, new CommRowMapper<T>(cls));
		T obj = list.get(0);
		return obj;
	}
	
	/**
	 * 根据主键找到一条记录<br/>
	 * 
	 * @param cls
	 * @param pkValue
	 * @param pkName
	 *            主键名称，如果为null，则默认是id
	 * @return
	 * @throws Exception
	 */
	public <T> T findByPK(Class<T> cls, Object pkValue, String pkName, String tableName)
			throws Exception {
		if(pkValue==null)
			return null;
		// 表名，如果未指定，则默认是java类名称
		String tableNm = tableName != null ? tableName : cls.getSimpleName();
		// 主键名，如果为指定，则默认为id
		String pkNm = pkName != null ? pkName : "id";
		// 根据主键名称，找到主键属性
		Field pkField = cls.getDeclaredField(pkNm);
		if (pkField == null)
			return null;
		// 组装查询sql，去要区分类型
		String sql = "select * from " + tableNm + " where " + pkNm + "=?";
		printSQL(sql);
		
		List<T> list = jdbcTemplate.query(sql, new Object[] {pkValue}, new CommRowMapper<T>(cls));

		if(GlobalUtils.isEmpty(list))
			return null;
		
		T obj = list.get(0);
		
		//需要做外键的判断
		String[] fks = getForeignKeyAttris(cls);
		if(fks.length>0) {
			for(String fdname: fks) {
				//找出具有外键关系的字段, 如 banji
				Field fd = cls.getDeclaredField(fdname);
				if(fd == null)
					continue;
				//找出该属性的anno
				LinkForeignKey anno = fd.getAnnotation(LinkForeignKey.class);
				if(anno == null)
					continue;
				//找出表示外键的那个属性，如 bjId
				Field fkfd = cls.getDeclaredField(anno.value());
				if(fkfd == null)
					continue;
				//保存外键值的属性，只能是int或者是long
				if(!(fkfd.getType().equals(int.class) || fkfd.getType().equals(long.class) || fkfd.getType().equals(Integer.class) || fkfd.getType().equals(Long.class)))
					continue;
				//找出外键当前的值
				Method fkgetter = cls.getMethod(GlobalUtils.nameOfGetter(fkfd.getName()));
				//现在外键的值，外键类型只能是int或者是long
				Object fkfdvalue = fkgetter.invoke(obj);
				if(fkfdvalue == null)
					continue;
				//根据外键值取外键表中查询记录，这里涉及到递归调用
				Object fkobj = findByPK(fd.getType(), fkfdvalue, anno.pkname().equals("")?"id":anno.pkname(), anno.tablename().equals("")?fd.getType().getSimpleName():anno.tablename());
				if(fkobj == null)
					continue;
				//找到该外键对象属性的setter方法，进行调用
				Method fdsetter = cls.getMethod(GlobalUtils.nameOfSetter(fd.getName()), fd.getType());
				fdsetter.invoke(obj, fkobj);
			}
		}
		
		return obj;
	}
	
	/**
	 * 根据主键找到一条记录 <br/>
	 * 默认使用java对象的类名称作为表名 <br/>
	 * @param cls
	 * @param pkValue
	 * @param pkName
	 * @return
	 * @throws Exception
	 */
	public <T> T findByPK(Class<T> cls, Object pkValue, String pkName)
			throws Exception {
		return findByPK(cls, pkValue, pkName, null);
	}
	
	/**
	 * 根据主键找到一条记录 <br/>
	 * 默认使用java对象的类名称作为表名 <br/>
	 * 主键名默认是id
	 * @param cls
	 * @param pkValue
	 * @return
	 * @throws Exception
	 */
	public <T> T findByPK(Class<T> cls, Object pkValue)
			throws Exception {
		return findByPK(cls, pkValue, null, null);
	}
	
	/**
	 * 返回满足条件的记录总数 <br/>
	 * whereSql: where语句，必须以where开始
	 * whereValue: where语句中的具体值用 ? 号表示，值的数量必须和 ? 数量相等
	 * @param cls
	 * @param where
	 * @param whereValue
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public <T> long total(Class<T> cls,String whereSql,Object[] whereValue,String tableName) throws Exception{
		// 表名，如果未指定，则默认是java类名称
		String tableNm = tableName != null ? tableName : cls.getSimpleName();
		String sql = "select count(*) from " + tableNm;
		if(!GlobalUtils.isEmpty(whereSql)) {
			String tmpstr = whereSql.trim();
			if(tmpstr.startsWith("where") || tmpstr.startsWith("WHERE")) {
				sql += " "+tmpstr;
			}
		}
		printSQL(sql);

		
		Long rst = new Long(0l);
		
		if(GlobalUtils.isEmpty(whereSql)) {
			rst = jdbcTemplate.queryForObject(sql, Long.class);
		}else {
			if(whereValue!=null && whereValue.length>0)
				rst = jdbcTemplate.queryForObject(sql, whereValue, Long.class);
			else
				rst = jdbcTemplate.queryForObject(sql, Long.class);
		}
		System.out.println(rst.longValue());
		return rst.longValue();
	}
	
	public <T> long total(Class<T> cls,String whereSql,Object[] whereValue) throws Exception{
		return total(cls, whereSql, whereValue, null);
	}
	
	public <T> long total(Class<T> cls) throws Exception{
		return total(cls, null, null, null);
	}
	
	/**
	 * 进行分页查询，查询条件等的组装都包含在参数pagination中
	 * @param cls
	 * @param pagination
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public <T> Pagination<T> search(Class<T> cls,Pagination<T> pagination,String tableName) throws Exception{
		// 表名，如果未指定，则默认是java类名称
		String tableNm = tableName != null ? tableName : cls.getSimpleName();
		//组装where语句
		String whereSql = pagination.gnrWhere();
		Object[]  whereValues = pagination.gnrWhereValues();
		//进行total的查询
		long total = total(cls, whereSql, whereValues);
		if(total>0) {
			//计算本页数据
			String sql = "select * from " + tableNm;
			sql += whereSql;
			//组装orderby
			sql += pagination.gnrOrderBy();
			//进行分页
			if(pagination.needPaging()) {
				sql += " limit " + pagination.getPagesize() + " offset " + pagination.getStartindex();
			}
			printSQL(sql);
			List<T> records = null;
			if(!whereSql.equals("") && whereValues!=null)
				records = jdbcTemplate.query(sql, whereValues, new CommRowMapper<T>(cls));
			else
				records = jdbcTemplate.query(sql, new CommRowMapper<T>(cls));
			
			//进行分页计算
			if(!GlobalUtils.isEmpty(records)) {
				
				//需要增加外键
				String[] fks = getForeignKeyAttris(cls);
				if(fks.length>0) {
					//将相同类型的相同id的对象，保存到map中
					Map<String, Object> map_fkobj = new HashMap<>();
					for(String fdname: fks) {
						//找出具有外键关系的字段, 如 banji
						Field fd = cls.getDeclaredField(fdname);
						if(fd == null)
							continue;
						//找出该属性的anno
						LinkForeignKey anno = fd.getAnnotation(LinkForeignKey.class);
						if(anno == null)
							continue;
						//找出表示外键的那个属性，如 bjId
						Field fkfd = cls.getDeclaredField(anno.value());
						if(fkfd == null)
							continue;
						//保存外键值的属性，只能是int或者是long
						if(!(fkfd.getType().equals(int.class) || fkfd.getType().equals(long.class) || fkfd.getType().equals(Integer.class) || fkfd.getType().equals(Long.class)))
							continue;
						//找出外键当前的值
						Method fkgetter = cls.getMethod(GlobalUtils.nameOfGetter(fkfd.getName()));
						//将外键内容设置进去的方法
						Method fdsetter = cls.getMethod(GlobalUtils.nameOfSetter(fd.getName()), fd.getType());
						//需要循环这些记录
						for(T objT: records) {
							//得到外键值，外键类型只能是int或者是long
							Object fkfdvalue = fkgetter.invoke(objT);
							if(fkfdvalue == null)
								continue;
							//根据属性名字_外键值作为key去map中找，如果有，则直接设置
							String keyInMap = fd.getName()+"_"+fkfdvalue.toString();
							//外键对应的对象
							Object fkobj = null;
							if(map_fkobj.containsKey(keyInMap)) {
								fkobj = map_fkobj.get(keyInMap);
							}else {
								//map中不存在，则需要去数据库找，并放入map
								fkobj = findByPK(fd.getType(), fkfdvalue, anno.pkname().equals("")?"id":anno.pkname(), anno.tablename().equals("")?fd.getType().getSimpleName():anno.tablename());
								map_fkobj.put(keyInMap, fkobj);
							}
							
							if(fkobj == null)
								continue;
							
							//进行设置
							fdsetter.invoke(objT, fkobj);
						}
						
					}
					
					map_fkobj.clear();
				}
				
				pagination.setRows(records);
				pagination.setRowsize(records.size());
			}else {
				records = new ArrayList<>();
				pagination.setRows(records);
			}
			
			pagination.setTotal(total);
		}
		
		return pagination;
	}
	
	public <T> Pagination<T> search(Class<T> cls,Pagination<T> pagination) throws Exception{
		return search(cls, pagination, null);
	}
	
	/**
	 * 直接根据sql语句执行删除，whereValues表示where中的 ? 号值 <br/>
	 * 返回结果为删除的记录数量
	 * @param sql
	 * @param whereValues
	 * @return
	 * @throws Exception
	 */
	public int deleteSql(String sql,Object[] whereValues) throws Exception{
		int rst = 0;
		if(whereValues==null || whereValues.length==0) 
			rst = jdbcTemplate.update(sql);
		else 
			rst = jdbcTemplate.update(sql, whereValues);
		return rst;
	}
	
	/**
	 * 删除单个记录，指定主键名称和表名称
	 * @param obj
	 * @param pkField
	 * @param tableName
	 * @throws Exception
	 */
	public <T> boolean delete(T obj,String pkField,String tableName) throws Exception{
		if (pkField == null || pkField.equals(""))
			return false;
		Field pk = obj.getClass().getDeclaredField(pkField);
		if(pk == null)
			return false;
		String tableNm = tableName != null ? tableName : obj.getClass().getSimpleName();
		String sql = "delete from " + tableNm;
		// 找到当前属性的值，根据getter方法调用
		// 组装obj中的getter方法
		String mtdName = GlobalUtils.nameOfGetter(pk.getName());
		Method mtdOfObj = obj.getClass().getMethod(mtdName);
		// 调用该方法，getter方法的参数为空
		Object value = mtdOfObj.invoke(obj);
		int rst = jdbcTemplate.update(sql, new Object[] {value});
		return rst>0;
	}
	
	public <T> boolean delete(T obj,String pkField) throws Exception{
		return delete(obj, pkField, null);
	}
	
	public <T> boolean delete(T obj) throws Exception{
		return delete(obj, "id", null);
	}
	
	/**
	 * 数据库对象保存，即根据主键来进行判断，是执行insert还是update<br/>
	 * 需要指定主键名称，以及是否自动增<br/>
	 * 如果保存失败，则返回null，否则返回obj对象本身，如果是自动增的insert情况，则返回值中携带最新的id号<br/>
	 * @param obj
	 * @param pkField
	 * @param isAI
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public <T> T save(T obj,String pkField,boolean isAI,String tableName) throws Exception{
		if(pkField==null || pkField.equals(""))
			return null;
		//找到主键字段
		Field pk = obj.getClass().getDeclaredField(pkField);
		if(pk == null)
			return null;
		//主键的类型，一般是int或者long型的
		String pkType = pk.getType().getSimpleName();
		//标记位，判断是insert还是update
		boolean isInsert = false;
		//执行方法调用，获得当前对象中的主键值
		Method pkGetterMtdName = obj.getClass().getMethod(GlobalUtils.nameOfGetter(pk.getName()));
		//主键值
		Object pkValue = pkGetterMtdName.invoke(obj);
		//主键值不能为null
		if(pkValue == null)
			return null;
		//根据是否自动增来判断
		if(isAI) {
			//如果是自动增的主键，那么通过 >0 来判断是否update或者insert
			//主键可能是int或者是long的，需要区分
			if(pkType.equals("int") || pkType.equals("Integer")) {
				int tmpvalue = ((Integer)pkValue).intValue();
				isInsert = (tmpvalue<=0);
			}else if (pkType.equals("long") || pkType.equals("Long")) {
				long tmpvalue = ((Long)pkValue).longValue();
				isInsert = (tmpvalue<=0);
			}else {
				//自动增的主键必须是int或者long型
				return null;
			}
		}else {
			//如果不是自动增的主键，那么需要去数据库中查询是否存在相同主键的记录
			Class<T> clsT = (Class<T>)obj.getClass();
			T other = findByPK(clsT,pkValue,pkField,tableName);
			isInsert = (other==null);
		}
		
		// 表名，如果未指定，则默认是java类名称
		String tableNm = tableName != null ? tableName : obj.getClass().getSimpleName();
		// 找到所有的私有属性
		Field[] allFields = obj.getClass().getDeclaredFields();
		// 计数器，用于判断是否添加逗号
		int counter = 0;
		//用于存储各个属性的值，用于 ? 值的对应
		List<Object> fdValuesList = new ArrayList<>();
		//根据是否insert执行不同的操作
		if(isInsert) {
			//insert 里面需要区分是否自动增的
			if(isAI) {
				StringBuffer sqlBuffer = new StringBuffer("insert into "+tableNm+"(");
				for (Field fd : allFields) {
					// java中的属性必须是private的
					if (fd.getModifiers() != 2)
						continue;
					// 属性的类型，必须在规定的范围内，即一些基础类型
					if (!GlobalUtils.simpleDataTypes.contains(fd.getType().getSimpleName()))
						continue;
					//自动增，主键不用放在字段里面，也不需要 ? 值
					if(fd.getName().equals(pk.getName()))
						continue;
					if (counter > 0)
						sqlBuffer.append(",");
					sqlBuffer.append(fd.getName());
					counter++;
				}
				sqlBuffer.append(")");
				//循环组装values
				sqlBuffer.append(" values(");
				counter = 0;
				for (Field fd : allFields) {
					// java中的属性必须是private的
					if (fd.getModifiers() != 2)
						continue;
					// 属性的类型，必须在规定的范围内，即一些基础类型
					if (!GlobalUtils.simpleDataTypes.contains(fd.getType().getSimpleName()))
						continue;
					//自动增，主键不用放在字段里面，也不需要 ? 值
					if(fd.getName().equals(pk.getName()))
						continue;
					if (counter > 0)
						sqlBuffer.append(",");
					sqlBuffer.append("?");
					counter++;
				}
				sqlBuffer.append(")");
				
				//自动在情况，执行sql，需要使用spring里面的keyholder使用
				//参考：https://blog.csdn.net/phantomes/article/details/37880365
				String sql = sqlBuffer.toString();
				printSQL(sql);
				KeyHolder keyHolder = new GeneratedKeyHolder();
				int upd = jdbcTemplate.update(new PreparedStatementCreator() {
					
					@Override
					public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
						PreparedStatement ps = jdbcTemplate.getDataSource().getConnection().prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
						//需要根据字段，一个个的调用set方法
						//参数的顺序从1开始
						int paramNo = 1;
						for (Field fd : allFields) {
							// java中的属性必须是private的
							if (fd.getModifiers() != 2)
								continue;
							// 属性的类型，必须在规定的范围内，即一些基础类型
							if (!GlobalUtils.simpleDataTypes.contains(fd.getType().getSimpleName()))
								continue;
							//自动增，主键不用放在字段里面，也不需要 ? 值
							if(fd.getName().equals(pk.getName()))
								continue;
							//根据属性的类型，调用不同的set方法
							String fdType = fd.getType().getSimpleName();
							//获得fd的值
							Object fdValue = null;
							try {
								// 组装obj中的getter方法
								Method fdGetterMtdName = obj.getClass().getMethod(GlobalUtils.nameOfGetter(fd.getName()));
								//该属性的值
								fdValue = fdGetterMtdName.invoke(obj);
							} catch (Exception e) {
								fdValue = null;
							}
							//设置 ? 值的方法需要根据不同类型进行
							if(fdType.equals("int") || fdType.equals("Integer")) {
								int dftValue = fdValue!=null?(((Integer)fdValue).intValue()):0;
								ps.setInt(paramNo, dftValue);
							}else if (fdType.equals("long") || fdType.equals("Long")) {
								long dftValue = fdValue!=null?(((Long)fdValue).longValue()):0l;
								ps.setLong(paramNo, dftValue);
							}else if (fdType.equals("double") || fdType.equals("Double")) {
								double dftValue = fdValue!=null?(((Double)fdValue).doubleValue()):0d;
								ps.setDouble(paramNo, dftValue);
							}else if (fdType.equals("boolean") || fdType.equals("Boolean")) {
								boolean dftValue = fdValue!=null?(((Boolean)fdValue).booleanValue()):false;
								ps.setBoolean(paramNo, dftValue);
							}else if (fdType.equals("float") || fdType.equals("Float")) {
								float dftValue = fdValue!=null?(((Float)fdValue).floatValue()):0f;
								ps.setFloat(paramNo, dftValue);
							}else if (fdType.equals("short") || fdType.equals("Short")) {
								short dftValue = fdValue!=null?(((Short)fdValue).shortValue()):0;
								ps.setShort(paramNo, dftValue);
							}else if (fdType.equals("byte") || fdType.equals("Byte")) {
								byte dftValue = fdValue!=null?(((Byte)fdValue).byteValue()):0;
								ps.setByte(paramNo, dftValue);
							}else {
								//其它类型都当作字符串处理
								String dftValue = fdValue!=null?(String)fdValue:"";
								ps.setString(paramNo, dftValue);
							}
							
							paramNo++;
						}
						
						return ps;
					}
				},keyHolder);
				
				if(upd<=0 || keyHolder.getKey()==null)
					return null;
				
				//更新成功之后，需要把新得到的key设置到主键去
				//主键区分int或者long型的
				if(pkType.equals("int") || pkType.equals("Integer")) {
					int keyNewId = keyHolder.getKey().intValue();
					if(keyNewId<=0)
						return null;
					//通过反射设置到主键值中去
					Method pkSetter = obj.getClass().getMethod(GlobalUtils.nameOfSetter(pk.getName()), int.class);
					pkSetter.invoke(obj, keyNewId);
					return obj;
				}else if (pkType.equals("long") || pkType.equals("Long")) {
					long keyNewId = keyHolder.getKey().longValue();
					if(keyNewId<=0)
						return null;
					//通过反射设置到主键值中去
					Method pkSetter = obj.getClass().getMethod(GlobalUtils.nameOfSetter(pk.getName()), long.class);
					pkSetter.invoke(obj, keyNewId);
					return obj;
				}else {
					return null;
				}
				
			}else {
				//非自动增，只要插入就可以了
				StringBuffer sqlBuffer = new StringBuffer("insert into "+tableNm+"(");
				for (Field fd : allFields) {
					// java中的属性必须是private的
					if (fd.getModifiers() != 2)
						continue;
					// 属性的类型，必须在规定的范围内，即一些基础类型
					if (!GlobalUtils.simpleDataTypes.contains(fd.getType().getSimpleName()))
						continue;
					if (counter > 0)
						sqlBuffer.append(",");
					sqlBuffer.append(fd.getName());
					counter++;
				}
				sqlBuffer.append(")");
				//循环组装values
				sqlBuffer.append(" values(");
				counter = 0;
				for (Field fd : allFields) {
					// java中的属性必须是private的
					if (fd.getModifiers() != 2)
						continue;
					// 属性的类型，必须在规定的范围内，即一些基础类型
					if (!GlobalUtils.simpleDataTypes.contains(fd.getType().getSimpleName()))
						continue;
					if (counter > 0)
						sqlBuffer.append(",");
					sqlBuffer.append("?");
					//获得fd的值，放入到值列表
					// 组装obj中的getter方法
					Method fdGetterMtdName = obj.getClass().getMethod(GlobalUtils.nameOfGetter(fd.getName()));
					//该属性的值
					Object fdValue = fdGetterMtdName.invoke(obj);
					fdValuesList.add(fdValue);
					counter++;
				}
				sqlBuffer.append(")");
				//执行sql语句
				String sql = sqlBuffer.toString();
				printSQL(sql);
				int upd = jdbcTemplate.update(sql, fdValuesList.toArray());
				return upd>0?obj:null;
			}
		}else {
			//update情况
			StringBuffer sqlBuffer = new StringBuffer("update "+tableNm+" set ");
			//循环各个属性，组装sql语句和 ? 
			for (Field fd : allFields) {
				// java中的属性必须是private的
				if (fd.getModifiers() != 2)
					continue;
				// 属性的类型，必须在规定的范围内，即一些基础类型
				if (!GlobalUtils.simpleDataTypes.contains(fd.getType().getSimpleName()))
					continue;
				// 如果当前字段是主键，则跳过
				if(fd.getName().equals(pk.getName()))
					continue;
				if (counter > 0)
					sqlBuffer.append(",");
				// 找到当前属性的值，根据getter方法调用
				// 组装obj中的getter方法
				Method fdGetterMtdName = obj.getClass().getMethod(GlobalUtils.nameOfGetter(fd.getName()));
				//该属性的值
				Object fdValue = fdGetterMtdName.invoke(obj);
				//加入到sql语句中
				sqlBuffer.append(fd.getName()+"=?");
				//将值加入到值列表
				fdValuesList.add(fdValue);
				
				counter++;
			}
			// 组装最后根据主键的where语句
			sqlBuffer.append(" where "+pk.getName()+"=?");
			//主键值也要加入到问号列表最后
			fdValuesList.add(pkValue);
			
			//执行sql语句
			String sql = sqlBuffer.toString();
			printSQL(sql);
			int upd = jdbcTemplate.update(sql, fdValuesList.toArray());
			return upd>0?obj:null;
		}
	}
	
	public <T> T save(T obj,String pkField,boolean isAI) throws Exception{
		return save(obj, pkField, isAI, null);
	}
	
	public <T> T save(T obj) throws Exception{
		return save(obj, "id", true, null);
	}
}
