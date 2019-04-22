/*
 * 定义MDUI中全局JQ操作
 * 官网网址：
 * 		https://www.mdui.org/docs/jq#jq-ajax
 */
var $$ = mdui.JQ;

//自定义方法，必须以 $$.my.fn 方式调用
$$.extend({
	gb:function(){
		//==================================
		//定义object对象，作为返回值
		//==================================
		var me = new Object();
		//***********************************************************************
		
		/*
		 * 定义私有属性和方法，以pri_开头
		 * ==========================================
		 * 
		 * V1.0: 2018-5-20
		 * 		以mdui的内置jquery重写一些常用方法
		 * 
		 * 
		 * ==========================================
		 */
		
		var pri_version = 'V1.0';
		var pri_version_date = '2018-05-20';
		
		//记录已经通过loadJs方法装载过的js的url
		var loaded_JsArray = new Array();
		//记录已经通过loadHtml方法装载过的html
		var loaded_HtmlUrlArray = new Array();
		var loaded_HtmlContentArray = new Array();
		
		/*
		 * js对象转成json字符串
		 */
		var pri_obj2json = function(obj){
			if(typeof(obj)!='object' || obj == null)
				return '';
			return JSON.stringify(obj);
		};
		
		/*
		 * 将一个json格式字符串转成js object 对象
		 */
		var pri_json2obj = function(jsonstr){
			return eval('(' + jsonstr + ')');
		};
		
		
		/*
		 * 获得表单数据，根据name组织成 js object
		 */
		var pri_formData = function(formId){
			var form = $$(formId.indexOf('#')==0?formId:'#'+formId);
			if(form.length == 0)
				return {};
			var obj = new Object();
			var array = form.serializeArray();
			if(array == null || array.length == 0)
				return {};
			for(var i=0;i<array.length;i++){
				obj[array[i].name]=array[i].value;
			}
			return obj;
		};
		
		
		/*
		 * 表单数据变成json字符串
		 */
		var pri_formJson = function(formId){
			return pri_obj2json(pri_formData(formId));
		};
		
		
		/*
		 * 表单数据序列号的字符串
		 */
		var pri_formStr = function(formId){
			var form = $$(formId.indexOf('#')==0?formId:'#'+formId);
			if(form.length == 0)
				return '';
			return form.serialize();
		};
		
		
		/*
		 * 返回表单元素
		 */
		var pri_formItem = function(name,formId){
			var selstr = '[name="'+name+'"]';
			if(typeof(formId)=='string' && formId!=''){
				selstr = formId.indexOf('#')==0?formId:'#'+formId+' '+selstr;
			}
			return $$(selstr);
		};
		
		
		/*
		 * 返回表单元素的值，根据name，字符型
		 */
		var pri_formItemVal = function(name,formId){
			var item = pri_formItem(name,formId);
			if(item.length==0)
				return '';
			return item.val();
		};
		
		
		/*
		 * 返回表单元素值，int型
		 */
		var pri_formItemInt = function(name,formId){
			var val = pri_formItemVal(name,formId);
			if(val == '')
				return 0;
			return parseInt(val);
		};
		
		
		/*
		 * 将对象中的数据放入到form中
		 */
		var pri_formLoad = function(formId,obj){
			for(var p in obj){
				var pv = obj[p];
				if(typeof(pv) == 'function')
					continue;
				//根据p去查询form中的一个对象
				var item = pri_formItem(p,formId);
				if(item.length==0)
					continue;
				item.val(pv);
			}
		};
		
		
		/*
		 * 判断是否数组
		 */
		var pri_isArray = function(array){
			//return Object.prototype.toString.call(array)=='[object Array]';
			return Array.isArray(array);
		};
		
		
		/*
		 * 判断内容是否在数组中
		 */
		var pri_inArray = function(val,array){
			if(!pri_isArray(array))
				return false;
			if(array.length==0)
				return false;
			for(var i=0;i<array.length;i++){
				if(array[i]==val)
					return true;
			}
			return false;
		};
		
		
		/*
		 * 获得html文件的内容
		 * GET 方式
		 */
		var pri_loadHtml = function(url,callback){
			if(typeof(url)!='string' || typeof(callback)!='function')
				return;
			
			//首先判断url是否已经读取过了
			var idx = -1;
			for(var i=0;i<loaded_HtmlUrlArray.length;i++){
				if(url==loaded_HtmlUrlArray[i]){
					idx = i;
					break;
				}
			}
			
			if(idx>=0){
				//已经读取过了，直接从内存变量中获得内容
				callback(loaded_HtmlContentArray[idx]);
				return;
			}
			
			//未读取
			$$.ajax({
				async:true,
				timeout:10000,
				dataType:'html',
				method:'GET',
				url:url,
				cache:false,
				success:function(result,status,xhr){
					loaded_HtmlUrlArray[loaded_HtmlUrlArray.length]=url;
					loaded_HtmlContentArray[loaded_HtmlContentArray.length]=result;
					callback(result);
				}
			});
		};
		
		
		/*
		 * 动态加载js文件
		 */
		var pri_loadJs = function(url,callback){
			if(typeof(url)!='string')
				return;
			
			//首先判断url是否已经读取过了
			var idx = -1;
			for(var i=0;i<loaded_JsArray.length;i++){
				if(url==loaded_JsArray[i]){
					idx = i;
					break;
				}
			}
			
			if(idx>=0){
				//已经读取过了，直接从内存变量中获得内容
				if(typeof(callback)=='function')
					callback();
				return;
			}
			
			
			$$.ajax({
				async:true,
				timeout:10000,
				dataType:'text',
				method:'GET',
				url:url,
				cache:false,
				success:function(result,status,xhr){
					//使用eval来执行脚本文字
					try{
						window["eval"].call(window, result);
					}catch(e){
						alert('JS "'+url+'" error: '+e.message);
						return;
					}
					
					loaded_JsArray[loaded_JsArray.length]=url;
					if(typeof(callback)=='function')
						callback();
				}
			});
		};
		
		
		/*
		 * MDUI 动态假如内容的渲染
		 */
		var pri_layout = function(){
			mdui.mutation();
		};
		
		
		/*
		 * 装载html到某个div中，可以同时加载js和调用回调函数
		 */
		var pri_loadHtmlToDiv = function(divId,htmlurl,jsurl,callback){
			if(typeof(divId)!='string' || divId=='')
				return;
			if(typeof(htmlurl)!='string' || htmlurl=='')
				return;
			var div = $$(divId.indexOf('#')==0?divId:'#'+divId);
			if(div.length==0)
				return;
			
			//后两个参数 jsurl和callback可以不指定，也可以任意位置
			var arg_jsurl;
			var arg_callback;
			
			if(typeof(arguments[2])=='string'){
				arg_jsurl = arguments[2];
			}else if(typeof(arguments[2])=='function'){
				arg_callback = arguments[2];
			}
			if(typeof(arguments[3])=='string'){
				arg_jsurl = arguments[3];
			}else if(typeof(arguments[3])=='function'){
				arg_callback = arguments[3];
			}
			
			//首先加载html
			pri_loadHtml(htmlurl,function(html){
				//清空div原来内容
				div.empty();
				//放入新的内容
				div.html(html);
				//新内容放入后，需要使用mdui进行渲染
				pri_layout();
				//此处判断是否需要加载jsurl
				if(typeof(arg_jsurl)!='string' || arg_jsurl==''){
					//无需加载，直接判断callback
					if(typeof(arg_callback)=='function')
						arg_callback();
				}else{
					//需要再次加载js
					pri_loadJs(arg_jsurl,arg_callback);
				}
			});
		};
		
		
		/*
		 * 请求json数据，一般是post到servlet
		 * data不存在时候，则使用get方式
		 */
		var pri_loadJson = function(url,data,callback){
			if(typeof(url)!='string' || url=='')
				return;
			
			var arg_data;
			var arg_callback;
			
			if(typeof(arguments[1])=='object'){
				arg_data = arguments[1];
			}else if(typeof(arguments[1])=='function'){
				arg_callback = arguments[1];
			}
			
			if(typeof(arguments[2])=='object'){
				arg_data = arguments[2];
			}else if(typeof(arguments[2])=='function'){
				arg_callback = arguments[2];
			}
			
			var params = {
					async:true,
					timeout:10000,
					dataType:'text',
					url:url,
					cache:false,
					success:function(result,status,xhr){
						var json_result;
						try{
							json_result = eval('(' + result + ')');
						}catch(e){
							alert('result text is not a json object. '+result);
							return;
						}
						if(typeof(arg_callback)=='function')
							arg_callback(json_result);
					}
			};
			if(typeof(arg_data)=='object'){
				params.method='POST';
				params.data=arg_data;
			}else{
				params.method='GET';
			}
			
			//发送ajax
			$$.ajax(params);
		};
		
		
		/*
		 * 将数据对象应用到模板html
		 * 数据部分使用： ${obj.name}
		 */
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
				var datatmp = 'undefined';
				try{
					datatmp = eval('obj.'+datastr);
				}catch(e){
					datatmp = 'error: '+e.message;
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
		
		
		/*
		 * 将html模板加载到div中
		 */
		var pri_loadTmplToDiv = function(divId,tmplurl,data){
			if(typeof(divId)!='string' || divId=='')
				return;
			if(typeof(tmplurl)!='string' || tmplurl=='')
				return;
			var div = $$(divId.indexOf('#')==0?divId:'#'+divId);
			if(div.length==0)
				return;
			pri_loadHtml(tmplurl,function(html){
				//清空div原来内容
				div.empty();
				//放入新的内容
				div.html(pri_templateHtml(html,data));
				//新内容放入后，需要使用mdui进行渲染
				pri_layout();
			});
		};
		
		/*
		 * 使用Html5中的FormData来进行文件上传
		 * callback第一个参数，表示返回的json转成js对象
		 */
		var pri_uploadFile = function(formId,callback,url){
			if(typeof(formId)!='string' || formId=='')
				return;
			if(typeof(callback)!='function')
				return;
			var form = $$(formId.indexOf('#')==0?formId:'#'+formId);
			if(form.length == 0)
				return;
			var tmpurl = '';
			if(typeof(url)=='string' && url!=''){
				tmpurl = url;
			}else{
				//没有指定url，则去使用form的action
				var tmpaction = form.attr('action');
				if(typeof(tmpaction)=='string' && tmpaction!='')
					tmpurl = tmpaction;
			}
			
			if(tmpurl=='')
				return;
			
			var formData = new FormData(form[0]);
			$$.ajax({
				async:true,
				timeout:10000,
				method:'POST',
				dataType:'json',
				url:tmpurl,
				data:formData,
				cache:false,
				contentType: false, 
		        processData: false,
				success:function(result,status,xhr){
					callback(result);
				}
			});
		};
		
		
		
		/*
		 * ========================================
		 * 定义共有属性和方法，必须为me的内置属性
		 * ========================================
		 */
		
		/*
		 * 将js对象转成json格式字符串
		 */
		me.obj2json = function(obj){
			return pri_obj2json(obj);
		};
		
		/*
		 * 将json格式字符串转成js对象
		 */
		me.json2obj = function(jsonstr){
			return pri_json2obj(jsonstr);
		};
		
		/*
		 * 根据表单id号获得表单的属性值，返回js对象
		 */
		me.formData = function(formId){
			return pri_formData(formId);
		};
		
		/*
		 * 根据表单id号获得表单的属性值，返回json格式字符串
		 */
		me.formJson = function(formId){
			return pri_formJson(formId);
		};
		
		/*
		 * 根据表单id号获得表单的属性值，返回紧凑型字符串，一般用于get方式
		 */
		me.formStr = function(formId){
			return pri_formStr(formId);
		};
		
		/*
		 * 获得表单属性的JQ对象，根据name
		 */
		me.formItem = function(name,formId){
			return pri_formItem(name,formId);
		};
		
		/*
		 * 获得表单属性的值，返回字符串
		 */
		me.formItemVal = function(name,formId){
			return pri_formItemVal(name,formId);
		};
		
		/*
		 * 获得表单属性的值，返回int型
		 */
		me.formItemInt = function(name,formId){
			return pri_formItemInt(name,formId);
		};
		
		/*
		 * 将js对象obj中的各个属性值加载到表单中
		 */
		me.formLoad = function(formId,obj){
			pri_formLoad(formId,obj);
		};
		
		/*
		 * 判断是否为数组
		 */
		me.isArray = function(array){
			return pri_isArray(array);
		};
		
		/*
		 * 判断某个值是否在数组中
		 */
		me.inArray = function(val,array){
			return pri_inArray(val,array);
		};
		
		/*
		 * 加载其它网页中的html，GET方式
		 * callback中第一个参数为获得的html字符串
		 */
		me.loadHtml = function(url,callback){
			pri_loadHtml(url,callback);
		};
		
		/*
		 * 动态加载js文件，GET方式
		 * callback无参数
		 */
		me.loadJs = function(url,callback){
			pri_loadJs(url,callback);
		};
		
		/*
		 * 渲染 MDUI 动态加载的内容
		 */
		me.layout = function(){
			pri_layout();
		};
		
		/*
		 * 将外部html和js同时加载到某个div中
		 * 加载完毕后，执行callback，无参数
		 */
		me.loadHtmlToDiv = function(divId,htmlurl,callback,jsurl){
			pri_loadHtmlToDiv(divId,htmlurl,callback,jsurl);
		};
		
		/*
		 * 请求servlet，携带参数
		 * callback第一个参数为返回的json转成js对象
		 */
		me.loadJson = function(url,data,callback){
			pri_loadJson(url,data,callback);
		};
		
		/*
		 * 将模板html和js对象拼装成html字符串
		 */
		me.tmpl = function(htmltmpl,obj){
			pri_templateHtml(htmltmpl,obj);
		};
		
		/*
		 * 获得外部的html模板并设置到div中
		 */
		me.loadTmplToDiv = function(divId,tmplurl,data){
			pri_loadTmplToDiv(divId,tmplurl,data);
		};
		
		/*
		 * 文件上传
		 * url可以省略，默认使用form的action
		 */
		me.uploadFile = function(formId,callback,url){
			pri_uploadFile(formId,callback,url);
		};
		
		
		//***********************************************************************
		//==================================
		//返回对象本身
		//==================================
		return me;
	}()
});
