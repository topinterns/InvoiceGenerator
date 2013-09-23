<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ page
 	import="com.google.appengine.api.blobstore.BlobstoreServiceFactory"
	import="com.google.appengine.api.blobstore.BlobstoreService"
%>
<%
	BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

 %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="../bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="../font-awesome/css/font-awesome-ie7.min.css">
<link rel="stylesheet" type="text/css" href="../font-awesome/css/font-awesome.min.css">
<!-- <link rel="stylesheet" type="text/css" href="../bootstrap/css/style.css" /> -->
<script type="text/javascript" src="/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="/js/jquery.dataTables.js"></script>
<script type="text/javascript" src="/js/jquery.dataTables.min.js"></script>
<!-- <script type="text/javascript" src="/js/jquery-latest.js"></script> 
<script type="text/javascript" src="/js/jquery.tablesorter.js"></script>  -->
<script type="text/javascript" src="../bootstrap/js/bootstrap.min.js"></script>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">


<script type="text/javascript" src="/js/loadDetails.js"></script>
		<script type="text/javascript" src="/js/ajaxfileupload.js"></script>

<style type="text/css">
.loading {
	position: absolute;
	top: 20%;
	left: 30%;
	width: 500px;
	height: 315px;
	background: #ffffff;
	z-index: 51;
	padding: 10px 25px;
	-webkit-border-radius: 5px;
	-moz-border-radius: 5px;
	border-radius: 5px;
	-moz-box-shadow: 0px 0px 5px #444444;
	-webkit-box-shadow: 0px 0px 5px #444444;
	box-shadow: 0px 0px 5px #444444;
	display: none;
}
.backdrop {
	position: absolute;
	top: 0px;
	left: 0px;
	width: 100%;
	height: 100%;
	background: #000;
	opacity: .0;
	filter: alpha(opacity = 0);
	z-index: 50;
	display: none;
}
</style>
<script type="text/javascript">
/* $(document).ready(function() 
	    { 
	        $("#myTable").tablesorter(); 
	    } 
	);  */
	    

var url='<%=blobstoreService.createUploadUrl("/admin/uploadcsv")%>';
		var uploadedData;
	     var test="haiadsfasdfdsa";
	     var taskQueueStatus= false;
		function uploadcsv(uploadedcsv)
		{
			$('.backdrop').animate({'opacity':'.50'}, 300, 'linear');
			$('.backdrop').css('display', 'block');
	    	$('#loading-indicator').show();
			 $('#loading-indicator').show();
		 var pictureElement = uploadedcsv;

		 var file_Id=uploadedcsv.id;

		  $.ajaxFileUpload({
		   url:url,
		   secureuri:false,
		   fileElementId:pictureElement.id,
		   success:function(data)
		   {

			   $('#loading-indicator').hide();
			   $('.backdrop').css('display', 'none');
			$('#checktaskqueue').show();

		   }
		  });
}

function checkTaskQueueStatus(){
		  console.info(taskQueueStatus+">>>heloooooo>>>>>>>>>");
		  if(!taskQueueStatus){
		   $.ajax({
			   type: 'POST',
			   url: '/admin/getTaskQueueStatus',
			   contentType: "application/json",
			   beforeSend: function ( jqXHR )
			   {
			    jqXHR.setRequestHeader("Connection", "close");
			   },
			   async: false,
			   data: "",
			    success: function response(resultObject)
			   {
			    	console.info(resultObject);
			    	taskQueueStatus=resultObject;
			    	if(taskQueueStatus){
			    		getUploadedData();
			    	}
			    	else{
			    		alert("Processing... please wait ")
			    		//setTimeout(checkTaskQueueStatus, 5000);
			    	}

			   },
			   dataType: ''
			  });
		  }

}

