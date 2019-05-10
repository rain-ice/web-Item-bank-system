 $(function(){
	 student_home();
 });
 
 
/*进入时的HOME显示相关*/
var student_home = function() {
	loadhtml('student_home');
	


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



/*错题集相关内容*/
var student_mistakes = function() {
	loadhtml('student_mistakes');
	studentmistakesys();
}

/*查看成绩*/
var student_score = function() {
	loadhtml('student_score');
	studentscoresys();
}

/*刷题*/
var student_brush = function() {
	loadhtml('student_brush');
	studentbrushsys();
}




