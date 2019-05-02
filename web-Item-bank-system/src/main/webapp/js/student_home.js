var student_scoreChart = function(score,create_date) {
	var ctx = $("#myChart").get(0).getContext("2d");
	// This will get the first returned node in the jQuery collection.
 var data = {};
	
	data.labels=create_date;
	data.datasets=[{
			label: '成绩',
			backgroundColor: "rgba(0,0,220,0.5)",
			borderColor: "rgba(0,0,220,0.5)",
			data: score,
			fill: false,
		}];
	var config = {
			data:data,
			type: 'line',
			options:{
				//标题
				title:{
					display:true,
					text:'近期十次成绩',
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
								labelString: '成绩'
							}
						}]
				}
			}
		};
	
	
	//var myLineChart = new Chart(ctx).Line(data, config);
	var myLineChart =Chart.Line(ctx,config);
	myLineChart.update();
}



