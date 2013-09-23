<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page
	import="com.google.appengine.api.blobstore.BlobstoreServiceFactory"
	import="com.google.appengine.api.blobstore.BlobstoreService"%>
<%
	BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

 %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript" src="/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="/js/ajaxfileupload.js"></script>
<script type="text/javascript" src="/js/loadDetails.js"></script>
<link rel="stylesheet" type="text/css"
	href="../bootstrap/css/bootstrap.css">
<link rel="stylesheet" type="text/css"
	href="../bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css"
	href="../font-awesome/css/font-awesome-ie7.min.css">
<link rel="stylesheet" type="text/css"
	href="../font-awesome/css/font-awesome.min.css">
<!-- <link rel="stylesheet" type="text/css" href="../bootstrap/css/style.css" /> -->

<script type="text/javascript" src="/js/jquery.dataTables.js"></script>
<script type="text/javascript" src="/js/jquery.dataTables.min.js"></script>
<!-- <script type="text/javascript" src="/js/jquery-latest.js"></script> 
<script type="text/javascript" src="/js/jquery.tablesorter.js"></script>  -->
<script type="text/javascript" src="../bootstrap/js/bootstrap.min.js"></script>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

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
	var taskQueueStatus = false;
	function uploadcsv(uploadedcsv) {
		$('.backdrop').animate({
			'opacity' : '.50'
		}, 300, 'linear');
		$('.backdrop').css('display', 'block');
		$('#loading-indicator').show();
		$('#loading-indicator').show();
		var pictureElement = uploadedcsv;

		var file_Id = uploadedcsv.id;

		$.ajaxFileUpload({
			url : url,
			secureuri : false,
			fileElementId : pictureElement.id,
			success : function(data) {

				$('#loading-indicator').hide();
				$('.backdrop').css('display', 'none');
				$('#checktaskqueue').show();

			}
		});
	}

	function checkTaskQueueStatus() {
		console.info(taskQueueStatus + ">>>heloooooo>>>>>>>>>");
		if (!taskQueueStatus) {
			$.ajax({
				type : 'POST',
				url : '/admin/getTaskQueueStatus',
				contentType : "application/json",
				beforeSend : function(jqXHR) {
					jqXHR.setRequestHeader("Connection", "close");
				},
				async : false,
				data : "",
				success : function response(resultObject) {
					console.info(resultObject);
					taskQueueStatus = resultObject;
					if (taskQueueStatus) {
						getUploadedData();
					} else {
						alert("Processing... please wait ")
						//setTimeout(checkTaskQueueStatus, 5000);
					}

				},
				dataType : ''
			});
		}

	}

	function getUploadedData() {
		
				$.ajax({
					type : 'POST',
					url : '/admin/getUploadedData',
					contentType : "application/json",
					beforeSend : function(jqXHR) {
						jqXHR.setRequestHeader("Connection", "close");
					},
					async : false,
					data : "",
					success : function response(data) {
						$('.backdrop').animate({
							'opacity' : '.50'
						}, 300, 'linear');
						$('.backdrop').css('display', 'block');
						$('#loading-indicator').show();
						console.info(data)
						$('#invoiceHeaders').show();
						uploadedData = data;
						var tableData = "";
						for (ind in uploadedData) {
							var currentRecord = uploadedData[ind];
							var trClass = uploadedData[ind].fromAddress[0].replace(" ","");
							 var tempIndex = "'"+ind+"'";
							tableData += '<tr class='+trClass+'><td>'
									+ uploadedData[ind].accountNumber
									+ '</td><td>'
									+ uploadedData[ind].phoneNumber
									+ '</td><td>'
									+ uploadedData[ind].invoiceDate
									+ '</td><td>' + uploadedData[ind].dateDue
									+ '</td><td>' + uploadedData[ind].address
									+ '</td>';
							var summarycharges = "";
							var summarycharges2 = "";
							var userIdSummary = "";
							for (index in uploadedData[ind].TotalDue) {
								tableData += '<td>'
								tableData += uploadedData[ind].TotalDue[index]
								tableData += '</td>'
							}
							for (index in uploadedData[ind].summaryOfCharges) {
								var tempscr = "";
								for (index1 in uploadedData[ind].summaryOfCharges[index]) {
									tempscr += uploadedData[ind].summaryOfCharges[index][index1]
											+ "  ";
								}
								summarycharges += tempscr;
							}
							for (index in uploadedData[ind].summaryOfCharges2) {
								var tempscr = "";
								for (index1 in uploadedData[ind].summaryOfCharges2[index]) {
									tempscr += uploadedData[ind].summaryOfCharges2[index][index1]
											+ "  ";
								}
								summarycharges2 += tempscr;
							}
							for (index in uploadedData[ind].userIdSummary) {
								var tempscr = "";
								for (index1 in uploadedData[ind].userIdSummary[index]) {
									tempscr += uploadedData[ind].userIdSummary[index][index1]
											+ "  ";
								}
								userIdSummary += tempscr;
							}
							tableData += '<td>' + summarycharges + '</td>';
							if (uploadedData[ind].totalCurrentCharges)
								tableData += '<td>'
										+ uploadedData[ind].totalCurrentCharges
										+ '</td>';
							else
								tableData += '<td></td>';
							if (uploadedData[ind].fromAddress)
								tableData += '<td id="fromAddress">'
										+ uploadedData[ind].fromAddress  
										+ '</td>';
							else
								tableData += '<td></td>';

							tableData += '<td>' + uploadedData[ind].accountName
									+ '</td><td>' + summarycharges2
									+ '</td><td>' + userIdSummary + '</td>';
							/* tableData+='<td><a href="#myModal" data-toggle="modal" >Download Pdf & Send an Email</a></td></tr>'; */
							/*   tableData+='<td><a href="#" onclick=generatepdf('+ind+') >Download Pdf & Send an Email</a></td></tr>'; */
							tableData += '<td><a href="#myModal" onclick=generatepdfForMail('
									+ tempIndex
									+ ') data-toggle="modal" >Send an Email</a></td>';
							$('.send').attr('id',ind);
							tableData +='<td><a href="#" onclick=generatepdf('+tempIndex+') >Download Pdf</a></td>';
							tableData +='<td><a target="_blank" href="/admin/generatehtmltemplate?currentRecord='+tempIndex+'">View in Html</a></td></tr>';
							
						}

						$('#invoiceBody').html(tableData);
						$('#mytable').dataTable({
							"bDestroy" : false,
							"bLengthChange" : false,
							"bFilter" : false,
							"bPaginate" : false,
							"bJQueryUI" : false,
						});
						$('#loading-indicator').hide();
						$('.backdrop').css('display', 'none');
						$("#myTable").tablesorter();
					},
					dataType : ''
				});
	}
	
	
