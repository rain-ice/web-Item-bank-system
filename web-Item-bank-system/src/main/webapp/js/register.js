

$(function(){
	//注入事件判断用户名是否可用
	 $('input[name=username]').change(function() {
		 $('#usernameError').text('用户名不可为空');
		 if($(this).val()!=null){
			 result = jqutils.loadJson('TuserCtrl.verification',{
				 username:$(this).val()
			 })
			 if(result.succ){
				 
				 $('#username').removeClass('mdui-textfield-invalid');
			 }else{
				 $('#usernameError').text('用户名已存在');
				 $('#username').addClass('mdui-textfield-invalid');
			 }
		 }
		
	});
	 //注入判断两次密码是否相同
	 $('input[name=password2]').change(function() {
		 if($(this).val()==$('input[name=password]').val()){
			 $('#password2').removeClass('mdui-textfield-invalid');
		 }else{
			 $('#password2').addClass('mdui-textfield-invalid');
		 }
	});
	 
	 	
});

var register = function() {
	var data = jqutils.formData('form1');
	console.log(data);
	result = jqutils.loadJson('TuserCtrl.save',data);
	if(result.succ){
		alert('注册成功');
		location.href='index.html';
	}else {
		alert('注册失败');
	}
}
