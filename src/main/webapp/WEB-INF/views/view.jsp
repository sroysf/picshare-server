<%@ taglib uri="http://java.sun.com/jsp/jstl/core" 
    prefix="c" %> 

<jsp:useBean id="image"
             scope="request"
             type="com.codechronicle.entity.Image" />
    
<HTML>
<HEAD>
   <TITLE></TITLE>
</HEAD>
<BODY>

<img src="${ image.webUrl }"/> </br>

<strong>Tags</strong><br/>
<ul>
<c:forEach var="tag" items="${ image.tags }">
	<li><c:out value="${ tag }"/></li>
</c:forEach>
</ul>

<a href="${ image.masterUrl }">Full resolution image</a>

</BODY>

</HTML>