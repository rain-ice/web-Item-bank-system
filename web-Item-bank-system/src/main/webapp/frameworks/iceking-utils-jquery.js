//2018-1-16
//针对jquery的一些常用功能汇总
(function(){
	$.iceking_utils_jquery=function(){
		/**
		==================================================================
		私有属性变量的定义
		==================================================================
		*/
		var pri_version = 'V1.0';
		var pri_version_date = '2018-04-20';
		
		
		/**
		==================================================================
		内部方法定义
		==================================================================
		*/
		
		//表单相关的操作
		var pri_getFormObj = function(formId){
			var jqform=null;
			if(formId.indexOf('#')==0){
				jqform=$(formId);
			}else{
				jqform=$('#'+formId);
			}
			if(jqform.length==0)
				return null;
			return jqform;
		};
		
		//通过jquery，从form中获得表单的字符形式，序列化之后的
		var pri_formDataStr = function(formId){
			var jqform= pri_getFormObj(formId);
			return jqform!=null?jqform.serialize():'';
		};
		
		//获得表单form的Object对象，便于转json字符的
		var pri_formDataObj = function(formId){
			var jqform= pri_getFormObj(formId);
			var obj = new Object();
			if(jqform==null)
				return obj;
			var array = jqform.serializeArray();
			if(array == null || array.length == 0)
				return obj;
			for(var i=0;i<array.length;i++){
				obj[array[i].name]=array[i].value;
			}
			return obj;
		};
		
		//获得表单对象的json字符形式
		var pri_formDataJson = function(formId){
			var jsobj = pri_formDataObj(formId);
			return JSON.stringify(jsobj);
		};
		
		//获得表单中的一个name
		var pri_formItem = function(name,formId){
			var selstr = '[name="'+name+'"]';
			if(typeof(formId)!='undefined' && formId!=null && formId!=''){
				if(formId.indexOf('#') == 0){
					selstr = formId+' '+selstr;
				}else{
					selstr = '#'+formId+' '+selstr;
				}
			}
			var jqobj = $(selstr);
			if(jqobj.length==0){
				return null;
			}
			return jqobj;
		};
		
		//获得表单值，根据name属性
		var pri_formItemVal = function(name,formId){
			var jqobj = pri_formItem(name,formId);
			if(jqobj == null)
				return '';
			return jqobj.val();
		};
		
		//获得表单值，int型，根据name属性
		var pri_formItemInt = function(name,formId){
			var tmpval = pri_formItemVal(name,formId);
			if(tmpval == '')
				return 0;
			return parseInt(tmpval);
		};
		
		//将一个对象中的数据放入到表单相应位置去
		var pri_formLoad = function(formId,obj){
			for(var p in obj){
				var pv = obj[p];
				if(typeof(pv) == 'function')
					continue;
				//根据p去查询form中的一个对象
				var item = pri_formItem(p,formId);
				if(item==null)
					continue;
				item.val(pv);
			}
		};
		
		//直接一个js的object变成json的字符形式
		var pri_objToJson = function(jsobj){
			if(typeof(jsobj)!='object' || jsobj == null)
				return '';
			return JSON.stringify(jsobj);
		};
		
		//将一个json格式的字符串转成js object对象
		var pri_jsonToObj = function(json){
			return eval('(' + json + ')');
		};
		
		//判断某个jquery对象，是否有属性
		var pri_hasAttr = function(jqobjorselector,attr){
			var toftmp = typeof(jqobjorselector);
			
			if(toftmp=='undefined')
				return false;
			
			var jqobj=null;
			
			//如果是jquery对象，则直接使用
			if(toftmp=='object'){
				jqobj=jqobjorselector;
			}else if(toftmp=='string'){
				jqobj=$(toftmp);
			}else{
				return false;
			}
			
			if(jqobj.length==0)
				return false;
			
			if(typeof(attr)=='undefined' || attr==null || attr=='')
				return false;
			
			if(typeof(jqobj.attr(attr))=='undefined')
				return false;
			return true;
		};
		
		//判断内容是否在数组中
		var pri_inArray = function(val,array){
			if(!$.isArray(array))
				return false;
			if(array.length==0)
				return false;
			for(var i=0;i<array.length;i++){
				if(array[i]==val)
					return true;
			}
			return false;
		};
		
		//根据url获取外部的html或者jsp
		var pri_loadHtml = function(url,data){
			var fetch_result_html = null;
			var tmpdata = {};
			var tmpmethod = 'GET';
			if(typeof(data)=='object'){
				tmpdata = data;
				tmpmethod = 'POST';
			}
				
			$.ajax({
				async:false,
				timeout:5000,
				dataType:'HTML',
				type:tmpmethod,
				url:url,
				data:tmpdata,
				cache:false,
				success:function(result,status,xhr){
					fetch_result_html = result;
				}
			});
			return fetch_result_html;
		};
		
		//请求json格式的返回，可以是post的方式的
		var pri_loadJson = function(url,data){
			var json_result = null;
			
			var tmpdata = {};
			var tmpmethod = 'GET';
			if(typeof(data)=='object'){
				tmpdata = data;
				tmpmethod = 'POST';
			}
			
			$.ajax({
				async:false,
				timeout:5000,
				dataType:'TEXT',
				type:tmpmethod,
				url:url,
				data:tmpdata,
				cache:false,
				success:function(result,status,xhr){
					json_result = eval('(' + result + ')')
				}
			});
			return json_result;
		};
		
		//将数据对象应用到模板html
		// 数据部分使用： ${obj.name}
		var pri_templateHtml_one = function(htmltmpl,obj){
			var tmpl_html=htmltmpl;
			while(true){
				var idx1=tmpl_html.indexOf('${');
				var idx2=tmpl_html.indexOf('}');
				if(idx1==-1 || idx2==-1)
					break;
				//idx1之前的字符串
				var bfstr=tmpl_html.substring(0,idx1);
				//${name}
				var datastr=tmpl_html.substring(idx1+2,idx2);
				//使用eval获取数据
				var datatmp = eval('obj.'+datastr);
				if(typeof(datatmp)=='undefined'){
					datatmp='undefined';
				}
				//idx2之后的字符传
				var aftstr=tmpl_html.substring(idx2+1);
				//重新组装
				tmpl_html=bfstr+datatmp+aftstr;
			}
			return tmpl_html;
		};
		
		//模板的数据可以是数组
		var pri_templateHtml=function(htmltmpl,obj){
			if(typeof(obj)!='object')
				return htmltmpl;
			
			//如果数据是数组，则循环模板
			if(Array.isArray(obj)){
				var tmparrayhtml='';
				for(var i=0;i<obj.length;i++){
					tmparrayhtml+=pri_templateHtml_one(htmltmpl,obj[i]);
				}
				return tmparrayhtml;
			}
			
			return pri_templateHtml_one(htmltmpl,obj);
		};
		
		//动态加载js文件，后面加上随机数，不缓存
		//需要使用script标签生成，使用docment.write
		var pri_loadJs_one = function(url){
			document.write('<script type="text/javascript" src="'+url+'?'+Math.random()+'"></script>');
		};
		
		var pri_loadCss = function(url){
			document.write('<link href="'+url+'?'+Math.random()+'" rel="stylesheet">');
		}
		
		//url可以多个，是数组形式
		var pri_loadJs = function(url){
			if(Array.isArray(url)){
				for(var i=0;i<url.length;i++){
					pri_loadJs_one(url[i]);
				}
			}else{
				pri_loadJs_one(url);
			}
		};
		
		
		/**
		==================================================================
		外部方法定义
		==================================================================
		*/
		
		//返回本工具的版本号
		this.version = function(){
			return pri_version+'('+pri_version_date+')';
		};
		
		//根据表单id号，获得表单属性的js object对象
		this.formData = function(formId){
			return pri_formDataObj(formId);
		};
		
		//根据表单id号，获得表单属性的紧凑型字符参数形式
		this.formDataStr = function(formId){
			return pri_formDataStr(formId);
		};
		
		//根据表单id号，获得表单属性的json字符串形式
		this.formDataJson = function(formId){
			return pri_formDataJson(formId);
		};
		
		//根据表单属性name，获得该属性的jquery对象
		this.formItem = function(name,formId){
			return pri_formItem(name,formId);
		};
		
		//根据表单属性name，获得该属性的字符型值
		this.formVal = function(name,formId){
			return pri_formItemVal(name,formId);
		};
		
		//根据表单属性name，获得该属性的整数型值
		this.formInt = function(name,formId){
			return pri_formItemInt(name,formId);
		};
		
		//将js object 装载到表单属性中
		this.formLoad = function(formId,obj){
			pri_formLoad(formId,obj);
		};
		
		//js object 对象转成 json格式字符串
		this.obj2json = function(obj){
			return pri_objToJson(obj);
		};
		
		//json格式字符串转成js object对象
		this.json2obj = function(json){
			return pri_jsonToObj(json);
		};
		
		//判断jquery对象是否包含某个属性名称
		this.hasAttr = function(jqobjorselector,attr){
			return pri_hasAttr(jqobjorselector,attr);
		};
		
		//判断数组array是否包含val值
		this.inArray = function(val,array){
			return pri_inArray(val,array);
		};
		
		//ajax方式获得html，可以携带动态参数data
		this.loadHtml = function(url,data){
			return pri_loadHtml(url,data);
		};
		
		//ajax方式获得json，可以携带动态参数data
		this.loadJson = function(url,data){
			return pri_loadJson(url,data);
		};
		
		//加载js文件，带随机数，避免浏览器缓存
		this.loadJs = function(url){
			return pri_loadJs(url);
		};
		//加载css文件，带随机数，避免浏览器缓存
		this.loadCss = function(url){
			return pri_loadCss(url);
		};
		
		//将data数据加载到模板html中，获得新的html
		this.tmpl = function(html,data){
			return pri_templateHtml(html,data);
		};
		
	};
})(jQuery);

//定义变量
var jqutils = new $.iceking_utils_jquery;