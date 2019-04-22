var newquestion_inst;
//创建或修改题库初始化
var teachernewquestionsys = function(id){
	newquestion_inst = new mdui.Tab('#tab');
	
	loadpoints('<option value="${id}">${name}</option>','point1');
	loadpoints('<option value="${id}">${name}</option>','point2');
	loadpoints('<option value="${id}">${name}</option>','point3');
	loadpoints('<option value="${id}">${name}</option>','point4');
	//判断是否是新建
	if(id>0){
		data={};
		data.id=id;
		model = jqutils.loadJson('QustionCtrl.presave',data);
		console.log(model);
		switch(model.type){
		case 1:
			newquestion_inst.show(0);
			//由于这个字段无法直接传送，所以只能这样操作
			var answer = $("input[name=answer]")
			for(var i=0;i<answer.length;i++){
				if(answer[i].value==model.answer){
					$(answer[i]).prop("checked",true);
					break;
				}
			}
			delete model.answer;
			jqutils.formLoad('form1',model);
			break;
		case 2:

			jqutils.formLoad('form2',model);
			newquestion_inst.show(1);
			break;
		case 3:
			newquestion_inst.show(2);
			jqutils.formLoad('form3',model);
			break;
		case 4:
			newquestion_inst.show(3);
			jqutils.formLoad('form4',model);
			break;
		}
	}
	mdui.mutation();
}
//保存题目
var question_save = function(type) {
	var data = jqutils.formData('form'+type);
	data.type=type;
	data.teacherid=LoginUser.id;
	
	console.log(data);
	var reslut = jqutils.loadJson('QustionCtrl.save',data);
	if(reslut.succ){
		alert('操作成功');
		location.href='teacher.html';
	}else{
		alert(reslut.error);
	}
}