function getUploadedData(){
		   $.ajax({
			   type: 'POST',
			   url: '/admin/getUploadedData',
			   contentType: "application/json",
			   beforeSend: function ( jqXHR )
			   {
			    jqXHR.setRequestHeader("Connection", "close");
			   },
			   async: false,
			   data: "",
			    success: function response(data)
			   {
			    	$('.backdrop').animate({'opacity':'.50'}, 300, 'linear');
					$('.backdrop').css('display', 'block');
			    	$('#loading-indicator').show();
			    	console.info(data)
			 		      uploadedData=data;
			 		      var tableData="";
			 		     for(ind in uploadedData){
			 		    	 var currentRecord = uploadedData[ind];
			 		      tableData+='<tr><td>'+uploadedData[ind].accountNumber+'</td><td>'+uploadedData[ind].phoneNumber+'</td><td>'+uploadedData[ind].invoiceDate+'</td><td>'+uploadedData[ind].dateDue+'</td><td>'+uploadedData[ind].address+'</td>';
			 		      var summarycharges="";
			 		      var summarycharges2="";
			 		     var userIdSummary="";
			 		      for(index in uploadedData[ind].TotalDue){
			 		       tableData+='<td>'
			 		       tableData+=uploadedData[ind].TotalDue[index]
			 		       tableData+='</td>'
			 		      }
			 		      for(index in uploadedData[ind].summaryOfCharges){
			 		       var tempscr="";
			 		        for(index1 in  uploadedData[ind].summaryOfCharges[index]){
			 		         tempscr += uploadedData[ind].summaryOfCharges[index][index1]+"  " ;
			 		                           }
			 		       summarycharges +=tempscr;
			 		     }
			 		      for(index in uploadedData[ind].summaryOfCharges2){
			 		       var tempscr="";
			 		        for(index1 in  uploadedData[ind].summaryOfCharges2[index]){
			 		         tempscr += uploadedData[ind].summaryOfCharges2[index][index1]+"  " ;
			 		                           }
			 		       summarycharges2 +=tempscr;
			 		     }
			 		     for(index in uploadedData[ind].userIdSummary){
						       var tempscr="";
						        for(index1 in  uploadedData[ind].userIdSummary[index]){
						         tempscr += uploadedData[ind].userIdSummary[index][index1]+"  " ;
						                           }
						        userIdSummary +=tempscr;
						     }
			 		      tableData+='<td>'+summarycharges+'</td>';
			 		       if(uploadedData[ind].totalCurrentCharges)
			 		       tableData +='<td>'+uploadedData[ind].totalCurrentCharges+'</td>';
			 		      else
			 		        tableData+='<td></td>';
			 		       if(uploadedData[ind].answerConnectAddress)
			 		        tableData +='<td>'+uploadedData[ind].answerConnectAddress+'</td>';
			 		       else
			 		         tableData+='<td></td>';

			 		        tableData+='<td>'+uploadedData[ind].accountName+'</td><td>'+summarycharges2+'</td><td>'+userIdSummary+'</td>';
			 		     tableData+='<td><a href="#myModal" onclick=generatepdf('+ind+') data-toggle="modal" >Download Pdf & Send an Email</a></td></tr>'; 
			 		      /* tableData+='<td><a href="#" onclick=generatepdf('+ind+') >Download Pdf & Send an Email</a></td></tr>'; */ 
			 		      $('.send').attr('id',ind); 
			 		     }
			 		   
			 		     $('#invoiceBody').html(tableData);
			 		    $('#mytable').dataTable({
			                "bDestroy"   : false,
			                "bLengthChange"  : false,
			                "bFilter"  : false,
			                "bPaginate"  : false,
			                "bJQueryUI"  : false,
			               } );
			 		    $('#loading-indicator').hide();
			 		   $('.backdrop').css('display', 'none');
			   },
			   dataType: ''
			  });
}

 
	
	/* 
	 HARIKA
	var i=0;
	var record;
function generatepdf(currentRecord)
{      i++;
		
		if(i==1)
	 {
     record = currentRecord;
     }
		
	 if(i==2){
		
		  var email=$("#emailid").val(); 
		  var subject=$("#subjectemail").val();
		  var body="";
		  
		  
		  body=$("#textareaemail").val();
		
		   alert(body);
		  var arrayBody=new Array();
		  arrayBody=body.split(" ");
	      var len = arrayBody.length;
		  
		  
		  var stringBody="";
		  for(var j=0;j<len;j++)
			  var stringBody=stringBody+arrayBody[j]+"_";
	//alert(" Thisis currentRecord "+record+"  email id ::= "+email+"  subject  "+subject+"  body "+stringBody);
	// if(!(email.contains("@"))
		//	alert("please enter valid mail id"); 
	//alert(email+currentRecord);
	var re = /\S+@\S+\.\S+/;
	if(re.test(email))
	{
	 var frm,iput;
	 iput = document.getElementById('allStaffDetailsJson').value = record+";"+email+";"+subject+";"+stringBody;
	 frm = document.getElementById("frmReportIframe");
	 
	 frm.action="/admin/generatesinglepdf";
	 frm.method="post";
	 frm.target="pdfdownloadIframe";
	 frm.submit();
	 i=0;

	 $("#emailid").val("");
	 $("#subjectemail").val("");
	 $("#textareaemail").val(" This is Invoice Generator, Thank you........");
	
	 
	}
	else{
		alert("Enter valid emailid");
		// var s="malli";
		//console.log("variable value:"+s); 
	}

	 }
}

 */	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// MALLI
	
