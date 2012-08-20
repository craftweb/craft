<%@page import="com.mongodb.BasicDBList"%>
<%@page import="com.mongodb.BasicDBObject"%>
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
	BasicDBList dbList = new BasicDBList();
	String entityName = entity.getName();
	DBCursor dbCursor = (DBCursor) request.getAttribute("summary");
	Set<String> headers = (Set<String>) request.getAttribute("headers");
	Iterator<DBObject> dbObjectIterator = dbCursor.iterator();
%>
<div class="container-fluid">
	<div class="well">
		<h1><%=entity.getDisplayName()%></h1>
		<div class="control-group">
			<div class="controls">
				<a class="btn btn-primary"
					href="<%=request.getContextPath()%>/<%=project.getContextPath()%>/<%=entity.getNormalizedName()%>/new">Create
					New <%=entity.getDisplayName()%></a>
			</div>
		</div>
		<table class="table table-striped table-bordered">
			<tr>
				<%
					for (String header : headers) {
				%>
				<th><%=header%></th>
				<%
					}
				%>
			</tr>
			<%
				while (dbObjectIterator.hasNext()) {
					DBObject dbObject = dbObjectIterator.next();
					if (dbObject != null) {
						Map<String, Object> map = dbObject.toMap();
						Iterator<Entry<String, Object>> iter = map.entrySet()
								.iterator();
			%>
			<tr>
				<%
					while (iter.hasNext()) {
								Map.Entry<String, Object> entry = iter.next();
								System.out.println(entry.getKey());
								Object value = entry.getValue();
								if (value != null && !(value instanceof BasicDBList)
										&& !entry.getKey().equals("_class")
										&& !entry.getKey().equals("_id")) {
				%>
				<td><%=value.toString()%></td>
				<%
					}
							}
						}
					}
				%>
			</tr>
		</table>
	</div>
</div>