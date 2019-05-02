var student_page;
var student_scoretmpl;
var studentscoresys = function() {
	if(student_scoretmpl==null){
		student_scoretmpl = jqutils.loadHtml('list/student_score.html');
	}
	student_searchscore(1);
}

var student_searchscore = function(page) {
	var model = jqutils.loadJson('DopaperCtrl.student_search',{page:page});
	console.log(model.rows);
	var html = jqutils.tmpl(student_scoretmpl,model.rows);
	$('#student_papertab').html(html);

}