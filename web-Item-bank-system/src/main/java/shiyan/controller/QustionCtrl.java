package shiyan.controller;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import shiyan.db.DbHelper;
import shiyan.db.Pagination;
import shiyan.model.QustionModel;
import shiyan.sys.Mydate;
import shiyan.table.Points;
import shiyan.table.Question;
import shiyan.table.Tuser;
@RestController
public class QustionCtrl {

	@Autowired DbHelper dbHelper;
	/**
	 * 查询操作
	 * @return 返回根据条件查询返回数据组
	 * @throws Exception
	 */
	@RequestMapping(value="QustionCtrl.search",method=RequestMethod.POST)
	public String search(QustionModel model)throws Exception{
		Pagination<Question> pagination = new Pagination<>(model.getPage(), model.getPagesize());
		String where="where 1=1 ";
		System.out.println(model.getPointid());
		if(model.getPointid()>0) {
			where+=" and pointid = ?";
			
			pagination.addWhereValue(model.getPointid());
		}
		if(model.getTeacherid()>0) {
			where+=" and teacherid = ?";
			
			pagination.addWhereValue(model.getTeacherid());
		}
		pagination.appendWhere(where);
		pagination =dbHelper.search(Question.class, pagination);
		List<Question> list = pagination.getRows();
		for(Question question:list) {
			question.setUsername(dbHelper.findByPK(Tuser.class,question.getTeacherid()).getName());
			question.setPointname(dbHelper.findByPK(Points.class, question.getPointid()).getName());
			//方便前端数据显示，可能在这一方面使用JSP处理会更方便
			switch (question.getGlevel()) {
			case 1:
				question.setLevel("简单");
				break;
			case 2:
				question.setLevel("普通");			
				break;
			case 3:
				question.setLevel("困难");
				break;
			}
			switch (question.getType()) {
			case 1:
				question.setTypename("选择题");
				break;
			case 2:
				question.setTypename("填空题");
				break;
			case 3:
				question.setTypename("判断题");
				break;
			case 4:
				question.setTypename("简答题");
				break;
			}
		}
		model.setList(list);
		model.setTotal(pagination.getTotal());
		model.setTotalpage(pagination.getTotalpage());
		model.setPage(pagination.getPage());
		model.setPagesize(pagination.getPagesize());
		JSONObject jsonObject = new JSONObject().fromObject(model);
		
		return jsonObject.toString();
	}

	/**
	 * 查询所有问题，进行统计
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="QustionCtrl.all",method=RequestMethod.POST)
	public String all()throws Exception{
		List<Question> list =  dbHelper.all(Question.class);
		JSONArray jsonArray = JSONArray.fromObject(list);
		return jsonArray.toString();
	}
	/**
	 * 查询对应的一条ID为接下来的修改做准备
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="QustionCtrl.presave",method=RequestMethod.POST)
	public String presave(int id)throws Exception{
		
		Question question = dbHelper.findByPK(Question.class, id);
		JSONObject jsonObject = JSONObject.fromObject(question);
		return jsonObject.toString();
		
	}
	/**
	 * 增加试题相关操作
	 * @param question
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="QustionCtrl.save",method=RequestMethod.POST)
	public String save(Question question)throws Exception{
		question.setCreate_date(Mydate.now());
		//判断是否是填空题，如果是填空题，则计算有多少空格
		if(question.getType()==2) {
			int number=0;
			if(question.getAnswer_a().equals("")&&question.getAnswer_a().equals(null)) {
				number++;
			}
			if(question.getAnswer_b().equals("")&&question.getAnswer_b().equals(null)) {
				number++;
			}
			if(question.getAnswer_c().equals("")&&question.getAnswer_c().equals(null)) {
				number++;
			}
			if(question.getAnswer_d().equals("")&&question.getAnswer_d().equals(null)) {
				number++;
			}
			question.setNumber(number);
		}else {
			question.setNumber(1);
		}
		
		boolean flag = true;
		String error="";
		question = dbHelper.save(question);
		if(question==null) {
			error="操作失败";
			flag=false;
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("succ", flag);
		jsonObject.put("error", error);
		return jsonObject.toString();
		
	}
	/**
	 * 题目删除相关操作
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="QustionCtrl.del",method=RequestMethod.POST)
	public String del(int id)throws Exception{
		boolean flag = true;
		String error="";
		String sql="delete from question where id="+id;

		int reslut =  dbHelper.deleteSql(sql,null);
		if(reslut==0) {
			error="删除失败";
			flag=false;
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("succ", flag);
		jsonObject.put("error", error);
		return jsonObject.toString();
	}
	
}
