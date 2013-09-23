<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>


<!-- <link rel="stylesheet" type="text/css" href="../bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="/bootstrap/css/bootstrap.css">
<script type="text/javascript" src="/js/jquery-1.7.2.min.js"></script>
<link rel="stylesheet" type="text/css" href="/bootstrap/css/style.css" />

<script type="text/javascript" src="/js/common.js"></script>
<script type="text/javascript" src="/js/loadDetails.js"></script> -->
<script type="text/javascript" src="/js/jquery-1.7.2.min.js"></script>
<link rel="stylesheet" type="text/css" href="../bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="/bootstrap/css/bootstrap.css">
<script type="text/javascript" src="/js/jquery-1.7.2.min.js"></script>
<link rel="stylesheet" type="text/css" href="/bootstrap/css/style.css" />
<link rel="stylesheet" type="text/css" href="../font-awesome/css/font-awesome-ie7.min.css">
<link rel="stylesheet" type="text/css" href="../font-awesome/css/font-awesome.min.css">
<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />

<!-- <link rel="stylesheet" type="text/css" href="../bootstrap/css/style.css" /> -->
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
<script type="text/javascript" src="/js/jquery.dataTables.js"></script>
<script type="text/javascript" src="/js/jquery.dataTables.min.js"></script>
<!-- <script type="text/javascript" src="/js/jquery-latest.js"></script> 
<script type="text/javascript" src="/js/jquery.tablesorter.js"></script>  -->
<script type="text/javascript" src="../bootstrap/js/bootstrap.min.js"></script>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">


<script type="text/javascript" src="/js/loadDetails.js"></script>
<script type="text/javascript" src="/js/ajaxfileupload.js"></script>

<script type='text/javascript'>

$('#date_entry').on('mouseleave',function(){
	alert("left");
});
var numberForSummary=1;
var numberForUser=1;


