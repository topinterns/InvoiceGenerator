 var reader; 

    /**
     * Check for the various File API support.
     */
    function checkFileAPI() {
        if (window.File && window.FileReader && window.FileList && window.Blob) {
        reader = new FileReader();
         return true;
        } else {
          alert('The File APIs are not fully supported by your browser. Fallback required.');
          return false;
        }
     }

    /**
     * read text input
     */
    var fileContent;
    var filepath;
    
    function readText(filePath) {
    filepath=filePath;
    var output = ""; //placeholder for text output
    if(filePath.files && filePath.files[0]) {
    reader.onload = function (e) {
    output = e.target.result;
    displayContents(output);
    };//end onload()
    reader.readAsText(filePath.files[0]);
    }//end if html5 filelist support
    else if(ActiveXObject && filePath) { //fallback to IE 6-8 support via ActiveX
    try {
    reader = new ActiveXObject("Scripting.FileSystemObject");
    var file = reader.OpenTextFile(filePath, 1); //ActiveX File Object
    output = file.ReadAll(); //text contents of file
    file.Close(); //close file "input stream"
    displayContents(output);
            } catch (e) {
                if (e.number == -2146827859) {
                    alert('Unable to access local files due to browser security settings. ' +
                     'To overcome this, go to Tools->Internet Options->Security->Custom Level. ' +
                     'Find the setting for "Initialize and script ActiveX controls not marked as safe" and change it to "Enable" or "Prompt"');
                }
            }
        }
        else { //this is where you could fallback to Java Applet, Flash or similar
            return false;
        }
        return true;
    }

    /**
     * display content using a basic HTML replacement
     */
    
     var arrayForLine = new Array();
     var arrayFields = new Array();
     var mapContainsFields={};
     var mapContainsMap={};
     
     function displayContents(txt) {
        arrayForLine= txt.toString().split("\n");
        for(var i=0;i<arrayForLine.length-1;i++)
        {
        	//alert("This is alert in iteration")
        arrayFields = arrayForLine[i].split("\t");
        sendingtoMapContainsFields(arrayFields);
        }
        // the function for uploading data into a table
        delimitUploading(mapContainsMap);
        // for to check the data in inspect - //console
   	}


     // the function calling for uploading data into a table
     function delimitUploading(mapContainsMap)
     {
     	//alert("mapContainsMap");
 	     	var tableDelimitData="";
 	     $('#invoiceHeaders').show();
	     var SOC2string="";
	     var UISstring="";
	     var SOCstring="";
	     for(ind in mapContainsMap){
	     tableDelimitData+='<tr><td>'+mapContainsMap[ind].accountNumber+'</td><td>'+mapContainsMap[ind].phoneNumber+'</td><td>'+mapContainsMap[ind].invoiceDate+'</td><td>'+mapContainsMap[ind].dateDue+'</td><td>'+mapContainsMap[ind].address+'</td>';
	     for(index in mapContainsMap[ind].TotalDue){
	     	tableDelimitData+='<td>'
	     	tableDelimitData+=mapContainsMap[ind].TotalDue[index]
	     	tableDelimitData+='</td>'
	 	  }
	     
	     var SOCstring="";
	     var SOCstring1="";	       
	     	for(index in mapContainsMap[ind].SOCArray1)	{
	     		
	         SOCstring+=" "+mapContainsMap[ind].SOCArray1[index];
	     	}
	     	for(index in mapContainsMap[ind].SOCArray2){
	     		
	     	SOCstring1+=" "+mapContainsMap[ind].SOCArray2[index];
	     	}
	     	
	     	SOCstring+=" "+SOCstring1;
	     	////console.log(SOCstring);
	     
	     
	     tableDelimitData+='<td>'+SOCstring+'</td><td>'+mapContainsMap[ind].totalCurrentCharges+'</td><td>'+mapContainsMap[ind].fromAddress+'</td><td>'+mapContainsMap[ind].accountName+'</td>';
	      var tempscr="";
	      for(index in mapContainsMap[ind].summaryOfCharges2){
	    	  tempscr += mapContainsMap[ind].summaryOfCharges2[index]+"  " ;
	                          }
	      SOC2string=tempscr;
		   //console.log(SOC2string);
		   var tempscr1="";
		  for(index in mapContainsMap[ind].userIdSummary){
		  tempscr1 += mapContainsMap[ind].userIdSummary[index]+"  " ;
	                           }
		  UISstring=tempscr1;
		  //console.log(UISstring);      
	     tableDelimitData+='<td>'+SOC2string+'</td><td>'+UISstring+'</td>';
	     tableDelimitData += '<td><a href="#myModalDelimit" onclick=generatedelimitpdfForMail('+ ind + ') data-toggle="modal" >Send an Email</a></td>';
	     $('.send').attr('id',ind);
	     tableDelimitData +='<td><a href="#" onclick=generatedelimitpdf('+ind+') >Download Pdf</a></td></tr>';		
	
	    // tableDelimitData+='<td><a href="#myModalDelimit" onclick=generatedelimitpdfForMail('+ind+') data-toggle="modal" >Send an Email</a></td>';
	    // tableDelimitData+='<td><a href="#" onclick=generatedelimitpdf('+ind+')  >Download Pdf </a></td></tr>';
	     
	     //$('.send').attr('id',ind); 
	     }
	     
	     $('#invoiceBody').html(tableDelimitData);
     }


    //Changes by Harika after HTML files 
    
    function sendingtoMapContainsFields(arrayFields)
    {
        var accountno = arrayFields[2];
        mapContainsFields["invoiceDate"]=arrayFields[0];
	    mapContainsFields["accountName"]=arrayFields[1];
	    mapContainsFields["accountNumber"]=arrayFields[2];
	    mapContainsFields["phoneNumber"]=arrayFields[3];
	    mapContainsFields["dateDue"]=arrayFields[17];
    mapContainsFields["totalCurrentCharges"]=arrayFields[20];
    
    //TotalDue 
    var totalDueArray=new Array();
    
    for(var j=0,i=6;i<16;i++,j++)
       totalDueArray[j]=arrayFields[i];
    mapContainsFields["TotalDue"]=totalDueArray;
   
                                                            //COPY ADDRESS AND GIVE TO MALLI
    //address
    var addressArray=new Array();
    var addressArray1=new Array();
    addressArray1=arrayFields[5].split("&");
    console.log(arrayFields[5]);
    var len;
    len=addressArray1.length;
    mapContainsFields["lengthOfAddress"]=len-1;
    //console.log(addressArray);
    
    
    for(var i=0;i<len;i++)
     {
     if(i==0)
     addressArray[i]=addressArray1[i].substring(1,addressArray1[0].length);
    
     else if(i>0 && i<len-1)
     addressArray[i]=addressArray1[i];
     
     else if(i<len && i>len-1)
     addressArray[i]=addressArray1[i].substring(0,addressArray1[0].length-1);
     
     }
     
    //console.log(addressArray);
    
    mapContainsFields["address"]=addressArray;
    
    //answerConnectAddress
    var fromAddressArray=new Array();
    fromAddressArray=arrayFields[16].split("&");
    mapContainsFields["fromAddress"]=fromAddressArray;
                                                             
                                                           //COPY SUMMARY OF CHARGES AND GIVE TO MALLI
    //summaryOfCharges 
    var SOCArray1=[];
    var SOCArray2=[];
    var sample=new Array();
    var dateReg=/^(0[1-9]|[12][0-9]|3[01])[- /.](0[1-9]|1[012])[- /.]\d\d+$/;
    
    //console.log(arrayFields[19]);
    sample=arrayFields[19].split("&");
    
    //console.log(arrayFields[2]);
    //console.log(sample);
    
    if(sample[0]=="Other Charges" || sample[0]==" Other Charges" && sample[2]=="")
    {
     SOCArray1[0]="Plan Overage Charges";
     SOCArray1[1]="0";
     
     SOCArray2[0]=sample[0];
     SOCArray2[1]=sample[1];
     
     mapContainsFields["SOCArray1"]=SOCArray1;
     mapContainsFields["SOCArray2"]=SOCArray2;
    }
    else if(sample[0]=="Plan Overage Charges" || sample[0]==" Plan Overage Charges" && sample[2]=="")
    {
     SOCArray1[0]=sample[0];
     SOCArray1[1]=sample[1];
     
     SOCArray2[0]="Other Charges";
     SOCArray2[1]="0";
     
     mapContainsFields["SOCArray1"]=SOCArray1;
     mapContainsFields["SOCArray2"]=SOCArray2;
     
    }
    else if(sample[0]=="Plan Overage Charges" || sample[0]==" Plan Overage Charges" && sample[2]=="Other Charges")
    {
     SOCArray1[0]=sample[0];
     SOCArray1[1]=sample[1];
     
     SOCArray2[0]=sample[2];
     SOCArray2[1]=sample[3];
     
     mapContainsFields["SOCArray1"]=SOCArray1;
     mapContainsFields["SOCArray2"]=SOCArray2;
     
    }
    else if(sample[0]=="" || sample[0]=="CALL DETAIL" || sample[0]=="INBOUND 8XX CALLS" || sample[0]==" INBOUND 8XX CALLS" || sample[0]==" CALL DETAIL")
     {
     SOCArray1[0]="Plan Overage Charges";
     SOCArray1[1]="0";
     
     SOCArray2[0]="Other Charges";
     SOCArray2[1]="0";
     
     mapContainsFields["SOCArray1"]=SOCArray1;
     mapContainsFields["SOCArray2"]=SOCArray2;
     }
    }
    
 
