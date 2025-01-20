/*	jQuery AdubyTree Plugin 
 *	version: 5.0 (2011-03-20)
 *
 * 	Copyright (c) 2011, Crystal, shimingxy@163.com
 * 	Dual licensed under the MIT and GPL licenses:
 * 	http://www.opensource.org/licenses/mit-license.php
 * 	http://www.gnu.org/licenses/gpl.html
 * 	Date: 2011-03-20 rev 12
 */
;(function ($) {
$.adubytree = $.adubytree||{};
$.fn.AdubyTree = function(config ) { 
	//default AdubyTree Config Settings
	var adubyTree ={
		config		:$.extend({
			url				: null,//remote url for ajax data
			id				: '-1',//root id
			datatype		: "json",//return the data type from remote url,datatype json��xml��default is json
			type			: "POST",//remote data method type ,GET��POST��default is GET
			cookie			: true,//allow cookie set for current
			treeType        : "extend",//three tree type simple��strengthen��extend
			checkboxes		: false,//true is use checkbox,false is not
			checkboxPos     :"before",//checkbox position before or after
			selected		: null,
			contextmenu		: null,
			onSelected		: null,
			onDblClick 		: null,
			onClick         : null,
			onCBXClick      : null,
			onCBXDblClick   : null,
			onMouseOver     : null,
			onMouseOut      : null,
			onOpen			: null,
			onAddNewNode	: null,
			onEditNode		: null,
			onMoveNode		: null,
			onMoveToNextNode: null,
			onMoveToChildNode:null,
			onDeleteNode	: null,
			onClose			: null,
			loadComplete	: null,
			loadonce		: false,
			contextmenuOPType	:	null,
			contextmenuTempData : null,
			contextmenuText	:{//default context Menu Text
					newText		:	"New",
					editText	:	"Edit",
					copyText	:	"Copy",
					moveText	:	"Move",
					pasteText	:	"Paste",
					moveToNextText	:"MoveToNext",
					moveToChildText	:"MoveToChild",
					deleteText	:	"Delete"
				},
			nodeConfig		: {//default Node configration items
								data				: 'Tree Item',
								jsaction   			: 'javascript:void(0);', //adubytree node action
								checked				: false,//
								open				: false,
								selected			: false,
								loaded				: false,
								haschild			: false,
								children			: []
				}
			}, 
			config ||{},
			{
				themes:"adubytree"+(config.themes||"basic").toLowerCase()//Basic Sample Books Org OrgUser Vista six themes
			}
		),
		isClick    : true,//is Click  or DblClick on checkbox
		RtnNode:null,//getnode return eq node
		//Convert one node object to html
		parseNode : function (node) {
			var str ="";
			if(node.indent&&node.indent.length){
				for(var i=1 ;i < node.indent.length -1; i++) {
					switch (this.config.treeType) {
					case "simple": 
					case "strengthen":
						str	+=	"<div class ='node Empty'></div>";
						break;
					case "extend":
						if(node.indent[i] == 0){
							str	+=	"<div class ='node I'></div>";
						}else{
							str	+=	"<div class ='node Empty'></div>";
						}
						break;
					}
				}
			}
			
			if(node.haschild){
				if(node.indent.length > 1){
					//simple strengthen extend
					switch (this.config.treeType) {
						case "simple": 
							str	+=	"<div class ='node Empty'></div>";
							break;
						case "strengthen":
							if(node.open){
								if(node.indent[node.indent.length-1] == 1){
									str	+=	"<div id='"+this.elemId+"-pm-"+node.id+"'  class ='node plusminus stOpen last'></div>";
								}else{
									str	+=	"<div id='"+this.elemId+"-pm-"+node.id+"'  class ='node plusminus  stOpen'></div>";
								}
							}else{
								if(node.indent[node.indent.length-1] == 1){
									str	+=	"<div id='"+this.elemId+"-pm-"+node.id+"'  class ='node plusminus stClose last'></div>";
								}else{
									str	+=	"<div id='"+this.elemId+"-pm-"+node.id+"'  class ='node plusminus stClose'></div>";
								}
							}
							break;
						case "extend":
							if(node.open){
								if(node.indent[node.indent.length-1] == 1){
									str	+=	"<div id='"+this.elemId+"-pm-"+node.id+"'  class ='node plusminus Lminus last'></div>";
								}else{
									str	+=	"<div id='"+this.elemId+"-pm-"+node.id+"'  class ='node plusminus Tminus'></div>";
								}
							}else{
								if(node.indent[node.indent.length-1 ]== 1){
									str	+=	"<div id='"+this.elemId+"-pm-"+node.id+"'  class ='node plusminus Lplus last'></div>";
								}else{
									str	+=	"<div id='"+this.elemId+"-pm-"+node.id+"'  class ='node plusminus Tplus'></div>";
								}
							}
							break;
					}
				}
				
				if(this.config.checkboxPos=="before"&&this.config.checkboxes){
					str	+=	"<div id='"+this.elemId+"-ckbx-"+node.id+"'  class ='node checkBoxUncheck checkbox'></div>";
				}
				
				if(node.open){
					str	+=	"<div id='"+this.elemId+"-fd-"+node.id+"'  class ='node  folder FolderOpen'></div>";
				}else{
					str	+=	"<div id='"+this.elemId+"-fd-"+node.id+"'  class ='node folder Folder'></div>";
				}
				
			}else{
				switch (this.config.treeType) {
					case "simple": 
					case "strengthen":
						str	+=	"<div class ='node Empty'></div>";
						break;
					case "extend":
						if(node.indent[node.indent.length-1] == 0){
							str	+=	"<div class ='node T'></div>";
						}else{
							str	+=	"<div class ='node L'></div>";
						}
						break;
				}
				if(this.config.checkboxPos=="before"&&this.config.checkboxes){
					str	+=	"<div id='"+this.elemId+"-ckbx-"+node.id+"'  class ='node checkbox checkBoxUncheck'></div>";
				}
				str	+=	"<div id='"+this.elemId+"-fd-"+node.id+"' class ='node leaf Leaf'></div>";
			}
			
			if(this.config.checkboxPos=="after"&&this.config.checkboxes){
				str	+=	"<div  id='"+this.elemId+"-ckbx-"+node.id+"'   class ='node checkbox checkBoxUncheck'></div>";
			}
			
			if(node.selected){
				str	+= 	"<a id='"+this.elemId+"-"+node.id+"' name='"+node.id+"' href='"+ node.jsaction +"'  data='"+this.object2JsonString(node)+"'   pid='"+node.pid+"'   class='node  selected' nowrap>"+node.data + "</a>";
			}else{
				str	+= 	"<a id='"+this.elemId+"-"+node.id+"' name='"+node.id+"' href='"+node.jsaction +"'   data='"+this.object2JsonString(node)+"'   pid='"+node.pid+"'   class='node' nowrap>"+node.data + "</a>";
			}
			if(node.open){
				str	+=	"<div id='"+this.elemId+"-div-child-"+node.id+"' class='"+this.config.themes+"'></div>";
			}else{
				str	+=	"<div id='"+this.elemId+"-div-child-"+node.id+"' class='"+this.config.themes+"' style='display:none'></div>";
			}
			return str ;
		},
		// Convert data Json to Html by node_id,then append to node_id child element
		parseJSON : function (node_id,data,indent,pNode) {
			data 		= 	data || this.config.data;
			var indent 	=	indent || [0]; 
			var str		=	"";
			data.indent=indent;data.checked=(data.checked ? true : false);
			if((data.children  && data.children.length||data.haschild=="true"||data.haschild)&&node_id){//not root
				if(data.children  && data.children.length){
					for(var i = 0; i < data.children.length; i++) {
						(i == data.children.length -1) ? indent.push(1) : indent.push(0);
						data.children[i].order=	i+1;
						data.children[i].pid=data.id;
						if(node_id==data.id){
							str	+=	"<div  class='"+this.config.themes+"'>";
							str += this.parseNode($.extend({},this.config.nodeConfig,data.children[i],{indent:indent},{haschild :(this.config.url?(data.children[i].haschild==true||data.children[i].haschild=="true")?true:false:data.children[i].children)},{pid :data.id}));
							str	+=	"</div>";
						}else{
							this.parseJSON(node_id,data.children[i],indent,data);
						}
						indent.pop();
					}
				}
			}else if(pNode){//leaf node
				var pNode		= pNode || this.config.data;
				str	+= this.parseNode($.extend({},this.config.nodeConfig,data,{indent:indent},{haschild :false},{pid :pNode.id}));
			}else{
				str	+= this.parseNode($.extend({},this.config.nodeConfig,data,{indent:indent},{haschild :true},{pid :this.config.data.id}));
			}
			if(node_id==data.id){
				$(this.elem+"-div-child-"+node_id).html(str);
				return;
			}
			return str;
		},
		//Open one Tree Node and set node open =true
		open : function(node_id,data) {
			node_id = node_id || this.config.data.id;
			if($(this.elem+"-fd-"+node_id).hasClass("Leaf"))return;
			if(this.config.url){//from url
				data = data || this.config.data;
				var node = $.extend({},this.config.nodeConfig,data);
				if(node.haschild && node.loaded == false && data.id==node_id){
					_this = this;data.loaded =true;data.open=true;
					$(this.elem+"-fd-"+node.id).addClass("loading");
					$.ajax({url: _this.config.url,type: _this.config.type,dataType: _this.config.dataType ,data:{ id : node_id},timeout: 15000,async: false,
						error: function(){alert('Error loading '+_this.adubyTree.config.dataType+' document');},
						complete:function(XMLHttpRequest){},
						success: function(ajaxdata, textStatus){
							if(_this.config.dataType=="xml"){//data from remote xml
								data.children=_this.getJsonFromXml(ajaxdata,"xml");
							}else{//data from remote json
								data.children=ajaxdata;
							}
							_this.open(node_id);
							$(_this.elem+"-fd-"+node_id).removeClass("loading");
						}
					});
					return;
				}else{
					if(data.children  && data.children.length){
						for(var i = 0; i < data.children.length; i++) {
							this.open(node_id,data.children[i]);
						}
					}
				}
			}
			if(node_id&&typeof(data)=="undefined"||(typeof(data)=="object"?data.id:false)==node_id&&this.config.url){
				if($(this.elem+"-div-child-"+node_id).html()==""){
					this.parseJSON(node_id);
				}
				$(this.elem+"-div-child-"+node_id).slideDown($.easing.easeOutBounce?{duration:500, easing: 'easeOutBounce'}:{duration:500});//jQuery Easing v1.3 is options
				$(this.elem+"-fd-"+node_id).removeClass("Folder").addClass("FolderOpen open");
				if($(this.elem+"-pm-"+node_id).hasClass("last")){
					switch (this.config.treeType) {
					case "simple": 
						break;
					case "strengthen":
						$(this.elem+"-pm-"+node_id).removeClass("stClose").addClass("stOpen");
						break;
					case "extend":
						$(this.elem+"-pm-"+node_id).removeClass("Lplus").addClass("Lminus");
						break;
					}
				}else{
					switch (this.config.treeType) {
					case "simple": 
						break;
					case "strengthen":
						$(this.elem+"-pm-"+node_id).removeClass("stClose").addClass("stOpen");
						break;
					case "extend":
						$(this.elem+"-pm-"+node_id).removeClass("Tplus").addClass("Tminus");
						break;
					}
				}
			}
			return;
		},
		//Close one Tree Node and set node open =false
		close : function(node_id,data) {
			$(this.elem +"-fd-"+node_id).removeClass("FolderOpen open").addClass("Folder");
			if($(this.elem +"-pm-"+node_id).hasClass("last")){
				switch (this.config.treeType) {
				case "simple": 
					break;
				case "strengthen":
					$(this.elem+"-pm-"+node_id).removeClass("stOpen").addClass("stClose");
					break;
				case "extend":
					$(this.elem+"-pm-"+node_id).removeClass("Lminus").addClass("Lplus");
					break;
				}
			}else{
				switch (this.config.treeType) {
				case "simple": 
					break;
				case "strengthen":
					$(this.elem+"-pm-"+node_id).removeClass("stOpen").addClass("stClose");
					break;
				case "extend":
					$(this.elem+"-pm-"+node_id).removeClass("Tminus").addClass("Tplus");
					break;
				}
			}	
			$(this.elem+"-div-child-"+node_id).slideUp($.easing.easeOutBounce?{duration:500, easing: 'easeOutBounce'}:{duration:500});//jQuery Easing v1.3 is options
		},
		//Check is the Tree Node open, if open return true or return false
		isOpen : function(node_id){
			return $(this.elem+"-fd-"+node_id).hasClass("open");
		},
		//Open All Tree Node and set node open =true
		openAll : function(data){
			data = data || this.config.data;
			if(data.children  && data.children.length){
				data.open = true;
				this.open(data.id,data.children[i]);
				for(var i = 0; i < data.children.length; i++) {
					this.openAll(data.children[i]);
				}
			}
		},
		//Close All Tree Node and set node open =false
		closeAll : function(data){
			data = data || this.config.data;
			if(data.children  && data.children.length){
				data.open = false;
				this.close(data.id,data.children[i]);
				for(var i = 0; i < data.children.length; i++) {
						this.closeAll(data.children[i]);
				}
			}	
		},
		//Change a Tree Node status,if node is open,change the node to close,or change to open
		changeStatus : function(node_id){
			if($(this.elem+"-fd-"+node_id).hasClass("open")){
				this.close(node_id);
			}else{
				this.open(node_id);
			}
		},
		//find a Tree node from node_id Aduby Tree for getNode
		findNode : function(node_id,data){
			data = data || this.config.data;
			if(data.id==node_id){
				this.RtnNode = $.extend({},this.config.nodeConfig,data);
				return;
			}else if(data.children  && data.children.length){
				for(var i = 0; i < data.children.length; i++) {	
					data.children[i].order=i;
					this.findNode(node_id,data.children[i]);
				}
			}
		},
		//get a Tree node from node_id Aduby Tree
		getNode : function(node_id){
			this.findNode(node_id);
			return this.RtnNode;
		},
		//Add a new node to current Aduby Tree
		addNode : function(node,pnode_id,data){
			pnode_id = pnode_id || this.config.data.id;
			data = data || this.config.data;
			if(data.id==pnode_id){
				data.children =	data.children || [];data.haschild="true";
				if(node.children&&node.children.length==0){
					delete  node.children;data.haschild="false";
				}
				if(data.children.length==0){
					if(node.constructor==Array){
						data.children=node;
					}else{
						data.children.push(node);
					}
				}else{
					if(node.constructor==Array){
						for(var i=0;i < node.length;i++){
							if(node[i].order&&node[i].order<data.children.length){
								var tempAddData=[];
								for(var i=0;i < data.children.length;i++){
									if(node[i].order==i)tempAddData.push(node[i]);
									tempAddData.push(data.children[i]);
								}
								data.children=tempAddData;
							}else{
								data.children.push(node);
							}
						}
					}else{
						if(node.order&&node.order<data.children.length){
							var tempAddData=[];
							for(var i=0;i < data.children.length;i++){
								if(node.order==i)tempAddData.push(node);
								tempAddData.push(data.children[i]);
							}
							data.children=tempAddData;
						}else{
							data.children.push(node);
						}
					}
				}
				$(this.elem+"-div-child-"+data.pid).html("");
				this.open(data.pid);
				this.open(pnode_id);
				this.applyCookie();
				return;
			}else if(data.children  && data.children.length){
				for(var i = 0; i < data.children.length; i++) {
					this.addNode(node,pnode_id,data.children[i]);
				}
			}
		},
		//Remove a Tree node from current Aduby Tree
		remove : function(node_id,data){
			data = data || this.config.data;
			if(node_id==this.config.data.id){alert("Node is root node,can't removed!");return;}
			if(data.children  && data.children.length){
				var dataChild=[];
				var isRemove =false;
				for(var i = 0; i < data.children.length; i++) {
					if(data.children[i].id==node_id){
						isRemove=true;
						continue;
					}
					dataChild.push(data.children[i]);
				}
				if(isRemove){
					if(dataChild.length==0){
						delete data.children;
						data.haschild="false";
						$(this.elem+"-div-child-"+data.pid).html("");
						this.open(data.pid);
					}else{
						data.children=dataChild;
						$(this.elem+"-div-child-"+data.id).html("");
						this.open(data.id);
					}
					
					this.applyCookie();
					return;
				}
				for(var i = 0; i < data.children.length; i++) {
					this.remove(node_id,data.children[i]);
				}
			}
		},
		mouseOver :function(elemObj,type){
			if(type == "mouseover"){
				if($(elemObj).hasClass("checked")){
					$(elemObj).removeClass("checkBoxChecked").addClass("checkBoxCheckedHover");
				}else{
					$(elemObj).removeClass("checkBoxUncheck").addClass("checkBoxUncheckHover");
				}
			}else{
				if($(elemObj).hasClass("checked")){
					$(elemObj).removeClass("checkBoxCheckedHover").addClass("checkBoxChecked");
				}else{
					$(elemObj).removeClass("checkBoxUncheckHover").addClass("checkBoxUncheck");
				}
			}	
		},
		setCheckStatus : function(node_id){
			if($(this.elem+"-ckbx-"+node_id).hasClass("checked")){
				if($(this.elem+"-ckbx-"+node_id).hasClass("checkBoxCheckedHover")){
					$(this.elem+"-ckbx-"+node_id).removeClass("checkBoxChecked checkBoxCheckedHover checked").addClass("checkBoxUncheck checkBoxUncheckHover");
				}else{
					$(this.elem+"-ckbx-"+node_id).removeClass("checkBoxChecked checked").addClass("checkBoxUncheck");
				}
			}else{
				if($(this.elem+"-ckbx-"+node_id).hasClass("checkBoxUncheckHover")){
					$(this.elem+"-ckbx-"+node_id).removeClass("checkBoxUncheck checkBoxUncheckHover").addClass("checkBoxChecked checkBoxCheckedHover checked");
				}else{
					$(this.elem+"-ckbx-"+node_id).removeClass("checkBoxUncheck").addClass("checkBoxChecked checked");
				}
			}		
		},
		setCheckStatusAll : function(node_id){
			this.setCheckStatus(node_id);
			if($(this.elem+"-ckbx-"+node_id).hasClass("checked")){
				$(this.elem+"-div-child-"+node_id+" .checkbox").each(function(i){
					$(this).removeClass("checkBoxChecked checked").addClass("checkBoxUncheck");
				});
			}else{
				$(this.elem+"-div-child-"+node_id+" .checkbox").each(function(i){
					$(this).removeClass("checkBoxUncheck").addClass("checkBoxChecked checked");
				});
			}
			while( node_id != this.config.data.id){
				node_id = $(this.elem+"-"+node_id).attr( "pid" );
				if(!$(this.elem+"-ckbx-"+node_id).hasClass("checked") ){
					$(this.elem+"-ckbx-"+node_id).removeClass("checkBoxUncheck").addClass("checkBoxChecked checked");
				}
			}
		},
		//Change Tree Node selected  status
		setSelect : function(node_id){
			if(this.selected){
				$(this.elem+"-"+this.selected).removeClass("selected");
			}
			this.selected = node_id;
			$(this.elem+"-"+node_id).addClass("selected");
		},
		refresh : function(){
			$(this.elem).html("");
			$(this.elem).html(this.parseJSON());	
			this.applyCookie();			
		},
		modify : function(node_id,node,data) {
			data = data || this.config.data;
			if(data.id == node_id){
				data=$.extend(data,this.config.nodeConfig,node);
				$($(this.elem+"-"+node_id)).html(node.data);	
				$($(this.elem+"-"+node_id)).attr("data",this.object2JsonString(data))
				return;
			}
			if(data.children  && data.children.length){
				for(var i = 0; i < data.children.length; i++) {
					this.modify(node_id,node,data.children[i]);
				}
			}
		},
		getChecked : function(){
			var strCheckedNodes ="";_this = this;
			$(this.elem+" div.checked").each(function(i){
				strCheckedNodes +=this.id.substring((_this.elemId+"-ckbx-").length) +",";
			});
			return strCheckedNodes.substring(0,strCheckedNodes.length -1 );			
		},
		getOpened : function(){
			var strOpenNodes ="";_this = this;
			$(this.elem+" div.open").each(function(i){
				strOpenNodes +=this.id.substring((_this.elemId+"-fd-").length) +",";
			});
			return strOpenNodes.substring(0,strOpenNodes.length -1 );			
		},
		//Get Tree selected nodes
		getSelected : function(){
			return this.selected;
		},
		setCookie : function(type){
			if(this.config.cookie){
				var cookieValue="";
				switch(type) {
					case "selected":
						if(this.getSelected())
							cookieValue=this.getSelected();
						break;
					case "checked":
						cookieValue=this.getChecked();
						break;
					case "opened":
						cookieValue=this.getOpened();
						break;
				}
				document.cookie = this.elem+"_"+type + "="+ escape (cookieValue);
			}
		},
		getCookie : function(type){
			var cookie=document.cookie.match(new RegExp("(^| )"+this.elem+"_"+type+"=([^;]*)(;|$)"))
			return cookie ? unescape(cookie[2]) : "";
		},
		applyCookie : function(type){
			var openedCookies = this.getCookie("opened").split(",");
			for(var i=0;i<openedCookies.length;i++){
				this.open(openedCookies[i]);
			}
			var checkedCookies = this.getCookie("checked").split(",");
			for(var i=0;i<checkedCookies.length;i++){
				this.setCheckStatus(checkedCookies[i]);
			}
			if(this.getCookie("selected")!="")
				this.setSelect(this.getCookie("selected"));	
		},
		xmlToObject: function(xml){
			var node={};
			for(var i=0;i<xml.childNodes.length &&xml.hasChildNodes();i++ ){
				var nodeName = (xml.childNodes[i].nodeName).toUpperCase();
				var nodeValue =xml.childNodes[i].textContent  || xml.childNodes[i].text;
				if(nodeName == "ID"){
					node.id = nodeValue;
				}else if(nodeName == "DATA"){
					node.data = nodeValue;
				}else if(nodeName == "HASCHILD"){
					node.haschild =  nodeValue=="true" ? true: false;
				}else if(nodeName == "JSACTION"){
					node.jsaction =  nodeValue;
				}else if(nodeName == "ICON"){
					node.icon =  nodeValue;
				}else if(nodeName == "OPENICON"){
					node.openIcon =  nodeValue;
				}else if(nodeName == "LEAFICON"){
					node.leafIcon =  nodeValue;
				}else if(nodeName == "CHECKED"){
					node.checked =  nodeValue=="true" ? true: false;
				}else{
					node[nodeName]=nodeValue;
				}
			}
			return node;
		},
		getJsonFromXml: function(xml,type){
			var xmlDoc;
			if(typeof(xml)!='object'){
				//alert(xml);
				xml=xml.substring(xml.indexOf("<root>"));
				try {//Internet Explorer
					xmlDoc=new ActiveXObject("Microsoft.XMLDOM");
					xmlDoc.async=false;
					xmlDoc.loadXML(xml);
				//	alert(xmlDoc.childNodes[0].text);
				}catch(e){
					try {//Firefox, Mozilla, Opera, etc.
						xmlDoc=(new DOMParser()).parseFromString(xml,"text/xml");
					}catch(e) {alert(e.message)}
				}
				xmlDoc =  xmlDoc.childNodes[0];
			}else{
				xmlDoc=xml;
				if(this.config.url){//if load from remort
					if($.browser.msie){//msie start 1
						xmlDoc=xmlDoc.childNodes[1];
					}else {
						xmlDoc=xmlDoc.childNodes[0];
					}
				}
			}
			
			
			var tree_obj = type ? [] :this.xmlToObject(xmlDoc);
			if(type){
				for(var i=0;i<xmlDoc.childNodes.length &&xmlDoc.hasChildNodes();i++ ){
					tree_obj.push(this.xmlToObject(xmlDoc.childNodes[i]));
				}
			}else{
				for(var i=0;i<xmlDoc.childNodes.length &&xmlDoc.hasChildNodes();i++ ){
					var node=xmlDoc.childNodes[i];
					 if((node.nodeName).toUpperCase() == "CHILDREN"){
						tree_obj.children = [];
						for(var j=0;j<node.childNodes.length &&node.hasChildNodes();j++ ){
							tree_obj.children.push(this.getJsonFromXml(node.childNodes[j]));
						}
					}
				}
			}
			return tree_obj;
		},
		//Object convert to Json String ,not include Array property,then replace ' with -
		object2JsonString: function(obj){
			var json	=	"{";
			for (variable in obj){
				if(obj[variable]!=null&&obj[variable].constructor!=Array){
					json	+=	"'"+variable+"':'"+obj[variable]+"',";
				}
			}
			json	+=	"'adubytree':''}";
			return json.replace(/'/g,"#");
		},
		//String replace- with ',then convert to Json
		string2Json: function(jsonString){
			return eval("(" + jsonString.replace(/#/g,"'") + ")");
		},
		loadTree	: function(){
			$(this.elem).html(this.parseJSON());
			this.open();
			//this.applyCookie();
		}
	};
	//$.fn AdubyTree Public function
	$.fn.extend({
		getChecked : function () {
			var adubyTreeChecked="";
			this.each( function() {
				if(this.adubytree) return; 
				adubyTreeChecked= this.adubyTree.getChecked();
			});
			return adubyTreeChecked;
		},
		getSelected : function () {
			var adubyTreeSelect="";
			this.each( function() {
				if(this.adubytree) return; 
				adubyTreeSelect= this.adubyTree.getSelected();
			});
			return adubyTreeSelect;
		},
		getNode	: function (node_id) {
			var adubyTreeNode;
			this.each( function() {
				if(this.adubytree) return; 
				adubyTreeNode =this.adubyTree.getNode(node_id);
			});
			return adubyTreeNode;
		},
		refresh : function () {
			return this.each( function() {
				if(this.adubytree) return;
				this.adubyTree.refresh();
			});
		},
		openAll : function () {
			return this.each( function() {
				if(this.adubytree) return;
				this.adubyTree.openAll();
			});
		},
		closeAll : function () {
			return this.each( function() {
				if(this.adubytree) return;
				this.adubyTree.closeAll();
			});
		},
		addNode : function (newNode,pnode_id) {
			return this.each( function() {
				if(this.adubytree) return;
				this.adubyTree.addNode(newNode,pnode_id);
			});
		},
		modify : function (node_id,node) {
			return this.each( function() {
				if(this.adubytree) return;
				this.adubyTree.modify(node_id,node);
				this.adubyTree.applyCookie();
			});
		},
		removeNode: function (node_id) {
			return this.each( function() {
				if(this.adubytree) return;
				this.adubyTree.remove(node_id);
			});
		}
	}); 
	
	return this.each( function() {
		if(this.adubytree) return;//init adubytree property judge
		var _this= this;
		$(_this).addClass(adubyTree.config.themes);//add adubytree themes
		//default AdubyTree Node Config
		_this.adubyTree = $.extend({elem:"#"+this.id},{elemId:this.id},adubyTree);
		// define events to Aduby Tree
		var applyEvents	= function(){
			//mouseover or mouseout div.checkbox change checkbox status
			$(_this.adubyTree.elem).on("mouseover mouseout","  div.checkbox", function (event) {
				if($(this).hasClass("checkbox")){
					event.preventDefault(); event.stopPropagation();
					_this.adubyTree.mouseOver(this,event.type);
					return false;
				}	
			});
			
			//click dblclick mouseover mouseout the node item
			$(_this.adubyTree.elem).on("click","  a", function (event) {
				if($(this).hasClass("node")){
					var node_id=this.id.substring((_this.adubyTree.elemId+"-").length);
					_this.adubyTree.setSelect(node_id);
					_this.adubyTree.setCookie("selected");
					if(_this.adubyTree.config.onSelected){
						_this.adubyTree.config.onSelected.call(null,_this.adubyTree.string2Json($(this).attr("data")));
					}
					if(_this.adubyTree.config.onClick){
						_this.adubyTree.config.onClick.call(null,_this.adubyTree.string2Json($(this).attr("data")));
					}
					return false;
				}
				
			}).on("dblclick", function (event) { 
				if($(this).hasClass("node")){
					var node_id=this.id.substring((_this.adubyTree.elemId+"-").length);
					event.preventDefault(); event.stopPropagation();
					_this.adubyTree.setSelect(node_id);
					_this.adubyTree.changeStatus(node_id);
					_this.adubyTree.setCookie("opened");
					if(_this.adubyTree.config.onDblClick){
						_this.adubyTree.config.onDblClick.call(null,_this.adubyTree.string2Json($(this).attr("data")));
					}
					return false;
				}
			}).on("mouseover mouseout", function (event) {
				if($(this).hasClass("node")){
					event.preventDefault(); event.stopPropagation();
					_this.adubyTree.mouseOver(this,event.type);
					if(event.type=="mouseover"){
						if(_this.adubyTree.config.onMouseOver){
							_this.adubyTree.config.onMouseOver.call(null,_this.adubyTree.string2Json($(this).attr("data")));
						}
					}else{
						if(_this.adubyTree.config.onMouseOut){
							_this.adubyTree.config.onMouseOut.call(null,_this.adubyTree.string2Json($(this).attr("data")));
						}
					}
					return false;
				}			
			}).on("mousedown", function (event) { //contextmenu
				if($(this).hasClass("node")&&(event.button == 2 || event.button == 3)&&_this.adubyTree.config.contextmenu){
					event.stopPropagation();
					var node_id=this.id.substring((_this.adubyTree.elemId+"-").length);
					_this.adubyTree.setSelect(node_id);
					document.oncontextmenu=function(){return false;}//remove right click browse action
					$(_this.adubyTree.elem+'-contextMenu').css({left:event.pageX-1, top:event.pageY-1});
					$(_this.adubyTree.elem+'-contextMenu').show();
					$(document).bind("click",function (event) { 
						$(_this.adubyTree.elem+'-contextMenu').hide();
						document.oncontextmenu=function(){return true;}//add right click browse action
					});
					return false
				}
			});
			//click dblclick folder plusminus or checkbox div
			$(_this.adubyTree.elem).on("click","  div", function (event) {
				if($(this).hasClass("folder")){
					var node_id=this.id.substring((_this.adubyTree.elemId+"-fd-").length);
					event.preventDefault(); event.stopPropagation();
					_this.adubyTree.changeStatus(node_id);
					_this.adubyTree.setCookie("opened");
					if(_this.adubyTree.isOpen(node_id)){
						if(_this.adubyTree.config.onOpen){
							_this.adubyTree.config.onOpen.call(null,_this.adubyTree.string2Json($(_this.adubyTree.elem +"-"+node_id).attr("data")));
						}
					}else{
						if(_this.adubyTree.config.onClose){
							_this.adubyTree.config.onClose.call(null,_this.adubyTree.string2Json($(_this.adubyTree.elem +"-"+node_id).attr("data")));
						}
					}
				}else if($(this).hasClass("plusminus")){
					var node_id=this.id.substring((_this.adubyTree.elemId+"-pm-").length);
					event.preventDefault(); event.stopPropagation();
					_this.adubyTree.changeStatus(node_id);
					_this.adubyTree.setCookie("opened");
					if(_this.adubyTree.isOpen(node_id)){
						if(_this.adubyTree.config.onOpen){
							_this.adubyTree.config.onOpen.call(null,_this.adubyTree.string2Json($(_this.adubyTree.elem +"-"+node_id).attr("data")));
						}
					}else{
						if(_this.adubyTree.config.onClose){
							_this.adubyTree.config.onClose.call(null,_this.adubyTree.string2Json($(_this.adubyTree.elem +"-"+node_id).attr("data")));
						}
					}
				}else if($(this).hasClass("checkbox")){
					var node_id=this.id.substring((_this.adubyTree.elemId+"-ckbx-").length);
					event.preventDefault(); event.stopPropagation();
					if(_this.adubyTree.isClick === true){//judge is click
						_this.adubyTree.isClick = false;
						_this.adubyTree.setCheckStatus(node_id);
						_this.adubyTree.setCookie("checked");
						if(_this.adubyTree.config.onCBXClick){//dblclick
							_this.adubyTree.config.onCBXClick.call(null,_this.adubyTree.string2Json($(_this.adubyTree.elem +"-"+node_id).attr("data")));
						}
					}
					window.setTimeout(function(){_this.adubyTree.isClick = true;},400);
				}
				event.stopPropagation();
				return false;
			}).on("dblclick", function (event) { 
				if($(this).hasClass("checkbox")){
					var node_id=this.id.substring((_this.adubyTree.elemId+"-ckbx-").length);
					event.preventDefault(); event.stopPropagation();
					_this.adubyTree.setCheckStatusAll(node_id);
					_this.adubyTree.setCheckStatus(node_id);
					_this.adubyTree.setCookie("checked");
					if(_this.adubyTree.config.onCBXDblClick){
						_this.adubyTree.config.onCBXDblClick.call(null,_this.adubyTree.string2Json($(_this.adubyTree.elem +"-"+node_id).attr("data")));
					}
					return false;
				}
			});
			if(_this.adubyTree.config.contextmenu){//event of contextmenu
				$(_this.adubyTree.elem).on("click", "-contextMenu li",function (event) {
					var nodeData=_this.adubyTree.string2Json($(_this.adubyTree.elem +"-"+_this.adubyTree.selected).attr("data"));
					if($(this).hasClass("new")){
						if(_this.adubyTree.config.onAddNewNode){
							_this.adubyTree.config.onAddNewNode.call(null,nodeData);
						}
						_this.adubyTree.contextmenuOPType="new";
					}else if($(this).hasClass("edit")){
						_this.adubyTree.contextmenuTempData=_this.adubyTree.getNode(_this.adubyTree.selected);
						if(_this.adubyTree.config.onEditNode){
							_this.adubyTree.config.onEditNode.call(null,nodeData);
						}
						_this.adubyTree.contextmenuOPType="edit";
					}else if($(this).hasClass("move")){
						if(_this.adubyTree.config.onMoveNode){
							_this.adubyTree.config.onMoveNode.call(null,nodeData);
						}
						_this.adubyTree.contextmenuTempData=_this.adubyTree.getNode(_this.adubyTree.selected);
						$(_this.adubyTree.elem +"-contextMenu .movetonext").show();
						$(_this.adubyTree.elem +"-contextMenu .movetochild").show();
						_this.adubyTree.contextmenuOPType="move";
					}else if($(this).hasClass("movetonext")&&_this.adubyTree.contextmenuOPType=="move"){
						if(_this.adubyTree.config.onMoveToNextNode){
							_this.adubyTree.config.onMoveToNextNode.call(null,nodeData);
						}
						//delete moved child
						var addPid=$(_this.adubyTree.elem+"-"+_this.adubyTree.selected).attr("pid");
						_this.adubyTree.remove(_this.adubyTree.contextmenuTempData.id);
	
						//add node to current next
						_this.adubyTree.contextmenuTempData.order=nodeData.order;
						_this.adubyTree.addNode(_this.adubyTree.contextmenuTempData,addPid);
					
						$(_this.adubyTree.elem +"-contextMenu .movetonext").hide();
						$(_this.adubyTree.elem +"-contextMenu .movetochild").hide();
						_this.adubyTree.contextmenuOPType="movetonext";
					}else if($(this).hasClass("movetochild")&&_this.adubyTree.contextmenuOPType=="move"){
						if(_this.adubyTree.config.onMoveToChildNode){
							_this.adubyTree.config.onMoveToChildNode.call(null,nodeData);
						}
						//delete moved child
						_this.adubyTree.remove(_this.adubyTree.contextmenuTempData.id);
					
						//add node to current child
						_this.adubyTree.addNode(_this.adubyTree.contextmenuTempData,_this.adubyTree.selected);
				
						$(_this.adubyTree.elem +"-contextMenu .movetonext").hide();
						$(_this.adubyTree.elem +"-contextMenu .movetochild").hide();
						_this.adubyTree.contextmenuOPType="movetochild";
					}else if($(this).hasClass("delete")){
						_this.adubyTree.contextmenuOPType="delete";
						if(_this.adubyTree.config.onDeleteNode){
							_this.adubyTree.config.onDeleteNode.call(null,nodeData);
						}
						_this.adubyTree.remove(_this.adubyTree.selected);
					}
				});
			}
		};
		var init	= function(){//init tree function 
			if(_this.adubyTree.config.url){//data from remote url
				$.ajax({url: _this.adubyTree.config.url,type: _this.adubyTree.config.type,dataType: _this.adubyTree.config.dataType,data:{ id : _this.adubyTree.config.id},timeout: 15000,async: false,
					error: function(){alert('Error loading '+_this.adubyTree.config.dataType+' document');},
					complete:function(XMLHttpRequest){},
					success: function(data, textStatus){
						if(_this.adubyTree.config.dataType=="xml"){//data from remote xml
							_this.adubyTree.config.data =_this.adubyTree.getJsonFromXml(data,"xml")[0];
						}else{//data from remote json
							_this.adubyTree.config.data =data[0];
						}
						_this.adubyTree.loadTree();
					}
				});
			}else {//local data
				if(_this.adubyTree.config.dataType=="xml"){//local data is xml,then convert to json data
					_this.adubyTree.config.data =_this.adubyTree.getJsonFromXml(_this.adubyTree.config.data);
				}
				_this.adubyTree.loadTree();
			}
			if(_this.adubyTree.config.contextmenu){
				var contextMenu='<ul id="'+_this.adubyTree.elemId+'-contextMenu" class="contextMenu">';
					contextMenu+='<li class="new"><a href="javascript:void(0);">'+_this.adubyTree.config.contextmenuText.newText+'</a></li>';
					contextMenu+='<li class="edit"><a href="javascript:void(0);">'+_this.adubyTree.config.contextmenuText.editText+'</a></li>';
					contextMenu+='<li class="move separator"><a href="#javascript:void(0);">'+_this.adubyTree.config.contextmenuText.moveText+'</a></li>';
					contextMenu+='<li class="movetonext"><a href="javascript:void(0);">'+_this.adubyTree.config.contextmenuText.moveToNextText+'</a></li>';
					contextMenu+='<li class="movetochild"><a href="javascript:void(0);">'+_this.adubyTree.config.contextmenuText.moveToChildText+'</a></li>';
					contextMenu+='<li class="delete  separator"><a href="javascript:void(0);">'+_this.adubyTree.config.contextmenuText.deleteText+'</a></li>';
					contextMenu+='</ul>';
				$(_this.adubyTree.elem).parent().append(contextMenu);
			}
		};
		//start the init...
		init();
		//Add events to Aduby Tree	
		applyEvents();
	});
};
})(jQuery);