// 	 function generatepdf(currentRecord)
// 	    {    
// 	    	   var mapForEmail={};
// 	    	   mapForEmail["currentRecord"]=currentRecord;
// 	    	   mapForEmail["emailId"]="NA";
// 	    	   mapForEmail["subject"]="NA";
// 	    	   mapForEmail["body"]="NA";
	    	   	   
// 	    //console.info(JSON.stringify(mapForEmail));
// 	     var frm,iput;
// 	     $('#allStaffDetailsJson').val(JSON.stringify(mapForEmail));
// 	     frm = document.getElementById("frmReportIframe");
// 	     frm.action="/admin/generatesinglepdf";
// 	     frm.method="post";
// 	     frm.target="pdfdownloadIframe";
// 	     frm.submit();
// 	    }

	 
// 	 var i=0;
// 	 var record="";
// 	 function generatepdfForMail (currentRecord)
// 	 {    
// 	   i++;
// 	   if(i==1)
// 	   record = currentRecord;
	   
// 	   if(i==2){
	 	
// 	   var email=$("#emailid").val(); 
// 	   var subject=$("#subjectemail").val();
// 	   var body=""; 
// 	   body=$("#textareaemail").val();
// 	  // alert( "Thisis currentRecord "+record+"  email id ::= "+email+"  subject  "+subject+"  body "+body);
	 	   
	 	  
// 	 	   var mapForEmail={};    
// 	 	   mapForEmail["currentRecord"]=record;
// 	 	   mapForEmail["emailId"]=email;
// 	 	   mapForEmail["subject"]=subject;
// 	 	   mapForEmail["body"]=body;
	 	   	   
// 	 //console.info(JSON.stringify(mapForEmail));
// 	 var re = /\S+@\S+\.\S+/;
// 	 if(re.test(email))
// 	 {
// 	  var frm,iput;
// 	  //iput = document.getElementById('allStaffDetailsJson').value = record+";"+email+";"+subject+";"+stringBody;
// 	 // document.getElementById('allStaffDetailsJson').value ="testststsetestsetes";
// 	  $('#allStaffDetailsJson').val(JSON.stringify(mapForEmail));
// 	  frm = document.getElementById("frmReportIframe");
	  
// 	  frm.action="/admin/generatesinglepdf";
// 	  frm.method="post";
// 	  frm.target="pdfdownloadIframe";
// 	  frm.submit();
// 	  i=0;

// 	  $("#emailid").val("");
// 	  $("#subjectemail").val("");
// 	  $("#textareaemail").val("This is Invoice Generator, Thank you........");

	  
// 	 }
// 	 else{
// 	 	alert("Enter valid emailid");
// 	 	 var s="malli";
// 	 	//console.log("variable value:"+s); 
// 	 	i=0;

