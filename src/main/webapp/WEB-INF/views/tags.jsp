<%@ taglib uri="http://java.sun.com/jsp/jstl/core" 
    prefix="c" %> 

<jsp:useBean id="tagList"
             scope="request"
             type="java.util.Collection" />
             
<!-- http://www.javascriptkit.com/jsref/text.shtml -->
    
<HTML>
<HEAD>
   <TITLE>Tags</TITLE>
</HEAD>

<H2>Tags</H2>

<BODY>

<c:forEach var="tag" items="${ tagList }">
	<a href="/picshare/image/thumbs?tag=<c:out value='${tag.value}&start=0&num=25'/>"><c:out value='${tag.value}'/></a><br/>
</c:forEach>

</BODY>

</HTML>