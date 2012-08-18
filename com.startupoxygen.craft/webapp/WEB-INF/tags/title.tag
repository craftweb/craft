<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="craft"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ tag import="com.startupoxygen.craft.Craft"%>
<%@ tag import="com.startupoxygen.craft.PageBean"%>
<%@ tag import="com.startupoxygen.craft.metadata.Project"%>
<%@ tag import="com.startupoxygen.craft.metadata.Entity"%>
<%@ tag import="com.startupoxygen.craft.metadata.Field"%>
<%@ tag import="org.apache.commons.lang3.StringUtils"%>
<craft:initcheck />
<%
	Craft craft = new Craft();
	PageBean pageBean = craft.getPageBean(request);
	Project project = pageBean.getProject();
	Entity entity = pageBean.getEntity();
%>
<title><%=project.getName()%><%=entity != null ? ": " + entity.getDisplayName() : ""%></title>