//HARIKA
/* function addFour(){
	 
	 $('#removeForUser').show();
	 numberForSummary=numberForSummary+1;
	 var content="";
	 content+='<span  id="spanSummaryId'+numberForSummary+'\"><span id="date_copy_entry"class="pull-left"><li ><input placeholder="PlaceHolder1" type="text"></li></span><span class="pull-left"> <li><input  placeholder="PlaceHolder2"  type="text"></li></span><span class="pull-left"><li><input placeholder="PlaceHolder3"  type="text"></li></span><span class="pull-left"> <li><input placeholder="PlaceHolder4"  type="text"></li></span></span>';
	 
	 $('#addFourFields').append(content);
	}
	function addFive(){
	 $('#removeForSummary').show();
	 numberForUser=numberForUser+1;
	 var content="";
	 content+='<span id="spanUserId'+numberForUser+'\"><span class="pull-left"><li ><input class="resizeInput" placeholder="PlaceHolder1" type="text"></li></span><span class="pull-left"> <li><input class="resizeInput"  placeholder="PlaceHolder2"  type="text"></li></span><span class="pull-left"><li><input placeholder="PlaceHolder3" class="resizeInput" type="text"></li></span><span class="pull-left"> <li><input class="resizeInput" placeholder="PlaceHolder4"  type="text"></li></span><span class="pull-left"><li ><input class="resizeInput" placeholder="PlaceHolder5" type="text"></span>';

	 $('#addFiveFields').append(content);
	 
	}

	function removeFour(){
	 $('#spanSummaryId'+numberForSummary).remove();
	 numberForSummary=numberForSummary-1;
	 if(numberForSummary==1){
	  $('#removeForUser').hide();
	 }
	}
	function removeFive(){
	 $('#spanUserId'+numberForUser).remove();
	 numberForUser=numberForUser-1;
	 if(numberForUser==1){
	  $('#removeForSummary').hide();
	 }
	}
 */
 
 function isNumberKey1(evt)
 {
    var charCode = (evt.which) ? evt.which :event.keyCode
    if (charCode > 31 && (charCode < 48 || charCode > 57 || charCode == 189 || charCode== 190))
       return false;
    return true;
 }
 function isNumberKey(evt)
 {
    var charCode = (evt.which) ? evt.which :event.keyCode;
    if (charCode > 31 && ((charCode!= 47 && charCode< 45)|| charCode > 57))
       return false;
    return true;
 }
 
 
 
 //MALLIKA
 function addFour(){
	 
	 
	 $('#removeForUser').show();
	 numberForSummary=numberForSummary+1;
	 var content="";
	 content+='<span  id="spanSummaryId'+numberForSummary+'\"><span id="date_copy_entry"class="pull-left"><li ><input placeholder="PlaceHolder1" type="text"></li></span><span class="pull-left"> <li><input  placeholder="PlaceHolder2"  type="text"></li></span><span class="pull-left"><li><input placeholder="PlaceHolder3"  type="text"></li></span><span class="pull-left"> <li><input placeholder="PlaceHolder4"  type="text"></li></span></span>';
	 
	 $('#addFourFields').append(content);
	}
	function addFive(){
	 var value=$('#date_entry').val();
	 $('#removeForSummary').show();
	 numberForUser=numberForUser+1;
	 var content="";
	 content+='<span id="spanUserId'+numberForUser+'\"><span class="pull-left"><li ><input class="resizeInput" value="'+value+'\"placeholder="PlaceHolder1" type="text"></li></span><span class="pull-left"> <li><input class="resizeInput"  placeholder="PlaceHolder2"  type="text"></li></span><span class="pull-left"><li><input placeholder="PlaceHolder3" class="resizeInput" type="text"></li></span><span class="pull-left"> <li><input class="resizeInput" placeholder="PlaceHolder4"  type="text"></li></span><span class="pull-left"><li ><input class="resizeInput" placeholder="PlaceHolder5" type="text"></span>';

	 $('#addFiveFields').append(content);
	 
	}

	function removeFour(){
	 $('#spanSummaryId'+numberForSummary).remove();
	 numberForSummary=numberForSummary-1;
	 if(numberForSummary==1){
	  $('#removeForUser').hide();
	 }
	}
	function removeFive(){
	 $('#spanUserId'+numberForUser).remove();
	 numberForUser=numberForUser-1;
	 if(numberForUser==1){
	  $('#removeForSummary').hide();
	 }
	}
	
	//Changed by Mallika
	$('#answerConnect').live('click',function(){
		 $("#fromAddress").html("<h5>AnswerConnect,<br>P.O. Box 80040,<br>Portland,OR,<br>972801040405</h5>");
		 $("#fromAddressText").val("AnswerConnect,P.O. Box 80040,Portland, OR,972801040405");
  $('#imageForLogo').attr('src','/bootstrap/img/ansc.png');
   $('#navigationForLogo').children().each(function() {
     var kid = $(this);
     if (kid.hasClass('active'))
      $(this).removeClass('active');

    });
  $(this).addClass('active');
 
  
 });
 $('#answerForce').live('click',function(){
	 $("#fromAddress").html("<h5>Answer Force,<br>P.O. Box 2115,<br>Tualatin, OR,<br>970622115151</h5>");
	 $("#fromAddressText").val("Answer Force,P.O. Box 2115,Tualatin, OR,970622115151");
  $('#imageForLogo').attr('src','/bootstrap/img/af.png');
   $('#navigationForLogo').children().each(function() {
     var kid = $(this);
     if (kid.hasClass('active'))
      $(this).removeClass('active');

    });
  $(this).addClass('active');
 
 });
 $('#memoSent').live('click',function(){
	 $("#fromAddress").html("<h5>Memosent,<br>P.O. Box 3304,<br>Tualatin, OR,<br>970623304042</h5>");
	 $("#fromAddressText").val("Memosent,P.O. Box 3304,Tualatin, OR,970623304042");
  $('#imageForLogo').attr('src','/bootstrap/img/memo2.png');
  $('#imageForLogo').attr('style','height:37px');
 
   $('#navigationForLogo').children().each(function() {
     var kid = $(this);
     if (kid.hasClass('active'))
      $(this).removeClass('active');

    });
  $(this).addClass('active');
 
 });
 $('#lexReception').live('click',function(){
	 $("#fromAddress").html("<h5>Lex Reception,<br>P.O. Box 80040,<br>Portland, OR,<br>972801040405</h5>"); 
	 $("#fromAddressText").val("Lex Reception,P.O. Box 80040,Portland, OR,972801040405");
  $('#imageForLogo').attr('src','/bootstrap/img/lex2.png');
   $('#navigationForLogo').children().each(function() {
     var kid = $(this);
     if (kid.hasClass('active'))
      $(this).removeClass('active');

    });
  $(this).addClass('active');
 
 });
 
 $(function() {
     $('.datepicker').datepicker({
       changeMonth: true,
       changeYear: true,
       //dateFormat: "dd/mm/yy"
     });
   });
 
 $(document).ready(function(){
	  $('.float').keypress(function(event) {
	   if(event.which < 46|| event.which > 59) {
	          event.preventDefault();
	      } // prevent if not number/dot

	      if(event.which == 46 && $(this).val().indexOf('.') != -1) {
	          event.preventDefault();
	      } // prevent if already dot
	  }); 
	 });
 
 //Changes over

 </script>
