var newPaper = function() {
	var model = jqutils.loadJson('StatisticsCtrl.searchNewPaper',null);
	
	var tmpl = '<li style="list-style-type:none;margin-top:10px;"> <span class="mdui-text-color-blue" >${tittle}</span>	-----${create_date}</li>';
	var html = jqutils.tmpl(tmpl,model.rows);
	$('#newpaper').html(html);
	
}

var doPaperCount = function() {
	var ctx = $("#myChart").get(0).getContext("2d");
	// This will get the first returned node in the jQuery collection.
 var data = {};
	
	data.labels=['2019-5-1','2019-5-2','2019-5-3','2019-5-4'];
	data.datasets=[{
			label: '人数',
			backgroundColor: "rgba(0,0,220,0.5)",
			borderColor: "rgba(0,0,220,0.5)",
			data: [5,9,3,8],
			fill: false,
		}];
	var config = {
			data:data,
			type: 'line',
			options:{
				//标题
				title:{
					display:true,
					text:'近10日考试',
					scaleFontSize:20,
				},
				scales: {
					//x轴数据
						xAxes: [{
							display: true,
							scaleLabel: {
								display: true,
								labelString: '日期'
							}
						}],
						//Y轴数据
						yAxes: [{
							display: true,
							scaleLabel: {
								display: true,
								labelString: '人数'
							}
						}]
				}
			}
		};
	
	
	//var myLineChart = new Chart(ctx).Line(data, config);
	var myLineChart =Chart.Line(ctx,config);
	myLineChart.update();
}