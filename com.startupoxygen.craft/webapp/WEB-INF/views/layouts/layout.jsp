<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="craft"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<craft:page>
	<craft:pagebody>
		<tiles:insertAttribute name="header" defaultValue="header.jsp"
			defaultValueType="template" />
		<tiles:insertAttribute name="navbar" defaultValue="navbar.jsp"
			defaultValueType="template" />
		<div style="margin-top: 50px;">
			<tiles:insertAttribute name="content" defaultValue="content.jsp"
				defaultValueType="template" />
		</div>
		<tiles:insertAttribute name="footer" defaultValue="footer.jsp"
			defaultValueType="template" />
	</craft:pagebody>
</craft:page>