/* function generatepdf(currentRecord)
		{

			 // var email = window.prompt("Enter email id"); 
			// window.alert(email);  
			  var email = $("#emailid").val(); 
			
			alert("Thisis currentRecord and email id :::::::::"+currentRecord+"   "+email);
			
			//if(!(email.contains("@"))
			//		alert("please enter valid mail id");
			//alert(email+currentRecord);
			var re = /\S+@\S+\.\S+/;
			if(re.test(email))
			{
			 var frm,iput;
			 iput = document.getElementById('allStaffDetailsJson').value = currentRecord+email;
			 frm = document.getElementById("frmReportIframe");
			 frm.action="/admin/generatesinglepdf";
			 frm.method="post";
			 frm.target="pdfdownloadIframe";
			 frm.submit();
			}
			else{
				alert("Enter valid emailid");
			//	var s="malli";
			//	console.log("variable value:"+s); 
			}

		}
	
 */		
		function generatebulk(){
			var frm,iput;
			/*  iput = document.getElementById('allStaffDetailsJson').value = currentRecord+email; */
			 frm = document.getElementById("frmReportIframe1");
			 frm.action="/admin/generatebulkpdf";
			 frm.method="post";
			 frm.target="pdfdownloadIframe1";
			 frm.submit();
		}
		
		function searchData()
		{
		    
		 var check="";
		 check = uploadedData[$('#textboxid').val()];
		 
		 if(check)
		  {
		  
		  $('#errorHeader').hide();
		  $('.tablesearch').show();
		  $('#search').show();
		  $('#searchbody').show();
		  
		 var tabledata="";

		  tabledata='<tr><td>'+check.accountNumber+'</td><td>'+check.phoneNumber+'</td><td>'+check.invoiceDate+'</td><td>'+check.dateDue+'</td><td>'+check.address+'</td>';

		  for(index in check.TotalDue){
		   tabledata+='<td>'
		         tabledata+=check.TotalDue[index]
		   tabledata+='</td>'
		    }
		   

		      var summarycharges="";
		      var summarycharges2="";
		     
		      for(index in check.summaryOfCharges)
		      {
		   var tempscr="";
		   for(index1 in  check.summaryOfCharges[index]){
		   tempscr += check.summaryOfCharges[index][index1]+"  " ;
		                                                    }
		   summarycharges +=tempscr;  
		    }
		       
		      tabledata+='<td>'+summarycharges+'</td>';
		     
		     
		   for(index in check.summaryOfCharges2)
		   {
		   var tempscr="";
		         for(index1 in  check.summaryOfCharges2[index]){
		      tempscr += check.summaryOfCharges2[index][index1]+"  " ;
		                                            }
		   summarycharges2 +=tempscr;
		       }




		       if(check.totalCurrentCharges)
		    tabledata +='<td>'+check.totalCurrentCharges+'</td>';
		    else
		      tabledata+='<td></td>';
		   if(check.answerConnectAddress)
		   tabledata +='<td>'+check.answerConnectAddress+'</td>';
		     else
		       tabledata+='<td></td>';
		       
		   tabledata+='<td>'+check.accountName+'</td><td>'+summarycharges2+'</td>';
		   tabledata+='<td><a href="#myModal" onclick=generatepdf("'+$('#textboxid').val()+'") data-toggle="modal" >Download Pdf & Send an Email</a></td></tr>';
		       
		       $('#searchbody').html(tabledata);
		       $('.send').attr('id',check); 
		      
		 
		} 
		 else{
		  
		  
		  $('#errorHeader').show();
		     $('#search').hide();
		     $('#searchbody').hide(); 
		 }
		}

