<%--
  Created by IntelliJ IDEA.
  User: author
  Date: 2017/9/25
  Time: 16:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>无限级菜单展示</title>
    <style type="text/css">
        a {
            text-decoration: none;
        }

        ul, li {
            list-style: none;
            margin: 0;
            padding: 0;
        }

        /*定义菜单*/

        .Menue li {
            background: #111;
            color: #fff;
            height: 30px;
            line-height: 30px;
            position: relative;
            float: left;
            margin-right: 5px;
            width: 100px;
            text-align: center;
            font-family: Arial, Helvetica, sans-serif;
        }

        .Menue li a {
            color: #fff;
            font-size: 14px;
            display: block;
        }

        /*下拉菜单样式*/

        ul.sub_menu {
            position: absolute;
            width: 100px;
            display: none;
            z-index: 999;
        }

        .Menue li ul.sub_menu li {
            background: none;
            color: #555;
            font-size: 12px;
            border-bottom: 1px #333 solid;
            position: relative;
            width: 100px;
            height: 30px;
        }

        .Menue li ul.sub_menu li.last {
            border-bottom: none;
        }

        /*js会对最后一个li添加该class，去掉border-bottom效果*/

        .Menue li ul.sub_menu li a {
            background: #222;
            color: #888;
            display: block;
            height: 30px;
        }

        .Menue li ul.sub_menu li a:hover, .Menue li ul.sub_menu li a.now {
            background: #f90;
            color: #fff;
        }

        .Menue li.now, .Menue li.current {
            background: #f60;
            color: #fff;
        }

        /*如果有下拉菜单添加的class*/

        .hasmenu {
            background: url(arrow.png) no-repeat right;
            padding-right: 15px;
        }

        /*主导航箭头向下*/

        .Menue li a.hasmenu {
            background: url(arrow.png) no-repeat right;
            padding-right: 15px;
            background-position: right -30px;
        }

        /*下拉菜单箭头向右*/

        .Menue li ul.sub_menu li a.hasmenu {
            background: #222 url(arrow.png) no-repeat right top;
        }

        .Menue li ul.sub_menu li a.hasmenu:hover {
            background: #f90 url(arrow.png) no-repeat right top;
            color: #fff;
        }

    </style>
    <%@include file="common.jsp" %>
</head>
<body>
<ul class="Menue">
    <%--<li class="Menue_li"><a href="#">菜单一</a>
       <ul class="sub_menu">
           <li><a href="#">一子菜单一</a></li>
           <li><a href="#">一子菜单二</a>
               <ul class="sub_menu">
                   <li><a href="#">一二子菜单一</a></li>
               </ul>
           </li>
       </ul>
   </li>
   <li class="Menue_li"><a href="#">菜单二</a>
       <ul class="sub_menu">
           <li><a href="#">二子菜单一</a></li>
           <li><a href="#">二子菜单二</a></li>
       </ul>
   </li>

  <li class="Menue_li"><a href="#">首页</a></li>

   <li class="Menue_li"><a href="#">菜单一</a>

       <ul class="sub_menu">

           <li><a href="#">过山车</a></li>

           <li><a href="#">火山爆发</a></li>

           <li><a href="#">小小鸟</a></li>

       </ul>

   </li>

   <li class="Menue_li"><a href="#">菜单二</a>

       <ul class="sub_menu">

           <li><a href="#">关于我们</a>

               <ul class="sub_menu">

                   <li><a href="#">山高地缘</a>

                       <ul class="sub_menu">

                           <li><a href="#">飞鸽传书</a></li>

                           <li><a href="#">生生世世</a></li>

                           <li><a href="#">飞黄腾达</a></li>

                       </ul>

                   </li>

                   <li><a href="#">数据库</a>

                       <ul class="sub_menu">

                           <li><a href="#">数据库表</a></li>

                           <li><a href="#">数据加密</a></li>

                           <li><a href="#">数据建模</a></li>

                       </ul>

                   </li>

                   <li><a href="#">C摄像头</a></li>

               </ul>

           </li>

           <li><a href="#">测试产品</a></li>

       </ul>

   </li>--%>

</ul>

</body>

<script>


    $.ajax({
        type: "GET",
        url: "<%=request.getContextPath()%>/menu/getMenus.do",
        dataType: "json",
        success: function (result) {
            if (result.status > 0) {
                //获取到菜单数据，进行展示
                //1,展示顶级菜单
                var list = result.body;
                var i;
                for (i = 0; i < list.length; i++) {
                    //console.log(typeof list[i].url == 'undefined');//如果url不存在，则为undefined，应该用typeof判断
                }
                //$(".Menue").html("");
                $(".Menue").html(showFirstLevelMenu(list));
                console.log(showFirstLevelMenu(list))
                //在菜单全部显示后，增加hover特性
                addHover();
            } else {
                alert(result.message);
            }
        },
        error: function () {
            alert("服务器故障，请刷新或稍后重试!");
        }
    })

    //    $(document).ready()
    function addHover() {

        //为导航设置默认高亮 与本菜单无关

        $("ul.Menue li.Menue_li:eq(0)").addClass("current")

        /*jquery menu 开始*/

        //为子菜单的最后一个li添加样式，适合为li添加下划线时去除最后一个的下划线

        $(".sub_menu").find("li:last-child").addClass("last")

        //遍历全部li，判断是否包含子菜单，如果包含则为其添加箭头指示状态

        $(".Menue li").each(function () {

            if ($(this).find("ul").length != 0) {
                $(this).find("a:first").addClass("hasmenu")
            }

        })


        //

        $(".Menue li").hover(function () {

            $(this).addClass("now");

            var menu = $(this);

            menu.find("ul.sub_menu:first").show();

        }, function () {

            $(this).removeClass("now");

            $(this).find("ul.sub_menu:first").hide();

        });


        var submenu = $(".sub_menu").find(".sub_menu")

        submenu.css({left: "100px", top: "0px"})

        $(".sub_menu li").hover(function () {

            $(this).find("a:first").addClass("now")

            $(this).find("ul:first").show();

        }, function () {

            $(this).find("a:first").removeClass("now")

            $(this).find("ul:first").hide()

        });

        /*jquery menu 结束*/

    }

    function showFirstLevelMenu(list) {
        //class = Menu_li
        //遍历list找到
        var menu = "";
        var subList = new Array();
        var i;
        for (i = 0; i < list.length; i++) {
            if (list[i].pid == 0) {
                subList.push(list[i]);
            }
        }
        subList.sort(function (a, b) {
            return a.seq - b.seq;//从小到大排序
        });
        alert(subList.length)
        for (i = 0; i < subList.length; i++) {
            menu += '<li class="Menue_li">' + showSubMenu(list, subList[i]) + '</li>';
        }
        return menu;
    }

    function showSubMenu(list, menuInfo) {
        //class = sub_menu
        var menu = '<a href="#">' + menuInfo.name + '</a>';
        if (menuInfo.isLeaf == 0) {//有子菜单
            var subList = new Array();
            var i;
            for (i = 0; i < list.length; i++) {
                if (list[i].pid == menuInfo.id) {
                    subList.push(list[i]);
                }
            }
            if (subList.length != 0) {
                subList.sort(function (a, b) {
                    return a.seq - b.seq;//从小到大排序
                });
                menu += '<ul class="sub_menu">';
                for (i = 0; i < subList.length; i++) {
                    menu += '<li>' + showSubMenu(list, subList[i]) + '</li>';
                }
                menu += '</ul>';
            }
        }
        return menu;
    }
</script>

</html>
