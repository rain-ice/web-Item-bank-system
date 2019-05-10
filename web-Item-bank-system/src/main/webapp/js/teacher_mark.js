var teacher_markData={};
var teacher_marktmpl;
var teacher_markpaperList;
var teachermarksys = function(paperid) {
	loadhtml('teacher_mark');
	if(teacher_marktmpl==null){
		teacher_marktmpl = jqutils.loadHtml('list/teacher_marklist.html')
	}
	console.log(paperid);
	//初始化页数和变量
	teacher_markData.paperid=paperid;
	teacher_markData.page=1;
	
	searchTeacherMark();
	mdui.mutation();
	$('.mdui-tooltip-open').removeClass('mdui-tooltip-open');
}
//查询相关的试卷记录
var searchTeacherMark = function(){
	console.log(teacher_markData);
	var model = jqutils.loadJson('DopaperCtrl.teacher_search',teacher_markData);
	if(model.totalpage>0){
		teacher_markData.page = model.page;
		teacher_markData.totalpage =model.totalpage;
		$('input[name=page]').val(model.page);
		$('#totalpage').text(model.totalpage);
		var html ="";
		for(var i=0;i<model.rows.length;i++){
			html+= jqutils.tmpl(teacher_marktmpl,model.rows[i]);
			if(model.rows[i].type==1){
				html+='<button class="mdui-btn mdui-color-light-blue-200"  onclick="markpaper('+model.rows[i].id+')">批改试卷</button>';
			}else{
				html+='<button class="mdui-btn mdui-color-light-blue-200" onclick="searchDopaper('+model.rows[i].id+')">查看详情</button>';
			}
			html+='	</td></tr>';
		}
		 
		$('tbody').html(html);
	}else{
		$('#content-main').html('没有考试记录');
	}
	
	console.log(model);
}
//批改试卷弹出框
var markpaper = function(id) {
	var inst = new mdui.Dialog('#dialog');
	var model = jqutils.loadJson('DopaperCtrl.presave',{dopaperid:id});
	teacher_markpaperList = model.dotestpapers;
	var html="";
	for(var i=0;i<model.testpapers.length;i++){
		if(model.testpapers[i].question.type==4){
			html+='<div style="font-size:20px;"	>问题：<span >'+model.testpapers[i].question.question+'</span></div>';
			html+='<div style="margin:20px;">学生回答：'+model.dotestpapers[i].answer+'</div>';
			html+='<div class="mdui-text-color-blue-200" style="font-size:15px;">正确答案：<span class="">'+model.testpapers[i].question.answer+'</span></div>';
			html+='<input type="hidden" value='+model.testpapers[i].score+'>';
			html+='<input type="number">'
			html+='<input type="hidden" value='+i+'>';
			html+='<div class="mdui-divider-inset" style="margin:20px;"></div>'
			console.log(i);
		}
	}
	$('.mdui-dialog-title').html('批改');
	$('.mdui-dialog-content').html(html);
	
	//注入点击监听
	$('#teacher_save').click(function() {
		saveMark(id);
	});
	//注入监听，将数据存入需要返回的类型中
	$('input[type=number]').change(function() {
		if(parseInt($(this).val())<=parseInt($(this).prev().val())&&parseInt($(this).val())>0){
			teacher_markpaperList[$(this).next().val()].score=$(this).val();
		}
		if(parseInt($(this).val())>parseInt($(this).prev().val())){
			$(this).val($(this).prev().val());
			teacher_markpaperList[$(this).next().val()].score=$(this).val();
		}
		console.log(teacher_markpaperList);
	});
	inst.open();
	inst.handleUpdate();
	mdui.mutation();
	console.log(model);
}
//批改完毕后保存
var saveMark = function(dopaperid){
	var data={};
	data.list=jqutils.obj2json(teacher_markpaperList);
	data.dopaperid = dopaperid;
	console.log(data);
	result = jqutils.loadJson('DopaperCtrl.marksave',data);
	if(result.succ){
		alert('1s');
	}
}

//下一页
var mark_nextpage = function(){
	if(teacher_markData.page>=teacher_markData.totalpage){
		return;
	}

	teacher_markData.page=teacher_markData.page+1;
	searchTeacherMark();
	
}
//上一页
var mark_previouspage = function() {
	if(teacher_markData.page<=1){
		return;
	}
	alert('s');
	teacher_markData.page=teacher_markData.page-1;
	searchTeacherMark();
}