</script>
<title>Invoice Creator</title>
<div class="header" style="background-color: lightblue;">
<div class="backdrop"></div>
<img src="/logo.gif"/>
<img src="/bootstrap/img/loading.gif" id="loading-indicator" class="loading"  />
</div>
</head>
<body bgcolor="#eee" onload="checkFileAPI();">
<!-- <div class="container" style="width: 1250px;"> -->
<div class="container" style="width: 1250px;">

<div class="navbar-search pull-right">
<i class="icon-search"></i>
   <input  type="text" id="textboxid" onkeyup="searchData()"  placeholder="Account Number"  style="background-color: antiquewhite" >
  <br><a href="/pages/CustomInvoice.jsp"><input class="btn btn-primary" style="align:center" type="submit" value="Custom Invoice"/></a>
  <br><br>
  
  <form id="frmReportIframe1" name="frmReportIframe1" target="pdfdownloadIframe1" action="/admin/generatebulkpdf" method="post">
    <input id="btnSubmit1" name="btnSubmit1" type="button" style="display:none;" />
   </form>
<iframe style="display: none" id="pdfdownloadIframe1" name="pdfdownloadIframe1"></iframe>
<input class="btn btn-large btn-success" onclick="generatebulk()" style="align:center" type="button" value="Download All Pdfs"/>
<!-- <a href="#" onclick="generatebulk()"> download all</a>  
 -->    <!-- <input type="search" id='textboxid' onsearch="searchData()">
<input type="button" value="search" id='searchbtnid' onclick="searchData()"/>
     -->

 </div>

<div><!-- class="header" style="background-color: lightblue;width: 456px;margin-left: 20px;height: 57px;margin-top: 27px;" -->
<br>
<span><b style="font-family: r;">Upload Text file</b><br><br><input class="btn btn-info" type="file" id="file" name="uploadFile" onchange="uploadcsv(this)" /></span></div>
&nbsp &nbsp<form action="/admin/downloadcsv" method="post">
<a href="#"><input class="btn btn-primary" id='checktaskqueue' style="align:center; display:none" onClick="checkTaskQueueStatus()" value="Click Here To Populate The Result"/></a>
<input class="btn btn-large btn-success" style="align:center" type="submit" value="Download"/>
</form>

<!-- CHOOSE DELIMITED FILE -->

 <div>
        <input type="file" onchange='readText(this)' />
        <input type="button" onClick='checkFileRead()' />
        <br/>
        <hr/>
        <h3>Contents of the Text file:</h3>
        <div id="main">
            ...
        </div>
    </div>




<h1 align="center">Invoice Generator</h1>
<center><h1 id="errorHeader" style="display:none">          NO   MATCHES  FOUND       </h1>

<table class="tablesearch" style="display:none">
<thead id="search">
<tr  bgcolor="#DCDCDC"> <td>Account Number</td><td>Phone Number</td><td>Invoice Date</td><td>Date Due</td><td>Address</td><td>TD</td>  <td>ALS</td> <td>PR</td> <td>DC</td> <td>U</td> <td>U</td><td>BF</td> <td>CC</td> <td>TDB</td> <td>TDA</td> <td>Summary Of Charges</td> <td>TCC</td><td>From Address </td><td>Account Name</td><td>Summary of charges(page2)</td><td>User Id Summary</td><td>Generate PDF's</td></tr>
</thead>
<tbody id="searchbody" >
</tbody>
</table>

<br><br>
</center>
<div style="overflow-y:auto;height:600px; width:1250px;">
<center>

