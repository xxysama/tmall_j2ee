<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="../include/admin/adminHeader.jsp"%>
<%@include file="../include/admin/adminNavigator.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>编辑产品属性</title>
<script>
	//
	$(function(){
		$("input.pvValue").keyup(function(){
			var value=$(this).val();//获取输入框的值
			var page="admin_product_updatePropertyValue";
			var pvid=$(this).attr("pvid");
			var parentSpan=$(this).parent("span");
			parentSpan.css("border","1px solid yellow");//修改边框颜色表示正在修改
			//JQuery的ajax函数 $.post，把id和值，提交到admin_product_updatePropertyValue
			$.post(
					page,{"value":value,"pvid":pvid},function(result){
						if("success"==result)
							parentSpan.css("border","1px solid green");
						else
							parentSpan.css("border","1px solid red");
					}
					);
		});
	});
</script>
</head>
<body>
	<div class="workingArea">
		<ol class="breadcrumb">
			<li><a href="admin_category_list">所有分类</a></li>
			<li><a href="admin_product_list?cid=${p.category.id }">${p.name }</a></li>
			<li class="active">${p.name }</li>
			<li class="active">编辑产品属性</li>
		</ol>
		
		<div class="editDiv">
			<c:forEach items="${pvs }" var="pv">
				<div class="eachPV">
					<span class="pvName">${pv.property.name }</span>
					<span class="pvValue"><input class="pvValue" 
						type="text" pvid="${pv.id }" value="${pv.value }"></span>
				</div>
			</c:forEach>
			<div style="clear:both"></div>
		</div>
	</div>
</body>
</html>