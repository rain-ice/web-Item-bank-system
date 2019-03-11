package shiyan.dao;




import shiyan.db.DbHelper;
import shiyan.db.GlobalUtils;
import shiyan.table.Tuser;

public class Userdao {

	/**
	 * 查询单个User	
	 * @param username
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static Tuser findUser(String username,String password,DbHelper dbHelper) throws Exception{
		
		if(username==null || username.equals("") || username.indexOf("'")!=-1 || username.indexOf("%")!=-1)
			return null;
		if(password==null || password.equals("") || password.indexOf("'")!=-1 || password.indexOf("%")!=-1)
			return null;
		
		String sql = "select * from Tuser where username='"+username+"'";
		sql += " and password='"+GlobalUtils.md5Encode(password)+"'";
		System.out.println(sql);
		Tuser user = dbHelper.query(Tuser.class, sql);
		
		return user;

	}
}