<table id ="mytable" class="table table-hover" >
<thead id="invoiceHeaders">
<!-- <tr  bgcolor="#DCDCDC"><td>Account Number <i class="icon-collapse"></i></td><td>Phone Number <i class="icon-collapse"></i></td><td>Invoice Date <i class="icon-collapse"></i></td><td>Date Due <i class="icon-collapse"></i></td><td>TD <i class="icon-collapse"></i></td><td>ALS <i class="icon-collapse"></i></td><td>PR <i class="icon-collapse"></i></td><td>D/C <i class="icon-collapse"></i></td><td>U <i class="icon-collapse"></i></td><td>U <i class="icon-collapse"></i></td><td>BF <i class="icon-collapse"></i></td> <td>8<i class="icon-collapse"></i></td> <td>9<i class="icon-collapse"></i></td> <td>10<i class="icon-collapse"></i></td> <td>Summary of charges <i class="icon-collapse"></i></td><td>Total Charges <i class="icon-collapse"></i></td><td>From Address <i class="icon-collapse"></i></td><td>Account Name <i class="icon-collapse"></i></td><td>Summary of charges(page2) <i class="icon-collapse"></i></td> <td>Generate Pdfs <i class="icon-collapse"></i></td> -->
 <tr  bgcolor="#DCDCDC"><td>Account Number<i class="icon-collapse"></i></td><td>Phone Number<i class="icon-collapse"></i></td><td>Invoice Date<i class="icon-collapse"></i></td><td>Date Due<i class="icon-collapse"></i></td><td>Address<i class="icon-collapse"></i></td><td>TD<i class="icon-collapse"></i></td> <td>ALS<i class="icon-collapse"></i></td> <td>PR<i class="icon-collapse"></i></td> <td>DC<i class="icon-collapse"></i></td> <td>U<i class="icon-collapse"></i></td> <td>U<i class="icon-collapse"></i></td> <td>BF<i class="icon-collapse"></i></td> <td>CC<i class="icon-collapse"></i></td> <td>TDB<i class="icon-collapse"></i></td> <td>TDA<i class="icon-collapse"></i></td> <td>Summary of charges<i class="icon-collapse"></i></td><td>TCC<i class="icon-collapse"></i> </td><td>From Address<i class="icon-collapse"></i></td><td>Account Name<i class="icon-collapse"></i></td><td>Summary of charges(page2)<i class="icon-collapse"></i></td> <td>UserId Summary<i class="icon-collapse"></i></td> <td>Generate Pdfs </td> 
</thead>
<tbody id="invoiceBody" >

</tbody>
</table>
</center>
</div>
</div>
<form id="frmReportIframe" name="frmReportIframe" target="pdfdownloadIframe" action="/admin/generatepdf" method="post">
    <input id="allStaffDetailsJson" name="allStaffDetailsJson" type="hidden" value="" />
    <input id="btnSubmit" name="btnSubmit" type="button" style="display:none;" />
   </form>
<iframe style="display: none" id="pdfdownloadIframe" name="pdfdownloadIframe"></iframe>
<center>

<div></div>

<!-- <form id="frmReportIframe" name="frmReportIframe" target="reportIframe" action="/userClockIn/generateUserClockInDetailsReportForAll.req" method="post">
    <input id="allStaffDetailsJson" name="allStaffDetailsJson" type="hidden" value="" />
    <input id="btnSubmit" name="btnSubmit" type="button" style="display:none;" />
   </form>
   <iframe style="display: none" id="reportIframe" name="reportIframe"></iframe>
   <input type="button" value="Download"/>
   -->
</center>

<!-- This div For invoice email --> 
<div id="myModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="false">
  <div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
    <h3 id="myModalLabel">New Message</h3>
   </div>
   <form>
   <div class="modal-body">
  Enter a valid Email id:<br>
  <p><input type="email" required name="Email" id="emailid" placeholder="test@gmail.com" ></p><br>
  <p><input type="text" required name="subject" id=subjectemail placeholder="Subject"></p><br>
  <p class="textareas"><textarea type="textarea" required name="textarea" id="textareaemail" rows="5" cols="70">
  This is Invoice Generator, Thank you........
  </textarea></p>
  </div>
  <div class="modal-footer">
    <button class="btn" data-dismiss="modal" aria-hidden="true">Close</button>
    <button  type="button" id="sendemail" class="btn btn-primary send" data-dismiss="modal" onclick="generatepdf(this.id)" >Send Email</button>
  </div>
  </form>
