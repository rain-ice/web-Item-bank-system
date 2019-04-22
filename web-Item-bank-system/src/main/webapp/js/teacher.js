$(function() {
	
	loadhtml('teacher_home');
	mdui.mutation();
});

//跳转页面或者是模态框？
var newQuestion = function(){
	
}


//加载相关主页
var loadhtml = function(url){
	var htm = 'fu/'+url+'.html';
	console.log(url);
	var html = jqutils.loadHtml(htm);
	$('#content-main').html(html);
	switch (url) {
	case 'teacher_question':
		teacherquestionsys();
		
		break;
	case 'teacher_paper':
		teacherpapersys();
		
		break;
	}

	
}
//查询自己写的试题
var searchMyQuestion = function() {

}

//创建试卷,难点
var newPaper = function(){

}