<!-- <script type="text/javascript">
 function test()
{
	alert("ajax call started");
	
	$.ajax({
		url:"/test",
		type:"POST",
		data : "hello",
			 contentType: "application/json",
			  beforeSend: function ( jqXHR )
			  {
			   jqXHR.setRequestHeader("Connection", "close");
			  },
			  processData: false,
			   success: function response(resultObject)
			  {

			  },
			  dataType: ''
	});
	alert("ajax call ended");
	} 
</script> -->






<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Custom Invoice</title>
</head>
<body >

<center>
 
<!-- <div class="individualDiv" style="display:none" overflow-y:auto;height:182px; width:1170px;">
</br></br>

<table class="tableIndividual" style="display:none">
<thead id="headersIndividual">
<tr  bgcolor="#DCDCDC"><td >Account Number</td><td>Invoice Date</td><td>Date Due</td><td>TD </td><td>PR </td><td>DC </td><td>BF </td> <td>CC </td> <td>TDB </td> <td>TDA </td> <td>SummOfCharg </td> <td>TotalCharg </td> <td>FromAdd </td> <td>AccName </td><td>SummOfCharg(pg2) </td><td>UserIdDetails </td><td>PDF DOWNLOAD</td></tr></thead>
<tbody id="bodyIndividual" >

</tbody>
</table>
</div> -->


<div class="individualDiv" style="display:none" overflow-y:auto;height:182px; width:1170px;">
</br></br>

<table class="tableIndividual" style="display:none">
<thead id="headersIndividual">
<!-- <tr  bgcolor="#DCDCDC"><td >Account Number</td><td>Phone No</td><td>Invoice Date</td><td>Date Due</td><td>Address</td><td>TD </td><td>2</td><td>PR </td><td>DC </td><td>5</td><td>6</td><td>BF </td> <td>CC </td> <td>TDB </td> <td>TDA </td> <td>SummOfCharg </td> <td>TotalCharg </td> <td>FromAdd </td> <td>AccName </td><td>SummOfCharg(pg2)</td><td> UserIdDetails </td><td>PDF DOWNLOAD</td></tr> -->
<tr bgcolor="#DCDCDC">
							<td>Account Number</td>
							<td>Phone Number</td>
							<td>Invoice Date</td>
							<td>Date Due</td>
							<td>Address</td>
							<td>TD</td>
							<td>ALS</td>
							<td>PR</td>
							<td>DC</td>
							<td>U</td>
							<td>U</td>
							<td>BF</td>
							<td>CC</td>
							<td>TDB</td>
							<td>TDA</td>
							<td>Summary of charges</td>
							<td>TCC </td>
							<td>From Address</td>
							<td>Account Name</td>
							<td>Summary of charges(page2)</td>
							<td>UserId Summary</td>
							<td>Send Mail</td>
							<td>Generate Pdf</td>
						</tr>
</thead>
<tbody id="bodyIndividual" >

</tbody>
</table>
</div>



 <!-- PDF GENERATE CODE -->
