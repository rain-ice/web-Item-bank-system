var question_tmpl1,question_tmpl2,question_tmpl3,question_tmpl4;
var mistakes_data={};



var studentmistakesys = function() {
	if(question_tmpl1==null){
		question_tmpl1 = jqutils.loadHtml('list/mistake_question1.html');
	}
	if(question_tmpl2==null){
		question_tmpl2 = jqutils.loadHtml('list/mistake_question2.html');
	}
	if(question_tmpl3==null){
		question_tmpl3 = jqutils.loadHtml('list/mistake_question3.html');
	}
	if(question_tmpl4==null){
		question_tmpl4 = jqutils.loadHtml('list/mistake_question4.html');
	}
	
	
	
	
	
	
	mistakes_data.page=1;
	searchmistakes();
	
}

var searchmistakes = function() {
	
	var model = jqutils.loadJson('MistakesCtrl.search',mistakes_data);

	console.log(model);
	var html='';
	//根据题目的分类分别使用不同的tmpl来加载
	for(var i=0;i<model.rows.length;i++){
		if(model.rows[i].dorecord==null){
			model.rows[i].dorecord={no:'没有记录'};
		}
		if(model.rows[i].question.type==1){
			html+= jqutils.tmpl(question_tmpl1,model.rows[i]);
		}
		if(model.rows[i].question.type==2){
			html+= jqutils.tmpl(question_tmpl2,model.rows[i]);
		}
		if(model.rows[i].question.type==3){
			html+= jqutils.tmpl(question_tmpl3,model.rows[i]);
		}
		if(model.rows[i].question.type==4){
			html+= jqutils.tmpl(question_tmpl4,model.rows[i]);
		}
	}
	searchmistakes.totalpage = model.totalpage;
	searchmistakes.page = model.page;
	$('input[name=page]').val(model.page);
	$('#totalpage').text(model.totalpage);
	$('.mdui-panel').html(html);
	mdui.mutation();
	
}
//添加错题
var addmistake = function(questionid) {
	result = jqutils.loadJson('MistakesCtrl.save',{questionid:questionid});
	if(result.succ){
		alert('添加成功');
	}else{
		alert(result.error);
	}
}

//删除错题
var mistakes_del = function(mistakeid) {
	var result = jqutils.loadJson('MistakesCtrl.del',{mistakeid:mistakeid});
	if(result.succ){
		alert('删除成功');
		searchmistakes();
	}else{
		alert(result.error);
	}
}




//下一页
var mistake_nextpage = function(){
	if(mistakes_data.page>=mistakes_data.totalpage){
		return;
	}

	mistakes_data.page=mistakes_data.page+1;
	searchmistakes();
	
}
//上一页
var mistake_previouspage = function() {
	if(mistakes_data.page<=1){
		return;
	}
	mistakes_data.page=mistakes_data.page-1;
	searchmistakes();
}