// Function for delimited (generatepdf) ----
    var i=0;
    var record="";
    function generatedelimitpdfForMail(currentRecord)
    {    
      i++;
      if(i==1){
      record = currentRecord;
      }
      if(i==2){
    	
      var email=$("#emailiddelimit").val(); 
      var subject=$("#subjectemaildelimit").val();
      var body=""; 
      body=$("#textareaemaildelimit").val();
      //alert( "Thisis currentRecord "+record+"  email id ::= "+email+"  subject  "+subject+"  body "+body);
    	   
    	  
    	   var mapForEmail={};
    	   mapForEmail["currentRecordDelimit"]=record;
    	   mapForEmail["emailIdDelimit"]=email;
    	   mapForEmail["subjectDelimit"]=subject;
    	   mapForEmail["bodyDelimit"]=body;
    	   	   
    	   
    //console.info(JSON.stringify(mapForEmail));
          
    var re = /\S+@\S+\.\S+/;
    if(re.test(email))
    {
    	//alert("The mail id is checking")
    	
     var frm,iput;
     //iput = document.getElementById('allStaffDetailsJson').value = record+";"+email+";"+subject+";"+stringBody;
    // document.getElementById('allStaffDetailsJson').value ="testststsetestsetes";
     $('#allStaffDetailsJsondelimit').val(JSON.stringify(mapForEmail));     
     $('#mapForDelimitJson').val(JSON.stringify(mapContainsMap[record]));
     frm = document.getElementById("frmReportIframedelimit");
     
     frm.action="/admin/generatedelimitpdf";
     frm.method="post";
     frm.target="pdfdownloadIframedelimit";
     frm.submit();
     
     i=0;

     $("#emailiddelimit").val("");
    // alert("emailid cleared");
     $("#subjectemaildelimit").val("");
     $("#textareaemaildelimit").val("This is Invoice Generator, Thank you........");
    
     
    }
    else{
    	 alert("Enter valid emailid");
    	i=0;
    	 $("#emailiddelimit").val("");
    	 $("#subjectemaildelimit").val("");
    	 $("#textareaemaildelimit").val("This is Invoice Generator, Thank you........");
         }

     }
    /*  alert("Your Message has been sent successfully.");*/
    }

    function generatedelimitpdf(currentRecord)
    {    
      record = currentRecord;
      var email="NA"; 
      var subject="NA";
      var body="NA"; 
    	  
    	   var mapForEmail={};
    	   mapForEmail["currentRecordDelimit"]=record;
    	   mapForEmail["emailIdDelimit"]=email;
    	   mapForEmail["subjectDelimit"]=subject;
    	   mapForEmail["bodyDelimit"]=body;
    	   	   
    //console.info(JSON.stringify(mapForEmail));
          
     var frm,iput;
     $('#allStaffDetailsJsondelimit').val(JSON.stringify(mapForEmail));     
     $('#mapForDelimitJson').val(JSON.stringify(mapContainsMap[record]));
     frm = document.getElementById("frmReportIframedelimit");
     
     frm.action="/admin/generatedelimitpdf";
     frm.method="post";
     frm.target="pdfdownloadIframedelimit";
     frm.submit();
     
      alert("Your Message has been sent successfully.");
    }

    function generatepdf(currentRecord)
    {    
    	   var mapForEmail={};
    	   mapForEmail["currentRecord"]=currentRecord;
    	   mapForEmail["emailId"]="NA";
    	   mapForEmail["subject"]="NA";
    	   mapForEmail["body"]="NA";
    	   	   
    //console.info(JSON.stringify(mapForEmail));
     var frm,iput;
     $('#allStaffDetailsJson').val(JSON.stringify(mapForEmail));
     frm = document.getElementById("frmReportIframe");
     frm.action="/admin/generatesinglepdf";
     frm.method="post";
     frm.target="pdfdownloadIframe";
     frm.submit();
    }

