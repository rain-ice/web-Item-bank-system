package shiyan.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import shiyan.db.DbHelper;
import shiyan.db.Pagination;
import shiyan.sys.Mydate;
import shiyan.table.Dopaper;
import shiyan.table.Dotestpaper;
import shiyan.table.Paper;
import shiyan.table.Question;
import shiyan.table.Testpaper;
import shiyan.table.Tuser;
@RestController
public class DopaperCtrl {
	@Autowired protected HttpServletRequest request;
	@Autowired DbHelper dbHelper;
	/**
	 * 教师查询操作
	 * @return 返回根据条件查询返回数据组
	 * @throws Exception
	 */
	@RequestMapping(value="DopaperCtrl.teacher_search",method = RequestMethod.POST)
	public String teachersearch(int  page,int paperid)throws Exception{
		Tuser tuser = (Tuser) request.getSession().getAttribute("loginUser");
		String paperidPack="";
		String where_dopaper="";
		if(paperid<=0) {
		String where_paper="where teacherid = "+tuser.getId();
		Pagination<Paper> pagination_paper = new Pagination<>();	
		pagination_paper.appendWhere(where_paper);
		pagination_paper=dbHelper.search(Paper.class, pagination_paper);
		for(Paper paper:pagination_paper.getRows()) {
			paperidPack+=paper.getId()+",";
		}
		if(!paperidPack.equals("")) {
			paperidPack = paperidPack.substring(0,paperidPack.length() - 1);
	       }
		where_dopaper =" where paperid in ("+paperidPack+") and type=1";
		}else {
			where_dopaper = "where paperid = "+paperid;
		}
		
		

		Pagination<Dopaper> pagination_dopaper = new Pagination<>(page,10);
		pagination_dopaper.appendWhere(where_dopaper);
		pagination_dopaper.appendOrderBy("order by create_date desc");
		pagination_dopaper = dbHelper.search(Dopaper.class, pagination_dopaper);
		JSONObject jsonObject = JSONObject.fromObject(pagination_dopaper);
		
		return jsonObject.toString();
	}
	/**
	 * 学生查询操作
	 * @return 返回根据条件查询返回数据组
	 * @throws Exception
	 */
	@RequestMapping(value="DopaperCtrl.student_search",method = RequestMethod.POST)
	public String studentsearch(int page)throws Exception{
		Tuser tuser = (Tuser) request.getSession().getAttribute("loginUser");
		Pagination<Dopaper> pagination = new Pagination<>(page,10);	
		pagination.appendWhere("where studentid = "+tuser.getId()+" and type=0");
		pagination.appendOrderBy("order by id desc");
		pagination =  dbHelper.search(Dopaper.class, pagination);
		List<Dopaper> list = pagination.getRows();
		for(Dopaper dopaper:list) {
			dopaper.setPaper(dbHelper.findByPK(Paper.class, dopaper.getPaperid()));
		}
		pagination.setRows(list);
		JSONObject jsonObject = JSONObject.fromObject(pagination);
		return jsonObject.toString();
	}
	
	
	/**
	 * 查询单个数据
	 * @return 返回单个的相关信息
	 * @throws Exception
	 */
	@RequestMapping(value="DopaperCtrl.presave")
	public String presave()throws Exception{
		return "操作之前的检索";
	}
	/**
	 * 增加或者修改操作（insert或者是update）
	 * @return 操作是否正确执行
	 * @throws Exception
	 */
	@RequestMapping(value="DopaperCtrl.save",method=RequestMethod.POST)
	public String save(String list,int paperid)throws Exception{
		
		Tuser tuser = (Tuser) request.getSession().getAttribute("loginUser");
		System.out.println(tuser.toString());
		JSONArray jsonArray = JSONArray.fromObject(list);
		System.out.println(jsonArray.toString());
		List<Dotestpaper> dotestpapers = JSONArray.toList(jsonArray, new Dotestpaper(), new JsonConfig());
		double total=0;
		
		Paper paper = dbHelper.findByPK(Paper.class, paperid);
		
		//存储主数据表
		Dopaper dopaper = new Dopaper();
		dopaper.setStudentid(tuser.getId());
		dopaper.setCreate_date(Mydate.now());
		dopaper.setType(paper.getAutomark());
		dopaper.setPaperid(paperid);
		dopaper =  dbHelper.save(dopaper);
		//创建需要inert的数组
		List<Object[]> params = new ArrayList<>();
		JdbcTemplate jdbcTemplate = dbHelper.getJdbcTemplate();
		String sqlString="insert into dotestpaper(dopaperid,testpaperid,answer,answer_a,answer_b,answer_c,answer_d) value("
        		+ dopaper.getId()+",?,?,?,?,?,?)";
        
		System.out.println("当前位置数量："+dotestpapers.size());
		for(Dotestpaper dotestpaper:dotestpapers) {
			dotestpaper.setId(0);
			dotestpaper.setDopaperid(dopaper.getId());
			total+=ProofreadingQuestion(dotestpaper);
			params.add(new Object[]{dotestpaper.getTestpaperid(),dotestpaper.getAnswer(),
					dotestpaper.getAnswer_a(),dotestpaper.getAnswer_b(),dotestpaper.getAnswer_c(),dotestpaper.getAnswer_d()});
			
		}
		
        jdbcTemplate.batchUpdate(sqlString, params);
		
		
		dopaper.setScore(total);
		dopaper = dbHelper.save(dopaper);
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("succ", true);
		
		
		return jsonObject.toString();
	}
	/**
	 * 删除操作，只有管理员能够操作
	 * @return 删除操作是否正确执行
	 * @throws Exception
	 */
	@RequestMapping(value="DopaperCtrl.del")
	public String del()throws Exception{
		return "删除操作";
	}
	/**
	 * 计算出分数并返回，同时存储信息到副表中
	 * @param dotestpaper
	 * @return
	 * @throws Exception
	 */
	private double ProofreadingQuestion (Dotestpaper dotestpaper)throws Exception{
		double score=0;
		
		Testpaper testpaper = dbHelper.findByPK(Testpaper.class, dotestpaper.getTestpaperid());
		System.out.println(testpaper.toString());
		Question question = dbHelper.findByPK(Question.class,testpaper.getQuestionid());
		//如果是选择或者是判断题
		if(question.getType()==1||question.getType()==3) {
			if(question.getAnswer().equals(dotestpaper.getAnswer())) {
				score+=testpaper.getScore();
			}
		}
		//这里的代码是否可以用映射代替EMMM
		else if(question.getType()==2) {
			double answerScore = testpaper.getScore()/(double)question.getNumber();
			if(question.getAnswer_a()!=null&&question.getAnswer_a()!="") {
				if(question.getAnswer_a().equals(dotestpaper.getAnswer_a())) {
					score+=answerScore;
				}
			}
			if(question.getAnswer_b()!=null&&question.getAnswer_b()!="") {
				if(question.getAnswer_b().equals(dotestpaper.getAnswer_b())) {
					score+=answerScore;
				}
			}
			if(question.getAnswer_c()!=null&&question.getAnswer_c()!="") {
				if(question.getAnswer_c().equals(dotestpaper.getAnswer_c())) {
					score+=answerScore;
				}
			}
			if(question.getAnswer_d()!=null&&question.getAnswer_d()!="") {
				if(question.getAnswer_d().equals(dotestpaper.getAnswer_d())) {
					score+=answerScore;
				}
			}
			
		}	
		
		return score;
		
		
	}

}
