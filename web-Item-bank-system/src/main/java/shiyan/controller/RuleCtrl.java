package shiyan.controller;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import net.sf.json.JSONObject;
import shiyan.db.DbHelper;
import shiyan.table.Paper;
import shiyan.table.Rule;

@RestController
public class RuleCtrl {
	@Autowired DbHelper dbHelper;
	
	@RequestMapping(value="RuleCtrl.presave",method=RequestMethod.POST)
	public String presave(int id)throws Exception{
		Rule rule;
		rule = dbHelper.findByPK(Rule.class,id);
		rule.setPointIds(Arrays.asList(rule.getIdpack().split(",")));

		JSONObject jsonObject = JSONObject.fromObject(rule);

		return jsonObject.toString();
		
	}

}