// 	 	 $("#emailid").val("");
// 	 	 $("#subjectemail").val("");
// 	 	 $("#textareaemail").val("This is Invoice Generator, Thank you........");
// 	 }

// 	  }
// 	 }
	 
	 
	/* var i = 0;
	var record;
	function generatepdf(currentRecord) {
		i++;
		if (i == 1) 
			record = currentRecord;
		if (i == 2) {
			var email = $("#emailid").val();
			var re = /\S+@\S+\.\S+/;
			if (re.test(email)) {
				var frm, iput;
				iput = document.getElementById('allStaffDetailsJson').value = record
						+ email;
				frm = document.getElementById("frmReportIframe");
				frm.action = "/admin/generatesinglepdf";
				frm.method = "post";
				frm.target = "pdfdownloadIframe";
				frm.submit();
				i = 0;
			} else 
				alert("Enter valid emailid");
		}
		$("#emailid").val("");
	} */
	
	/*var i=0;
	var record;
	function generatepdf(currentRecord)
	{    
	      i++;
		  if(i==1)
		  record = currentRecord;
	      
		  if(i==2){
			
		  var email=$("#emailid").val(); 
		  var subject=$("#subjectemail").val();
		  var body=""; 
		  body=$("#textareaemail").val();
		  alert( "Thisis currentRecord "+record+"  email id ::= "+email+"  subject  "+subject+"  body "+body);
			   
			  
			   var mapForEmail={};
			   mapForEmail["currentRecord"]=record;
			   mapForEmail["emailId"]=email;
			   mapForEmail["subject"]=subject;
			   mapForEmail["body"]=body;
			   	   
		console.info(JSON.stringify(mapForEmail));
		var re = /\S+@\S+\.\S+/;
		if(re.test(email))
		{
		 var frm,iput;
		 //iput = document.getElementById('allStaffDetailsJson').value = record+";"+email+";"+subject+";"+stringBody;
		// document.getElementById('allStaffDetailsJson').value ="testststsetestsetes";
		 $('#allStaffDetailsJson').val(JSON.stringify(mapForEmail));
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
			/* var s="malli";
			console.log("variable value:"+s); */
		//}

		 ///}
