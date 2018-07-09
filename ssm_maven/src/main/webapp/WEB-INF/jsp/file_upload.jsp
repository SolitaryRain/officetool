<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
    <meta name="keywords" content="">
    <meta name="description" content="">
	<title>文件上传</title>
	
    <link rel="shortcut icon" href="favicon.ico"> 
    <link href="${pageContext.request.contextPath}/assets/templetHtml/css/bootstrap.min.css?v=3.3.6" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/templetHtml/css/font-awesome.css?v=4.4.0" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/templetHtml/css/animate.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/templetHtml/css/plugins/dropzone/basic.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/templetHtml/css/plugins/dropzone/dropzone.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/templetHtml/css/style.css?v=4.1.0" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/templetHtml/css/plugins/sweetalert/sweetalert.css" rel="stylesheet">

</head>

<body class="gray-bg">
    <div class="wrapper wrapper-content animated fadeIn">
        <div class="row">
            <div class="col-sm-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5>文件上传</h5>                    
                    </div>
                    <div class="ibox-content">
                        <form id="my-awesome-dropzone" class="dropzone" action="/exceldev/excel/deal">
                            <div class="dropzone-previews"></div>
                            <button type="submit" class="btn btn-primary pull-right">提交</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

    </div>

    <!-- 全局js -->
    <script src="${pageContext.request.contextPath}/assets/templetHtml/js/jquery.min.js?v=2.1.4"></script>
    <script src="${pageContext.request.contextPath}/assets/templetHtml/js/bootstrap.min.js?v=3.3.6"></script>



    <!-- 自定义js -->
    <script src="${pageContext.request.contextPath}/assets/templetHtml/js/content.js?v=1.0.0"></script>
    <script src="${pageContext.request.contextPath}/assets/templetHtml/js/plugins/sweetalert/sweetalert.min.js"></script>


    <!-- DROPZONE -->
    <script src="${pageContext.request.contextPath}/assets/templetHtml/js/plugins/dropzone/dropzone.js"></script>


    <script>
        $(document).ready(function () {

            Dropzone.options.myAwesomeDropzone = {

                  addRemoveLinks: true,
                  autoProcessQueue:false,
                  parallelUploads:8,
                  maxFiles: 1,//最大可上传的文件个数
                  maxFilesize: 20,
                  acceptedFiles: ".xlsx", //上传的类型
                // Dropzone settings
                init: function () {
                    var myDropzone = this;

                    this.element.querySelector("button[type=submit]").addEventListener("click", function (e) {
                        e.preventDefault();
                        e.stopPropagation();
                        myDropzone.processQueue();
                    });
                    this.on("sendingmultiple", function () {});
                    this.on("successmultiple", function (files, response) {});
                    this.on("errormultiple", function (files, response) {});
                },
                success: function(file,data) {
                	if(data.message=="E"){
                		file.previewElement.classList.add("dz-error");
                        swal({
                            title: "拒绝上传",
                            text: "今日合计已经生成,去找赵旭吧",
                            type: "error",  
                        }, function () {
                       		//window.location.href = "/exceldev/page/redir?index_v1";
                        });
                	}else if(data.message!="Y"){
                		file.previewElement.classList.add("dz-error");
                		swal({
                            title: "上传异常",
                            text: "复制错误信息找赵旭\n错误信息："+data.message,
                            type: "error",  
                        });
                	}else{
                		 file.previewElement.classList.add("dz-success");
                         swal({
                             title: "上传成功",
                             text: "随便了，没啥说的",
                             type: "success",  
                         }, function () {
                        	//window.location.href = "/exceldev/page/redir?index_v1";
                         });
                	}
                   
                },
                error: function(file, message) {
                	console.log(message);
                    var node, _i, _len, _ref, _results;
                    file.previewElement.classList.add("dz-error");
                    if (typeof message !== "String" && message.error) {
                        message = message.error;
                    }
                    _ref = file.previewElement.querySelectorAll("[data-dz-errormessage]");
                    _results = [];
                    for (_i = 0, _len = _ref.length; _i < _len; _i++) {
                        node = _ref[_i];
                        _results.push(node.textContent = message);
                    }
                    return _results;
                },
            
	            dictInvalidFileType: "文件类型仅支持xlsx,用WPS另存为xlsx格式",
                dictMaxFilesExceeded: "只能上传一个文件",
	            dictDefaultMessage:"拖动文件到该区域或点击上传文件",
	            dictCancelUpload:"取消",
	            dictCancelUploadConfirmation:"取消上传操作",
	            dictRemoveFile:"删除",
	            dictFileTooBig:"可添加的最大文件大小为{{maxFilesize}}Mb，当前文件大小为{{filesize}}Mb"
            }
	        });
    </script>

    
    

</body>

</html>
