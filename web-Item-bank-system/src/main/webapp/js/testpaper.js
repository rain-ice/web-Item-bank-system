var test_quesitontmpl1,test_quesitontmpl2,test_quesitontmpl3,test_quesitontmpl4;
var answerResult;
var testquestionlist;
var nowquestion;
var paperid
//初始化答题界面的时候
var testpapersys = function(data,paperid) {
	testquestionlist=data;
	this.paperid=paperid;
	console.log(testquestionlist);
	answerResult = new Array(data.length);
	if(LoginUser.type==1){
		$('#submitPaperbtn').remove();
	}
	if(test_quesitontmpl1==null){
		test_quesitontmpl1= jqutils.loadHtml('list/test_quesitont1.html');
		
	}
	if(test_quesitontmpl2==null){
		test_quesitontmpl2= jqutils.loadHtml('list/test_quesitont2.html');
		
	}
	if(test_quesitontmpl3==null){
		test_quesitontmpl3= jqutils.loadHtml('list/test_quesitont3.html');
		
	}
	if(test_quesitontmpl4==null){
		test_quesitontmpl4= jqutils.loadHtml('list/test_quesitont4.html');
		
	}
	
	var cardhtml="";
	var cardtmpl;
	for(var i=0;i<testquestionlist.length;i++){
		cardtmpl='<li ><a href="javascript:void(0)" onclick="loadQustion('+i+')">'+(i+1)+'</a></li>';
		cardhtml += jqutils.tmpl(cardtmpl,testquestionlist[i].question);
	}
	$('#questioncard').html(cardhtml);
	loadQustion(0);
	
	
}


var loadQustion = function(index){
	nowquestion=index;
	var question = testquestionlist[index].question;
	console.log(question);
	var html;
	switch (question.type) {	
	case 1:
		html = jqutils.tmpl(test_quesitontmpl1,question);
		break;
	case 2:
		html = jqutils.tmpl(test_quesitontmpl2,question);
		break;
	case 3:
		html = jqutils.tmpl(test_quesitontmpl3,question);
	break;
	case 4:
		html = jqutils.tmpl(test_quesitontmpl4,question);
	break;
	}
	$('input[name=type]').val(question.type);
	$('#testpaper_question').html(html);
	//如果已经做个这题，那么能够直接从数组中获取这个数据
	if(answerResult[index]!=null){
		$('input').each(function(){
			//根据类别使用不同的方式传递数据
			if($(this).attr("type")=='radio'&&$(this).val()==answerResult[index]['answer']){
				console.log($(this).val());
				console.log(answerResult[index]['answer']);
				$(this).attr('checked','checked');
			}else {
				$(this).val(answerResult[index][$(this).attr("name")]);
			}
		});
	}
	//对填空题做出判断，查看有几个空格拥有数据
	if(question.type==2){
		if(question.answer_b==null||question.answer_b==''){
			$('input[name=answer_b]').attr("disabled",true)
		}
		if(question.answer_c==null||question.answer_c==''){
			$('input[name=answer_c]').attr("disabled",true)
		}
		if(question.answer_d==null||question.answer_d==''){
			$('input[name=answer_d]').attr("disabled",true)
		}
	}
	
	//注入监听事件
	$('input').change(function(){
		saveAnswer($(this).val(),$(this).attr("name"),index);
	});
	$('textarea').change(function(){
		saveAnswer($(this).val(),$(this).attr("name"),index);
	});
	mdui.mutation();
}
//保存记录到数据集合中
var saveAnswer = function(data,name,index) {
	var answer={};
	if(name!='answer'){
		$('input').each(function(){
			if($(this).val()==null){
				answer[$(this).attr("name")]=""
			}else{
				answer[$(this).attr("name")]=$(this).val();
			}
		})
	}else {
		answer[name]=data;
	}
	answer.testpaperid=testquestionlist[index].id;
	answerResult[index]=answer;
	$('#questioncard li').eq(index).addClass("mdui-color-amber-300");
	
}
//提交试卷的答案
var submitPaper = function(){
	for(var i=0;i<answerResult.length;i++){
		if(answerResult[i]==null){
			alert('试卷中还有未完成的题目');
			return;
		}
	}
	if(!confirm("确定提交？")){
		return;
	}
	data={};
	data.list=jqutils.obj2json(answerResult);
	data.paperid=paperid;
	console.log(data);
	var result = jqutils.loadJson('DopaperCtrl.save',data);
	student_paper();
	answerResult=null;
}

var nextquestion = function(){
	if(nowquestion>=testquestionlist.length){
		return;
	}
	nowquestion++;
	loadQustion(nowquestion);
}

var prequestion = function(){
	if(nowquestion<=1){
		return;
	}
	nowquestion--;
	loadQustion(nowquestion);
}


