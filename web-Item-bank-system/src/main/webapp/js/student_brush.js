var brush_quesitontmpl1,brush_quesitontmpl2,brush_quesitontmpl3,brush_quesitontmpl4;
var brush_question;
var studentbrushsys = function() {
	if(brush_quesitontmpl1==null){
		brush_quesitontmpl1= jqutils.loadHtml('list/test_quesitont1.html');
		
	}
	if(brush_quesitontmpl2==null){
		brush_quesitontmpl2= jqutils.loadHtml('list/test_quesitont2.html');
		
	}
	if(brush_quesitontmpl3==null){
		brush_quesitontmpl3= jqutils.loadHtml('list/test_quesitont3.html');
		
	}
	if(brush_quesitontmpl4==null){
		brush_quesitontmpl4= jqutils.loadHtml('list/test_quesitont4.html');
		
	}
	
	
	brushNewQuestion();
}

var brushNewQuestion = function() {
	brush_question = jqutils.loadJson('BrushQuestionCtrl.search',null);
	if(brush_question.type==2){
		var answer;
		if(brush_question.answer_a!=null){
			answer += brush_question.answer_a
		}
		if(brush_question.answer_b!=null){
			answer += '、'+brush_question.answer_b
		}
		if(brush_question.answer_c!=null){
			answer += '、'+brush_question.answer_c
		}
		if(brush_question.answer_d!=null){
			answer += '、'+brush_question.answer_d
		}
		brush_question.answer=answer;
		
	}
	console.log(brush_question);
	var html;
	switch (brush_question.type) {	
	case 1:
		html = jqutils.tmpl(brush_quesitontmpl1,brush_question);
		break;
	case 2:
		html = jqutils.tmpl(brush_quesitontmpl2,brush_question);
		break;
	case 3:
		html = jqutils.tmpl(brush_quesitontmpl3,brush_question);
	break;
	case 4:
		html = jqutils.tmpl(brush_quesitontmpl4,brush_question);
	break;
	}
	$('#brush_question').html(html);
	
	//对填空题做出判断，查看有几个空格拥有数据
	if(brush_question.type==2){
		if(brush_question.answer_b==null||brush_question.answer_b==''){
			$('input[name=answer_b]').attr("disabled",true)
		}
		if(brush_question.answer_c==null||brush_question.answer_c==''){
			$('input[name=answer_c]').attr("disabled",true)
		}
		if(brush_question.answer_d==null||brush_question.answer_d==''){
			$('input[name=answer_d]').attr("disabled",true)
		}
	}
	$('#tureanswer').empty();
	mdui.mutation();
	

}
var brushNextQustion = function() {
	if(brush_question.type==4){
		brushNewQuestion();
		return;
	}
	
	var brush_answer="";
	var data={};
	var type=false;
	data.questionid = brush_question.id;
	if(brush_question.type==2){
		if(brush_question.type==2){
			if($('input[name=answer_a]').val()!=null){
				brush_answer += $('input[name=answer_a]').val()
			}
			if($('input[name=answer_b]').val()!=null){
				brush_answer += '、'+$('input[name=answer_b]').val()
			}
			if($('input[name=answer_c]').val()!=null){
				brush_answer += '、'+$('input[name=answer_c]').val()
			}
			if($('input[name=answer_d]').val()!=null){
				brush_answer += '、'+$('input[name=answer_d]').val()
			}
		}
	
	}else{
		brush_answer = $('input[name=answer]').val()
	}
	if(brush_question.answer == brush_answer){
		data.type=true;
	}
	if(brush_answer!=null||brush_answer!=''||brush_answer!='、、、'){
		model = jqutils.loadJson('BrushQuestionCtrl.save',data);
	}
	
	
	brushNewQuestion();
}

var lookAnswer = function() {
	$('#tureanswer').text(brush_question.answer);
	$('#brush_question').prepend('<button class="mdui-btn mdui-btn-icon" style="float: right;" onclick="addmistake('+brush_question.id+')"><i class="mdui-icon material-icons">star_border</i></button>');
}