//	}*/

	/* function generatepdf(currentRecord)
		{

			  var email = window.prompt("Enter email id"); 
			// window.alert(email);  
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
			}

		}  */

	/* function generatebulk() {
		var frm, iput;
		  iput = document.getElementById('allStaffDetailsJson').value = currentRecord+email; 
		frm = document.getElementById("frmReportIframe1");
		frm.action = "/admin/generatebulkpdf";
		frm.method = "post";
		frm.target = "pdfdownloadIframe1";
		frm.submit();
	} */
	
	

	/* var i=0;
	var record="";
	function generatepdf(currentRecord)
	{    
	  i++;
	  if(i==1)
	  record = currentRecord;
	  
	  if(i==2){
		
	  var email=$("#emailid").val(); 
	  var subject=$("#subjectemail").val();
	  var body=""; 
	  body=$("#textareaemail").val();
	 // alert( "Thisis currentRecord "+record+"  email id ::= "+email+"  subject  "+subject+"  body "+body);
		   
		  
		   var mapForEmail={};
		  // mapForEmail["currentRecord"]=record;
		   mapForEmail["emailId"]=email;
		   mapForEmail["subject"]=subject;
		   mapForEmail["body"]=body;
		   	   
	console.info(JSON.stringify(mapForEmail));
	var re = /\S+@\S+\.\S+/;
	if(re.test(email))
	{
	 var frm,iput;
	 //iput = document.getElementById('allStaffDetailsJson').value = record+";"+email+";"+subject+";"+stringBody;
	// document.getElementById('allStaffDetailsJson').value ="testststsetestsetes";
	 $('#allStaffDetailsJson').val(JSON.stringify(mapForEmail));
	 $('#mapWithCurrentRecordJson').val(JSON.stringify(uploadedData[record]));
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
		i=0;

		 $("#emailid").val("");
		 $("#subjectemail").val("");
		 $("#textareaemail").val(" This is Invoice Generator, Thank you........");
	}

	 }
	} */
	
	function generatebulk(){
		   var frm,iput;
		     //iput = document.getElementById('allStaffDetailsJson').value = currentRecord+email; 
		  //   console.log(JSON.stringify(uploadedData));
		     //frm.action="http://jsbackend.3.topinterns.appspot.com/admin/generatebulkpdf";
		     console.log("data in the generate bulk pdf:"+uploadedData);
		   $('#allStaffDetailsJson1').val(JSON.stringify(uploadedData));
		    frm = document.getElementById("frmReportIframe1");
		    frm.action = "http://test-backend.topinterns.appspot.com/admin/generatebulkpdf";
		    frm.method="post";
		    frm.target="pdfdownloadIframe1";
		    frm.submit();
		  } 
	
	function generateSelected(brand){
		var mapContainsMap = {};
		$("#invoiceBody tr:."+brand).each(function() {
			  $this = $(this)
			  var value = $this.find("td").html();
			  mapContainsMap[value] = uploadedData[value];
			 });
		console.log(mapContainsMap);
		var frm,iput;
	   $('#allStaffDetailsJson1').val(JSON.stringify(mapContainsMap));
	    frm = document.getElementById("frmReportIframe1");
	    frm.action = "/admin/generatebulkpdf";
	    frm.method="post";
	    frm.target="pdfdownloadIframe1";
	    frm.submit();
	}
	function cloudStore(){
		var parametersJSON = JSON.stringify(uploadedData);
		$.ajax({
			   type: 'POST',
			   url: '/admin/cloudStore',
			   contentType: "application/json",
			   beforeSend: function ( jqXHR )
			   {
			    jqXHR.setRequestHeader("Connection", "close");
			   },
			   async: false,
			   data : parametersJSON ,
			    success: function response(resultObject)
			   {
			    alert("Success");
			    },
			   dataType: ''
			  });
	}
	

	
	function searchData()
	{
	    
	 var check="";
	 var check1="";
	 var lenOftextboxid;
	 	
	 lenOftextboxid=$('#textboxid').val().length;
	 
	 check1= mapContainsMap[$('#textboxid').val()];
	 
	 
	
	  if(check1)
	 check=undefined;
	  else if(lenOftextboxid==10)
     check = uploadedData[$('#textboxid').val()];
	
	 
	 
	  if(lenOftextboxid==0)
	  { 

	   $('#errorHeader').hide();
	   $('.tablesearch').hide();
	   $('#search').hide();
	   $('#searchbody').hide();

	  }
	else if(check)
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
	      var userIdSummary = "";
	     
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
	   for (index in check.userIdSummary) {
			var tempscr = "";
			for (index1 in check.userIdSummary[index]) {
				tempscr += check.userIdSummary[index][index1]
						+ "  ";
			}
			userIdSummary += tempscr;
		}

	       if(check.totalCurrentCharges)
	    tabledata +='<td>'+check.totalCurrentCharges+'</td>';
	    else
	      tabledata+='<td></td>';
	   if(check.fromAddress)
	   tabledata +='<td>'+check.fromAddress+'</td>';
	     else
	       tabledata+='<td></td>';
	       
	       tabledata += '<td>' + check.accountName
			+ '</td><td>' + summarycharges2
			+ '</td><td>' + userIdSummary + '</td>';
		tabledata += '<td><a href="#myModal" onclick=generatepdfForMail('+ ind+ ') data-toggle="modal" >Send an Email</a></td>';
		tabledata +='<td><a href="#" onclick=generatepdf('+ind+') >Download Pdf</a></td></tr>';	
	       
	       $('#searchbody').html(tabledata);
	       $('.send').attr('id',check); 
	       
	       
	 
	} 
	  
	 else if(check1)
	  {
	    //alert("This is check1");
	    $('#errorHeader').hide();
	    $('.tablesearch').show();
	    $('#search').show();
	    $('#searchbody').show();
	  
	    var accountNo=check1.accountNumber;
	    
	    var tableDelimitData="";
	    var SOC2string="";
	    var UISstring="";
	    var SOCstring="";
	 
	    tableDelimitData+='<tr><td>'+check1.accountNumber+'</td><td>'+check1.phoneNumber+'</td><td>'+check1.invoiceDate+'</td><td>'+check1.dateDue+'</td><td>'+check1.address+'</td>';
	    for(index in check1.TotalDue){
	     tableDelimitData+='<td>'
	     tableDelimitData+=check1.TotalDue[index]
	     tableDelimitData+='</td>'
	   }
	    
	    var SOCstring="";
	    var SOCstring1="";        
	     for(index in check1.SOCArray1) {
	      
	        SOCstring+=" "+check1.SOCArray1[index];
	     }
	     for(index in check1.SOCArray2){
	      
	     SOCstring1+=" "+check1.SOCArray2[index];
	     }
	     
	     SOCstring+=" "+SOCstring1;
	     //console.log(SOCstring);
	    
	    
	    tableDelimitData+='<td>'+SOCstring+'</td><td>'+check1.totalCurrentCharges+'</td><td>'+check1.fromAddress+'</td><td>'+check1.accountName+'</td>';
	    
	    var tempscr="";
	    for(index in check1.summaryOfCharges2){
	    tempscr += check1.summaryOfCharges2[index]+"  " ;
	                            }
	     SOC2string=tempscr;
	     //console.log(SOC2string);
	     var tempscr1="";
	    for(index in check1.userIdSummary){
	    tempscr1 += check1.userIdSummary[index]+"  " ;
	                             }
	   UISstring=tempscr1;
	    console.log(UISstring);      
	       
	  
	    tableDelimitData+='<td>'+SOC2string+'</td><td>'+UISstring+'</td>';
	    tableDelimitData+='<td><a href="#myModalDelimit" onclick=generatedelimitpdfForMail('+ind+') data-toggle="modal" >Send an Email</a></td>';
	    tableDelimitData+='<td><a href="#" onclick=generatedelimitpdf('+ind+')  >Download Pdf</a></td></tr>';
	    //$('.send').attr('id',ind); 
	   
	    
	    $('#searchbody').html(tableDelimitData);
	  
	  }  
	  
	  
	  
	  
	 else{
	  
	  
	  $('#errorHeader').show();
	     $('#search').hide();
	     $('#searchbody').hide(); 
	 }
	}


	/* function searchData() {

		var check = "";
		check = uploadedData[$('#textboxid').val()];
		alert(check.accountNumber);
		if (check) {

			$('#errorHeader').hide();
			$('.tablesearch').show();
			$('#search').show();
			$('#searchbody').show();

			var tabledata = "";

			tabledata = '<tr><td>' + check.accountNumber + '</td><td>'
					+ check.phoneNumber + '</td><td>' + check.invoiceDate
					+ '</td><td>' + check.dateDue + '</td><td>' + check.address
					+ '</td>';

			for (index in check.TotalDue) {
				tabledata += '<td>'
				tabledata += check.TotalDue[index]
				tabledata += '</td>'
			}

			var summarycharges = "";
			var summarycharges2 = "";
			var userIdSummary = "";

			for (index in check.summaryOfCharges) {
				var tempscr = "";
				for (index1 in check.summaryOfCharges[index]) {
					tempscr += check.summaryOfCharges[index][index1] + "  ";
				}
				summarycharges += tempscr;
			}

			tabledata += '<td>' + summarycharges + '</td>';

			for (index in check.summaryOfCharges2) {
				var tempscr = "";
				for (index1 in check.summaryOfCharges2[index]) {
					tempscr += check.summaryOfCharges2[index][index1] + "  ";
				}
				summarycharges2 += tempscr;
			}
			
			for (index in check.userIdSummary) {
				var tempscr = "";
				for (index1 in check.userIdSummary[index]) {
					tempscr += check.userIdSummary[index][index1]
							+ "  ";
				}
				userIdSummary += tempscr;
			}

			if (check.totalCurrentCharges)
				tabledata += '<td>' + check.totalCurrentCharges + '</td>';
			else
				tabledata += '<td></td>';
			if (check.fromAddress)
				tabledata += '<td>' + check.fromAddress + '</td>';
			else
				tabledata += '<td></td>';

			 
			
			tabledata += '<td>' + check.accountName
					+ '</td><td>' + summarycharges2
					+ '</td><td>' + userIdSummary + '</td>';
			
			tabledata+='<td><a href="#myModal" onclick=generatepdf("'+$('#textboxid').val()+'") data-toggle="modal" >Download Pdf & Send an Email</a></td></tr>';
			         
			$('.send').attr('id',check);
			$('#searchbody').html(tabledata);
			


		} else {
			alert("no matches");
			$('#errorHeader').show();
			$('#search').hide();
			$('#searchbody').hide();
		}
	} */
	
	/* function show(){
		
		alert($('#myTable th').text());
	} */
