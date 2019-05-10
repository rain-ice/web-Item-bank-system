package shiyan.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import shiyan.db.DbHelper;
import shiyan.db.Pagination;
import shiyan.sys.Mydate;
import shiyan.table.Paper;
import shiyan.table.Tuser;
/**
 * 对于一些数据需要使用到orderby之类的crtl
 * @author anstar
 *
 */
@RestController
public class StatisticsCtrl {
	@Autowired protected HttpServletRequest request;
	@Autowired DbHelper dbHelper;
	/**
	 * 计算最新的5张试卷
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="StatisticsCtrl.searchNewPaper",method = RequestMethod.POST)
	public String searchNewPaper()throws Exception{
		Tuser tuser = (Tuser) request.getSession().getAttribute("loginUser");
		Pagination<Paper> pagination = new Pagination<>(1, 5) ;
		pagination.appendWhere("where teacherid = "+tuser.getId());
		pagination.appendOrderBy("order by create_date desc");
		pagination = dbHelper.search(Paper.class, pagination);
		JSONObject jsonObject = JSONObject.fromObject(pagination);
		return jsonObject.toString();
	}
	
	@RequestMapping(value="StatisticsCtrl.doPaperCount",method = RequestMethod.POST)
	public String doPaperCount()throws Exception{
		Tuser tuser = (Tuser) request.getSession().getAttribute("loginUser");
		JdbcTemplate jdbcTemplate = dbHelper.getJdbcTemplate();
		String now = Mydate.now();
		SimpleDateFormat myFmt=new SimpleDateFormat("yyyy-MM-dd"); 
		Date date = myFmt.parse(now);  

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -10);
        now = myFmt.format(calendar.getTime());
		String sql="select count(*) as total,create_date from dopaper where create_date>'"+now+"' group by create_date";
		System.out.println(sql);
		List<Map<String, Object>> s=jdbcTemplate.queryForList(sql);
		
		return s.toString();
		
	}
	

}