var i=0;
var record="";
function generatepdfForMail (currentRecord)
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
	   mapForEmail["currentRecord"]=record;
	   mapForEmail["emailId"]=email;
	   mapForEmail["subject"]=subject;
	   mapForEmail["body"]=body;
	   	   
//console.info(JSON.stringify(mapForEmail));
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
 $("#textareaemail").val("This is Invoice Generator, Thank you........");

 
}
else{
	alert("Enter valid emailid");
	 var s="malli";
	//console.log("variable value:"+s); 
	i=0;

	 $("#emailid").val("");
	 $("#subjectemail").val("");
	 $("#textareaemail").val("This is Invoice Generator, Thank you........");
}

 }
}




function paymentDetails()
{
	var accountNumber =$('#accountNumberP').val();
	var phoneNumber=$('#phoneNumberP').val();
	var invoiceDate=$('#invoiceDateP').val();
	var toAddressForm=$('#toAddressP').val();
	var fromAddressForm=$('#fromAddressText').val();
	var toAddressArray= new Array();
	toAddressArray=toAddressForm.split(",");

	var fromAddressArray= new Array();
	fromAddressArray=fromAddressForm.split(",");

	lengthOfToAddress=toAddressArray.length;
	lengthOfFromAddress=fromAddressArray.length;

	var summaryOfChargesArray=new Array();
	var i=0;
	var j=0;
	var summaryOfChargesString="";
	var arr= new Array();
	$('#addTwoFields li').each(function (){
	arr[i]=$(this).find('input').val();
	summaryOfChargesString += arr[i]+" ";
	i++;
	if(i==2){
		summaryOfChargesArray[j]=arr;
	j++;
	arr = new Array();
	i=0;
	}
	});
	//alert("hai from payment");
	//alert(accountNumber);
	//alert(phoneNumber);alert(invoiceDate);alert(fromAddressArray);alert(toAddressArray);
	//alert(summaryOfChargesArray);
	var dataToBeSent={};
	
	// storing in FINAL MAP - dataToBeSent

	dataToBeSent["accountNumberForm"]=accountNumber;
	dataToBeSent["phoneNumberForm"]=phoneNumber;
	dataToBeSent["invoiceDateForm"]=invoiceDate;

	dataToBeSent["lengthOfToAddressForm"]=lengthOfToAddress;
	dataToBeSent["lengthOfFromAddressForm"]=lengthOfFromAddress;

	//summaryOfCharges
	dataToBeSent["summaryOfChargesFormArray"]=summaryOfChargesArray;

	dataToBeSent["toaddressForm"]=toAddressArray;
	dataToBeSent["fromaddressForm"]=fromAddressArray;
	var parametersJSON =JSON.stringify(dataToBeSent) ;
	var frm,iput;
    //console.log(parametersJSON);
    $('#allStaffDetailsJson').val(parametersJSON);
   frm = document.getElementById("frmReportIframe");
   frm.action="/admin/paymentReceipt";
   frm.method="post";
   frm.target="pdfdownloadIframe";
   frm.submit();
	
	//alert(parametersJSON);
	/*$.ajax({
		   type: 'POST',
		   url: '/admin/paymentReceipt',
		   contentType: "application/json",
		   beforeSend: function ( jqXHR )
		   {
		    jqXHR.setRequestHeader("Connection", "close");
		   },
		   async: false,
		   data:parametersJSON ,
		    success: function response(resultObject)
		   {
		    //alert("resultObject "+resultObject);
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
		  });*/
	
	/*var table="";
	//alert(accountNumber);
	table +='<tr><td>'+accountNumber+'</td><td>'+phoneNumber+'</td><td>'+invoiceDate+'</td><td>'+toAddressForm+'</td><td>'+fromAddressForm+'</td><td>'+summaryOfChargesString+'</td>';
	table += '<td><a href="#myModal" onclick=generatepdf('+accountNumber+') data-toggle="modal" >Download Pdf & Send an Email</a></td></tr>'
	table+='<td><a href="#myModal" onclick=generatepdf("'+$('#acc_no').val()+'") data-toggle="modal" >Download Pdf & Send an Email</a></td></tr>';
	$('#inBody').html(table);*/
	
	
}





