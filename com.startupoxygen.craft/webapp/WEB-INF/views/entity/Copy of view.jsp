<%@page import="com.mongodb.BasicDBList"%>
<%@page import="com.mongodb.BasicDBObject"%>
<%@page import="org.apache.jasper.tagplugins.jstl.core.ForEach"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="com.mongodb.DBCursor"%>
<%@ page import="com.mongodb.DBObject"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.Set"%>
<%@ page import="java.util.Map.Entry"%>
<%@ page import="java.util.Collection"%>
<%@ page import="com.startupoxygen.craft.PageBean"%>
<%@ page import="com.startupoxygen.craft.metadata.Project"%>
<%@ page import="com.startupoxygen.craft.metadata.Entity"%>
<%@ page import="com.startupoxygen.craft.metadata.Field"%>
<%
	PageBean pageBean = (PageBean) request.getAttribute("pageBean");
	Project project = pageBean.getProject();
	Entity entity = pageBean.getEntity();
	Collection<Field> fields = entity.getFields();
	String entityName = entity.getName();
%>
<div class="container-fluid">
	<div class="well">
		<h1><%=entity.getDisplayName()%></h1>
		<div class="control-group">
			<div class="controls">
				<a class="btn btn-primary"
					href="<%=request.getContextPath()%>/<%=entityName%>/new">Create
					Another <%=entity.getDisplayName()%></a> <a class="btn btn-primary"
					href="<%=request.getContextPath()%>/<%=entityName%>/edit">Edit
					<%=entity.getDisplayName()%></a> <a class="btn btn-primary"
					href="<%=request.getContextPath()%>/<%=entityName%>/delete">Delete
					<%=entity.getDisplayName()%></a>
			</div>
		</div>
		<table class="table table-striped table-bordered">
			<%
				Map<String, String> dataMap = (Map<String, String>) request
						.getAttribute("datamap");
				Iterator<String> iter = dataMap.keySet().iterator();
				while (iter.hasNext()) {
					String key = iter.next();
					String val = dataMap.get(key);
			%>
			<tr>
				<td><%=key%></td>
				<td><%=val%></td>
			</tr>
			<%
				}
			%>
		</table>
	</div>
</div>