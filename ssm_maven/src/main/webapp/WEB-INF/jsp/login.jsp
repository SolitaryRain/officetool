<!DOCTYPE html>
<html lang="en" class="no-js">
<head>
<meta charset="utf-8">
<title>用户登录</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
    <script src="https://cdn.bootcss.com/jquery/2.2.4/jquery.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <link href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/login.css">
</head>
<body>
    <div class="container">
        <div class="form row">
            <div class="form-horizontal col-md-offset-3" id="login_form">
                <h3 class="form-title"></h3>
               <span style="color: coral; padding-left: 15%">${checkMsg}</span>
                 <div class="col-md-9">
                  <form action="/exceldev/user/login" method="post">
                    <div class="form-group">
                        <i class="fa fa-user fa-lg"></i>
                        <input class="form-control required" type="text" placeholder="Username" id="username" name="username" autofocus="autofocus" maxlength="20"/>
                    </div>
                    <div class="form-group">
                            <i class="fa fa-lock fa-lg"></i>
                            <input class="form-control required" type="password" placeholder="Password" id="password" name="password" maxlength="8"/>
                    </div>
                    <!--  <div class="form-group">
                        <label class="checkbox">
                            <input type="checkbox" name="remember" value="1"/>记住我
                        </label>
                    </div>-->
                    <div class="form-group col-md-offset-9">
                        <button type="submit" class="btn btn-success pull-right" name="submit">登づ入</button>
                    </div>
                    </form>
                   <!--  <div class="form-group col-md-offset-9">
                        <button type="submit" class="btn btn-info pull-right" name="submit" onclick="register()">投名状</button>
                    </div> -->
                </div>
            </div>
        </div>
    </div>
    
    <script src="${pageContext.request.contextPath}/assets/templetHtml/layer/layer.js"></script>
    
    <script type="text/javascript">
    function register(){
    	//示范一个公告层
        layer.open({
          type: 1
          ,title: false //不显示标题栏
          ,closeBtn: false
          ,area: '300px;'
          ,shade: 0.8
          ,id: 'LAY_layuipro' //设定一个id，防止重复弹出
          ,resize: false
          ,btn: ['入']
          ,btnAlign: 'c'
          ,moveType: 1 //拖拽模式，0或者1
          ,content: '<div style="padding: 50px; line-height: 22px; background-color: #393D49; color: #fff; font-weight: 300;">内容<br>内容</div>'
          ,success: function(layero){
            var btn = layero.find('.layui-layer-btn');
            btn.find('.layui-layer-btn0').attr({
              href: '/exceldev'
            });
          }
        });
    }
    </script>
</body>
</html>