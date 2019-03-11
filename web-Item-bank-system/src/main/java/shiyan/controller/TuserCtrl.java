package shiyan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import shiyan.db.DbHelper;

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
	@RequestMapping(value="TuserCtrl.save")
	public String save()throws Exception{
		return "新增或者修改操作";
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
