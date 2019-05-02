var teacher_markData={};
var teacher_marktmpl;
var teachermarksys = function(paperid) {
	loadhtml('teacher_mark');
	if(teacher_marktmpl==null){
		teacher_marktmpl = jqutils.loadHtml('list/teacher_marklist.html')
	}
	console.log(paperid);
	teacher_markData.paperid=paperid;
	searchTeacherMark(1);
	mdui.mutation();
	
}

var searchTeacherMark = function(page){
	teacher_markData.page=page;
	console.log(teacher_markData);
	var model = jqutils.loadJson('DopaperCtrl.teacher_search',teacher_markData);
	teacher_markData.page = model.page;
	teacher_markData.totalpage =model.totalpage;
	
	
	console.log(model);
}