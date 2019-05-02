var student_papertmpl;

//页面加载时的工作
var  studentpapersys = function() {
	if(student_papertmpl==null){
		student_papertmpl= jqutils.loadHtml('list/student_paperlist.html');
		
	}
	student_searchPaper(1);
	mdui.mutation();
	
}

//查看能够考试的试卷
var student_searchPaper = function(page) {
	var model = jqutils.loadJson('TestpaperCtrl.studentSearch',{page:page});
	console.log(model);
	var html = jqutils.tmpl(student_papertmpl,model);
	$('#student_papertab').html(html);
	
}

