package shiyan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import net.sf.json.JSONObject;
import shiyan.db.DbHelper;
import shiyan.table.Tuser;

/**
 * 用户相关操作，增删改查
 * @author anstar
 *
 */
@RestController
public class TuserCtrl {
	@Autowired DbHelper dbHelper;

	/**
	 * 查询操作
	 * @return 返回根据条件查询的用户列表
	 * @throws Exception
	 */
	@RequestMapping(value="TuserCtrl.search")
	public String search()throws Exception{
		return "查询";
	}
	
	@RequestMapping(value="TuserCtrl.verification",method=RequestMethod.POST)
	public String verification(String username)throws Exception{
		Tuser tuser = null;
		boolean flag=true;
		tuser =  dbHelper.findByPK(Tuser.class, username, "username");
		if(tuser != null) {
			flag=false;
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("succ", flag);
		return jsonObject.toString();
			
		
		
	}
	
	
	/**
	 * 查询单个用户
	 * @return 返回单个用户的相关信息
	 * @throws Exception
	 */
	@RequestMapping(value="TuserCtrl.presave")
	public String presave()throws Exception{
		return "操作之前的检索";
	}
	/**
	 * 增加或者修改操作（insert或者是update）
	 * @return 操作是否正确执行
	 * @throws Exception
	 */
	@RequestMapping(value="TuserCtrl.save",method= RequestMethod.POST)
	public String save(Tuser tuser)throws Exception{
		System.out.println(tuser.toString());
		boolean flag=false;
		tuser =  dbHelper.save(tuser);
		if(tuser.getId()>0) {
			flag=true;
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("succ", flag);
		
		return jsonObject.toString();
	}
	/**
	 * 删除操作
	 * @return 删除操作是否正确执行
	 * @throws Exception
	 */
	@RequestMapping(value="TuserCtrl.del")
	public String del()throws Exception{
		return "删除操作";
	}
	
	

}