</div>







 <form id="frmReportIframedelimit" name="frmReportIframedelimit" target="pdfdownloadIframedelimit" action="/admin/generatedelimitpdf" method="post">
    <input id="allStaffDetailsJsondelimit" name="allStaffDetailsJsondelimit" type="hidden" value="" />
    <input id="mapForDelimitJson" name="mapForDelimitJson" type="hidden" value="" />
    <input id="btnSubmitdelimit" name="btnSubmitdelimit" type="button" style="display:none;" />
   </form>
<iframe style="display: none" id="pdfdownloadIframedelimit" name="pdfdownloadIframedelimit"></iframe>






<!-- This div for delimit email-->
<div id="myModalDelimit" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="false">
  <div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
    <h3 id="myModalLabel">New Message</h3>
   </div>
   <form>
   <div class="modal-body">
  Enter a valid Email id:<br>
  <p><input type="email" required name="Email" id="emailiddelimit" placeholder="test@gmail.com" ></p><br>
  <p><input type="text" required name="subject" id="subjectemaildelimit" placeholder="Subject"></p><br>
  <p class="textareas"><textarea type="textarea" required name="textarea" id="textareaemaildelimit" rows="5" cols="70">
  This is Invoice Generator, Thank you........
  </textarea></p>
  </div>
  <div class="modal-footer">
    <button class="btn" data-dismiss="modal" aria-hidden="true">Close</button>
    <button  type="button" id="sendemail" class="btn btn-primary send" data-dismiss="modal" onclick="generatedelimitpdf(this.id)" >Send Email</button>
  </div>
  </form>
</div>















<!--         <table id="myTable" class="tablesorter"> 
<thead> 
<tr> 
    <th>Last Name</th> 
    <th>First Name</th> 
    <th>Email</th> 
    <th>Due</th> 
    <th>Web Site</th> 
</tr> 
</thead> 
<tbody> 
<tr> 
    <td>Smith</td> 
    <td>John</td> 
    <td>jsmith@gmail.com</td> 
    <td>$50.00</td> 
    <td>http://www.jsmith.com</td> 
</tr> 
<tr> 
    <td>Bach</td> 
    <td>Frank</td> 
    <td>fbach@yahoo.com</td> 
    <td>$50.00</td> 
    <td>http://www.frank.com</td> 
</tr> 
<tr> 
    <td>Doe</td> 
    <td>Jason</td> 
    <td>jdoe@hotmail.com</td> 
    <td>$100.00</td> 
    <td>http://www.jdoe.com</td> 
</tr> 
<tr> 
    <td>Conway</td> 
    <td>Tim</td> 
    <td>tconway@earthlink.net</td> 
    <td>$50.00</td> 
    <td>http://www.timconway.com</td> 
</tr> 
</tbody> 
</table>  -->

</body>
</html>


<!-- <body >

<span><b style="font-family: r;">Upload Text file</b><br><br><input type="file" id="file" name="uploadFile" onchange="uploadcsv(this)" /></span></div>
<h1 align="center">Invoice Generator</h1>
<div style="overflow-y:auto;height:600px; width:1250px;">
<center>
<table >
<thead id="invoiceHeaders">
<tr  bgcolor="#DCDCDC"><td >Account Number</td><td>Phone Number</td><td>Invoice Date</td><td>Date Due</td><td>Address</td><td> Charges</td>  <td>2</td> <td>3</td> <td>4</td> <td>5</td> <td>6</td> <td>7</td> <td>8</td> <td>9</td> <td>10</td> <td>Summary of charges</td><td>Total Charges </td><td>From Address</td><td>Account Name</td><td>Summary of charges(page2)</td>
</thead>
<tbody id="invoiceBody" >

</tbody>
</table>

</center>
</div>
<form action="/admin/downloadcsv" method="post">
<input style="align:center" type="submit" value="Download"/>
</form>
</body>
</html> -->