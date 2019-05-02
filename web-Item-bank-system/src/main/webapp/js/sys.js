//获取后台已经保存在Session中的用户
var LoginUser;
var pointsmodel;
$(function(){
	var result = jqutils.loadJson('LoginCtrl.getlogin');
	if(result.succ){
		LoginUser = result.loginUser;
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
var searchTestpaper = function(id) {
	
	var model = jqutils.loadJson("TestpaperCtrl.presave",{paperid:id});
//	获取试卷数据跳转到相关页面
	loadhtml('testpaper');
	testpapersys(model,id);
}


//加载相关主页
var loadhtml = function(url){
	var htm = 'fu/'+url+'.html';
	console.log(url);
	var html = jqutils.loadHtml(htm);
	$('#content-main').html(html);

}