</script>
<title>Invoice Creator</title>

<div class="header" >
	<div class="backdrop"></div>
	<!-- <img src="/logo.gif" /> --> 
	<img src="/bootstrap/img/loading.gif"
		id="loading-indicator" class="loading" />
		
 
<!-- changing here ***************************************************************************************************** -->

 <div class="navbar" style="display:block">
   
      <div class="container">

       <div class="nav-collapse collapse navbar-responsive-collapse">
  <ul class="nav " id="nav">
        <li class='active' ><a href='#'>Home</a></li>
        
         <li  id="All"><a target="_blank" href="../pages/CustomInvoice.jsp">Custom Invoice</a></li>
         
         <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown">Uploads <b class="caret"></b></a>
                        <ul class="dropdown-menu">
                          <li><a href="#" ><input  type="file" id="file" name="uploadFile" onchange="uploadcsv(this)" />CAS File</a></li>
                          <li id="uploadDelimit"><a href="#"><input type="file" id="file" onchange='readText(this)' />Delimited File</a></li>
							 <script type="text/javascript">
							 checkFileAPI('"uploadDelimit"');
							 </script> 
                        </ul>
        </li>
         <li class="dropdown">
         <a href="#" class="dropdown-toggle" data-toggle="dropdown">Downloads <b class="caret"></b></a>
                        <ul class="dropdown-menu">
                         
                          <li><form action="/admin/downloadcsv" method="post"><input type="submit" class="notAButton" value="Download CSV File"></form></li>
                         
                          <li><a onclick="generatebulk()"><input type="submit" class="notAButton" value="Download all PDF"></a></li>
                          <li><a onclick="generateSelected('AnswerConnect')"><input type="submit" class="notAButton" value="Download AC PDFs"></a></li>
                          <li><a onclick="generateSelected('LexReception')"><input type="submit" class="notAButton" value="Download LR PDFs"></a></li>
                          <li><a onclick="generateSelected('AnswerForce')"><input type="submit" class="notAButton" value="Download AF PDFs"></a></li>
                          <li><a onclick="generateSelected('Memosent')"><input type="submit" class="notAButton" value="Download MS PDFs"></a></li>
                        </ul>
         </li>
         <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown">Reciepts <b class="caret"></b></a>
                        <ul class="dropdown-menu">
                          <li><a target="_blank" href="../pages/receipt.jsp">Payment Reciept</a></li>
                          <li><a target="_blank" href="../pages/receipt.jsp">Credit Reciept</a></li>
                        </ul>
          </li>
       
