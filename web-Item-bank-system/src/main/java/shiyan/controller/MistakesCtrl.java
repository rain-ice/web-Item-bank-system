package shiyan.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import net.sf.json.JSONObject;
import shiyan.db.DbHelper;
import shiyan.db.Pagination;
import shiyan.table.Dorecord;
import shiyan.table.Mistakes;
import shiyan.table.Points;
import shiyan.table.Question;
import shiyan.table.Tuser;

@RestController
public class MistakesCtrl {

	@Autowired protected HttpServletRequest request;
	@Autowired DbHelper dbHelper;
	
	@RequestMapping(value="MistakesCtrl.search",method=RequestMethod.POST)
	public String search(int page)throws Exception{
		Tuser tuser = (Tuser) request.getSession().getAttribute("loginUser");
		
		Pagination<Mistakes> pagination = new Pagination<>(1,10);
		pagination.setPage(page);
		pagination.appendWhere("where studentid = "+tuser.getId());
		pagination = dbHelper.search(Mistakes.class, pagination);
		
		for(Mistakes mistakes:pagination.getRows()) {
			mistakes.setQuestion(dbHelper.findByPK(Question.class, mistakes.getQuestionid()));
			mistakes.setDorecord(dbHelper.query(Dorecord.class, "select * from dorecord where studentid="+tuser.getId()+" and questionid="+mistakes.getQuestionid()));
			mistakes.getQuestion().setPointname(dbHelper.findByPK(Points.class, mistakes.getQuestion().getPointid()).getName());
		}
		
		
		JSONObject jsonObject = JSONObject.fromObject(pagination);
		return jsonObject.toString();
		
		
	}
	
	
	
	
	@RequestMapping(value="MistakesCtrl.save",method=RequestMethod.POST)
	public String save(int questionid)throws Exception{
		Tuser tuser = (Tuser) request.getSession().getAttribute("loginUser");
		boolean flag = true;
		String error="";
		JdbcTemplate jdbcTemplate = dbHelper.getJdbcTemplate();
		Mistakes mistakes =  dbHelper.query(Mistakes.class, "select * from mistakes where questionid = "+questionid+" and studentid="+tuser.getId());
		if(mistakes==null) {
			jdbcTemplate.update("insert into mistakes(studentid,questionid) values("+tuser.getId()+","+questionid+")");
		}else {
			flag=false;
			error="已经存在这个错题";
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("succ", flag);
		jsonObject.put("error", error);
		return jsonObject.toString();		
		
	}
	
	@RequestMapping(value="MistakesCtrl.del",method=RequestMethod.POST)
	public String del(int mistakeid)throws Exception{
		boolean flag=false;
		String error="操作失败";
		int number =  dbHelper.deleteSql("delete from mistakes where id="+mistakeid, null);
		if(number>0) flag=true;
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("succ", flag);
		jsonObject.put("error", error);
		return jsonObject.toString();
		
	}
	
}
