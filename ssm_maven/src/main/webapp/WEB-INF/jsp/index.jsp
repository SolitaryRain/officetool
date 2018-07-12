<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>首页</title>
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="Excel">
	<%@ include  file="commonCss.jspf"%>
    <link href="${pageContext.request.contextPath}/assets/templetHtml/css/plugins/sweetalert/sweetalert.css" rel="stylesheet">

  </head>
  
  
<body class="fixed-sidebar full-height-layout gray-bg" style="overflow:hidden">
    <div id="wrapper">
        <!--左侧导航开始-->
        <nav class="navbar-default navbar-static-side" role="navigation">
            <div class="nav-close"><i class="fa fa-times-circle"></i>
            </div>
            <div class="sidebar-collapse">
                <ul class="nav" id="side-menu">
                    <li class="nav-header">
                        <div class="dropdown profile-element">
                            <a data-toggle="dropdown" class="dropdown-toggle" href="#">
                                <span class="clear">
                                    <span class="block m-t-xs" style="font-size:20px;">
                                        <i class="fa fa-area-chart"></i>
                                        <strong class="font-bold">Excel处理</strong>
                                    </span>
                                </span>
                            </a>
                        </div>
                        <div class="logo-element">Excel处理
                        </div>
                    </li>
                    <li class="hidden-folded padder m-t m-b-sm text-muted text-xs">
                        <span class="ng-scope">分类</span>
                    </li>
                    <li>
                        <a class="J_menuItem" href="/exceldev/page/redir?index_v1" >
                            <i class="fa fa-home"></i>
                            <span class="nav-label">列表</span>
                        </a>
                    </li>

                </ul>
            </div>
        </nav>
        <!--左侧导航结束-->
        <!--右侧部分开始-->
        <div id="page-wrapper" class="gray-bg dashbard-1">
            <div class="row border-bottom">
                <nav class="navbar navbar-static-top" role="navigation" style="margin-bottom: 0">
                    <div class="navbar-header"><a class="navbar-minimalize minimalize-styl-2 btn btn-info-red " href="/exceldev/user/logout"><i class="fa">退出</i> </a>
                       
                        <form role="search" class="navbar-form-custom" method="post" >
                            <div class="form-group">
                                <input type="text"  value="当前店铺代码：${ sessionScope.user.username }" class="form-control" name="top-search" id="top-search" readonly="readonly">
                            	<input type="hidden" value="${ sessionScope.user.status }" id="userStatus">
                            	<input type="hidden" value="${ sessionScope.user.username }" id="userFlag">
                            </div>
                        </form>
                    </div>
                    <ul class="nav navbar-top-links navbar-right">
                        <li class="dropdown" style="margin:0 50 0 0">
                            <a class="dropdown-toggle count-info" data-toggle="dropdown" href="javascript:void(0)" onclick="redirPage('/exceldev/page/redir?file_upload')">
                                <span class="label label-warning" style="line-height:35px" >导入</span>
                            </a>
                        </li>
                        <li class="dropdown">
                            <a class="dropdown-toggle count-info" data-toggle="dropdown" href="javascript:void(0)" onclick="exportData('/exceldev/excel/merge')">
                                <span class="label label-primary" style="line-height:35px">导出</span>
                            </a>
                       
                        </li>
                    </ul>
                </nav>
            </div>
            <div class="row J_mainContent" id="content-main">
                <iframe id="J_iframe" width="100%" height="100%" src="/exceldev/page/redir?file_upload" frameborder="0" ></iframe>
            </div>
        </div>
        <!--右侧部分结束-->
    </div>

    <!-- 全局js -->
    <%@ include  file="commonJs.jspf"%>
    <script src="${pageContext.request.contextPath}/assets/templetHtml/js/plugins/metisMenu/jquery.metisMenu.js"></script>
    <script src="${pageContext.request.contextPath}/assets/templetHtml/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>

    <!-- 自定义js -->
    <script src="${pageContext.request.contextPath}/assets/templetHtml/js/hAdmin.js?v=4.1.0"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/assets/templetHtml/js/index.js"></script>

    <!-- 第三方插件 -->
    <script src="${pageContext.request.contextPath}/assets/templetHtml/js/plugins/pace/pace.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/templetHtml/js/plugins/sweetalert/sweetalert.min.js"></script>
    
    
    <script type="text/javascript">
	    $(function(){
	    	if($("#userStatus").val()==0){
		    	swal({
		    	      type: "prompt",
		    		  html: true,
		    		  title: '密码改改',
		    		  text: "<input type='text' name='pwdContext' id='pwd'>",
		    		  confirmButtonText: '提交',
		    		  confirmButtonClass: 'btn btn-success',
		    		  closeOnConfirm:false
		    		},function(){
  	                	pwd = $("#pwd").val();
  	                	if($.trim(pwd)==""){
  	                		swal.showInputError("输入你要改的密码!");
  	                	}else{
			    			$.ajax({
			    				 type: "post",
			    	                url: "/exceldev/user/revisePw",
			    	                data: {"password": pwd},
			    	                success: function (data) {
			    	                	window.location.href = "/exceldev/excel/download?fileName=initLogin";
			    	                	//示范一个公告层
			    	                	layer.open({
			    	                	  type: 1
			    	                	  ,title: false //不显示标题栏
			    	                	  ,closeBtn: false
			    	                	  ,area: '300px;'
			    	                	  ,shade: 0.8
			    	                	  ,id: 'LAY_layuipro' //设定一个id，防止重复弹出
			    	                	  ,resize: false
			    	                	  ,btn: ['哦']
			    	                	  ,btnAlign: 'c'
			    	                	  ,moveType: 1 //拖拽模式，0或者1
			    	                	  ,content: '<div style="padding: 50px; line-height: 22px; background-color: #393D49; color: #fff; font-weight: 300;">密码已经重置好了<br/>Excel导入在右上角<br/>模板文件也已经自动下载<br/>这个东西是为了规范Excel格式<br/>所以务必请按照模板格式导入<br/>在赵旭汇总之前,随便导入<br/>他汇总后,今天就不再让导入了<br/>出现格式问题找赵旭<br/>如果有想加的功能要改进的来找我就行<br/>这个东西就弹一次,所以看看就行,别当真</div>'
			    	                	});
			    	                	swal.close();
			    	                }
			    			});
		    			}
		    		});
		    	};
	    });
    
    	function redirPage(pagePath){
    		console.log(pagePath);
    		$('#J_iframe').attr('src',pagePath);
    		console.log($('#J_iframe').attr('src'));
    		
    	}
    	
    	function exportData(url){
    		if($("#userFlag").val()!="admin"){
    			swal({ 
    			  type:"error",
  				  title: "权限不足", 
  				  text: "这个是由赵旭控制,有兴趣可以找他要,当然他也不知道怎么开权限,所以这段话你看看就行", 
  				  timer: 5000, 
  				  showConfirmButton: false 
  				});
    			return;
    		}
    		var index = layer.load(0,{
    			  shade: [0.3,'#fff'] //0.1透明度的白色背景
    		}); 
            $.ajax({
                type:"POST",
                url: url,
                contentType: "application/json", //如果提交的是json数据类型，则必须有此参数,表示提交的数据类型 
                //dataType: "json", //表示返回值类型，不必须           
                success: function (data) {                  
                    //关闭
                    window.location.href = "/exceldev/excel/download";
                    layer.msg("处理完毕,共合并"+data.fileNum+"个文件");
            		layer.close(index);  
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                	swal({ 
          			  type:"warning",
       				  title: "按错了吧？", 
       				  text: "没有找到需要合并的文件", 
       				  timer: 5000, 
       				  showConfirmButton: false 
       				});
             		layer.close(index); 
                }
            });      
    	}
    </script>
</body>
</html>
