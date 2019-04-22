package shiyan.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import shiyan.db.DbHelper;
import shiyan.db.Pagination;
import shiyan.ga.Ga;
import shiyan.ga.Population;
import shiyan.model.PaperModel;
import shiyan.sys.Mydate;
import shiyan.table.Paper;
import shiyan.table.Question;
import shiyan.table.Rule;
import shiyan.table.Testpaper;
import shiyan.table.Tuser;
//这个Ctrl偏离轨道，EMMM，可能是因为自己菜的很真实
@RestController
public class TestpaperCtrl {
	@Autowired protected HttpServletRequest request;
	@Autowired DbHelper dbHelper;
	private Tuser loginUser ;
	/**
	 * 查询操作
	 * @return 返回根据条件查询返回数据组
	 * @throws Exception
	 */
	@RequestMapping(value="TestpaperCtrl.teacherSearch",method=RequestMethod.POST)
	public String teachersearch()throws Exception{
		
		Pagination<Paper> pagination = new Pagination<>(1, 10);
		loginUser = (Tuser)request.getSession().getAttribute("loginUser");
		
		pagination.appendWhere("where teacherid = "+loginUser.getId());
		//查询Paper主表
		pagination = dbHelper.search(Paper.class, pagination);
		List<Paper> papers = pagination.getRows();
		//存入相关数据
		for(Paper paper :papers) {
			paper.setRule(dbHelper.findByPK(Rule.class, paper.getRuleid()));
		}
		JSONArray jsonArray = JSONArray.fromObject(papers);
		
		return jsonArray.toString();
	}
	
	@RequestMapping(value="TestpaperCtrl.studentSearch",method=RequestMethod.POST)
	public String studentsearch()throws Exception{
		
		Pagination<Paper> pagination = new Pagination<>(1, 10);
		loginUser = (Tuser)request.getSession().getAttribute("loginUser");
		
		pagination.appendWhere("where teacherid = "+loginUser.getId()+" and releasetype=1");
		//查询Paper主表
		pagination = dbHelper.search(Paper.class, pagination);
		List<Paper> papers = pagination.getRows();
		//存入相关数据
		for(Paper paper :papers) {
			paper.setRule(dbHelper.findByPK(Rule.class, paper.getRuleid()));
		}
		JSONArray jsonArray = JSONArray.fromObject(papers);
		
		return jsonArray.toString();
	}
	/**
	 * 查询单个数据
	 * @return 返回单个的相关信息
	 * @throws Exception
	 */
	@RequestMapping(value="TestpaperCtrl.presave",method=RequestMethod.POST)
	public String presave(int paperid)throws Exception{
		Pagination<Testpaper> pagination = new Pagination<>();
		pagination.appendWhere("where paperid = ?");
		pagination.addWhereValue(paperid);
		pagination =  dbHelper.search(Testpaper.class, pagination);
		
		List<Testpaper> list = pagination.getRows();
		for(Testpaper testpaper:list) {
			testpaper.setQuestion(dbHelper.findByPK(Question.class, testpaper.getQuestionid()));
		}
		JSONArray jsonArray  = JSONArray.fromObject(list);
		
		return jsonArray.toString();
	}
	/**
	 * 增加或者修改操作（insert或者是update）
	 * @return 操作是否正确执行
	 * @throws Exception
	 */
	@RequestMapping(value="TestpaperCtrl.save",method=RequestMethod.POST )
	public String save(String  papername,Rule rule,int paperid)throws Exception{
		loginUser = (Tuser)request.getSession().getAttribute("loginUser");
		PaperModel resultPaper = null;
		// 迭代计数器
        int count = 0;
        int runCount = 4;
        // 适应度期望值
        double expand = 0.99;
        //存放可以保存到数据库中的ID集合
        rule.setIdpack(rule.getPointIds().toString().substring(1, rule.getPointIds().toString().indexOf("]")));
        rule  =  dbHelper.save(rule);
        //生成试卷放入resultPaper中
        Population population;
        if (rule != null) {
            // 初始化种群
             population = new Population(dbHelper,20, true, rule);
            System.out.println("初次适应度  " + population.getFitness().getAdaptationDegree());
            while (count < runCount && population.getFitness().getAdaptationDegree() < expand) {
                count++;
                population = Ga.evolvePopulation(dbHelper,population, rule);
                System.out.println("第 " + count + " 次进化，适应度为： " + population.getFitness().getAdaptationDegree());
            }
            System.out.println("进化次数： " + count);
            System.out.println(population.getFitness().getAdaptationDegree());
            resultPaper = population.getFitness();
        }
        //存储试卷主体或者更新试卷主体
        Paper paper =  new Paper();
        paper.setRuleid(rule.getId());
        paper.setTeacherid(loginUser.getId());
        paper.setTittle(papername);
        paper.setCreate_date(Mydate.now());
        paper.setId(paperid);
        paper.setReleasetype(0);
        if(rule.getSubjectiveNum()>0) {
        	paper.setAutomark(1);
        }else {
			paper.setAutomark(0);
		}
        paper = dbHelper.save(paper);
        //如果存在老的试卷详情，那么首先清除试卷详情
        dbHelper.deleteSql("delete from testpaper where paperid= "+paper.getId(), null);
        /* 错误方法，会导致连接数到达上限还是怎么的，反正就是会死机，王冰老师的方法在处理数据量较大的时候容易死机
     	Testpaper testpaper = new Testpaper();
        testpaper.setTeacherid(loginUser.getId());
        testpaper.setPaperid(paper.getId());
        for(Question question : resultPaper.getQuestionList()) {
        	System.out.println("可用内存："+Runtime.getRuntime().freeMemory());
        	testpaper.setScore(question.getScore());
        	testpaper.setQuestionid(question.getId());
        	testpaper.setId(0);
        	dbHelper.save(testpaper);
        }*/
        //使用jdbc直接插入多条数据
        JdbcTemplate jdbcTemplate = dbHelper.getJdbcTemplate();
        String sqlString="insert into testpaper(teacherid,paperid,questionid,score) value("
        		+ loginUser.getId()+","+paper.getId()+",?,?)";
        
        List<Object[]> params = new ArrayList<>();
        for(Question question : resultPaper.getQuestionList()) {
   
        	params.add(new Object[]{question.getId(),question.getScore()});
   
        }
        jdbcTemplate.batchUpdate(sqlString, params);
        JSONObject jsonObject = JSONObject.fromObject(resultPaper);
        return jsonObject.toString();
        
	}


