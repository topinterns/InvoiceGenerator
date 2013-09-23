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

<script type="text/javascript" src="../bootstrap/js/bootstrap.min.js"></script>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript" src="/js/jquery-1.7.2.min.js"></script>
		<script type="text/javascript" src="/js/ajaxfileupload.js"></script>
<script type="text/javascript">
var url='<%=blobstoreService.createUploadUrl("/admin/uploadcsv")%>';
		var uploadedData;
	     var test="haiadsfasdfdsa";
		function uploadcsv(uploadedcsv)
		{
			 $('#loading-indicator').show();
		 var pictureElement = uploadedcsv;

		 var file_Id=uploadedcsv.id;

		  $.ajaxFileUpload({
		   url:url,
		   secureuri:false,
		   fileElementId:pictureElement.id,
		   success:function(data)
		   {
		    if(data.indexOf("<")==0)
		     data = $(data).html();
		      uploadedData=JSON.parse(data);
		      var tableData="";
		     for(ind in uploadedData){
		    	 var currentRecord = uploadedData[ind];
		      tableData+='<tr><td>'+uploadedData[ind].accountNumber+'</td><td>'+uploadedData[ind].phoneNumber+'</td><td>'+uploadedData[ind].invoiceDate+'</td><td>'+uploadedData[ind].dateDue+'</td><td>'+uploadedData[ind].address+'</td>';
		      var summarycharges="";
		      var summarycharges2="";

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

		      tableData+='<td>'+summarycharges+'</td>';
		       if(uploadedData[ind].totalCurrentCharges)
		       tableData +='<td>'+uploadedData[ind].totalCurrentCharges+'</td>';
		      else
		        tableData+='<td></td>';
		       if(uploadedData[ind].answerConnectAddress)
		        tableData +='<td>'+uploadedData[ind].answerConnectAddress+'</td>';
		       else
		         tableData+='<td></td>';

		     tableData+='<td>'+uploadedData[ind].accountName+'</td><td>'+summarycharges2+'</td>';

		    /*  tableData+='<td><a href="#" onclick="generatepdf("'+a+'")">hai</a></td></tr>'; */
		     tableData+='<td><a href="#" onclick=generatepdf("'+ind+'")>Download Pdf</a></td></tr>';
		       /* var a = document.createElement('a');
		     var linkText = document.createTextNode("Generate Pdf");
		     a.appendChild(linkText);
		     a.title = "Generate Pdf";
		     a.href = "#";
		     a.onclick = generatepdf();
		     tableData+= '<td>'+a+'</td></tr>';  */

		     }
		     $('#invoiceBody').html(tableData);
		   }
		  }); /* var data ='{"test28@a-cti.com":{"lastName":"test","phoneNumber":"9791067294","firstName":"28","emailid":"test28@a-cti.com"},"test2@a-cti.com":{"lastName":"blalji","phoneNumber":"9791067294","firstName":"ram","emailid":"test2@a-cti.com"},"test3@a-cti.com":{"lastName":"raju","phoneNumber":"9791067294","firstName":"Bhadri","emailid":"test3@a-cti.com"},"test29@a-cti.com":{"lastName":"test","phoneNumber":"9791067294","firstName":"28","emailid":"test29@a-cti.com"},"test30@a-cti.com":{"lastName":"test","phoneNumber":"9791067294","firstName":"28","emailid":"test30@a-cti.com"},"test31@a-cti.com":{"lastName":"test","phoneNumber":"9791067294","firstName":"28","emailid":"test31@a-cti.com"},"test32@a-cti.com":{"lastName":"test","phoneNumber":"9791067294","firstName":"28","emailid":"test32@a-cti.com"},"test33@a-cti.com":{"lastName":"test","phoneNumber":"9791067294","firstName":"28","emailid":"test33@a-cti.com"},"test34@a-cti.com":{"lastName":"test","phoneNumber":"9791067294","firstName":"28","emailid":"test34@a-cti.com"},"test35@a-cti.com":{"lastName":"test","phoneNumber":"9791067294","firstName":"28","emailid":"test35@a-cti.com"},"test36@a-cti.com":{"lastName":"test","phoneNumber":"9791067294","firstName":"28","emailid":"test36@a-cti.com"},"test37@a-cti.com":{"lastName":"test","phoneNumber":"9791067294","firstName":"28","emailid":"test37@a-cti.com"}}';
 uploadedData=JSON.parse(data);
	var popuplilistcontent="";
 for(ind in uploadedData){
	 popuplilistcontent+='<li class="popupli" ><input type="checkbox" class="chkbx" name="staffselect" value='+uploadedData[ind].emailid+'>'+uploadedData[ind].emailid+'</span><br/></li>';
 }
 $('#popuplilist').html(popuplilistcontent);
 $('#popuplilist').show();
 $('#multipleStaffUpload').show(); */
}
		/* function download(){
			var frm,iput;
			 iput = document.getElementById('allStaffDetailsJson').value = uploadedData;
			 frm = document.getElementById("frmReportIframe");
			 frm.action="/admin/downloadcsv";
			 frm.method="post";
			 frm.target="reportIframe";
			 frm.submit();
		} */



		function generatepdf(currentRecord)
		{

			  var frm,iput;
			 iput = document.getElementById('allStaffDetailsJson').value = currentRecord;
			 frm = document.getElementById("frmReportIframe");
			 frm.action="/admin/generatepdf";
			 frm.method="post";
			 frm.target="pdfdownloadIframe";
			 frm.submit();

		}
		function searchData()
		{

			var check="";
			check = uploadedData[$('#textboxid').val()];
			var tabledata="";

			 tabledata='<tr><td>'+check.accountNumber+'</td><td>'+check.phoneNumber+'</td><td>'+check.invoiceDate+'</td><td>'+check.dateDue+'</td><td>'+check.address+'</td>';

			 for(index in check.TotalDue){
				 tabledata+='<td>'
		         tabledata+=check.TotalDue[index]
				 tabledata+='</td>'
					 }


						 var summarycharges="";
						 var summarycharges2="";

						 for(index in check.summaryOfCharges){
							 var tempscr="";
								 for(index1 in  check.summaryOfCharges[index]){
									 tempscr += check.summaryOfCharges[index][index1]+"  " ;
			                         }
							 summarycharges +=tempscr;
						}
						 tabledata+='<td>'+summarycharges+'</td>';


						 for(index in check.summaryOfCharges2){
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

					 tabledata+='<td>'+check.accountName+'</td><td>'+summarycharges2+'</td></tr>';

									$('#invoiceBody').html(tabledata);
		}

</script>
<title>Invoice Creator</title>
<img src="bootstrap/img/loading.gif" id="loading-indicator" style="display:none" />
<div class="header" style="background-color: lightblue;">
<img src="/logo.gif"/>
</div>
</head>
<body bgcolor="#eee">
<!-- <div class="container" style="width: 1250px;"> -->
<div class="container" style="width: 1250px;">

<div class="navbar-search pull-right">
<i class="icon-search"></i>
   <input  type="text" id="textboxid" onkeyup="searchData()"  placeholder="Account Number"  style="background-color: antiquewhite" >
  <br><a href="/pages/NewFile.jsp"><input class="btn btn-primary" style="align:center" type="submit" value="Custom Invoice"/></a>
    <!-- <input type="search" id='textboxid' onsearch="searchData()">
<input type="button" value="search" id='searchbtnid' onclick="searchData()"/>
     -->

 </div>

<div><!-- class="header" style="background-color: lightblue;width: 456px;margin-left: 20px;height: 57px;margin-top: 27px;" -->
<br>
<span><b style="font-family: r;">Upload Text file</b><br><br><input class="btn btn-info" type="file" id="file" name="uploadFile" onchange="uploadcsv(this)" /></span></div>
&nbsp &nbsp<form action="/admin/downloadcsv" method="post">
<input class="btn btn-large btn-success" style="align:center" type="submit" value="Download"/>
</form>
<h1 align="center">Invoice Generator</h1>
<div style="overflow-y:auto;height:600px; width:1250px;">
<center>
<table class="table table-hover" >
<thead id="invoiceHeaders">
<tr  bgcolor="#DCDCDC"><td >Account Number</td><td>Phone Number</td><td>Invoice Date</td><td>Date Due</td><td>Address</td><td> Charges</td>  <td>2</td> <td>3</td> <td>4</td> <td>5</td> <td>6</td> <td>7</td> <td>8</td> <td>9</td> <td>10</td> <td>Summary of charges</td><td>Total Charges </td><td>From Address</td><td>Account Name</td><td>Summary of charges(page2)</td> <td>Generate Pdfs </td>
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



<!-- <form id="frmReportIframe" name="frmReportIframe" target="reportIframe" action="/userClockIn/generateUserClockInDetailsReportForAll.req" method="post">
    <input id="allStaffDetailsJson" name="allStaffDetailsJson" type="hidden" value="" />
    <input id="btnSubmit" name="btnSubmit" type="button" style="display:none;" />
   </form>
   <iframe style="display: none" id="reportIframe" name="reportIframe"></iframe>
   <input type="button" value="Download"/>
   -->
</center>
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