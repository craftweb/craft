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
	<header class="jumbotron">
		<!-- 	<div class="control-group">
		<div class="controls pull-right error">
			<form class="form-inline" action="#login">
				<input type="email" class="input-small" placeholder="Email">
				<input type="password" class="input-small" placeholder="Password">
				<label class="checkbox"> <input type="checkbox">
					Remember me
				</label>
				<button type="submit" class="btn btn-primary">Sign in</button>
			</form>
		</div>
	</div> -->
	</header>
</craft:craft>