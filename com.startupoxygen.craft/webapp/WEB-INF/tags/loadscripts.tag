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
<craft:craft>
	<script type="text/javascript"
		src="${craft.getCraftContextPath(request)}/assets/js/bootstrap.js"></script>
	<link rel="stylesheet" type="text/css"
		href="${craft.getCraftContextPath(request)}/assets/css/bootstrap-blue.css" />
	<link rel="stylesheet" type="text/css"
		href="${craft.getCraftContextPath(request)}/assets/css/bootstrap-responsive.css" />
</craft:craft>
