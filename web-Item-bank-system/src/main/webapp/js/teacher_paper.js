var teacher_papertmpl;
var teahcer_testpapertmpl;
var teacher_paperData={};
//教师试卷页面初始化
var teacherpapersys = function(){
	if(teacher_papertmpl==null){
		teacher_papertmpl= jqutils.loadHtml('list/teacher_paperlist.html');
		
	}
	if(teacher_papertmpl==null){
		teahcer_testpapertmpl= jqutils.loadHtml('list/teacher_testpaperlist.html');
		
	}
	teacher_paperData.page=1;
	teacher_searchPaper();
	
	mdui.mutation();
}
//查询试卷
var teacher_searchPaper = function(){
	$('.mdui-table-cell-checkbox').remove();
	var model = jqutils.loadJson('TestpaperCtrl.teacherSearch',teacher_paperData);
	console.log(model);
	var html =  "";
	for(var i=0;i<model.rows.length;i++){
		if(model.rows[i].releasetype){
			model.rows[i].release='发布中';
		}else {
			model.rows[i].release='发布';
		}
		html+=jqutils.tmpl(teacher_papertmpl,model.rows[i]);
	}
	teacher_paperData.totalpage = model.totalpage;
	teacher_paperData.page = model.page;
	$('input[name=page]').val(model.page);
	$('#totalpage').text(model.totalpage);
	
	$('#teacher_papertab').html(html);
	
	mdui.updateTables();
	mdui.mutation();

}


//打开对话框
var openPaperRule = function(id,paperid,papername) {
	
	var inst = new mdui.Dialog('#dialog',{overlay: false});
	
	$('.mdui-dialog-title').html('组卷规则 <div style="float:right">总分：<span id="totalMark"></span></div>');
	html=jqutils.loadHtml('fu/paper_rule.html');
	$('.mdui-dialog-content').html(html);
	var tmpl = '<label class="mdui-checkbox" style="margin:10px">';
		tmpl+='<input type="checkbox" value="${id}" name="pointIds"/>';
		tmpl+= '<i class="mdui-checkbox-icon"></i>${name}</label>' ;
	loadpoints(tmpl,'pointIds');
	if(id>0){
		var data={};
		data.id=id;
		var model = jqutils.loadJson('RuleCtrl.presave',data);
		//遍历向多选框中确定勾选状态
		for(var i=0 ;i<model.pointIds.length;i++){
			//根据数值向对应的checkbox中选择是否选中状态
			$('input[name=pointIds]'+'[value='+model.pointIds[i]+']').attr('checked','checked');

		}
		delete model.pointIds;
		model.paperid=paperid;
		model.papername=papername;
		jqutils.formLoad('form1',model);
		totalMark();
	}
	//添加数据监听，只要有数据改变就改变总分
	$('input[type=number]').change(function(){ 
		totalMark();
	});
	$('#teacher_save').click(function() {
		teacher_saverule();
	});
	inst.open();
	inst.handleUpdate();
	mdui.mutation();
	/*var number=jqutils.loadJson('TestpaperCtrl.newPaper',{});
	console.log(number)*/
}
//计算总分
var totalMark = function(){
	var singleNum = $('input[name=singleNum]').val();
	var singleScore = $('input[name=singleScore]').val();
	var JudgmentNum = $('input[name=judgmentNum]').val();
	var JudgmentScore = $('input[name=judgmentScore]').val();
	var completeNum = $('input[name=completeNum]').val();
	var completeScore = $('input[name=completeScore]').val();
	var subjectiveNum = $('input[name=subjectiveNum]').val();
	var subjectiveScore = $('input[name=subjectiveScore]').val();
	var totalMark=0;
	    totalMark+=singleNum*singleScore;
		totalMark+=JudgmentNum*JudgmentScore;
		totalMark+=completeNum*completeScore;
		totalMark+=subjectiveNum*subjectiveScore;//计算当前总分
	$('input[name=totalMark]').val(totalMark);
	$('#totalMark').text(totalMark);
}

//上传规则获得试卷
var teacher_saverule = function() {

	if(!confirm("确定生成试卷？")){
		return;
	}
	data = jqutils.formData('form1');
	var pointIds={};
	i=0;
	//遍历获取所有的勾选状况
	$('input[name="pointIds"]').each(function() {
		if($(this).prop('checked') ){
			pointIds[i]=$(this).val();
			i++;	
		}
	});
	
	data.pointIds=pointIds;
	var reslut = jqutils.loadJson("TestpaperCtrl.save",data);
	if(reslut.succ){
			$('#dialog').removeClass('mdui-dialog-open');
	}else{
		alert('创建失败')
	}	
}
//删除试卷，可以多选择一起删除
var delPaper = function() {
	if(!confirm("确定删除试卷？")){
		return;
	}
	data={};
	data.list=[];
	$('.mdui-table-row-selected').each(function() {
		data.list.push($(this).children("td:first").next().text());
	});
	console.log(data);
	var reslut = jqutils.loadJson("TestpaperCtrl.del",data);
	if(reslut.succ){
		alert('操作成功');
	}else{
		alert(reslut.error);
	}
	teacher_searchPaper();
	
}




//改变发布状态
var changeRelease = function(id,type) {

	data={};
	data.id=id;
	if(type==0){
		data.type=1;
	}else {
		data.type=0;
	}
	var reslut = jqutils.loadJson("TestpaperCtrl.changeRelease",data);
	if(reslut.succ){
		teacher_searchPaper();
	}else{
		alert(reslut.error);
	}

}

//下一页
var paper_nextpage = function(){
	console.log(question_data);
	if(teacher_paperData.page>=teacher_paperData.totalpage){
		return;
	}

	teacher_paperData.page=teacher_paperData.page+1;
	teacher_searchPaper();
	
}
//上一页
var paper_previouspage = function() {
	if(teacher_paperData.page<=1){
		return;
	}
	teacher_paperData.page=teacher_paperData.page-1;
	teacher_searchPaper();
}