<!-- <form id="frmReportIframe" name="frmReportIframe" target="pdfdownloadIframe" action="/admin/generatepdf" method="post">
    <input id="allStaffDetailsJson" name="allStaffDetailsJson" type="hidden" value="" />
    <input id="btnSubmit" name="btnSubmit" type="button" style="display:none;" />
   </form>
<iframe style="display: none" id="pdfdownloadIframe" name="pdfdownloadIframe"></iframe>
 -->

<form id="frmReportIframe" name="frmReportIframe"
		target="pdfdownloadIframe" action="/admin/generatepdf" method="post">
		<input id="allStaffDetailsJson" name="allStaffDetailsJson"
			type="hidden" value="" />
			<input id="mapWithCurrentRecordJson" name="mapWithCurrentRecordJson"
			type="hidden" value="" /> 
			 <input id="btnSubmit" name="btnSubmit"
			type="button" style="display: none;" />
	</form>
	<iframe style="display: none" id="pdfdownloadIframe"
		name="pdfdownloadIframe"></iframe>



<!--  <input type="button" value="test" onclick="return test()"/>  -->
<div class="container">
<!-- Changes by Mallika -->
<ul class="nav nav-tabs" id='navigationForLogo'>
  <li class="active" id='answerConnect'><a>Answer Connect</a></li>
  <li id='answerForce'><a>Answer Force</a></li>
  <li id='memoSent'><a>Memo Sent</a></li>
  <li id='lexReception'><a>Lex Reception</a></li>
  <img id='imageForLogo' class='pull-right' src='/bootstrap/img/ansconnlogo.gif' height='100px' width='100px'/>
</ul>
<!-- Changes over -->
<table class="tableForDetails" ></table>
<form class="form-horizontal"  method="post">
<div>
<h4 class='heading'><span>Statement Summary </span><span class='pull-right' style='margin-right:37%'>Address</span></h4>
<span class="summary">
  <div class="control-group">
    <label class="control-label" >Account Number</label>
    <div class="controls">
      <input type="text" id="acc_no" name="acc_no" placeholder="Account Number" onkeypress="return isNumberKey1(event)">
    </div>
  </div>
  <div class="control-group">
    <label class="control-label" >Phone Number</label>
    <div class="controls">
      <input type="text" id="phone_number" name="phone_number" placeholder="Phone Number" onkeypress="return isNumberKey(event)">
    </div>
  </div>
  <div class="control-group">
    <label class="control-label" >Invoice Date</label>
    <div class="controls">
      <input type="text"  class="datepicker" id="invoice_date" name="invoice_date" placeholder="Invoice Date">
    </div>
  </div>
  <div class="control-group">
    <label class="control-label" >Date Due</label>
    <div class="controls">
      <input type="text" class="datepicker" id="date_due" name="date_due" placeholder="Date Due">
    </div>
  </div>
  <div class="control-group">
    <label class="control-label" >Total Due</label>
    <div class="controls">
      <input type="text" id="total_due" name="total_due" class="float" placeholder="Total Due" value="$">
    </div>
  </div>
  <div class="control-group">
    <label class="control-label" >Amount Enclosed</label>
    <div class="controls">
      <input type="text" id="amount_enclosed" name="amount_enclosed" class="float" placeholder="Amount Enclosed">
    </div>
  </div>
   <div class="control-group" style="margin-left: 449px;margin-top: -241px;">
   <label class="control-label" style="margin-right: -151px;margin-top: -200px;" >To Address</label>
    <div class="controls">
     <textarea rows="4" cols="50" placeholder="Enter To address" style="margin-top: -18px;" id="toaddress">
</textarea>
    </div>
  </div>
  <div class="control-group" style="margin-left: 449px;margin-top: -241px;">
   <label class="control-label" style="margin-left: 2px;margin-top: -50px;">From Address</label>
    <div class="controls" id="fromAddress" style="margin-top: 30%; margin-left: 50%;"><h5>AnswerConnect,<br>P.O. Box 80040,<br>Portland, OR,<br>972801040405</h5></div>
	<input id="fromAddressText" name="fromAddressText" type="hidden" value="AnswerConnect,P.O. Box 80040,Portland, OR,972801040405" />

    </div>
  </div>
  
  
  </span>
