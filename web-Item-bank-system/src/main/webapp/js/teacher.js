$(function() {
	
	teacher_home();
	
});


var teacher_home = function() {
	loadhtml('teacher_home');
	newPaper();
	doPaperCount();
	mdui.mutation();
}

var teacher_question = function() {
	loadhtml('teacher_question');
	teacherquestionsys();
}

var teacher_paper = function(){
	loadhtml('teacher_paper');
	teacherpapersys();
}






