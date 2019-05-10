//获取后台已经保存在Session中的用户
var LoginUser;
var pointsmodel;
$(function(){
	var result = jqutils.loadJson('LoginCtrl.getlogin');
	if(result.succ){
		LoginUser = result.loginUser;
		var inst = new mdui.Tooltip('#user', {
			  content: LoginUser.name
			});
	}else{
		location.href='index.html';
	}

});

//登出
var Logout = function() {
	var result = jqutils.loadJson('LoginCtrl.outlogin');
	if(result.succ) location.href='index.html';

	
}

//查询所有知识分类 并存入相应位置
var	loadpoints  = function(tmpl,position) {
	if(pointsmodel==null){
		pointsmodel =  jqutils.loadJson('PointsCtrl.all',null);
	}
	
	console.log(pointsmodel);
	var html = jqutils.tmpl(tmpl,pointsmodel);
	$('#'+position).html(html);
	mdui.mutation();

}

//跳转到试卷页面
var searchTestpaper  = function(id) {
	
	var model = jqutils.loadJson("TestpaperCtrl.presave",{paperid:id});
//	获取试卷数据跳转到相关页面
	loadhtml('testpaper');
	testpapersys(model,id);
}

//跳转到试卷记录的详情页面
var searchDopaper = function(id) {
	var model = jqutils.loadJson("DopaperCtrl.presave",{dopaperid:id});
//	获取试卷数据跳转到相关页面
	loadhtml('dopaper');
	dopapersys(model,id);
}


//加载相关主页
var loadhtml = function(url){
	var htm = 'fu/'+url+'.html';
	console.log(url);
	var html = jqutils.loadHtml(htm);
	$('#content-main').html(html);
	deltooltip();
}

var deltooltip = function() {
	$('.mdui-tooltip-open').remove();
}


var modifyUser = function() {
	var inst = new mdui.Dialog('#dialog');
	$('.mdui-dialog-title').html('修改信息');
	html=jqutils.loadHtml('list/modifyUserlist.html');
	$('.mdui-dialog-content').html(html);
	jqutils.formLoad('form1',LoginUser);
	inst.open();
	
	$('#teacher_save').click(function() {
		saveUser(inst);
	});
}

var saveUser = function(inst){
	data = jqutils.formData('form1');
	console.log(data);
	var reslut = jqutils.loadJson("TuserCtrl.save",data);
	if(reslut.succ){
		inst.close();
	}else{
		alert(reslut.error);
	}
		
}
		





