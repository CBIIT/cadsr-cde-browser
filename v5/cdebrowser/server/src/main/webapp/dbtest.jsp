<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<sql:clientQuery var="rs" dataSource="jdbc/CDEBrowserDS">
    select id, foo, bar from testdata
</sql:clientQuery>

<html>
<head>
    <title>DB Test</title>
</head>
<body>

<h2>Results</h2>

<c:forEach var="row" items="${rs.rows}">
    Foo ${row.foo}<br/>
    Bar ${row.bar}<br/>
</c:forEach>

</body>
</html>
