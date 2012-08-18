<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="craft"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ tag import="com.startupoxygen.craft.*"%>
<%@ tag import="com.startupoxygen.craft.metadata.*"%>
<%@ tag import="java.util.*"%>
<craft:initcheck />
<%
	Craft craft = new Craft();
	PageBean pageBean = craft.getPageBean(request);
	Project project = pageBean.getProject();
	Entity entity = pageBean.getEntity();
%>
<div class="navbar navbar-fixed-top">
	<div class="navbar-inner">
		<div class="container-fluid">
			<a class="brand" href="http://www.optaamaze.com"
				title="<%=project.getDescription()%>"> <%=project.getName()%></a>
			<div class="nav">
				<ul class="nav" id="mainTabs">
					<%
						Collection<Entity> entities = project.getEntities();
						if (entities != null && !entities.isEmpty()) {
							int i = 0;
							for (Entity entityTab : entities) {
								if (entityTab != null && entityTab.isMain()) {
									boolean currentTab = (entity != null && entity
											.getName()
											.equalsIgnoreCase(entityTab.getName()));
					%>
					<li <%=currentTab ? "class=\"active\"" : ""%>><a
						href="<%=craft.getEntityContextPath(request,
								entityTab)%>"><%=entityTab.getDisplayName()%></a>
					</li>
					<%
						}
							}
						}
					%>
				</ul>
			</div>
		</div>
	</div>
</div>