package shiyan.controller;





import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



import net.sf.json.JSONObject;
import shiyan.dao.Userdao;
import shiyan.db.DbHelper;
import shiyan.table.Tuser;

/**
 * 登录相关操作
 * @author anstar
 *
 */

@RestController
public class LoginCtrl{
	@Autowired protected HttpServletRequest request;
	@Autowired private DbHelper dbhelper;
	/**
	 * 登录过程
	 * @param tuser
	 * @return
	 * @throws Exception
	 */
    @RequestMapping(value="/LoginCtrl.login",method=RequestMethod.POST)
    public  String  login(Tuser tuser)throws Exception {
    	
		Tuser loginUser = Userdao.findUser(tuser.getUsername(), tuser.getPassword(), dbhelper);
		JSONObject json = new JSONObject();
		if(loginUser==null) {
			
			json.put("succ", false);
			json.put("error", "用户名不正确或者密码错误.");
			
			return json.toString();
		}

		json.put("succ", true);
		request.getSession().setAttribute("loginUser", loginUser);
        return json.toString();
    }
    /**
     * 登出
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/LoginCtrl.outlogin")
    public String logout() throws Exception{
		request.getSession().removeAttribute("loginUser");
		JSONObject json = new JSONObject();	
		json.put("succ", true);
		return json.toString();
	}
    /**
     * 获取后台中保存的登录信息
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/LoginCtrl.getlogin")
	public String getLoginUser() throws Exception{
		Tuser loginUser = (Tuser)request.getSession().getAttribute("loginUser");
		JSONObject json = new JSONObject();
		if(loginUser==null) {
			json.put("succ", false);
			json.put("error",  "登入信息不存在");
			return json.toString();
		}

		json.put("succ", true);
		json.put("loginUser", loginUser);
		return json.toString();
    }

}