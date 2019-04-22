 $(function(){
	 home();
 });
 
 
/*进入时的HOME显示相关*/
var home = function() {
	var html = jqutils.loadHtml('fu/student_home.html');
	$('#content-main').html(html);
	
	var number =  showMistakesNumber();
	$('#mistakesnumber').text(number);
	setTimeout(function(){chuangjian();},50);
}

/*显示错题集中题目数量*/
var showMistakesNumber = function() {
	var number=jqutils.loadJson('MistakesCtrl.shownumber',{id:LoginUser.id});
	return number;
}






/*错题集相关内容*/
var mistakes = function() {
	var html = jqutils.loadHtml('fu/student_mistakes.html');
	$('#content-main').html(html);
}



/*根据题型测试*/



