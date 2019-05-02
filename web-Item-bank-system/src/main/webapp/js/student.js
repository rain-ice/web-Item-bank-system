 $(function(){
	 student_home();
 });
 
 
/*进入时的HOME显示相关*/
var student_home = function() {
	loadhtml('student_home');
	
	var number =  showMistakesNumber();
	$('#mistakesnumber').text(number);
	var model = jqutils.loadJson('DopaperCtrl.student_search',{page:1});
	console.log(model);
	var score=[];
	var date=[];
	for(var i=0;i<model.rows.length;i++){
		score[i]=model.rows[i].score;
		date[i] = model.rows[i].create_date;
	}
	setTimeout(function(){student_scoreChart(score,date);},50);
}

var student_paper = function(){
	loadhtml('student_paper');
	studentpapersys();
	
}

/*显示错题集中题目数量*/
var showMistakesNumber = function() {
	var number=jqutils.loadJson('MistakesCtrl.shownumber',{id:LoginUser.id});
	return number;
}






/*错题集相关内容*/
var student_mistakes = function() {
	loadhtml('student_mistakes');
}

/*查看成绩*/
var student_score = function() {
	loadhtml('student_score');
	studentscoresys();
}

/*刷题*/
var student_brush = function() {
	loadhtml('student_brush');
}


/*根据题型测试*/



