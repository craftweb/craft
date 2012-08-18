<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="craft"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ tag import="com.startupoxygen.craft.*"%>
<%@ tag import="com.startupoxygen.craft.metadata.*"%>
<%@ tag import="java.util.*"%>
<%@ tag import="com.startupoxygen.craft.metadata.Field"%>
<%@ tag import="org.apache.commons.lang3.StringUtils"%>
<%@ variable name-given="field"
	variable-class="com.startupoxygen.craft.metadata.Field"%>
<craft:initcheck />
<%
	Craft craft = new Craft();
	PageBean pageBean = craft.getPageBean(request);
	Project project = pageBean.getProject();
	Entity entity = pageBean.getEntity();
	Collection<Field> fields = entity.getFields();
	if (fields != null && fields.size() > 0) {
		for (Field field : fields) {
%>
<c:set var="field" value="<%=field%>" />
<jsp:doBody />
<%
	}
	}
%>