function saveDetails(){
	
	
	alert("hi from js");
		
	// First page
var accountNumber =$('#acc_no').val();
var phoneNumber=$('#phone_number').val();
var invoiceDate=$('#invoice_date').val();
var dateDue=$('#date_due').val();
var amountEnclosed=$('#amt_enclosed').val();
toaddressForm=$('#toaddress').val();
fromaddressForm=$('#fromAddressText').val();
//Amount Enclosed is not using in this code

var summaryOfChargesArray1=new Array();
var summaryOfChargesArray2=new Array();

var dataToBeSent={};
var individualArray=new Array();

var stringArray1=[1];
var stringArray2=[1];
var lengthOfAddress;
//var lengthOfIndividualArray;


var toaddressArray= new Array();
toaddressArray=toaddressForm.split(",");

var fromaddressArray= new Array();
fromaddressArray=fromaddressForm.split(",");

lengthOfToAddress=toaddressArray.length;
lengthOfFromAddress=fromaddressArray.length;

var accountName="";
accountName=toaddressArray[0];

var totalDue=$('#total_due').val();
individualArray[0]=totalDue;

individualArray[1]="0";
var paymentsReceived=$('#payment_rec').val();
individualArray[2]=paymentsReceived;

var debitsCredits=$('#debit_credit').val();
individualArray[3]=debitsCredits;

var balanceForward=$('#bal_fwd').val();
individualArray[6]=balanceForward;

individualArray[4]="0";
individualArray[5]="0"; 

var currentCharges=$('#curent_charge').val();
individualArray[7]=currentCharges;

var totalDueby=$('#total_due_by').val();
individualArray[8]=totalDueby;

var totalDueafter=$('#total_due_after').val();
individualArray[9]=totalDueafter;

//lengthOfIndividualArray=individualArray.length;
                                                                                                         //alert("individualArray "+individualArray);

var planOverage=$('#poc').val();
summaryOfChargesArray1[0]="Plan Overage Charges";
summaryOfChargesArray1[1]=planOverage;

var otherCharge=$('#ocharges').val();
summaryOfChargesArray2[0]="Other Charges";
summaryOfChargesArray2[1]=otherCharge;

var lengthOfSummaryOfChargesArray = summaryOfChargesArray2.length;


var summOfCharString="";
summOfCharString="PlanOverCharges "+planOverage+"\n"+"OtherCharges "+otherCharge;

var totalCurrentCharges="";
totalCurrentCharges=$('#tcc').val(); 
var totalCurrentChargesArray = new Array();
totalCurrentChargesArray[0]=totalCurrentCharges;



/*
//2nd page ArrayList-arrays

var arrayListForSummayOfCharges=new Array();
var arrayListSOCIndex2=new Array();
var i=0;
var j=0;
var k=0;
var l=0;
var m=0;
var n=0;
var summaryOfChargesString="";
var arr= new Array();
var arrIndex2= new Array();
$('#addFiveFields li').each(function (){
	i++;
	if(i==2)
		{
		arrIndex2[m]=$(this).find('input').val();
		
		arrayListSOCIndex2[l]=arrIndex2;
		l++;
		arrIndex2= new Array();
		}
if(i!=2){
arr[n]=$(this).find('input').val();
summaryOfChargesString +=arr[n]+" ";
n=n+1;
}
if(i==5){
	
	//dataToBeSent["arrForm"]=arr;
	arrayListForSummayOfCharges[j]=arr;
	summaryOfChargesString +="\n";
j++;
arr = new Array();
m=0;
i=0;
n=0;
}


});
*/


// 2nd page ArrayList-ArrayList
var arrayListForSummayOfCharges=new Array();
var i=0;
var j=0;
var m=0;
var n=0;
var summaryOfChargesString="";
var arr= new Array();
var arrIndex2= new Array();
$('#addFiveFields li').each(function (){
	i++;
	if(i==3)
		{
		arrIndex2[m]=$(this).find('input').val();
		summaryOfChargesString+=arrIndex2[m]+" ";
		}
if(i!=3){
arr[n]=$(this).find('input').val();
summaryOfChargesString +=arr[n]+" ";
n=n+1;
}
if(i==5){
	arrayListForSummayOfCharges[j]=arr;
	j=j+1;
	arrayListForSummayOfCharges[j]=arrIndex2;
	summaryOfChargesString +="\n";
	j=j+1;
arr = new Array();
arrIndex2= new Array();
m=0;
i=0;
n=0;
}
});



// 3rd page

var arrayListForUserIdSummary= new Array();
var i=0;
var j=0;
var userIdSummaryString="";
var arr= new Array();
$('#addFourFields li').each(function (){
arr[i]=$(this).find('input').val();
userIdSummaryString += arr[i]+" ";
i++;
if(i==4){
	arrayListForUserIdSummary[j]=arr;
j++;
arr = new Array();
i=0;
}

});





// storing in FINAL MAP - dataToBeSent

dataToBeSent["accountNumberForm"]=accountNumber;
dataToBeSent["phoneNumberForm"]=phoneNumber;
dataToBeSent["invoiceDateForm"]=invoiceDate;
dataToBeSent["dateDueForm"]=dateDue;

dataToBeSent["lengthOfToAddressForm"]=lengthOfToAddress;
dataToBeSent["lengthOfFromAddressForm"]=lengthOfFromAddress;
dataToBeSent["totalDueForm"]=individualArray;

//summaryOfCharges
dataToBeSent["summaryOfChargesFormArray1"]=summaryOfChargesArray1;
dataToBeSent["summaryOfChargesFormArray2"]=summaryOfChargesArray2;

//summaryOfCharges2 (page2)
dataToBeSent["summaryOfCharges2Form"]=arrayListForSummayOfCharges;

//totalCurrentCharges
dataToBeSent["totalCurrentChargesForm"]=totalCurrentCharges;

//alert("storing in js map");

//userIdSummary (page3)
dataToBeSent["userIdSummaryForm"]=arrayListForUserIdSummary;
dataToBeSent["toaddressForm"]=toaddressArray;
dataToBeSent["fromaddressForm"]=fromaddressArray;
dataToBeSent["accountNameForm"]=accountName;


//alert(" The values of 1st page  accountNumber="+accountNumber+"&phoneNumber="+phoneNumber+"&invoiceDate="+invoiceDate+"&dateDue="+dateDue+"&totalDue="+totalDue+"&amountEnclosed="+amountEnclosed+"&paymentsReceived="+paymentsReceived+"&debitsCredits="+debitsCredits+"&balanceForward="+balanceForward+"&currentCharges="+currentCharges+"&totalDueby="+totalDueby+"&totalDueafter="+totalDueafter+"&planOverage="+planOverage+"&otherCharges="+otherCharges+"&totalCurrentCharges="+totalCurrentCharges);


           //validations  
if(accountNumber.length==0){
	alert("Account Number is empty");
	return false;
}
else if(phoneNumber.length==0)
{
	alert("phone number is empty");
	return false;
}
else if(invoiceDate.length==0){
		alert("invoice date is empty");
return false;
}
	else if(dateDue.length==0){
	alert("date due is empty");
return false;
	}
else if(totalDue.length==0){
	alert("total due is Empty");
	return false;
	}
else if(paymentsReceived.length==0){
	alert("Payments Received is Empty");
	return false;
	}
else if(debitsCredits.length==0){
;	alert("Debits & Credits is Empty");
	return false;
	}
else if(balanceForward.length==0){
	alert("Balance Forw;ard is Empty");
	return false;
	}
else if(currentCharges.length==0){
	alert("Current Charges is Empty");
	return false;
	}
else if(totalDueby.length==0){
	alert("total due by is Empty");
	return false;
	}
else if(totalDueafter.length==0){
	alert("total due after is Empty");
	return false;
	}
else if(planOverage.length==0){
	alert("plan Overage Charges is Empty");
	return false;
	}
else if(otherCharge.length==0){
	alert("Other Charges is Empty");
	return false;
	}
else if(totalCurrentCharges.length==0){
	alert("Total Current Charges is Empty");
	return false;
	}
else
	alert("Your Details are successfully submitted..");




//storing in Table 

//alert("This is the Individual table function");

$('.individualDiv').show();
$('.tableIndividual').show();
$('#headersIndividual').show();

var replaceThisString="";
replaceThisString =summaryOfChargesString+"\n"+userIdSummaryString;
var table="";
//alert(accountNumber);
table +='<tr><td>'+accountNumber+'</td><td>'+phoneNumber+'</td><td>'+invoiceDate+'</td><td>'+dateDue+'</td><td>'+toaddressForm+'</td><td>'+totalDue+'</td><td>'+0+'</td><td>'+paymentsReceived+'</td><td>'+debitsCredits+'</td><td>'+0+'</td><td>'+0+'</td><td>'+balanceForward+'</td><td>'+currentCharges+'</td><td>'+totalDueby+'</td><td>'+totalDueafter+'</td><td>'+summOfCharString+'</td><td>'+totalCurrentCharges+'</td><td>'+fromaddressForm+'</td><td>'+accountName+'</td><td>'+summaryOfChargesString+'</td><td>'+userIdSummaryString+'</td>';
/*table += '<td><a href="#myModal" onclick=generatepdf('+accountNumber+') data-toggle="modal" >Download Pdf & Send an Email</a></td></tr>'*/
table+='<td><a href="#myModal" onclick=generatepdfForMail ("'+$('#acc_no').val()+'") data-toggle="modal" >Send an Email</a></td>';
table+='<td><a href="#" onclick=generatepdf("'+$('#acc_no').val()+'")>Download pdf</a></td></tr>';
$('#bodyIndividual').html(table);




     // calling ajax to set in map in controller
//alert("ajax call started");

var parametersJSON =JSON.stringify(dataToBeSent) ;
//alert(parametersJSON);
$.ajax({
	   type: 'POST',
	   url: '/admin/ShowDetailsSubmitted',
	   contentType: "application/json",
	   beforeSend: function ( jqXHR )
	   {
	    jqXHR.setRequestHeader("Connection", "close");
	   },
	   async: false,
	   data:parametersJSON ,
	    success: function response(resultObject)
	   {
	    //alert("resultObject "+resultObject);
//	    	taskQueueStatus=resultObject;
//	    	if(taskQueueStatus){
//	    		getUploadedData();
//	    	}
//	    	else{
//	    		alert("Processing... please wait ")
//	    		//setTimeout(checkTaskQueueStatus, 5000);
//	    	}

	   },
	   dataType: ''
	  });

//alert("ajax call ended");



/*
// storing in Table 

alert("This is the Individual table function");

$('.individualDiv').show();
$('.tableIndividual').show();
$('#headersIndividual').show();

var table="";
table='<tr><td>'+accountNumber+'</td><td>'+invoiceDate+'</td><td>'+dateDue+'</td><td>'+totalDue+'</td><td>'+paymentsReceived+'</td><td>'+debitsCredits+'</td><td>'+balanceForward+'</td><td>'+currentCharges+'</td><td>'+totalDueby+'</td><td>'+totalDueafter+'</td><td>'+summOfCharString+'</td><td>'+floatArray[0]+'</td><td>'+addressForm+'</td><td>'+accountName+'</td><td>'+summaryOfChargesString+'</td><td>'+userIdSummaryString+'</td>';
table += '<td><a href="#" onclick=generatepdf("'+accountNumber+'")>Download Pdf</a></td></tr>'
$('#bodyIndividual').html(table);

*/






	/*$.ajax({
		type:'post'
		  dataType: "json",
		  url: "/ShowDetailsSubmitted",
		  data: JSON.stringify(parametersJSON),
		  success: function (){
			  alert("test");
		  }
		});*/


/*  2nd page 
var i=0;
var j=0;	

var mapForSummayOfCharges ={};
var placeHolders = new Array(); 
$('.addFiveFields').each(function (){
placeHolders[i]=$('#place1').val();
i++;
placeHolders[i]=$('#place2').val();
i++;
placeHolders[i]=$('#place3').val();
i++;
placeHolders[i]=$('#place4').val();
i++;
placeHolders[i]=$('#place5').val();
i++;
if(i%5==0){
	
	mapForSummayOfCharges[j]= placeHolders;
j++;
placeHolders = new Array();
i=0;
}
});*/

}