	/**
	 * 删除操作
	 * @return 删除操作是否正确执行
	 * @throws Exception
	 */
	@RequestMapping(value="TestpaperCtrl.del" ,method=RequestMethod.POST)
	public String del(@RequestParam(value = "list[]")Integer[] list)throws Exception{
		boolean flag = true;
		String error="";	
		//做循环删除主表中的信息并删除相关附表中的信息
		for(int i=0;i<list.length;i++) {
			String sql1="delete from testpaper where paperid="+list[i];
			String sql2="delete from rule where id=(select ruleid from paper where id="+list[i]+")";
			String sql3="delete from paper where id="+list[i];
			
			int reslut = dbHelper.deleteSql(sql1,null);
			if(reslut==0) {
				error="删除失败";
				flag=false;
				break;
			}
			reslut = dbHelper.deleteSql(sql2,null);
			if(reslut==0) {
				error="删除失败";
				flag=false;
				break;
			}
			reslut = dbHelper.deleteSql(sql3,null);
			if(reslut==0) {
				error="删除失败";
				flag=false;
				break;
			}
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("succ", flag);
		jsonObject.put("error", error);
		return jsonObject.toString();

	}
	
	@RequestMapping(value="TestpaperCtrl.changeRelease",method=RequestMethod.POST)
	public String changeRelease(int id,int type)throws Exception{
		String error="报错";
		boolean flag=false;
		JdbcTemplate jdbcTemplate = dbHelper.getJdbcTemplate();
		String sql="update paper set releasetype = "+type+" where id ="+id;
		int i=0;
		i =jdbcTemplate.update(sql);
		if(i>0) {
			flag=true;
			error="";
		}
		System.out.println(i);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("succ", flag);
		jsonObject.put("error", error);
		return jsonObject.toString();
		
	}
	
}