</ul> 
<!-- <ul  class= "nav">
<li class='active' ><a href='#'>Home</a></li>
<li class='' ><a href="#" class="dropdown-toggle" data-toggle="dropdown">Uploads <b class="caret"></b></a>
						<ul class="dropdown-menu">
                          <li><a href="#" ><input  type="file" id="file" name="uploadFile" onchange="uploadcsv(this)" />CAS File</a></li>
                          <li id="uploadDelimit"><a href="#"><input type="file" id="file" onchange='readText(this)' />Delimited File</a></li>
							 <script type="text/javascript">checkFileAPI('"uploadDelimit"');</script> 
                        </ul>
</li>
<li class='' ><a href='#'>Home</a></li>
<li class=''><a href="#" class="dropdown-toggle" data-toggle="dropdown">Downloads <b class="caret"></b></a>
						<ul class="dropdown-menu">
                          <li><form action="/admin/downloadcsv" method="post"><input type="submit" class="notAButton" value="Download CSV File"></form></li>
                          <li><a onclick="generatebulk()">Download all PDF</a></li>
                        </ul>
</li>
<li class='' ><a href='#'>Home</a></li>
</ul> -->
        <form class="navbar-search pull-right">
        <!--  <input onkeyup='searchData()' type="text" class="search-query span2" 
          placeholder="Account Number" id="searchField"> -->
          <input class="search-query span2" type="text" id="textboxid" onkeyup="searchData()" placeholder="Account Number" > 
				<i class="icon-search"></i> 
        </form>
       </div>
      <!--  /* /.nav-collapse */ -->
      </div>
   
     <!-- /*-- /navbar-inner */ -->
    </div>



 <!-- ********************************************************************************************************************************** --> 
</div>