<h4 class='heading'><span>Summary Of Account History </span><span class='pull-right' style='margin-right:25%'>Summary Of Charges</span></h4>  <span class='summary'>
  <div class="control-group">
    <label class="control-label" >Payments Received</label>
    <div class="controls">
      <input type="text" id="payment_rec" name="payment_rec" class="float" placeholder="Payments Received" >
    </div>
  </div>
  
  <div class="control-group">
    <label class="control-label" >Debits and Credits</label>
    <div class="controls">
      <input type="text" id="debit_credit" name="debit_credit" class="float" placeholder="Debits and Credits" >
    </div>
  </div>
   <div class="control-group">
    <label class="control-label" >Balance Forward</label>
    <div class="controls">
      <input type="text" id="bal_fwd" name="bal_fwd" class="float" placeholder="Balance Forward" >
    </div>
  </div>
   <div class="control-group">
    <label class="control-label" >Current Charges</label>
    <div class="controls">
      <input type="text" id="curent_charge" name="curent_charge" class="float" placeholder="Current Charges" >
    </div>
  </div>
   <div class="control-group">
    <label class="control-label" >Total Due By</label>
    <div class="controls">
      <input type="text" id="total_due_by" name="total_due_by" class="float" placeholder="Total Due By" >
    </div>
  </div>
   <div class="control-group">
    <label class="control-label" >Total Due After</label>
    <div class="controls">
      <input type="text" id="total_due_after" name="total_due_after" class="float" placeholder="Total Due After" >
    </div>
  </div>

<!--    HERE THE SUBMIT BUTTON 

 <input class='btn btnModified' type="button" onclick="saveDetails()" value="Submit"></input> -->


  </span>
  
  <!-- <span><h4  class='heading'>Summary of Charges</h4></span> -->
  <!-- <div class="summary" style="margin-top: -319px;margin-right: 5px;"> -->
  <div class="summary" style="margin-top: -319px;margin-right: 5px;height:300px;">
   <div class="control-group">
    <label class="control-label" >Plan Overage Charges</label>
    <div class="controls">
      <input type="text" id="poc" name="poc" class="float" placeholder="Plan Overage Charges" >
    </div>
  </div>
   <div class="control-group">
    <label class="control-label" >Other Charges</label>
    <div class="controls">
      <input type="text" id="ocharges" name="ocharges" class="float" placeholder="Other Charges" >
    </div>
  </div>
   <div class="control-group">
    <label class="control-label" >Total Current Charges</label>
    <div class="controls">
      <input type="text" id="tcc" name="tcc" class="float" placeholder="Total Current Charges" >
    </div>
 </div>
  </div>
  
  <!-- HARIKA -->
  <!-- 
      <div class='FourDiv' >
  <h4 class='heading'><span>Summary Of Charges </span><span class='pull-right' style='margin-right:25%'></span></h4>  

  <span class='summaryNew' >

   <ul class="addFiveFields" id='addFiveFields'  style="height:200px;overflow-y:auto;" >
		<span id='spanSummaryId1'>
            
             <span class='pull-left' > <li ><input class='resizeInput' placeholder="PlaceHolder1" type="text" ></li></span>
         	<span class='pull-left'> <li><input class='resizeInput' placeholder="PlaceHolder2" type="text"></li></span>
            <span class='pull-left'> <li><input class='resizeInput' placeholder="PlaceHolder3"  type="text"></li></span>
            <span class='pull-left'> <li><input class='resizeInput' placeholder="PlaceHolder4" type="text"></li></span>
            <span class='pull-left'> <li><input class='resizeInput' placeholder="PlaceHolder5" type="text"></li></span>
             <span class='addIcon'> <i class='icon-plus pull-right' onclick="addFive()"></i><i style='display:none' id='removeForSummary' class='icon-minus' onclick="removeFive()"></i></span>
             
        </span>
        
    </ul>


