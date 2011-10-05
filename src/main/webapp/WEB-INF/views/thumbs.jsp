<%@ taglib uri="http://java.sun.com/jsp/jstl/core" 
    prefix="c" %> 

<!-- http://www.javascriptkit.com/jsref/text.shtml -->

<jsp:useBean id="imageList"
             scope="request"
             type="java.util.Collection" />
                 
<HTML>
<HEAD>
	<c:set var="foo" scope="session" value="5"/>  
   <TITLE>Simple UI <c:out value="${foo}"/></TITLE>
</HEAD>
<BODY>

<h3><c:out value="${ tag }"/></h3>

<a href="/picshare/">Home</a>
<br/>
<br/>

<c:if test='${prevDisabled == false}'>
	<a href="/picshare/image/thumbs?tag=<c:out value='${tag}'/>&start=<c:out value='${prevStart}'/>&num=25">Previous Page</a>     
</c:if>

<c:if test='${(prevDisabled == false) && (nextDisabled == false)}'>
&nbsp;&nbsp;|&nbsp;&nbsp;
</c:if>

<c:if test='${nextDisabled == false}'>
<a href="/picshare/image/thumbs?tag=<c:out value='${tag}'/>&start=<c:out value='${nextStart}'/>&num=25">Next Page</a>
</c:if>

<br/>
<br/>
       
<c:forEach var="image" items="${ imageList }">
	<a href="/picshare/image/web?id=<c:out value='${ image.id }'/>"><img src="<c:out value='${ image.thumbUrl }'/>"/></a>
</c:forEach>

<br/>
<br/>


<c:if test='${prevDisabled == false}'>
	<a href="/picshare/image/thumbs?tag=<c:out value='${tag}'/>&start=<c:out value='${prevStart}'/>&num=25">Previous Page</a> |     
</c:if>

<c:if test='${nextDisabled == false}'>
<a href="/picshare/image/thumbs?tag=<c:out value='${tag}'/>&start=<c:out value='${nextStart}'/>&num=25">Next Page</a>
</c:if>             

</BODY>

</HTML>