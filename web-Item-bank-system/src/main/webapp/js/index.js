//登录
var login = function(){
	var data=jqutils.formData('form1');
	
	if(data.username==''){			
		alert('请输入用户名');
		jqutils.formItem('username','form1').focus();
		return;	
	}
	if(data.password==''){
		alert('请输入密码');
		jqutils.formItem('password','form1').focus();
		return;	
	}
	

	var result=jqutils.loadJson('LoginCtrl.login',data);
	if(result.succ){
		var user=jqutils.loadJson('LoginCtrl.getlogin');
		console.log(user.loginUser.type);
		if(user.succ){
			switch (user.loginUser.type) {
			case 99:
				location.href='admin.html';
				break;
			case 1:
				location.href='teacher.html';		
				break;
			case 2:
				location.href='student.html';
				break;
			}	
		}

	}else{
		alert(result.error);
	}
}

//注册
var register = function(){
	location.href='register.html';
}