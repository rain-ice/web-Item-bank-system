package shiyan.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

import model.User;
import shiyan.dao.userdao;



@Controller
@RequestMapping("/shiyan1")
public class Shiyan1 {
	
	@Autowired(required=false) private userdao nd;
	

    @RequestMapping("/shiyan2")
    
    public  String home() {
        return "../index.jsp";
    }
    @RequestMapping(value="/save",method =RequestMethod.POST)
    @ResponseBody
    public String uersave (User user) {

    	System.out.println(nd.namedao());
    	return user.getUsername();
    }

}