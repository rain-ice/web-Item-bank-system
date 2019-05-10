package shiyan.controller;

import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import net.sf.json.JSONObject;
import shiyan.db.DbHelper;
import shiyan.table.Dorecord;
import shiyan.table.Question;
import shiyan.table.Tuser;

@RestController
public class BrushQuestionCtrl {
	@Autowired protected HttpServletRequest request;
	@Autowired DbHelper dbHelper;
	
	@RequestMapping(value="BrushQuestionCtrl.search",method = RequestMethod.POST)
	public String search()throws Exception{
		
		long count = dbHelper.total(Question.class);
		int index =  (int) (Math.random()*count);
		Question question = dbHelper.findByPK(Question.class,index);
		JSONObject jsonObject = JSONObject.fromObject(question);
		return jsonObject.toString();
	}
	
	@RequestMapping(value="BrushQuestionCtrl.save",method = RequestMethod.POST)
	public String save(int questionid,boolean type)throws Exception{
		Tuser tuser = (Tuser) request.getSession().getAttribute("loginUser");
		boolean flag=false;
		String error="出现错误";
		JdbcTemplate jdbcTemplate = dbHelper.getJdbcTemplate();
		
		Dorecord dorecord =  dbHelper.query(Dorecord.class,"select * from dorecord where studentid = "+tuser.getId()+" and questionid="+questionid);
		if(dorecord==null) {
			int number;
			if(type) {
				number = jdbcTemplate.update("insert into dorecord(studentid,questionid,yes) values("+tuser.getId()+","+questionid+",1)");
			}else {
				number = jdbcTemplate.update("insert into dorecord(studentid,questionid,no) values("+tuser.getId()+","+questionid+",1)");
			}
			if(number>0) {
				flag=true;
			}
			
		}else {
			if(type) {
				dorecord.setYes(dorecord.getYes()+1);
			}else {
				dorecord.setNo(dorecord.getNo()+1);
			}
			 dbHelper.save(dorecord);
			 flag=true;
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("succ", flag);
		jsonObject.put("error", error);
		
		return jsonObject.toString();
	}

}
