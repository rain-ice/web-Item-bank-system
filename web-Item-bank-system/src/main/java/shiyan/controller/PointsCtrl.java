package shiyan.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import shiyan.db.DbHelper;
import shiyan.table.Points;
@RestController
public class PointsCtrl {
	
	@Autowired DbHelper dbHelper;
	/**
	 * 查询操作
	 * @return 返回根据条件查询返回数据组
	 * @throws Exception
	 */
	@RequestMapping(value="PointsCtrl.search")
	public String search()throws Exception{
		return "查询";
	}
	
	/**
	 * 查询所有数据
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="PointsCtrl.all")
	public String all()throws Exception{
		
		List<Points> list = new ArrayList<Points>();
		list= dbHelper.all(Points.class);
		JSONArray model = JSONArray.fromObject(list);
		return model.toString();
	}
	
	
	/**
	 * 查询单个数据
	 * @return 返回单个的相关信息
	 * @throws Exception
	 */
	@RequestMapping(value="PointsCtrl.presave")
	public String presave()throws Exception{
		return "操作之前的检索";
	}
	/**
	 * 增加或者修改操作（insert或者是update）
	 * @return 操作是否正确执行
	 * @throws Exception
	 */
	@RequestMapping(value="PointsCtrl.save")
	public String save()throws Exception{
		return "新增或者修改操作";
	}
	/**
	 * 删除操作
	 * @return 删除操作是否正确执行
	 * @throws Exception
	 */
	@RequestMapping(value="PointsCtrl.del")
	public String del()throws Exception{
		return "删除操作";
	}

}