</head>
<body bgcolor="#eee" >

	<!-- <div class="container" style="width: 1250px;"> -->
	<div class="container" style="width: 1250px;">

		<!--  <div class="navbar-search pull-right">
			<i class="icon-search"></i> <input type="text" id="textboxid"
				onkeyup="searchData()" placeholder="Account Number"
				style="background-color: antiquewhite"> <br>  -->
			<!-- <a href="/pages/CustomInvoice.jsp"><input class="btn btn-primary"
				style="align: center" type="submit" value="Custom Invoice" /></a> <br>
			<br>
			<a href="/pages/receipt.jsp"><input class="btn btn-primary"
				style="align: center" type="submit" value="Payment Receipt Generator" /></a> <br>
				<br><a href="/pages/receipt.jsp"><input class="btn btn-primary"
				style="align: center" type="submit" value="Credit Receipt Generator" /></a> <br> -->
			
			<!-- <input class="btn btn-large btn-success" onclick="generatebulk()"
				style="align: center" type="button" value="Download All Pdfs" /> -->
	<!-- 	</div>  -->

		<!-- <div>
			<br> <span><b style="font-family: r;">Upload Text
					file</b><br>
			<br>
			<input class="btn btn-info" type="file" id="file" name="uploadFile"
				onchange="uploadcsv(this)" /></span>
		</div>
		&nbsp &nbsp
		<br> -->
		<!-- <form action="/admin/downloadcsv" method="post"> -->
		
		<a href="#"><input type="button" class="btn btn-primary" id='checktaskqueue' style="align: center; display: none"
				onClick="checkTaskQueueStatus()"
				value="Click Here To Populate The Result" /></a> 
		
		<a href="#"><input type="button" class="btn btn-danger" id="cloud" value="Push data to cloud storage" onClick="cloudStore()"/></a>
		
			<!-- <input class="btn btn-large btn-success" style="align: center"
				type="submit" value="Download CSV" /> -->
		<!-- </form> -->
		
		<!-- CHOOSE DELIMITED FILE -->

		<!-- <div id="uploadDelimit">
			<br> <span><b style="font-family: r;">Upload
					Delimited File</b><br>
			<br> <input class="btn btn-info" type="file" id="file"
				onchange='readText(this)' />
				<br />
		</div> 
		 <script type="text/javascript">
			checkFileAPI('"uploadDelimit"');
		</script> -->

		<!-- TABLES  STARTED-->
		
		<!-- Table for Search Function -->
		<h1 align="center">Invoice Generator</h1>
		<center>
			<h3 id="errorHeader" style="display: none; color : red">NO MATCHES FOUND</h3>
			<table class="tablesearch" style="display: none">
				<thead id="search">
					<tr bgcolor="#DCDCDC">
						<td>Account Number</td>
						<td>Phone Number</td>
						<td>Invoice Date</td>
						<td>Date Due</td>
						<td>Address</td>
						<td>Total Due</td>
						<td>Last Statement</td>
						<td>Payments Received</td>
						<td>Debits/ Credits</td>
						<td>U</td>
						<td>U</td>
						<td>Balance Forward</td>
						<td>Current Charges</td>
						<td>Due By</td>
						<td>Due After</td>
						<td>Summary of charges</td>
						<td>Total Current Charges</td>
						<td>From Address</td>
						<td>Account Name</td>
						<td>Summary of charges(page2)</td>
						<td>UserIdSummary</td>
						<td>Send an Email</td>
						<td>Generate Pdf </td>
					</tr>
				</thead>
				<tbody id="searchbody">
				</tbody>
			</table>
			<br>
			<br>
		</center>
		
		<!-- Table for Invoice Details -->
		<div style="overflow-y: auto; height: 600px; width: 1250px;">
			<center>
				<table id="mytable" class="table table-hover">
					<thead id="invoiceHeaders" style="display:none;">
						<!-- <tr  bgcolor="#DCDCDC"><td>Account Number <i class="icon-collapse"></i></td><td>Phone Number <i class="icon-collapse"></i></td><td>Invoice Date <i class="icon-collapse"></i></td><td>Date Due <i class="icon-collapse"></i></td><td>TD <i class="icon-collapse"></i></td><td>ALS <i class="icon-collapse"></i></td><td>PR <i class="icon-collapse"></i></td><td>D/C <i class="icon-collapse"></i></td><td>U <i class="icon-collapse"></i></td><td>U <i class="icon-collapse"></i></td><td>BF <i class="icon-collapse"></i></td> <td>8<i class="icon-collapse"></i></td> <td>9<i class="icon-collapse"></i></td> <td>10<i class="icon-collapse"></i></td> <td>Summary of charges <i class="icon-collapse"></i></td><td>Total Charges <i class="icon-collapse"></i></td><td>From Address <i class="icon-collapse"></i></td><td>Account Name <i class="icon-collapse"></i></td><td>Summary of charges(page2) <i class="icon-collapse"></i></td> <td>Generate Pdfs <i class="icon-collapse"></i></td> -->
						<tr bgcolor="#DCDCDC">
							<td>Account Number<i class="icon-sort-by-order"></i></td>
							<td>Phone Number<i class="icon-sort-by-order"></i></td>
							<td>Invoice Date<i class="icon-sort-by-order"></i></td>
							<td>Date Due<i class="icon-sort-by-order"></i></td>
							<td>Address<i class="icon-sort-by-alphabet"></i></td>
							<td>Total Due<i class="icon-sort-by-order"></i></td>
							<td>Last Statement<i class="icon-sort-by-order"></i></td>
							<td>Payments Received<i class="icon-sort-by-order"></i></td>
							<td>Debits/ Credits<i class="icon-sort-by-order"></i></td>
							<td>U<i class="icon-sort-by-order"></i></td>
							<td>U<i class="icon-sort-by-order"></i></td>
							<td>Balance Forward<i class="icon-sort-by-order"></i></td>
							<td>Current Charges<i class="icon-sort-by-order"></i></td>
							<td>Due By<i class="icon-sort-by-order"></i></td>
							<td>Due After<i class="icon-sort-by-order"></i></td>
							<td>Summary of charges<i class="icon-sort-by-order"></i></td>
							<td>Total Current Charges<i class="icon-sort-by-order"></i>
							</td>
							<td>From Address<i class="icon-sort-by-alphabet"></i></td>
							<td>Account Name<i class="icon-sort-by-alphabet"></i></td>
							<td>Summary of charges(page2)<i class="icon-sort-by-order"></i></td>
							<td>UserId Summary<i class="icon-sort-by-order"></i></td>
							<td>Send an Email</td>
							<td>Generate Pdfs</td>
							<td>View as HTML</td>
						</tr>
					</thead>
					<tbody id="invoiceBody">
					</tbody>
				</table>
			</center>
		</div>
	</div>
	
	<!-- TABLES ENDED -->
	
	<!-- HIDDEN FORMS FOR SUBMITTING THE DATA TO THE CONTROLLER STARTED-->
	
	<!-- Form for single PDF download -->
	
	<form id="frmReportIframe" name="frmReportIframe" target="pdfdownloadIframe" action="/admin/generatepdf" method="post">
		<input id="allStaffDetailsJson" name="allStaffDetailsJson" type="hidden" value="" />
		<input id="mapWithCurrentRecordJson" name="mapWithCurrentRecordJson" type="hidden" value="" /> 
		<input id="btnSubmit" name="btnSubmit" type="button" style="display: none;" />
	</form>
	<iframe style="display: none" id="pdfdownloadIframe" name="pdfdownloadIframe"></iframe>
	
		
	<!-- Form for delimit file data submit -->	
	
	<form id="frmReportIframedelimit" name="frmReportIframedelimit" target="pdfdownloadIframedelimit" action="/admin/generatedelimitpdf" method="post">
    	<input id="allStaffDetailsJsondelimit" name="allStaffDetailsJsondelimit" type="hidden" value="" />
    	<input id="mapForDelimitJson" name="mapForDelimitJson" type="hidden" value="" />
   	 	<input id="btnSubmitdelimit" name="btnSubmitdelimit" type="button" style="display:none;" />
   </form>
	<iframe style="display: none" id="pdfdownloadIframedelimit" name="pdfdownloadIframedelimit"></iframe>
	
	
	<!-- Form for Bulk Download  -->
	
	<form id="frmReportIframe1" name="frmReportIframe1" target="pdfdownloadIframe1" action="/admin/bulkPdf" method="post">
		<input id="allStaffDetailsJson1" name="allStaffDetailsJson1" type="hidden" value="" /> 
		<input id="btnSubmit1" name="btnSubmit1" type="button" style="display: none;" />
	</form>
	<iframe style="display: none" id="pdfdownloadIframe1" name="pdfdownloadIframe1"></iframe>

	<!-- HIDDEN FORMS FOR SUBMITTING THE DATA TO THE CONTROLLER ENDED-->
	
	<!-- MODAL FOR EMAIL EDITOR STARTED -->
	
	<!-- Modal for text file email editor -->
	<div id="myModal" class="modal hide fade" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="false">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"
				aria-hidden="true">×</button>
			<h3 id="myModalLabel">New Message</h3>
		</div>
		<div class="modal-body">
			Enter a valid Email id:<br>
			<p>
				<input type="email" required name="Email" id="emailid"
					placeholder="test@gmail.com">
			</p>
			<br>
			<p>
				<input type="text" required name="subject" id=subjectemail
					placeholder="Subject">
			</p>
			<br>
			<p class="textareas">
				<textarea type="textarea" required name="textarea"
					id="textareaemail" rows="5" cols="70">This is Invoice Generator, Thank you........
  				</textarea>
			</p>
		</div>
		<div class="modal-footer">
			<button class="btn" data-dismiss="modal" aria-hidden="true">Close</button>
			<button type="button" id="sendemail" class="btn btn-primary send"
				data-dismiss="modal" onclick="generatepdfForMail(this.id)">Send
				Email</button>
		</div>
	</div>

	<!-- Modal for Delimited file email editor -->
	<div id="myModalDelimit" class="modal hide fade" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel" aria-hidden="false">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"
				aria-hidden="true">×</button>
			<h3 id="myModalLabel">New Message</h3>
		</div>
		<form>
			<div class="modal-body">
				Enter a valid Email id:<br>
				<p>
					<input type="email" required name="Email" id="emailiddelimit"
						placeholder="test@gmail.com">
				</p>
				<br>
				<p>
					<input type="text" required name="subject" id="subjectemaildelimit"
						placeholder="Subject">
				</p>
				<br>
				<p class="textareas">
					<textarea type="textarea" required name="textarea"
						id="textareaemaildelimit" rows="5" cols="70">This is Invoice Generator, Thank you........
  </textarea>
				</p>
			</div>
			<div class="modal-footer">
				<button class="btn" data-dismiss="modal" aria-hidden="true">Close</button>
				<button type="button" id="sendemail" class="btn btn-primary send"
					data-dismiss="modal" onclick="generatedelimitpdfForMail(this.id)">Send Email</button>
			</div>
		</form>
	</div>

	<!-- MODAL FOR EMAIL EDITOR ENDED -->
	
</body>
</html>


<!-- 
	------------------PREVIOUS CODE---------------------
<body >
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