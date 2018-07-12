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
    
<style type="text/css">
.col-xs-6{
	width: 25%
	}
</style>
</head>

<body class="gray-bg">
<input type="hidden" value="${ sessionScope.user.username }" id="userFlag">
    <div class="wrapper wrapper-content">
        <div class="row">
        	<c:if test="${null!=shop}">
              <div class="col-sm-10">
                <div class="row">
                    <div class="col-sm-4" style="width: 100%">
                        <div class="row row-sm text-center">
                            <div class="col-xs-6" >
                                <div class="panel padder-v item">
                                    <div class="h1 text-info font-thin h1">$${shop.totalActual}</div>
                                    <span class="text-muted text-xs">销售总额</span>
                                    <div class="top text-right w-full">
                                        <i class="fa fa-caret-down text-warning m-r-sm"></i>
                                    </div>
                                </div>
                            </div>
                            <div class="col-xs-6">
                                <div class="panel padder-v item bg-info">
                                    <div class="h1 text-fff font-thin h1">${shop.countNum}</div>
                                    <span class="text-muted text-xs">今日销量</span>
                                    <div class="top text-right w-full">
                                        <i class="fa fa-caret-down text-warning m-r-sm"></i>
                                    </div>
                                </div>
                            </div>
                            <div class="col-xs-6">
                                <div class="panel padder-v item bg-primary">
                                    <div class="h1 text-fff font-thin h1">$${shop.totalExpect}</div>
                                    <span class="text-muted text-xs">预计金额</span>
                                    <div class="top text-right w-full">
                                        <i class="fa fa-caret-down text-warning m-r-sm"></i>
                                    </div>
                                </div>
                            </div>
                            <!--  <div class="col-xs-6">
                                <div class="panel padder-v item" style="background: palevioletred">
                                    <div class="font-thin h1">$129</div>
                                    <span class="text-muted text-xs">本月总额</span>
                                    <div class="bottom text-left">
                                        <i class="fa fa-caret-up text-warning m-l-sm"></i>
                                    </div>
                                </div>
                            </div>-->
                        </div>
                    </div>
                </div>
               </div>
               </c:if>  
            <div class="col-sm-2">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5>Excel导入列表</h5>
                    </div>
                    <div class="ibox-content">
                        <ul class="todo-list m-t small-list ui-sortable">
                        <c:forEach items="${shopMap}" var="item">
	                            <c:if test="${item.value.upFlag}">
		                             <li>
		                                <a href="javascript:void(0)" onclick="downFile('${item.value.upName}','${item.value.upPath}')" class="check-link"><i class="fa fa-check-square"></i> 
		                                <span  class="m-l-xs todo-completed">${item.key}</span></a>
		                                <input type="hidden" name="shopId" value="${item.value.upName}">
		                                <input type="hidden" name="shopFile" value="${item.value.upPath}">
		                            </li>
								   </c:if>  
								   <c:if test="${!item.value.upFlag}">
									<li>
		                                <a href="javascript:void(0)" style="cursor:default" class="check-link"><i class="fa fa-square-o"></i> </a>
		                                <span class="m-l-xs">${item.key}</span>
		                            </li> 
								   </c:if>  
                          </c:forEach>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- 全局js -->
    <%@ include  file="commonJs.jspf"%>
    <!-- Flot -->
    <script src="${pageContext.request.contextPath}/assets/templetHtml/js/plugins/flot/jquery.flot.js"></script>
    <script src="${pageContext.request.contextPath}/assets/templetHtml/js/plugins/flot/jquery.flot.tooltip.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/templetHtml/js/plugins/flot/jquery.flot.resize.js"></script>
    <script src="${pageContext.request.contextPath}/assets/templetHtml/js/plugins/flot/jquery.flot.pie.js"></script>
    <!-- 自定义js -->
    <script src="${pageContext.request.contextPath}/assets/templetHtml/js/content.js"></script>
    <script src="${pageContext.request.contextPath}/assets/templetHtml/js/plugins/sweetalert/sweetalert.min.js"></script>
    
    <script type="text/javascript">
    function downFile(shopId,shopFile){
    	console.log();
    	var index = shopFile .lastIndexOf("\/");  
    	shopFile  = shopFile .substring(index + 1, shopFile .length);
    	if($("#userFlag").val()!="admin"){
	    	if($("#userFlag").val()!=shopId){
				swal({ 
				  type:"error",
					  title: "权限不足", 
					  text: "自己店铺只能查看自己的,当然这也是赵旭控制的", 
					  timer: 5000, 
					  showConfirmButton: false 
					});
				return;
			}
    	}
		layer.msg("后台已开始下载");
    	window.location.href = "/exceldev/excel/download?fileName="+shopFile;
       }
    </script>
</body>

</html>
