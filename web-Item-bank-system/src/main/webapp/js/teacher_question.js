//选择填空等题目不同的下拉框tmpl
var question_tmpl1,question_tmpl2,question_tmpl3,question_tmpl4;
var question_data={};
//关于进入老师的题库界面的基础加载
var teacherquestionsys = function(){
	var tmpl = '<li class="mdui-list-item mdui-ripple" onclick="findquestionOfpoint(${id})"><div class="mdui-list-item-content">${name}</div></li>';
	loadpoints(tmpl,'pointslist');
	question_tmpl1 = jqutils.loadHtml('list/teacher_question1.html');
	question_tmpl2 = jqutils.loadHtml('list/teacher_question2.html');
	question_tmpl3 = jqutils.loadHtml('list/teacher_question3.html');
	question_tmpl4 = jqutils.loadHtml('list/teacher_question4.html');
	//初始化question_data变量
	question_data.page=1;
	question_data.teacherid=0;
	question_data.type=0;
	question_data.pointid=0;
	
	//添加page的回车动作
	$('#input_question_page').bind('keypress',function(event){   
        if(event.keyCode == "13"){   
            if($('#input_question_page').val()<=question_data.totalpage){
            	question_data.page=$('#input_question_page').val();
            	searchquestion();
            }
        }  
	});
	searchquestion();
	//加载上方标签
	qeustion_insert();
}

//查询题库的主体
var searchquestion = function() {
	console.log(question_data);
	var model = jqutils.loadJson('QustionCtrl.search',question_data);
	console.log(model);
	var html="";
	//根据题目的分类分别使用不同的tmpl来加载
	for(var i=0;i<model.list.length;i++){
		if(model.list[i].type==1){
			html+= jqutils.tmpl(question_tmpl1,model.list[i]);
		}
		if(model.list[i].type==2){
			html+= jqutils.tmpl(question_tmpl2,model.list[i]);
		}
		if(model.list[i].type==3){
			html+= jqutils.tmpl(question_tmpl3,model.list[i]);
		}
		if(model.list[i].type==4){
			html+= jqutils.tmpl(question_tmpl4,model.list[i]);
		}
	}
	question_data.totalpage = model.totalpage;
	question_data.page = model.page;
	$('input[name=page]').val(model.page);
	$('#totalpage').text(model.totalpage);
	$('.mdui-panel').html(html);
	mdui.mutation();
}
//点击左侧知识点的时候需要使用的方法
var findquestionOfpoint = function(pointid) {
	question_data.pointid=pointid;
	question_data.page=1;
	searchquestion();
}
//自己加入题库中的信息
var myquestion = function() {
	question_data.teacherid = LoginUser.id;
	question_data.page=1;
	searchquestion();
}
//删除相关信息
var question_del = function(id,teacherid){
	if(teacherid!=LoginUser.id){
		alert('非题目创建者，不可操作');
		return;
	}
	data={};
	data.id=id
	var reslut = jqutils.loadJson('QustionCtrl.del',data);
	if(reslut.succ){
		alert("删除成功");
	}else{
		alert(reslut.error);
	}
}

//修改或者新建问题，到相关页面执行
var modifyquestion = function(id,teacherid) {
	console.log(teacherid);
	if(teacherid!=LoginUser.id&&teacherid!=null){
		alert('非题目创建者，不可操作');
		return;
	}
	//执行newquestion的带参数传递操作
	var html = jqutils.loadHtml('fu/teacher_newquestion.html');
	$('#content-main').html(html);
	teachernewquestionsys(id);
}


//下一页
var question_nextpage = function(){
	console.log(question_data);
	if(question_data.page>=question_data.totalpage){
		return;
	}

	question_data.page=question_data.page+1;
	searchquestion();
	
}
//上一页
var question_previouspage = function() {
	if(question_data.page<=1){
		return;
	}
	question_data.page=question_data.page-1;
	searchquestion();
}

//插入上方文本
var qeustion_insert = function() {
	$('#pointnumber').text(pointsmodel.length);
	var result = jqutils.loadJson('QustionCtrl.all',null);
	var myquestion=0;
	for(var i=0;i<result.length;i++){
		if(result[i].teacherid==LoginUser.id){
			myquestion+=1;
		}
	}
	$('#questionnumber').text(result.length);
	$('#myquestionnumber').text(myquestion);
}

