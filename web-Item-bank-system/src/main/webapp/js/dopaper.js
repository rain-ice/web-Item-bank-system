var dopaper_quesitontmpl1,dopaper_quesitontmpl,dopaper_quesitontmpl3,dopaper_quesitontmpl4;
var dopaper;
var nowquestion;
var paperid
//颜色 mdui-color-red mdui-color-orange mdui-color-blue

var dopapersys = function(data,dopaperid) {
	dopaper=data;
	this.dopaperid=dopaperid;
	console.log(data);
	if(dopaper_quesitontmpl1==null){
		dopaper_quesitontmpl1= jqutils.loadHtml('list/dopaper_question1.html');
		
	}
	if(dopaper_quesitontmpl==null){
		dopaper_quesitontmpl= jqutils.loadHtml('list/dopaper_question.html');
		
	}
	if(dopaper_quesitontmpl3==null){
		dopaper_quesitontmpl3= jqutils.loadHtml('list/dopaper_quesiton3.html');
		
	}
	if(dopaper_quesitontmpl4==null){
		dopaper_quesitontmpl4= jqutils.loadHtml('list/dopaper_quesiton4.html');
		
	}
	
	var cardhtml="";
	var cardtmpl;
	for(var i=0;i<dopaper.dotestpapers.length;i++){
		
		if(dopaper.dotestpapers[i].score==0){
			cardtmpl='<li class="mdui-color-red"><a href="javascript:void(0)" onclick="dopaper_loadQustion('+i+')">'+(i+1)+'</a></li>';
		}else if(dopaper.dotestpapers[i].score==dopaper.testpapers[i].score){
			cardtmpl='<li class="mdui-color-blue"><a href="javascript:void(0)" onclick="dopaper_loadQustion('+i+')">'+(i+1)+'</a></li>';
		}else{
			cardtmpl='<li class="mdui-color-orange"><a href="javascript:void(0)" onclick="dopaper_loadQustion('+i+')">'+(i+1)+'</a></li>';
		}
		
		cardhtml += jqutils.tmpl(cardtmpl,dopaper.dotestpapers.question);
	}
	$('#dopaperScore').html(dopaper.score);
	$('#doQuestioncard').html(cardhtml);
	dopaper_loadQustion(0);
	
}


var dopaper_loadQustion = function(index) {
	nowquestion=index;
	var question = dopaper.testpapers[index].question;
	var studentAnswer='';
	var answer=''
	if(question.type==2){		
		if(dopaper.dotestpapers[index].answer_a!=''){
			studentAnswer+=dopaper.dotestpapers[index].answer_a;
			answer+=dopaper.testpapers[index].question.answer_a;
		}
		if(dopaper.dotestpapers[index].answer_b!=''){
			studentAnswer+='、'+dopaper.dotestpapers[index].answer_b;
			answer+=dopaper.testpapers[index].question.answer_b;
		}
		if(dopaper.dotestpapers[index].answer_c!=''){
			studentAnswer+='、'+dopaper.dotestpapers[index].answer_c;
			answer+=dopaper.testpapers[index].question.answer_c;
		}
		if(dopaper.dotestpapers[index].answer_d!=''){
			studentAnswer+='、'+dopaper.dotestpapers[index].answer_d;
			answer+=dopaper.testpapers[index].question.answer_d
		}
		question.answer=answer;
	}else{
		studentAnswer=dopaper.dotestpapers[index].answer;
	}
	question.studentAnswer = studentAnswer;
	console.log(question);
	var html="";
	if(LoginUser.type==2){
		html+='<button class="mdui-btn mdui-btn-icon" style="float: right;" onclick="addmistake('+question.id+')"><i class="mdui-icon material-icons">star_border</i></button>'
	}
	if (question.type==1) {
		html += jqutils.tmpl(dopaper_quesitontmpl1,question);
	}else{
		html += jqutils.tmpl(dopaper_quesitontmpl,question);
	}
		
		
	$('#dopaper_question').html(html);
	
}

