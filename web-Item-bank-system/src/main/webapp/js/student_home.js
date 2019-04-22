var chuangjian = function() {
	var ctx = $("#myChart").get(0).getContext("2d");
	// This will get the first returned node in the jQuery collection.
	var config = {
			type: 'line',
			options:{
				//标题
				title:{
					display:true,
					text:'近期成绩',
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
	
	data = {};
	
	data.labels= ['January', 'February', 'March', 'April', 'May', 'June', 'July'];
	data.datasets=[{
			label: '成绩',
			backgroundColor: "rgba(0,0,220,0.5)",
			borderColor: "rgba(0,0,220,0.5)",
			data: [10, 59, 90, 81, 56, 55, 40],
			fill: false,
		}];
	var myLineChart = new Chart(ctx,config);
}