</span>
</div>

 <div class='FiveDiv' height='auto'>
  <h4 class='heading'><span>User ID Summary</span><span class='pull-right' style='margin-right:25%'></span></h4>  
 
 
  <span class='summaryUser'>

   <ul class="addFourFields" id='addFourFields'>
		<span id='spanUserId1'>
            <span class='pull-left' > <li ><input placeholder="PlaceHolder1"  type="text"></li></span>
         	<span class='pull-left'> <li><input placeholder="PlaceHolder2" type="text"></li></span>
            <span class='pull-left'> <li><input placeholder="PlaceHolder3"  type="text"></li></span>
            <span class='pull-left'> <li><input placeholder="PlaceHolder4" type="text"></li></span>
        </span>
    </ul>
   

</span>
</div>

<span class='addIcon'> <i class= 'icon-plus' onclick="addFour()"></i><i style='display:none' id='removeForUser' class='icon-minus' onclick="removeFour()"></i></span>
  </div>


-->


<!-- MALLIKA -->

 <div class='FourDiv' >
  <h4 class='heading'><span>Summary Of Charges </span><span class='pull-right' style='margin-right:25%'></span></h4>  

  <span class='summaryNew' >

   <ul class="addFiveFields" id='addFiveFields'  style="height:200px;overflow-y:auto;" >
  <span id='spanSummaryId1'>
            
             <span class='pull-left' > <li ><input class='resizeInput datepicker'  id='date_entry'  placeholder="Date"  type="text"></li></span>
          <span class='pull-left'> <li><input class='resizeInput' placeholder="Summary" type="text"></li></span>
            <span class='pull-left'> <li><input class='resizeInput' placeholder="Summary"  type="text"></li></span>
            <span class='pull-left'> <li><input class='resizeInput'  placeholder="Nom" type="text" onkeypress="return isNumberKey1(event)" ></li></span>
            <span class='pull-left'> <li><input class='resizeInput' class="float" placeholder="Totals" type="text" ></li></span>
             <span class='addIcon'> <i class='icon-plus pull-right' onclick="addFive()"></i><i style='display:none' id='removeForSummary' class='icon-minus' onclick="removeFive()"></i></span>
             
        </span>
        
    </ul>


</span>
</div>

 <div class='FiveDiv' height='auto'>
  <h4 class='heading'><span>User ID Summary</span><span class='pull-right' style='margin-right:25%'></span></h4>  
 
 
  <span class='summaryUser'>

   <ul class="addFourFields" id='addFourFields'>
  <span id='spanUserId1'>
            <span class='pull-left' > <li ><input placeholder="Description"  type="text"></li></span>
          <span class='pull-left'> <li><input  placeholder="No. of Calls" type="text" onkeypress="return isNumberKey1(event)"></li></span>
            <span class='pull-left'> <li><input class="float" placeholder="Minutes"  type="text" ></li></span>
            <span class='pull-left'> <li><input class="float" placeholder="Amount" type="text" ></li></span>
        </span>
    </ul>
   

</span>
</div>

<span class='addIcon'> <i class= 'icon-plus' onclick="addFour()"></i><i style='display:none' id='removeForUser' class='icon-minus' onclick="removeFour()"></i></span>
  </div>

 
  <br/>  <br/>  <br/>
 
  <!--   HERE THE SUBMIT BUTTON -->

 <input class='btn btnModified' type="button" onclick="saveDetails()" value="Submit" style='margin-left:-4%; margin-top:-6%'></input>
 
</form>

  </div>
  </center>
  
 
  
 <div id="myModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="false"style="height:70%;">
  <div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
    <h3 id="myModalLabel">New Message</h3>
   </div>
   
   <div class="modal-body">
  Enter a valid Email id:<br>
  <p><input type="email" required name="Email" id="emailid" placeholder="test@gmail.com" ></p><br>
  <p><input type="text" required name="subject" id=subjectemail placeholder="Subject"></p><br>
  <textarea type="textarea" class="email" required name="textarea" id="textareaemail" rows="5" cols="70">This is Invoice Generator, Thank you........
  </textarea>
  </div>
  <div class="modal-footer">
    <button class="btn" data-dismiss="modal" aria-hidden="true">Close</button>
    <button  type="button" id="sendemail" class="btn btn-primary send" data-dismiss="modal" onclick="generatepdfForMail(this.id)" >Send Email</button>
  </div>
  
</div>
  
  
  
  
</body>
</html>