package com.invoicgenerator;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;


import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsInputChannel;
import com.google.appengine.tools.cloudstorage.GcsOutputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.channels.Channels;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.jdo.PersistenceManager;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.appengine.api.ThreadManager;
import com.google.appengine.api.backends.BackendServiceFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileReadChannel;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.taskqueue.TaskOptions.Method;
import com.google.appengine.repackaged.org.codehaus.jackson.JsonGenerationException;
import com.google.appengine.repackaged.org.codehaus.jackson.map.JsonMappingException;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;


@Controller
@RequestMapping(value = "/admin")
public class AdminController {

	public static final Logger log = Logger.getLogger(AdminController.class.getName());
	public static final HashMap<String, Map<String, Object>> lReturnUserObj = new HashMap<String, Map<String, Object>>();
	public static boolean taskqueueStatus = false;
	public static BlobKey blobKey;
	//String eachInvoice = "Invoice Date\tAccount Name\tAccount Number\tPhone Number\tUser Id Summary\tTo Address\tTotal Due\tAmount Of Last Statement\tPayments Received\tDebits or Credits\tUnknown\tUnknown\tBalance Forward\tCurrent Charges\tTotal Due By Due Date\tTotal Due After Due Date\tFrom Address\tDate Due\tSummary Of Charges\tSummary Of Charges\tTotal Current Charges\n";
	@Autowired
	ServletContext context;
	@RequestMapping("/")
	public String re_directhome(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		return "home";
	}
	@RequestMapping("/home")
	public String homePage(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		//System.out.println("heloo Welcome");
		return "home";
	}
	BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
	@RequestMapping("/uploadcsv")
	public @ResponseBody String  uploadCsvFile(HttpServletRequest request, HttpServletResponse response,@RequestBody String text) throws IOException {
		HttpSession lSession = request.getSession();
		String responseText = "";
		ArrayList<String> customersList = new ArrayList<String>();
		ServletOutputStream out = response.getOutputStream();
			Map<String, BlobKey> blobs = blobstoreService.getUploadedBlobs(request);
			blobKey = blobs.get("uploadFile");
			Queue queue 			= QueueFactory.getQueue("taskQueueToReadTextFileAndCreateStaticMap");
			queue.add(withUrl("/admin/taskQueueToReadTextFileAndCreateStaticMap").param("data",""));
			return blobKey.getKeyString();
		}
	@RequestMapping("/getUploadedData")
	public @ResponseBody HashMap<String, Map<String, Object>>  getUploadedData(HttpServletRequest request, HttpServletResponse response,@RequestBody String text) throws IOException {
		HttpSession lSession = request.getSession();
		//HashMap<String, Map<String, Object>> lReturnUserObj = new HashMap<String, Map<String, Object>>();
		//lReturnUserObj = (HashMap<String, Map<String, Object>>) lSession.getAttribute("totalMap");
				//System.out.println("getUploadedData service method  ...... "+lReturnUserObj.size());
			return lReturnUserObj;
		}
	@RequestMapping("/getTaskQueueStatus")
	public @ResponseBody boolean  getTaskQueueStatus(HttpServletRequest request, HttpServletResponse response) throws IOException {
			//System.out.println("inside taskqueueStatus service method   "+taskqueueStatus);
			return taskqueueStatus;
		}
	@RequestMapping("/taskQueueToReadTextFileAndCreateStaticMap")
	public @ResponseBody void getUrlForImportedContactsOnChrome(HttpServletRequest request, HttpServletResponse response,@RequestBody String text) throws IOException {
		ServletOutputStream out = response.getOutputStream();
		////System.out.println("map size:"+lReturnUserObj.size());
		//HashMap<String, Map<String, Object>> lReturnUserObj = new HashMap<String, Map<String, Object>>();
		try {
			FileService fileServices = FileServiceFactory.getFileService();
			AppEngineFile fileURL = fileServices.getBlobFile(blobKey);
			fileURL.getFileSystem();
			FileReadChannel readChannel = fileServices.openReadChannel(fileURL,true);
			BufferedReader reader = new BufferedReader(Channels.newReader(readChannel, "UTF8"));
			String line;
			String key="";
			String accountNumber = "";
			String phoneNumber = "";
			String invoiceDate = "";
			String dateDue = "";
			//String[] TotalDue = new String[10];
			String[] address;
			Double totalCurrentCharges= 0.0;
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("0100")) {
					HashMap<String, Object> detailsObject = new HashMap<String, Object>();
					String firstLine = line.replaceAll(" ", "").replaceFirst("0100", "");
					accountNumber = firstLine.substring(0, 10);
					phoneNumber = firstLine.substring(10, 22);
					invoiceDate = firstLine.substring(22, 30);
					dateDue = firstLine.substring(30, 38);
					String[] amtArray = generateAmountsFromFirstRow(firstLine
							.substring(38, firstLine.length()));
					detailsObject.put("TotalDue", amtArray);
					detailsObject.put("accountNumber", accountNumber);
					detailsObject.put("phoneNumber", phoneNumber);
					detailsObject.put("invoiceDate",generateRequiredDateFormat(invoiceDate));
					detailsObject.put("dateDue",generateRequiredDateFormat(dateDue));
					line = reader.readLine();
					String addressString = line.replaceFirst("0200", "");
					address = addressString.split("\\s{3,}");
					/*key = accountNumber+"_"+invoiceDate;
					//JDO
					try{
						PersistenceManager pm = PMF.get().getPersistenceManager();
					 try{
						 AccountJDO objpojo = pm.getObjectById(AccountJDO.class,accountNumber);
						 }
						 catch(Exception e){
							 AccountJDO results = new AccountJDO();
						 results.setAccountNumberPojo(accountNumber);
						           results.setAddressLine1(address[0]);
						           results.setAddressLine2(address[1]);
						           if(address.length == 3)
						        	   results.setAddressLine3(address[2]);
						 
						           if(address.length ==4){
						        	   results.setAddressLine3(address[2]);
						        	   results.setAddressLine4(address[3]);
						        	   }
						           if(address.length ==5 ){
						        	   results.setAddressLine3(address[2]);
						        	   results.setAddressLine4(address[3]);
						        	   results.setAddressLine5(address[4]);   
						           }
						           if(address.length ==6){
						        	   results.setAddressLine3(address[2]);
						        	   results.setAddressLine4(address[3]);
						        	   results.setAddressLine5(address[4]);
						        	   //results.setAddressLine5(address[4]);
						           }
						          pm.makePersistent(results);
						}
					}
					   catch(Exception e){
						    e.printStackTrace();
					    	}
					//JDO ended
*/					detailsObject.put("address", address);
					String accountName = address[0];
					detailsObject.put("accountName", accountName);
					ArrayList<Object> summaryOfCharges = new ArrayList<Object>();
					ArrayList<Object> summaryOfCharges2 = new ArrayList<Object>();
					ArrayList<Object> userIdSummary = new ArrayList<Object>();
					while ((line = reader.readLine()) != null) {
						if (line.startsWith("9900"))
							break;
						if (line.startsWith("1100")) {
							String summaryChargesString = line.replaceFirst(
									"1100", "");
							String[] each1100 = summaryChargesString
									.split("\\s{3,}");
							if(each1100.length == 2)
								each1100[1] = Double.toString(Double.parseDouble(each1100[1]));
							summaryOfCharges.add(each1100);
						}
						if(line.startsWith("4100"))
						{
							String summaryChargesString = line.replaceFirst(
									"4100", "");
							ArrayList<Object> each4100Array = new ArrayList<Object>();
							if ( summaryChargesString.trim().length() > 35) {
									String date = summaryChargesString.substring(0, 8);
									if (Character.isDigit(date.charAt(0))) {
										date = generateRequiredDateFormat(date);
										each4100Array.add(date);
										String description = summaryChargesString.substring(8, 34);
										each4100Array.add(description);
										Integer nom = Integer.parseInt(summaryChargesString
												.substring(34, 42));
										each4100Array.add(nom);
										Double fee = Double.parseDouble(summaryChargesString
												.substring(42));
										each4100Array.add(fee);
									}
								}
							else
								each4100Array.add(summaryChargesString);
							summaryOfCharges2.add(each4100Array);
						}
						if(line.startsWith("9200"))
						{
							String summaryChargesString = line.replaceFirst(
									"9200", "");
							ArrayList<Object> each9200Array = new ArrayList<Object>();
							if ( summaryChargesString.trim().length() > 0) {
								String[] each9200Description = summaryChargesString.split("\\s{3,}");
								if(each9200Description[0].trim().length() != 0)
										each9200Array.add(each9200Description[0]);
								String[] each9200RestOfFields = each9200Description[1].split(" ");
								Integer noOfCalls = Integer.parseInt(each9200RestOfFields[0]);
								each9200Array.add(noOfCalls);
								Double minutes = Double.parseDouble(each9200RestOfFields[1]);
								each9200Array.add(minutes);
								Double amount = Double.parseDouble(each9200RestOfFields[2]);
								each9200Array.add(amount);
							}
							else
								each9200Array.add(" ");
							userIdSummary.add(each9200Array);
							
						}
						if (line.startsWith("0500")) {
							String[] fromAddress = line.replaceFirst(
									"0500", "").split("\\s{3,}");
							detailsObject.put("fromAddress",
									fromAddress);

						}
						if (line.startsWith("1200")) {

							try {
								totalCurrentCharges = Double.parseDouble(line
										.replace("1200", ""));
								detailsObject.put("totalCurrentCharges",
										totalCurrentCharges);
							} catch (Exception e) {
								// TotalDue=firstLine.substring(38, 50);
								detailsObject.put("totalCurrentCharges",
										line.replace("1200", ""));
							}

						}
						
						//JDO
/*						try{
							PersistenceManager pm = PMF.get().getPersistenceManager();
						 try{
							 InvoicesJDO invoicePojo = pm.getObjectById(InvoicesJDO.class,key);
							 }
							 catch(Exception e){
							 InvoicesJDO results1 = new InvoicesJDO();
							 results1.setInvoicekey(key);
							           
							 results1.setPhoneNumberIn(phoneNumber);
							 results1.setInvoiceDateIn(invoiceDate);
							 results1.setDateDueIn(dateDue);
							 results1.setTotalDueIn(amtArray[0]);
							 results1.setPaymentsReceivedIn(amtArray[1]);
							 results1.setDebitsIn(amtArray[2]);
							 results1.setBalanceForwardIn(amtArray[3]);
							 results1.setCurrentChargesIn(amtArray[4]);
							 results1.setTotalDueByIn(amtArray[5]);
							 results1.setTotalDueAfterIn(amtArray[6]);
							 results1.setTotalCurrentChargesIn(totalCurrentCharges);
                             ////System.out.println("This is invoices presistence");
                             ////System.out.println("results1 value is ::::"+results1);
							 pm.makePersistent(results1);
						}
						}
						   catch(Exception e){
							    e.printStackTrace();
						    	}*/
						//JDO Ended
					}

					detailsObject.put("summaryOfCharges", summaryOfCharges);
					detailsObject.put("summaryOfCharges2", summaryOfCharges2);
					detailsObject.put("userIdSummary", userIdSummary);

					lReturnUserObj.put(accountNumber, detailsObject);
					//request.getSession().setAttribute("totalMap", lReturnUserObj);
					////System.out.println("map size At end:"+lReturnUserObj.size());
					
				}
				////System.out.println("map values:"+lReturnUserObj);
			}

		} catch (Exception e) {
			e.printStackTrace();
			out.println("fileerroruploading");
			//System.out.println("Exception in sendig File URL.." + e);
		}
		////System.out.println(lReturnUserObj.size());
		taskqueueStatus = true;
    //System.out.println("task Queue Execution completed now");
	}

	public String generateRequiredDateFormat(String data) {
		String year = data.substring(0, 4);
		String month = data.substring(4, 6);
		String date = data.substring(6, 8);
		return month + "/" + date + "/" + year.substring(2, 4);
	}

	public String[] generateAmountsFromFirstRow(String str) {
		String[] tempArray = new String[10];
		for (int i = 0; i < 10; i++) {
			tempArray[i] = str.substring(0, (str.indexOf(".") + 3));
			str = str.substring((str.indexOf(".") + 3), str.length());
		}
		tempArray[2] = (tempArray[2].substring(8, tempArray[2].length()));
		String[] TotalDue = new String[10];
		for (int i = 0; i < tempArray.length; i++) {
			try {
				TotalDue[i] = Double.parseDouble(tempArray[i]) + "";
			}
			catch (Exception e) {
				TotalDue[i] = tempArray[i].substring(8, tempArray[i].length());
			}
		}
		return TotalDue;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String generator(Map<String, Map<String, Object>> totalDetails) {
		String totalData = "";
		Iterator iterateTotalDetails = totalDetails.entrySet().iterator();
		while (iterateTotalDetails.hasNext()) {
			String eachInvoice = "";
			Map.Entry mapEntry = (Map.Entry) iterateTotalDetails.next();
			Map<String, Object> individualDetails = (Map<String, Object>) mapEntry
					.getValue();
			Iterator iterateIndividualDetails = individualDetails.entrySet().iterator();
			while (iterateIndividualDetails.hasNext()) {
				Map.Entry mapEntry1 = (Map.Entry) iterateIndividualDetails.next();
				Object eachField = mapEntry1.getValue();
				String eachKey = (String) mapEntry1.getKey();
				
				if (eachField instanceof String) {
					eachInvoice += eachField + "\t";
				} 
				
				else if (eachField instanceof String[]) {
					String[] stringArray = (String[]) eachField;
					String address = "";
					if (eachKey.equals("TotalDue")) {
						for (int i = 0; i < stringArray.length; i++) 
							eachInvoice += stringArray[i] + "\t ";
					} else if (eachKey.equals("address")) {
						for (int i = 0; i < stringArray.length; i++) 
							address += stringArray[i] + "&";
						eachInvoice += address + "\t";
					} else if (eachKey.equals("fromAddress")) {
						for (int i = 0; i < stringArray.length; i++) 
							address += stringArray[i] + " ";
						eachInvoice += address + "\t";
					}
				}
				
				else if (eachField instanceof ArrayList) {
					ArrayList<Object> list = (ArrayList<Object>) eachField;
					String summary = "";
					if (eachKey.equals("summaryOfCharges")) {
						for (Object summaryOfChargesList : list) {
							if (summaryOfChargesList instanceof String[]) {
								String[] stringArray = (String[]) summaryOfChargesList;
								for (int i = 0; i < stringArray.length; i++) 
									summary += stringArray[i] + "&";
							}
						}
						eachInvoice += summary + "\t";
					} else if (eachKey.equals("summaryOfCharges2")) {
						for (Object summaryOfCharges2List : list) {
							if (summaryOfCharges2List instanceof ArrayList) {
								ArrayList<Object> stringArrayList = (ArrayList<Object>) summaryOfCharges2List;
								for (Object eachSummaryString : stringArrayList) 
									summary += eachSummaryString.toString().trim() + "&";
							}
						}
						eachInvoice += summary + "\t";
					}
					else if(eachKey.equals("userIdSummary")){
						for (Object userIdSummaryList : list){
							if (userIdSummaryList instanceof ArrayList)
							{
								ArrayList<Object> stringArrayList = (ArrayList<Object>) userIdSummaryList;
								for (Object eachUserIdSummaryString : stringArrayList)
									summary += eachUserIdSummaryString.toString().trim() + "&";
							}
						}
						eachInvoice += summary + "\t";
					}
				}
				
				else if (eachField instanceof Double) {
					Double floatValue = (Double) eachField;
					eachInvoice += floatValue + "\t";
				}
				
				else
					eachInvoice += "\t";
			}
			totalData += eachInvoice + "\n";

		}
		// //System.out.println("In generator:"+totalData);
		return totalData;
	}

	@RequestMapping(value = "/downloadcsv", method = RequestMethod.POST)
	public void downloadFile(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		//final Logger log = Logger.getLogger(AdminController.class.getName());
		//System.out.println("in download");
		//HashMap<String, Map<String, Object>> lReturnUserObj = new HashMap<String, Map<String, Object>>();
		//lReturnUserObj = (HashMap<String, Map<String, Object>>) request.getSession().getAttribute("totalMap");
		String invoiceHeader = "Invoice Date\tAccount Name\tAccount Number\tPhone Number\tUser Id Summary\tTo Address\tTotal Due\tAmount Of Last Statement\tPayments Received\tDebits or Credits\tUnknown\tUnknown\tBalance Forward\tCurrent Charges\tTotal Due By Due Date\tTotal Due After Due Date\tFrom Address\tDate Due\tSummary Of Charges\tSummary Of Charges\tTotal Current Charges\n";
		String eachInvoice = generator(lReturnUserObj);
		// log.info(eachInvoice);
		response.setContentType("text/csv");
		response.setHeader("Content-Disposition",
				"attachment;filename=InvoiceDataDelimited.txt");
		response.getWriter().write(invoiceHeader);
		response.getWriter().write(eachInvoice);
		//System.out.println("in download");
	}
//	
//	@RequestMapping("/bulkPdf")
//	public void bulkPdf(HttpServletRequest request, HttpServletResponse response) throws IOException {
//		String backendAddress = BackendServiceFactory.getBackendService()
//                .getBackendAddress("task-backend");
//		TaskOptions task = withUrl("/admin/generatebulkpdf").method(Method.POST)
//                .header("Host", backendAddress);
//		Queue queue = QueueFactory.getQueue("generateBulkPdf");
//		queue.add(task);	
//		
//		} 
	
	
	/*@RequestMapping(value = "/generatebulkpdf", method = RequestMethod.POST)
	public void generateBulkPdf(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, JsonGenerationException,
			JsonMappingException, IOException, DocumentException {
		//HashMap<String, Map<String, Object>> lReturnUserObj = new HashMap<String, Map<String, Object>>();
		//lReturnUserObj = (HashMap<String, Map<String, Object>>) request.getSession().getAttribute("totalMap");
		generatePdf(lReturnUserObj, response,"mounika505@gmail.com","","");
		return;
	}*/
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/generatesinglepdf", method = RequestMethod.POST)
	public void generateSinglePdf(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, JsonGenerationException,
			JsonMappingException, IOException, DocumentException {
	
		ByteArrayOutputStream baos = null;
		String requeststring =request.getParameter("allStaffDetailsJson");
		Map<String,Object> requestMap = new ObjectMapper().readValue(requeststring, HashMap.class);
		String mapIndex=requestMap.get("currentRecord").toString();
		String emailId=requestMap.get("emailId").toString();
		String subject=requestMap.get("subject").toString();
		String body=requestMap.get("body").toString();
		////System.out.println("email id:"+emailId);
		Map<String, Object> mapDetails = lReturnUserObj.get(mapIndex);
		////System.out.println(mapDetails);
		HashMap<String, Map<String, Object>> mapWithSingleRow = new HashMap<String, Map<String, Object>>();
		////System.out.println(lReturnUserObj.size());
		mapWithSingleRow.put(mapIndex, mapDetails);
		try{
			if(emailId.equals("NA") && subject.equals("NA") && body.equals("NA")){
				//zout = new ZipOutputStream(response.getOutputStream());
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition","attachment;filename="+mapIndex+".pdf");
				
			}
		baos = generatePdf(mapWithSingleRow);
		ServletOutputStream os = response.getOutputStream();
		baos.writeTo(os);
		
		if(!emailId.equals("NA") && !subject.equals("NA") && !body.equals("NA"))
        mailing(baos, emailId,subject,body);
		os.flush();
		
		
		}
		catch(Exception e){
			
		}
	}
	
	@RequestMapping(value = "/generatebulkpdf", method = RequestMethod.POST)
	 public void generateBulkPdf(HttpServletRequest request,HttpServletResponse response) throws ServletException, JsonGenerationException,JsonMappingException, IOException, DocumentException {
		ZipOutputStream zout = null;
		String emailId = "NA";
		String subject = "NA";
		String body = "NA";
		ByteArrayOutputStream baos = null;
		try{
		if(emailId.equals("NA") && subject.equals("NA") && body.equals("NA")){
			zout = new ZipOutputStream(response.getOutputStream());
			response.setContentType("application/zip");
			response.setHeader("Content-Disposition","attachment;filename=Invoice.zip");
		}
	  String rquestMap = request.getParameter("allStaffDetailsJson1");
	  //System.out.println("map from front end:"+rquestMap);
	  HashMap<String, Map<String, Object>> lReturnUserObjFront = new ObjectMapper().readValue(rquestMap, HashMap.class);
	  Set<String> keys = lReturnUserObjFront.keySet();
	  /*for(String s: keys)
		  log.info(s);*/
	  /*log.warning(lReturnUserObjFront.size()+"map size");*/
	 
	  Iterator it = lReturnUserObjFront.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry mapEntryForMap = (Map.Entry) it.next();
			HashMap<String, Object> eachValueMap = (HashMap<String, Object>) mapEntryForMap.getValue();
			String eachKeyForMap = (String) mapEntryForMap.getKey();
			HashMap<String, Map<String, Object>> mapWithSingleRow = new HashMap<String, Map<String, Object>>();
			mapWithSingleRow.put(eachKeyForMap, eachValueMap);
	  baos = generatePdf(mapWithSingleRow);
		
	  if(emailId.equals("NA") && subject.equals("NA") && body.equals("NA")){
			//zout = new ZipOutputStream(baos);
		ZipEntry zipentry = new ZipEntry(eachKeyForMap+".pdf");                                
		zout.putNextEntry(zipentry);                
        zout.write(baos.toByteArray());
	  }
       // if(!emailId.equals("NA") && !subject.equals("NA") && !body.equals("NA"))
         // mailing(baos, emailId,subject,body);
	  
	  }
		}
	  finally
	  {
	        try {
	            // Flush the stream
	            zout.flush();
	        } catch (Exception e) {                
	        }

	        try {
	            // Close the stream
	            zout.close();
	        } catch (Exception e) {                
	        }
	    }
			
	  
	  return;
	 }
	
	public String getInvoiceDate(Map<String, Object> map) {
		String invoiceDate = (String)map.get("invoiceDate");
		return invoiceDate;
	}
	
	@SuppressWarnings("unchecked")
	public ByteArrayOutputStream generatePdf(HashMap<String, Map<String, Object>> mapToGenerate) throws IOException
	{
		//ZipOutputStream zout = null;
		/*if(emailId.equals("NA") && subject.equals("NA") && body.equals("NA")){
			zout = new ZipOutputStream(response.getOutputStream());
			response.setContentType("application/zip");
			response.setHeader("Content-Disposition","attachment;filename=Invoice.zip");
			
		}*/
		ByteArrayOutputStream baos=null;
		Iterator it = mapToGenerate.entrySet().iterator();
		try{
		while (it.hasNext()){
			////System.out.println("In iteration"+k);
			String accountName = "";
			String accountNumber = "";
			String invoiceDate = "";
			String dateDue = "";
			String phoneNumber = "";
			String[] TotalDue = new String[10];
			String addressString = "";
			String fromAddressString = "";
			//String[] fromAddress = new String[10];
			
			String description = "";
			String noOfCalls = "";
			String minutes = "";
			String amount = "";
			String datePage2 ="";
			String summary ="";
			String nom ="";
			String charges = "";
			Double totalCurrentCharges = 0.0;
			String planOverageCharges = "0.00";
			String otherCharges = "0.00";
			String inbound = "";
			String inboundCalls ="";
			Double sumOfCharges = 0.0;
			//String emailId ="";
			Map.Entry mapEntryForMap = (Map.Entry) it.next();
			HashMap<String, Object> eachValueMap = (HashMap<String, Object>) mapEntryForMap.getValue();
			String eachKeyForMap = (String) mapEntryForMap.getKey();
			Iterator it1 = eachValueMap.entrySet().iterator();
			while(it1.hasNext()){
				Map.Entry mapEntry = (Map.Entry) it1.next();
				Object eachValue = mapEntry.getValue();
				String eachKey = (String) mapEntry.getKey();
			if (eachValue instanceof String) {
				if (eachKey.equals("accountNumber"))
					accountNumber = (String) mapEntry.getValue();
				else if (eachKey.equals("accountName"))
					accountName = (String) mapEntry.getValue();
				else if (eachKey.equals("invoiceDate"))
					invoiceDate = (String) mapEntry.getValue();
				else if (eachKey.equals("phoneNumber"))
					phoneNumber = (String) mapEntry.getValue();
				else if (eachKey.equals("dateDue"))
					dateDue = (String) mapEntry.getValue();
				////System.out.println("accountnumber::" + accountNumber);
			} else if (eachValue instanceof String[]) {
				String[] stringArray = (String[]) eachValue;
				if (eachKey.equals("TotalDue")) {
					for (int i = 0; i < stringArray.length; i++) {
						TotalDue[i] = stringArray[i];
					}
				} else if (eachKey.equals("address")) {
					for (int i = 0; i < stringArray.length-1; i++) {
						addressString += stringArray[i] + "\n";
					}
				} else if (eachKey.equals("fromAddress")) {
					for (int i = 0; i < stringArray.length; i++) {
						fromAddressString += stringArray[i] + "\n";
					}
				}
			}else if (eachValue instanceof ArrayList  ) {
				ArrayList<Object> list = (ArrayList<Object>) eachValue;
				if (eachKey.equals("TotalDue")) {
					int i=0;
					for (Object o : list) {
						TotalDue[i] = (String)o;
						i++;
					}
				}
				 else if (eachKey.equals("address")) {
					 String country = (String)list.get(list.size()-1);
					 for (Object o : list) {
						 String lastIndexCheck = (String)o;
						 if(!(lastIndexCheck.equals(country)))
						 addressString += (String)o + "\n";
					 }
				 }
				 else if (eachKey.equals("fromAddress")) {
					 for (Object o : list) 
						 fromAddressString += (String)o + "\n";
				 } 
				 else if (eachKey.equals("summaryOfCharges")) {
					for (Object o : list) {
						//String[] summaryOfCharges = new String[20];
						if (o instanceof String[]) {
							String[] stringArray = (String[]) o;
							if(stringArray.length != 0){
								if(stringArray.length == 2){
								//System.out.println("when length 2 "+stringArray[0]);
								//System.out.println(stringArray[1]);
								if (stringArray[0].trim().equals("Plan Overage Charges")) {
									planOverageCharges = Double.toString(Double
											.parseDouble(stringArray[1]));
								} else if (stringArray[0].trim().equals("Other Charges")) {
									otherCharges = Double.toString(Double
											.parseDouble(stringArray[1]));
								}
								else if(stringArray[0].trim().equals("INBOUND 8XX CALLS")){
									inbound = stringArray[0];
									inboundCalls = stringArray[1];
									otherCharges = "0.00";
									planOverageCharges = "0.00";
								}
								else {
									planOverageCharges = "0.00";
									otherCharges = "0.00";
								}
								}
							}
						}
						if(o instanceof ArrayList){
							ArrayList<Object> eachSummary = (ArrayList<Object>)o;
							if( eachSummary != null && eachSummary.size() != 0 ){
								//System.out.println(eachSummary.get(0));
								if(eachSummary.size() == 2){
								String eachSummary0 = (String) eachSummary.get(0);
								String eachSummary1 = (String) eachSummary.get(1);
								//System.out.println(eachSummary.get(1));
								if (eachSummary0.trim().equals("Plan Overage Charges")) {
									//System.out.println("plan overage charges:"+Double.toString(Double
										//	.parseDouble(eachSummary1)));
									planOverageCharges = Double.toString(Double
											.parseDouble(eachSummary1));
								} else if (eachSummary0.trim().equals("Other Charges")) {
									otherCharges = Double.toString(Double
											.parseDouble(eachSummary1));
								}
								else if(eachSummary1.trim().equals("INBOUND 8XX CALLS")){
									inbound = eachSummary0;
									inboundCalls = eachSummary1;
									otherCharges = "0.00";
									planOverageCharges = "0.00";
								}
								}
							}
							else
							{
								planOverageCharges = "0.00";
								otherCharges = "0.00";
							}
						}
						
					}
				} else if (eachKey.equals("summaryOfCharges2")) {
					for (Object o : list) {
						if (o instanceof ArrayList) {
							ArrayList<Object> each4100 = (ArrayList<Object>) o;
							if(each4100.size() == 4){
								if(each4100.get(0).toString().length() > 0)
								datePage2 += each4100.get(0).toString().substring(0, 5)+"\n";
								summary += each4100.get(1).toString().trim()+"\n";
								nom += each4100.get(2).toString()+"\n";
								Double charge = Double.parseDouble(each4100.get(3).toString());
								charges += charge+"\n";
								//System.out.println("Charge value:"+charge);
								sumOfCharges += (Double)charge;
								//System.out.println("sum:"+sumOfCharges);
								//System.out.println("Double value:"+(Double)sumOfCharges);
							}
							else if(each4100.size() == 1){
								datePage2 += "\n";
								summary += each4100.get(0).toString().trim()+"\n";
								nom += "\n";
								charges += "\n";
								
							}
						}
								
					}
				}
				else if(eachKey.equals("userIdSummary")){
					for (Object o : list){
						if (o instanceof ArrayList)
						{
							ArrayList<Object> each9200 = (ArrayList<Object>) o;
							if(each9200.size() == 4){
								description += "     "+each9200.get(0).toString()+"\n";
								noOfCalls += each9200.get(1).toString()+"\n";
								minutes += each9200.get(2).toString()+"\n";
								amount += each9200.get(3).toString()+"\n";
								}
						}
					}
				}
			} 
			else if (eachValue instanceof Object) {
				Double floatValue;
				if(eachKey.equals("totalCurrentCharges")){
				//System.out.println("each value in double "+eachValue);
				try{
				floatValue = (Double) eachValue;
				totalCurrentCharges = floatValue;
				}
				catch(Exception e){
					floatValue = Double.parseDouble(eachValue.toString());
					totalCurrentCharges = floatValue;
				}
				}
			}
			}
			log.info("b4 try");
			log.info("entering try");
			PdfReader reader= null;
			try {
				// We get a resource from our web app
				if(description.equals("")){
					if(fromAddressString.trim().contains("AnswerConnect") || fromAddressString.trim().contains("CTI Long Distance") || fromAddressString.trim().contains("VoiceCurve") || fromAddressString.trim().contains("Synclio") )
						reader = new PdfReader("templates/TemplateWith2pagesModifiedAC.pdf");
					else if(fromAddressString.trim().contains("Answer Force"))
						reader = new PdfReader("templates/TemplateWith2pagesModifiedAF.pdf");
					else if(fromAddressString.trim().contains("Lex Reception"))
						reader = new PdfReader("templates/TemplateWith2pagesModifiedLR.pdf");
					else if(fromAddressString.trim().contains("Memosent"))
						reader = new PdfReader("templates/TemplateWith2pagesModifiedMS.pdf");
				}
				else {
					if(fromAddressString.trim().contains("AnswerConnect") || fromAddressString.trim().contains("CTI Long Distance") || fromAddressString.trim().contains("VoiceCurve") || fromAddressString.trim().contains("Synclio"))
						reader = new PdfReader("templates/TemplateWith3pagesModifiedAC.pdf");
					else if(fromAddressString.trim().contains("Answer Force"))
						reader = new PdfReader("templates/TemplateWith3pagesModifiedAF.pdf");
					else if(fromAddressString.trim().contains("Lex Reception"))
						reader = new PdfReader("templates/TemplateWith3pagesModifiedLR.pdf");
					else if(fromAddressString.trim().contains("Memosent"))
						reader = new PdfReader("templates/TemplateWith3pagesModifiedMS.pdf");
				}
				//reader = new PdfReader("templates/TemplateWith2pagesModifiedAC.pdf");
				log.info("Read file successfully");
				try{
				// We create an OutputStream for the new PDF
				baos = new ByteArrayOutputStream();
				}
				catch(Exception e){
					//System.out.println("Exception in creating stream");
					e.printStackTrace();
				}
				// Now we create the PDF
				PdfStamper stamper = new PdfStamper(reader, baos);
				log.info("Created file successfully");
				// We alter the fields of the existing PDF
				AcroFields form = stamper.getAcroFields();
				form.setField("accountNumber", accountNumber);
				form.setField("phoneNumber", phoneNumber);
				form.setField("invoiceDate", invoiceDate);
				form.setField("dueDate", dateDue);
				form.setField("fromAddress", fromAddressString);
				String totalDue = TotalDue[0].substring(1);
				form.setField("totalDue", (TotalDue[0].contains("-")) ? "-$"+totalDue: "$"+TotalDue[0]);
				form.setField("amountofLastStatement", TotalDue[1]);
				form.setField("paymentReceived", TotalDue[2]);
				form.setField("debitOrCredit", TotalDue[3]);
				form.setField("balanceForward", TotalDue[6]);
				form.setField("currentCharges", TotalDue[7]);
				form.setField("totalDueByDate", dateDue);
				form.setField("totalDueAfterDate", dateDue);
				form.setField("totalDueByDate", dateDue);
				form.setField("totalDueByDateAmount", TotalDue[8]);
				form.setField("totalDueAfterDateAmount", TotalDue[9]);
			
				form.setField("planOverageCharges", planOverageCharges);
				form.setField("otherCharges", otherCharges);
				form.setField("inbound",inbound);
				form.setField("inboundCalls", inboundCalls);
				form.setField("address", addressString);
				form.setField("totalCurrentCharges",
						Double.toString(totalCurrentCharges));

				// page 2

				form.setField("accountName", accountName);
				form.setField("accountNumber", accountNumber);
				form.setField("invoiceDate", invoiceDate);

				// ----summary of charges------
				summary += "\nTotal";
				charges += "\n"+Math.round((sumOfCharges.doubleValue()) * 100.0) / 100.0;
				//System.out.println("In pdf generation:"+sumOfCharges);
				form.setField("datePage2", datePage2);
				form.setField("summary", summary);
				form.setField("nom", nom);
				form.setField("totals", charges);
				
				form.setField("Description3_1", description);
				form.setField("Calls3_1", noOfCalls);
				form.setField("Minutes3_1", minutes);
				form.setField("Amount3_1", amount);
				//form.setField(name, value, display);
				//page 3 completed
				stamper.setFormFlattening(true);
				//stamper.setFreeTextFlattening(true);
				stamper.close();
				reader.close();
				log.info("altered successfully");

				// We write the PDF bytes to the OutputStream
				/*ServletOutputStream os = response.getOutputStream();
				baos.writeTo(os);*/
				
				/*if(emailId.equals("NA") && subject.equals("NA") && body.equals("NA")){
				//zout = new ZipOutputStream(baos);
				ZipEntry zipentry = new ZipEntry(accountNumber+".pdf");                                
				zout.putNextEntry(zipentry);                
	            zout.write(baos.toByteArray());
				}*/
	            //
	            /*if(!emailId.equals("NA") && !subject.equals("NA") && !body.equals("NA"))
	            mailing(baos, emailId,subject,body);*/
				///zout.flush();
				//zout.close();
				//os.flush();
				//k++;
			} catch (DocumentException | IOException e) {
				//System.out.println("In Exception of generating pdf::");
				e.printStackTrace();
				throw new IOException(e.getMessage());
			}
			
		}
		}
		catch (Exception e) {
			//System.out.println("In Exception of map::");
			e.printStackTrace();
			throw new IOException(e.getMessage());
		}
		return baos;
		
	}
		
	/* code for receiving a map from front end instead of record
	 * @SuppressWarnings("rawtypes")
	@RequestMapping(value = "/generatesinglepdf", method = RequestMethod.POST)
	public void generateSinglePdf(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, JsonGenerationException,
			JsonMappingException, IOException, DocumentException {
		String requeststring =request.getParameter("allStaffDetailsJson");
		String mapWithCurrentValuesString = request.getParameter("mapWithCurrentRecordJson");
		//System.out.println("requeststring"+requeststring);
		//System.out.println("map string"+mapWithCurrentValuesString);
		Map<String,Object> requestMap = new ObjectMapper().readValue(requeststring, HashMap.class);
		Map<String,Object> mapWithCurrentValues = new ObjectMapper().readValue(mapWithCurrentValuesString, HashMap.class);
		//System.out.println(mapWithCurrentValues);
		String emailId=requestMap.get("emailId").toString();
		String subject=requestMap.get("subject").toString();
		String body=requestMap.get("body").toString();
		HashMap<String, Map<String, Object>> mapWithSingleRow = new HashMap<String, Map<String, Object>>();
		mapWithSingleRow.put(mapWithCurrentValues.get("accountNumber").toString(), mapWithCurrentValues);
		generatePdf(mapWithSingleRow, response,emailId,subject, body);
	}*/
	
	
	//@SuppressWarnings("unchecked")
	public void mailing(ByteArrayOutputStream baos, String emailId, String subject, String body) throws IOException
	{
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		//System.out.println("mailing started");
		try {
		    Message msg = new MimeMessage(session);
		    msg.setFrom(new InternetAddress("fin.dev@a-cti.com", "Finance Team"));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(emailId, "Mr. User"));
		    msg.setSubject(subject);
		    Multipart mp = new MimeMultipart();
		    MimeBodyPart htmlPart = new MimeBodyPart();
		    htmlPart.setContent(body, "text/html");
		    mp.addBodyPart(htmlPart);

		    MimeBodyPart attachment = new MimeBodyPart();
		    attachment.setFileName("Invoice.pdf"); // we will use mp3 as an example
		    DataSource src = new ByteArrayDataSource(baos.toByteArray(), "application/pdf");
		    attachment.setDataHandler(new DataHandler(src));
		    mp.addBodyPart(attachment);

		    msg.setContent(mp);
		    Transport.send(msg);
		    //System.out.println("Message has been sent successfully");
		} catch (AddressException e) {
			//System.out.println("Error occured in the address");
		} catch (MessagingException e) {
			//System.out.println("Error occured in messaging");
		}
	}
	
	@RequestMapping(value = "/ShowDetailsSubmitted", method = RequestMethod.POST)
	public @ResponseBody
	HashMap<String, Object> LoadDetails(
			@RequestBody HashMap<String, Object> ManualEntry,
			HttpServletRequest request, HttpServletResponse response)
			throws JsonGenerationException, JsonMappingException, IOException
	/*
	 * public @ResponseBody HashMap<String,Object> LoadDetails(@RequestBody
	 * String ManualEntry,HttpServletRequest request, HttpServletResponse
	 * response) throws JsonGenerationException, JsonMappingException,
	 * IOException
	 */
	{
		HashMap<String, Object> detailsMap = new HashMap<String, Object>();

		try {

			String accoutNumber = ManualEntry.get("accountNumberForm")
					.toString();

			detailsMap.put("accountNumber",
					ManualEntry.get("accountNumberForm"));
			detailsMap.put("phoneNumber", ManualEntry.get("phoneNumberForm"));
			detailsMap.put("invoiceDate", ManualEntry.get("invoiceDateForm"));
			detailsMap.put("dateDue", ManualEntry.get("dateDueForm"));
			detailsMap.put("accountName", ManualEntry.get("accountNameForm"));

			// address
			int lengthOfToAddress = (int) ManualEntry
					.get("lengthOfToAddressForm");
			detailsMap.put("address", ((ArrayList<String>) ManualEntry
					.get("toaddressForm"))
					.toArray(new String[lengthOfToAddress]));

			int lengthOfFromAddress = (int) ManualEntry
					.get("lengthOfFromAddressForm");
			detailsMap.put("fromAddress",
					((ArrayList<String>) ManualEntry.get("fromaddressForm"))
							.toArray(new String[lengthOfFromAddress]));
			// TotalDue
			detailsMap.put("TotalDue", ((ArrayList<String>) ManualEntry
					.get("totalDueForm")).toArray(new String[10]));

			// summaryOfcharges
			String[] summaryOfChargesArray1 = ((ArrayList<String>) ManualEntry
					.get("summaryOfChargesFormArray1")).toArray(new String[2]);
			String[] summaryOfChargesArray2 = ((ArrayList<String>) ManualEntry
					.get("summaryOfChargesFormArray2")).toArray(new String[2]);

			List<String[]> summayOfChargesArrayList = new ArrayList<String[]>();

			summayOfChargesArrayList.add(summaryOfChargesArray1);
			summayOfChargesArrayList.add(summaryOfChargesArray2);

			detailsMap.put("summaryOfCharges",
					(ArrayList<String[]>) summayOfChargesArrayList);

			// totalCurrentCharges
			Double value = 0.0;
			// //System.out.println(
			// "this is double value "+ManualEntry.get("totalCurrentChargesForm"));

			value = Double.parseDouble(ManualEntry.get(
					"totalCurrentChargesForm").toString());
			// //System.out.println("This is the total current charges value "+value);
			detailsMap.put("totalCurrentCharges", value);
			List<ArrayList> SOC2List = new ArrayList<ArrayList>();
			SOC2List = (ArrayList<ArrayList>) ManualEntry
					.get("summaryOfCharges2Form");
			detailsMap
					.put("summaryOfCharges2", (ArrayList<ArrayList>) SOC2List);

			// userIdSummary -ArrayList in Objects
			List<ArrayList> UISList = new ArrayList<ArrayList>();
			UISList = (ArrayList<ArrayList>) ManualEntry
					.get("userIdSummaryForm");
			detailsMap.put("userIdSummary", (ArrayList<ArrayList>) UISList);

			// //System.out.println("The details of the map storing "+detailsMap);
			lReturnUserObj.put(accoutNumber, detailsMap);
		}

		catch (Exception e) {
			//System.out.println("exception is tableForDetails::");
			e.printStackTrace();
		}
		return detailsMap;
	}
	 
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/paymentReceipt",method=RequestMethod.POST)
	   /*public @ResponseBody
	    void paymentReceipt(@RequestBody HashMap<String,Object> PaymentReceipt,HttpServletRequest request, HttpServletResponse response) throws JsonGenerationException, JsonMappingException, IOException*/
	 public void paymentReceipt(HttpServletRequest request,
			   HttpServletResponse response) throws ServletException, JsonGenerationException,
			   JsonMappingException, IOException, DocumentException 
	 {
		response.setContentType("application/pdf");
		ZipOutputStream zout = null;
		zout = new ZipOutputStream(response.getOutputStream());
		String requeststring =request.getParameter("allStaffDetailsJson");
		HashMap<String,Object> PaymentReceipt = new ObjectMapper().readValue(requeststring, HashMap.class);
		String paymentDescription="";
		String payments="";
		String accountNumber = PaymentReceipt.get("accountNumberForm").toString();
		String phoneNumber = PaymentReceipt.get("phoneNumberForm").toString();
		String date = PaymentReceipt.get("invoiceDateForm").toString();
		int lengthOfToAddress =(int)PaymentReceipt.get("lengthOfToAddressForm");
		String toAddress = "";
		String fromAddress ="";
		Iterator it = ((ArrayList<String>) PaymentReceipt.get("summaryOfChargesFormArray")).iterator();
		while (it.hasNext())
		{
			Iterator it1 = ((ArrayList<String>) it.next()).iterator();
			int i=0;
			while(it1.hasNext()){
				if(i==0){
					paymentDescription += it1.next().toString()+"\n";
					i++;
				}
				else if(i==1){
					payments += it1.next().toString()+"\n";
					i--;
				}
			}
		}
		Iterator it1 = ((ArrayList<String>) PaymentReceipt.get("toaddressForm")).iterator();
		while (it1.hasNext())
		{
			toAddress += "\n"+it1.next().toString().trim();
		}
		Iterator it2 = ((ArrayList<String>) PaymentReceipt.get("fromaddressForm")).iterator();
		while (it2.hasNext())
			fromAddress += "\n"+it2.next().toString().trim();
		
		log.info("b4 try");
		log.info("entering try");
		response.setHeader("Content-Disposition","attachment;filename="+date+".pdf");
		PdfReader reader= null;
		try {
			// We get a resource from our web app
			if(fromAddress.trim().contains("AnswerConnect") || fromAddress.trim().contains("CTI Long Distance") || fromAddress.trim().contains("VoiceCurve"))
				reader = new PdfReader("templates/PaymentReceiptGeneratorAC.pdf");
			else if(fromAddress.trim().contains("Answer Force"))
				reader = new PdfReader("templates/PaymentReceiptGeneratorAF.pdf");
			else if(fromAddress.trim().contains("Lex Reception"))
				reader = new PdfReader("templates/PaymentReceiptGeneratorLR.pdf");
			else if(fromAddress.trim().contains("Memosent"))
				reader = new PdfReader("templates/PaymentReceiptGeneratorMS.pdf");
			//reader = new PdfReader("templates/PaymentReceiptGeneratorAC.pdf");
			
			//reader = new PdfReader("templates/TemplateWith2pagesModifiedAC.pdf");
			log.info("Read file successfully");
			// We create an OutputStream for the new PDF
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			// Now we create the PDF
			PdfStamper stamper = new PdfStamper(reader, baos);
			log.info("Created file successfully");
			// We alter the fields of the existing PDF
			AcroFields form = stamper.getAcroFields();
			form.setField("accountNumber", accountNumber);
			form.setField("phoneNumber", phoneNumber);
			form.setField("address", toAddress);
			form.setField("invoiceDate",date);
			form.setField("fromAddress", fromAddress);
			form.setField("paymentDescription", paymentDescription);
			form.setField("payments", payments);
			//stamper.setFormFlattening(true);
			stamper.setFreeTextFlattening(true);
			stamper.close();
			reader.close();
			log.info("altered successfully");

			// We write the PDF bytes to the OutputStream
			ServletOutputStream os = response.getOutputStream();
			baos.writeTo(os);
            //mailing(baos, emailId,subject,body);
			os.flush();
		} catch (DocumentException | IOException e) {
			throw new IOException(e.getMessage());
		}
	}
	
	/*//--------------------------------------------------------------------------------------------------------------------------(START)--------------Actual functionality
		@SuppressWarnings("rawtypes")
		@RequestMapping(value = "/generatedelimitpdf", method = RequestMethod.POST)
		public void generateDelimitPdf(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, JsonGenerationException,
				JsonMappingException, IOException, DocumentException {
			String requeststring =request.getParameter("allStaffDetailsJsondelimit");
			//System.out.println(" requeststring "+requeststring);
					
			String requeststring1 =request.getParameter("mapForDelimitJson");
			
		    //System.out.println("The map in generateDelimitPDF :::"+requeststring1);
		    HashMap<String,String> mapForDelimit = new ObjectMapper().readValue(requeststring1, HashMap.class);
			
			HashMap<String,Object> requestMap = new ObjectMapper().readValue(requeststring, HashMap.class);
			 //HashMap<String, Map<String, Object>> mapForDelimit = new ObjectMapper().readValue(requeststring1, HashMap.class);
			
			//---------------------------------------START------------------------------------------------
			 Iterator it = mapForDelimit.entrySet().iterator(); 
			   while(it.hasNext()){
			 Map.Entry mapEntry = (Map.Entry) it.next();  
				String eachValue = (String)mapEntry.getValue();
				String eachKey = (String) mapEntry.getKey();
			 //System.out.println("this is in while loop   :::"+eachValue+"     "+eachKey);
			   }
			//------------------------------------------END----------------------------------------------- 
			
			String mapIndex=requestMap.get("currentRecordDelimit").toString();
			String emailId=requestMap.get("emailIdDelimit").toString();
			String subject=requestMap.get("subjectDelimit").toString();
			String body=requestMap.get("bodyDelimit").toString();
			
			////System.out.println("These are the values of mapForEmail ::: currentRecord"+mapIndex+" emailId "+emailId+" subject "+subject+" body "+body);
			
			
			////System.out.println("email id:"+emailId);
	       //Map<String, Object> mapDetails = mapForDelimit.get(mapIndex);
		  //HashMap<String, Map<String, Object>> mapWithSingleRow = new HashMap<String, Map<String, Object>>();
		 ////System.out.println(lReturnUserObj.size());
			generatePdfForDelimit(mapForDelimit, response,emailId,subject,body);
		}
		@SuppressWarnings("unchecked")
		public void generatePdfForDelimit(HashMap<String, String> mapContainOneMap, HttpServletResponse response, String emailId, String subject, String bodyPart) throws IOException
		{
				response.setContentType("application/zip");
				response.setHeader("Content-Disposition","attachment;filename=invoice.zip");
			int k=0;			
			ZipOutputStream zout = null;
			zout = new ZipOutputStream(response.getOutputStream());
			//System.out.println("map size in method:"+mapContainOneMap.size());
			
		
		 //--------------------------------------------START---------------------------------------------------
		  	String[] stringArray=new String[10];
			String string="";
			 Iterator it1 = mapContainOneMap.entrySet().iterator(); 
			   while(it1.hasNext()){
			 Map.Entry mapEntry = (Map.Entry) it1.next();  
		     
				String eachKey = (String) mapEntry.getKey();
				Object eachValue = mapEntry.getValue();
			     
			
				if (eachValue instanceof Object) 
				 stringArray = (String[]) eachValue;
				else
					string = (String) eachValue;
			 //System.out.println("this is in while loop   :::"+stringArray+"     "+string+"     "+eachKey);
			   }
		//-------------------------------------------------END-----------------------------------------------------
			
			
			
			
				//page 1
			     String addressString="";
				 String[] addressArray;
				 String[] stringForSOC;
				
				String accountNumber1= "";
				String phoneNumber1= "";
		        String invoiceDate1= "";
		        String dueDate1="";
		        String totalDue1="";
		        String amountofLastStatement1="";
		        String paymentReceived1="";
		        String debitOrCredit1="";
		        String balanceForward1="";
		        String currentCharges1="";
		        String totalDueByDate1="";
		        String totalDueAfterDate1="";
		        String totalDueByDateAmount1="";
		        String totalDueAfterDateAmount1="";
		        String planOverageCharges1="";
		        String otherCharges1="";
		        String address1="";
		        String totalCurrentCharges1="";
		        
				//page 2
		        String accountName1= "";
		        String datePage2="";
		        String summary="";
		        String nom="";
		        String totals="";
		        
		        //page3
		        String Description3_1= "";
		        String Calls3_1="";
		        String Minutes3_1="";
		        String Amount3_1="";
				
				String addressString1 = "";
				String[] fromAddress = new String[10];
				String[] summaryOfCharges = new String[20];
				
				
		        try{
		        Iterator it = mapContainOneMap.entrySet().iterator();
		    		
				while(it.hasNext()){
					
					Map.Entry mapEntry = (Map.Entry) it.next();
					String eachValue = (String)mapEntry.getValue();
					String eachKey = (String) mapEntry.getKey();
				//if (eachValue instanceof String) {
					if (eachKey.equals("accountNumber"))
						accountNumber1 = (String) mapEntry.getValue();
					else if (eachKey.equals("phoneNumber"))
						phoneNumber1 = (String) mapEntry.getValue();
					
					else if (eachKey.equals("invoiceDate"))
						invoiceDate1 = (String) mapEntry.getValue();
					else if (eachKey.equals("dueDate"))
						dueDate1 = (String) mapEntry.getValue();
					else if (eachKey.equals("totalDue"))
						totalDue1 = (String) mapEntry.getValue();
					else if (eachKey.equals("ALSone"))
						amountofLastStatement1 = (String) mapEntry.getValue();
					else if (eachKey.equals("paymentReceived"))
						paymentReceived1 = (String) mapEntry.getValue();
					else if (eachKey.equals("debitOrCredit"))
						debitOrCredit1 = (String) mapEntry.getValue();
					else if (eachKey.equals("balanceForward"))
						balanceForward1 = (String) mapEntry.getValue();
					else if (eachKey.equals("currentCharges"))
						currentCharges1 = (String) mapEntry.getValue();
					else if (eachKey.equals("dueDate"))
						totalDueByDate1 = (String) mapEntry.getValue();
					else if (eachKey.equals("dueDate"))
						totalDueAfterDate1 = (String) mapEntry.getValue();
					else if (eachKey.equals("totalDueByDateAmount"))
						totalDueByDateAmount1 = (String) mapEntry.getValue();
					else if (eachKey.equals("totalDueAfterDateAmount"))
						totalDueAfterDateAmount1 = (String) mapEntry.getValue();
					else if (eachKey.equals("summaryOfCharges"))
					{ 
						String string1="";
						string1=(String) mapEntry.getValue();
						stringForSOC=string1.split(" ");
						//System.out.println(stringForSOC.length);
				    
						planOverageCharges1=stringForSOC[4];
						otherCharges1=stringForSOC[7];
					}
					
					//(String) mapEntry.getValue();
					else if (eachKey.equals("address"))
					{   addressString =(String) mapEntry.getValue();
					    addressArray =addressString.split("&");
					    addressString="";
					    for(int i=0;i<addressArray.length;i++)
					    addressString+=addressArray[i]+"\n";
					    address1=addressString;
					}
					else if (eachKey.equals("accountName"))
						accountName1 = (String) mapEntry.getValue();
					else if (eachKey.equals("totalCurrentCharges"))
						totalCurrentCharges1 = (String) mapEntry.getValue();
					
					//page 2
					else if(eachKey.equals("datePage2"))
						datePage2=(String) mapEntry.getValue();
					else if(eachKey.equals("summary"))
						summary=(String) mapEntry.getValue();
					else if(eachKey.equals("nom"))
						nom=(String) mapEntry.getValue();
					else if(eachKey.equals("totals"))
						totals=(String) mapEntry.getValue();
					
					
					//page 3
					else if(eachKey.equals("Description3_1"))
						Description3_1=(String) mapEntry.getValue();
					else if(eachKey.equals("Calls3_1"))
						Calls3_1=(String) mapEntry.getValue();
					else if(eachKey.equals("Minutes3_1"))
						Minutes3_1=(String) mapEntry.getValue();
					else if(eachKey.equals("Amount3_1"))
						Amount3_1=(String) mapEntry.getValue();
					
					
					}  //if
				log.info("b4 try");
				log.info("entering try");
				PdfReader reader;
				try {
					// We get a resource from our web app
					if(Description3_1.equals(""))
						reader = new PdfReader("templates/TemplateWith2pagesModified.pdf");
					else 
						reader = new PdfReader("templates/TemplateWith3pagesModified.pdf");
					log.info("Read file successfully");
					// We create an OutputStream for the new PDF
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					// Now we create the PDF
					PdfStamper stamper = new PdfStamper(reader, baos);
					log.info("Created file successfully");
					// We alter the fields of the existing PDF
					AcroFields form = stamper.getAcroFields();
					
					
					
					form.setField("accountNumber", accountNumber1);
					form.setField("phoneNumber", phoneNumber1);
					form.setField("invoiceDate", invoiceDate1);
					form.setField("dueDate", dueDate1);
					form.setField("totalDue", "$" + totalDue1);
					form.setField("amountofLastStatement", amountofLastStatement1);
					form.setField("paymentReceived", paymentReceived1);
					form.setField("debitOrCredit", debitOrCredit1);
					form.setField("balanceForward", balanceForward1);
					form.setField("currentCharges",currentCharges1);
					form.setField("totalDueByDate", totalDueByDate1);
					form.setField("totalDueAfterDate", totalDueAfterDate1);
					
					form.setField("totalDueByDateAmount", totalDueByDateAmount1);
					form.setField("totalDueAfterDateAmount", totalDueAfterDateAmount1);
					form.setField("planOverageCharges", planOverageCharges1);
					form.setField("otherCharges", otherCharges1);
					form.setField("address", address1);
					form.setField("accountName", accountName1);
					form.setField("totalCurrentCharges", totalCurrentCharges1);
					
					//page2
					form.setField("datePage2", datePage2);
					form.setField("summary", summary);
					form.setField("nom", nom);
					form.setField("totals", totals);
					
					//page3
					form.setField("Description3_1", Description3_1);
					form.setField("Calls3_1", Calls3_1);
					form.setField("Minutes3_1", Minutes3_1);
					form.setField("Amount3_1", Amount3_1);
					
									
					stamper.setFormFlattening(true);
					stamper.close();
					reader.close();
					log.info("altered successfully");

					// We write the PDF bytes to the OutputStream
					ServletOutputStream os = response.getOutputStream();
					baos.writeTo(os);
					
					
					//zout = new ZipOutputStream(baos);
					ZipEntry zipentry = new ZipEntry("invoice"+k+".pdf");                                
					zout.putNextEntry(zipentry);                
		            zout.write(baos.toByteArray());
		            mailing(baos, emailId,subject,bodyPart);
					///zout.flush();
					//zout.close();
					//os.flush();
					k++;
				} //try
				
				catch (DocumentException | IOException e) {
					throw new IOException(e.getMessage());
				}
				
			
			}
			finally{
		        try {
		            // Flush the stream
		            zout.flush();
		        } catch (Exception e) {                
		        }

		        try {
		            // Close the stream
		            zout.close();
		        } catch (Exception e) {                
		        }
		    }
		
		}	
*/
/*	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/generatedelimitpdf", method = RequestMethod.POST)
	public void generateDelimitPdf(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, JsonGenerationException,
			JsonMappingException, IOException, DocumentException {
		String requeststring =request.getParameter("allStaffDetailsJsondelimit");
		//System.out.println(" requeststring "+requeststring);
				
		String requeststring1 =request.getParameter("mapForDelimitJson");
		
	    //System.out.println("The map in generateDelimitPDF :::"+requeststring1);
	    Map<String, Object> mapForDelimit = new ObjectMapper().readValue(requeststring1, HashMap.class);
		
		HashMap<String,Object> requestMap = new ObjectMapper().readValue(requeststring, HashMap.class);
		 //HashMap<String, Map<String, Object>> mapForDelimit = new ObjectMapper().readValue(requeststring1, HashMap.class);
		
		
		 Iterator it = mapForDelimit.entrySet().iterator(); 
		   while(it.hasNext()){
		 Map.Entry mapEntry = (Map.Entry) it.next();  
			String eachValue = (String)mapEntry.getValue();
			String eachKey = (String) mapEntry.getKey();
		 //System.out.println("this is in while loop   :::"+eachValue+"     "+eachKey);
		   }
		 
		
		
		//conversion to Original Map structure
		
		 // address
		   int lengthOfAddress = (int) mapForDelimit.get("lengthOfAddress");
		   mapForDelimit.put("address", ((ArrayList<String>) mapForDelimit.get("address")).toArray(new String[lengthOfAddress]));
		   
		   //TotalDue
		   mapForDelimit.put("TotalDue",((ArrayList<String>) mapForDelimit.get("TotalDue")).toArray(new String[10]));
		  
		   //summaryOfcharges
		   String[] summaryOfChargesArray1 =  ((ArrayList<String>) mapForDelimit.get("SOCArray1")).toArray(new String[2]);
		   String[] summaryOfChargesArray2 =  ((ArrayList<String>) mapForDelimit.get("SOCArray2")).toArray(new String[2]);
		   
		  
		   List<String[]> summayOfChargesArrayList= new ArrayList<String[]>();
		   
		    summayOfChargesArrayList.add(summaryOfChargesArray1);
		    summayOfChargesArrayList.add(summaryOfChargesArray2);
		    
		    mapForDelimit.put("summaryOfCharges",(ArrayList<String[]>)summayOfChargesArrayList);
		   
		    // totalCurrentCharges	   
		   Double value=0.0;
		   //System.out.println( "this is double value "+mapForDelimit.get("totalCurrentChargesForm"));
		   
		   value=Double.parseDouble( mapForDelimit.get("totalCurrentCharges").toString());
		   //System.out.println("This is the total current charges value "+value);
		   mapForDelimit.put("totalCurrentCharges",value);
		
		String mapIndex=requestMap.get("currentRecordDelimit").toString();
		String emailId=requestMap.get("emailIdDelimit").toString();
		String subject=requestMap.get("subjectDelimit").toString();
		String body=requestMap.get("bodyDelimit").toString();
		
		//System.out.println("These are the values of mapForEmail ::: currentRecord"+mapIndex+" emailId "+emailId+" subject "+subject+" body "+body);
		
	HashMap<String, Map<String, Object>> mapWithSingleRow = new HashMap<String, Map<String, Object>>();
    mapWithSingleRow.put(mapIndex, mapForDelimit);
		
		
		
		////System.out.println("email id:"+emailId);
       //Map<String, Object> mapDetails = mapForDelimit.get(mapIndex);
	  //HashMap<String, Map<String, Object>> mapWithSingleRow = new HashMap<String, Map<String, Object>>();
	 ////System.out.println(lReturnUserObj.size());
		generatePdf(mapWithSingleRow, response,emailId,subject,body);

	}
*/

		
	/*	//@SuppressWarnings("rawtypes")
		@RequestMapping(value = "/generatedelimitpdf", method = RequestMethod.POST)
		public void generateDelimitPdf(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, JsonGenerationException,
				JsonMappingException, IOException, DocumentException {
			String requeststring =request.getParameter("allStaffDetailsJsondelimit");
			
			//System.out.println(" requeststring "+ requeststring);
					
			String requeststring1 =request.getParameter("mapForDelimitJson");
			
		   //System.out.println("The map in generateDelimitPDF :::"+requeststring1);
		    Map<String, Object> mapForDelimit = new ObjectMapper().readValue(requeststring1, HashMap.class);
			
			HashMap<String,Object> requestMap = new ObjectMapper().readValue(requeststring, HashMap.class);
			 //HashMap<String, Map<String, Object>> mapForDelimit = new ObjectMapper().readValue(requeststring1, HashMap.class);
			
			
			 Iterator it = mapForDelimit.entrySet().iterator(); 
			   while(it.hasNext()){
			 Map.Entry mapEntry = (Map.Entry) it.next();  
				String eachValue = (String)mapEntry.getValue();
				String eachKey = (String) mapEntry.getKey();
			 //System.out.println("this is in while loop   :::"+eachValue+"     "+eachKey);
			   }
			 
			
			
			//conversion to Original Map structure
			
			 // address
			   int lengthOfAddress = (int) mapForDelimit.get("lengthOfAddress");
			   mapForDelimit.put("address", ((ArrayList<String>) mapForDelimit.get("address")).toArray(new String[lengthOfAddress]));
			   
			   //TotalDue
			   mapForDelimit.put("TotalDue",((ArrayList<String>) mapForDelimit.get("TotalDue")).toArray(new String[10]));
			  
			   //summaryOfcharges
			   String[] summaryOfChargesArray1 =  ((ArrayList<String>) mapForDelimit.get("SOCArray1")).toArray(new String[2]);
			   String[] summaryOfChargesArray2 =  ((ArrayList<String>) mapForDelimit.get("SOCArray2")).toArray(new String[2]);
			   
			  
			   List<String[]> summayOfChargesArrayList= new ArrayList<String[]>();
			   
			    summayOfChargesArrayList.add(summaryOfChargesArray1);
			    summayOfChargesArrayList.add(summaryOfChargesArray2);
			    
			    mapForDelimit.put("summaryOfCharges",(ArrayList<String[]>)summayOfChargesArrayList);
			   
			    // totalCurrentCharges	   
			   Double value=0.0;
			  // //System.out.println( "this is double value "+mapForDelimit.get("totalCurrentChargesForm"));
			   
			   value=Double.parseDouble( mapForDelimit.get("totalCurrentCharges").toString());
			   ////System.out.println("This is the total current charges value "+value);
			   mapForDelimit.put("totalCurrentCharges",value);
			
			String mapIndex=requestMap.get("currentRecordDelimit").toString();
			String emailId=requestMap.get("emailIdDelimit").toString();
			String subject=requestMap.get("subjectDelimit").toString();
			String body=requestMap.get("bodyDelimit").toString();
			
			////System.out.println("These are the values of mapForEmail ::: currentRecord"+mapIndex+" emailId "+emailId+" subject "+subject+" body "+body);
			
		HashMap<String, Map<String, Object>> mapWithSingleRow = new HashMap<String, Map<String, Object>>();
	    mapWithSingleRow.put(mapIndex, mapForDelimit);
			
			
			
			////System.out.println("email id:"+emailId);
	       //Map<String, Object> mapDetails = mapForDelimit.get(mapIndex);
		  //HashMap<String, Map<String, Object>> mapWithSingleRow = new HashMap<String, Map<String, Object>>();
		 ////System.out.println(lReturnUserObj.size());
	    ////System.out.println("Map constructed:"+mapWithSingleRow);
	    //s//System.out.println();
			generatePdf(mapWithSingleRow, response,emailId,subject,body);

		}
*/
	
	 //Harika  in this code map Structured like Original map
		 @SuppressWarnings("rawtypes")
		 @RequestMapping(value = "/generatedelimitpdf", method = RequestMethod.POST)
		 public void generateDelimitPdf(HttpServletRequest request, HttpServletResponse response)
		   throws ServletException, JsonGenerationException,
		   JsonMappingException, IOException, DocumentException {
		  String requeststring =request.getParameter("allStaffDetailsJsondelimit");
		  //System.out.println(" requeststring "+requeststring);
		    
		  String requeststring1 =request.getParameter("mapForDelimitJson");
		  
		     //System.out.println("The map in generateDelimitPDF :::"+requeststring1);
		     Map<String, Object> mapForDelimit = new ObjectMapper().readValue(requeststring1, HashMap.class);
		  
		  HashMap<String,Object> requestMap = new ObjectMapper().readValue(requeststring, HashMap.class);
		   //HashMap<String, Map<String, Object>> mapForDelimit = new ObjectMapper().readValue(requeststring1, HashMap.class);
		  
		  
		  /* Iterator it = mapForDelimit.entrySet().iterator(); 
		     while(it.hasNext()){
		   Map.Entry mapEntry = (Map.Entry) it.next();  
		   String eachValue = (String)mapEntry.getValue();
		   String eachKey = (String) mapEntry.getKey();
		   //System.out.println("this is in while loop   :::"+eachValue+"     "+eachKey);
		     }*/
		   
		  
		  
		  //conversion to Original Map structure
		  
		   // address
		     int lengthOfAddress = (int) mapForDelimit.get("lengthOfAddress");
		     mapForDelimit.put("address", ((ArrayList<String>) mapForDelimit.get("address")).toArray(new String[lengthOfAddress]));
		     
		     //TotalDue
		     mapForDelimit.put("TotalDue",((ArrayList<String>) mapForDelimit.get("TotalDue")).toArray(new String[10]));
		     
		     //summaryOfcharges
		     
		     ArrayList<String> test1=  (ArrayList<String>) mapForDelimit.get("SOCArray1");
		     ArrayList<String> test2=  (ArrayList<String>) mapForDelimit.get("SOCArray2");
		     ////System.out.println(test1.size()+"first array size"+test2.size());
		     
		     String[] summaryOfChargesArray1 = new String[test1.size()];
		     summaryOfChargesArray1 = test1.toArray(summaryOfChargesArray1);

		     String[] summaryOfChargesArray2 = new String[test2.size()];
		     summaryOfChargesArray2 = test2.toArray(summaryOfChargesArray2);
		     
		     List<String[]> summayOfChargesArrayList= new ArrayList<String[]>();
		     
		      summayOfChargesArrayList.add(summaryOfChargesArray1);
		      summayOfChargesArrayList.add(summaryOfChargesArray2);
		      
		      mapForDelimit.put("summaryOfCharges",(ArrayList<String[]>)summayOfChargesArrayList);
		     
		    
		    /* //summaryOfcharges
		     ////System.out.println("This is SOC Array1  "+mapForDelimit.get("SOCArray1")+"This is SOC Array2"+mapForDelimit.get("SOCArray2"));
		     
		     //String[] summaryOfChargesArray1=new String[2];
		     String[] summaryOfChargesArray1=(String[]) ((ArrayList<String>) mapForDelimit.get("SOCArray1")).toArray(new String[2]);
		     
		    // String[] summaryOfChargesArray2=new String[2]; 
		     String[] summaryOfChargesArray2=(String[]) ((ArrayList<String>) mapForDelimit.get("SOCArray2")).toArray(new String[2]);
		     
		    
		     String[] summaryOfChargesArray1 =  ((ArrayList<String>) mapForDelimit.get("SOCArray1")).toArray(new String[2]);
		     String[] summaryOfChargesArray2 =  ((ArrayList<String>) mapForDelimit.get("SOCArray2")).toArray(new String[2]);
		     
		    
		     List<String[]> summayOfChargesArrayList= new ArrayList<String[]>();
		     
		      summayOfChargesArrayList.add(summaryOfChargesArray1);
		      summayOfChargesArrayList.add(summaryOfChargesArray2);
		      
		      mapForDelimit.put("summaryOfCharges",(ArrayList<String[]>)summayOfChargesArrayList);
		    */ 
		      
		      // totalCurrentCharges    
		     Double value=0.0;
		     ////System.out.println( "this is double value "+mapForDelimit.get("totalCurrentChargesForm"));
		     
		     value=Double.parseDouble( mapForDelimit.get("totalCurrentCharges").toString());
		     //System.out.println("This is the total current charges value "+value);
		     mapForDelimit.put("totalCurrentCharges",value);
		  
		  String mapIndex=requestMap.get("currentRecordDelimit").toString();
		  String emailId=requestMap.get("emailIdDelimit").toString();
		  String subject=requestMap.get("subjectDelimit").toString();
		  String body=requestMap.get("bodyDelimit").toString();
		  
		  //System.out.println("These are the values of mapForEmail ::: currentRecord"+mapIndex+" emailId "+emailId+" subject "+subject+" body "+body);
		  
		 HashMap<String, Map<String, Object>> mapWithSingleRow = new HashMap<String, Map<String, Object>>();
		    mapWithSingleRow.put(mapIndex, mapForDelimit);
		  
		  
		  
		  ////System.out.println("email id:"+emailId);
		       //Map<String, Object> mapDetails = mapForDelimit.get(mapIndex);
		   //HashMap<String, Map<String, Object>> mapWithSingleRow = new HashMap<String, Map<String, Object>>();
		  ////System.out.println(lReturnUserObj.size());
		    
		  //generatePdf(mapWithSingleRow, response,emailId,subject,body);

		 }
		
		/* Harika*/
		@RequestMapping(value = "/generatehtmltemplate")
		
		public  ModelAndView generateHtmlTemplate(@RequestParam("currentRecord")String currentRecord, HttpServletRequest request, HttpServletResponse response) throws IOException
		{
	       
		      //System.out.println("This is generatehtml template 'currentRecord is'   ::: "+currentRecord);	
			//  ModelAndView mv=new ModelAndView("AC2pages");
		      ModelAndView mv=null;

			 Map<String, Object> mapDetails = lReturnUserObj.get(currentRecord);
			 //System.out.println("THIS IS GENERATE HTML    ::::::"+mapDetails);
			 HashMap<String, Map<String, Object>> mapWithSingleRow = new HashMap<String, Map<String, Object>>();
			 mapWithSingleRow.put(currentRecord, mapDetails);
				////System.out.println(lReturnUserObj.size());
				//generatePdf(mapWithSingleRow, response,emailId,subject,body);
			
			
			String accountName = "";
			String accountNumber = "";
			String invoiceDate = "";
			String dateDue = "";
			String phoneNumber = "";
			String[] TotalDue = new String[10];
			String addressString = "";
			String fromAddressString = "";
			//String[] fromAddress = new String[10];
			String[] summaryOfCharges = new String[20];
			String description = "";
			String noOfCalls = "";
			String minutes = "";
			String amount = "";
			String datePage2 ="";
			String summary ="";
			String nom ="";
			String charges = "";
			Double totalCurrentCharges = 0.0;
			
			Iterator it = mapWithSingleRow.entrySet().iterator();
			while (it.hasNext()){
				////System.out.println("In iteration"+k);
				
				//String emailId ="";
				Map.Entry mapEntryForMap = (Map.Entry) it.next();
				HashMap<String, Object> eachValueMap = (HashMap<String, Object>) mapEntryForMap.getValue();
				String eachKeyForMap = (String) mapEntryForMap.getKey();
				Iterator it1 = eachValueMap.entrySet().iterator();
				while(it1.hasNext()){
					Map.Entry mapEntry = (Map.Entry) it1.next();
					Object eachValue = mapEntry.getValue();
					String eachKey = (String) mapEntry.getKey();
				if (eachValue instanceof String) {
					if (eachKey.equals("accountNumber"))
						accountNumber = (String) mapEntry.getValue();
					else if (eachKey.equals("accountName"))
						accountName = (String) mapEntry.getValue();
					else if (eachKey.equals("invoiceDate"))
						invoiceDate = (String) mapEntry.getValue();
					else if (eachKey.equals("phoneNumber"))
						phoneNumber = (String) mapEntry.getValue();
					else if (eachKey.equals("dateDue"))
						dateDue = (String) mapEntry.getValue();
					////System.out.println("accountnumber::" + accountNumber);
				} else if (eachValue instanceof String[]) {
					String[] stringArray = (String[]) eachValue;
					if (eachKey.equals("TotalDue")) {
						for (int i = 0; i < stringArray.length; i++) {
							TotalDue[i] = stringArray[i];
						}
					} else if (eachKey.equals("address")) {
						for (int i = 0; i < stringArray.length; i++) {
							addressString += stringArray[i] + "<br/>";
						}
					} else if (eachKey.equals("fromAddress")) {
						for (int i = 0; i < stringArray.length; i++) {
							fromAddressString += stringArray[i] + "<br/>";
						}
					}
				}else if (eachValue instanceof ArrayList  ) {
					ArrayList<Object> list = (ArrayList<Object>) eachValue;
					if (eachKey.equals("TotalDue")) {
						int i=0;
						for (Object o : list) {
							TotalDue[i] = (String)o;
							i++;
						}
					}
					 else if (eachKey.equals("address")) {
						 for (Object o : list) 
							 addressString += (String)o + "<br/>";
					 }
					 else if (eachKey.equals("fromAddress")) {
						 for (Object o : list) 
							 fromAddressString += (String)o + "<br/>";
					 } 
					 else if (eachKey.equals("summaryOfCharges")) {
						for (Object o : list) {
							if (o instanceof String[]) {
								String[] stringArray = (String[]) o;
								for (int i = 0; i < stringArray.length; i++) {
									summaryOfCharges[i] = stringArray[i];
								}
							}
						}
					} else if (eachKey.equals("summaryOfCharges2")) {
						for (Object o : list) {
							if (o instanceof ArrayList) {
								ArrayList<Object> each4100 = (ArrayList<Object>) o;
								if(each4100.size() == 4){
									if(each4100.get(0).toString().length() > 0)
									datePage2 += each4100.get(0).toString().substring(0, 5)+"<br/>";
									summary += each4100.get(1).toString().trim()+"<br/>";
									nom += each4100.get(2).toString()+"<br/>";
									charges += each4100.get(3).toString()+"<br/>";
								}
								else if(each4100.size() == 1){
									datePage2 += "<br/>";
									summary += each4100.get(0).toString().trim()+"<br/>";
									nom += "<br/>";
									charges += "<br/>";
									
								}
							}
									
						}
					}
					else if(eachKey.equals("userIdSummary")){
						for (Object o : list){
							if (o instanceof ArrayList)
							{
								ArrayList<Object> each9200 = (ArrayList<Object>) o;
								if(each9200.size() == 4){
									description += "     "+each9200.get(0).toString()+"<br/>";
									noOfCalls += each9200.get(1).toString()+"<br/>";
									minutes += each9200.get(2).toString()+"<br/>";
									amount += each9200.get(3).toString()+"<br/>";
									}
							}
						}
					}
				} 
				else if (eachValue instanceof Double) {
					Double floatValue = (Double) eachValue;
					totalCurrentCharges = floatValue;
				}
				}
			
			try {
				// We get a resource from our web app
				
				//System.out.println("This is the string of from Address (in generate html):::"+fromAddressString);
			
				if(description.equals("")){
					if(fromAddressString.trim().contains("AnswerConnect") || fromAddressString.trim().contains("CTI Long Distance") || fromAddressString.trim().contains("VoiceCurve") || fromAddressString.trim().contains("Synclio"))
						//reader = new PdfReader("templates/TemplateWith2pagesModifiedAC.pdf");
						  mv=new ModelAndView("AC2pages");
					else if(fromAddressString.trim().contains("Answer Force"))
						//reader = new PdfReader("templates/TemplateWith2pagesModifiedAF.pdf");
						  mv=new ModelAndView("AF2pages");
					else if(fromAddressString.trim().contains("Lex Reception"))
						//reader = new PdfReader("templates/TemplateWith2pagesModifiedLR.pdf");
					      mv=new ModelAndView("LR2pages");
					else if(fromAddressString.trim().contains("Memosent"))
						//reader = new PdfReader("templates/TemplateWith2pagesModifiedMS.pdf");
						   mv=new ModelAndView("MS2pages");
				}
				else {
					if(fromAddressString.trim().contains("AnswerConnect") || fromAddressString.trim().contains("CTI Long Distance") || fromAddressString.trim().contains("VoiceCurve") || fromAddressString.trim().contains("Synclio"))
						//reader = new PdfReader("templates/TemplateWith3pagesModifiedAC.pdf");
						  mv=new ModelAndView("AC3pages");
					else if(fromAddressString.trim().contains("Answer Force"))
						//reader = new PdfReader("templates/TemplateWith3pagesModifiedAF.pdf");
						  mv=new ModelAndView("AF3pages");
					else if(fromAddressString.trim().contains("Lex Reception"))
						//reader = new PdfReader("templates/TemplateWith3pagesModifiedLR.pdf");
						  mv=new ModelAndView("LR3pages");
					else if(fromAddressString.trim().contains("Memosent"))
						//reader = new PdfReader("templates/TemplateWith3pagesModifiedMS.pdf");
						  mv=new ModelAndView("MS3pages");
				}
			
			//mv=new ModelAndView("AC3pages");
			try
			  {
		      mv.addObject("accountNumber", accountNumber);
		      mv.addObject("phoneNumber", phoneNumber);
		      mv.addObject("invoiceDate", invoiceDate);
		      mv.addObject("dateDue", dateDue);
		      mv.addObject("fromAddress", fromAddressString);
		      String totalDue = TotalDue[0].substring(1);
		      mv.addObject("totalDue", (TotalDue[0].contains("-")) ? "-$"+totalDue: "$"+TotalDue[0]);
		      mv.addObject("amountofLastStatement", TotalDue[1]);
		      mv.addObject("paymentReceived", TotalDue[2]);
		      mv.addObject("debitOrCredit", TotalDue[3]);
		      mv.addObject("balanceForward", TotalDue[6]);
		      mv.addObject("currentCharges", TotalDue[7]);
		      mv.addObject("totalDueByDate", dateDue);
		      mv.addObject("totalDueAfterDate", dateDue);
		      mv.addObject("totalDueByDateAmount", TotalDue[8]);
		      mv.addObject("totalDueAfterDateAmount", TotalDue[9]);
		      
		    
				
				if (summaryOfCharges[0] != null) {
					////System.out.println(summaryOfCharges[0]);
					////System.out.println(summaryOfCharges[1]);
					if (summaryOfCharges[0].trim().equals("Plan Overage Charges")) {
						mv.addObject("planOverageCharges", Double.toString(Double
								.parseDouble(summaryOfCharges[1])));
						if (summaryOfCharges[2] != null) {
							if (summaryOfCharges[2].trim().equals("Other Charges"))
								mv.addObject("otherCharges", Double
										.toString(Double
												.parseDouble(summaryOfCharges[3])));
						} else
							mv.addObject("otherCharges", "0.00");
					} else if (summaryOfCharges[0].trim().equals("Other Charges")) {
						mv.addObject("otherCharges", Double.toString(Double
								.parseDouble(summaryOfCharges[1])));
						mv.addObject("planOverageCharges", "0.00");
					}
					else if(summaryOfCharges[0].trim().equals("INBOUND 8XX CALLS")){
						mv.addObject("inbound", summaryOfCharges[0]);
						mv.addObject("inboundCalls", summaryOfCharges[1]);
						mv.addObject("otherCharges", "0.00");
						mv.addObject("planOverageCharges", "0.00");
					}
				}
				else {
					mv.addObject("otherCharges", "0.00");
					mv.addObject("planOverageCharges", "0.00");
				}

				mv.addObject("address", addressString);
				mv.addObject("totalCurrentCharges",
						Double.toString(totalCurrentCharges));

				// page 2

				mv.addObject("accountName", accountName);
				mv.addObject("accountNumber", accountNumber);
				mv.addObject("invoiceDate", invoiceDate);

				// ----summary of charges------

				mv.addObject("datePage2", datePage2);
				mv.addObject("summary", summary);
				mv.addObject("nom", nom);
				mv.addObject("totals", charges);
				
				mv.addObject("Description3_1", description);
				mv.addObject("Calls3_1", noOfCalls);
				mv.addObject("Minutes3_1", minutes);
				 mv.addObject("Amount3_1", amount);
				 
			  
			}
				 
				  
				  catch(Exception e)
				  {
				   e.printStackTrace();
				  }
			
			
			
			
			 //return new ModelAndView("redirect:htmlTemplates/AC2pages.html");
			//System.out.println("This is the ModalandView  :::  "+mv);
			}
		
			catch(Exception e)
			  {
			   e.printStackTrace();
			  }
		
		
		}
				//CODE TO CONVERT THE mv OBJECT TO THE BYTE ARRAY FOR THE PURPOSE OF STORING IN CLOUD STORAGE
			  /*ByteArrayOutputStream b = new ByteArrayOutputStream();
	        ObjectOutputStream o = new ObjectOutputStream(b);
	        o.writeObject(mv);
	        byte[] byteArray = b.toByteArray();
	        uploadFileToGCS(response, byteArray);*/
			return mv;
		}
			
		
		@RequestMapping(value = "/cloudStore")
		
		public @ResponseBody boolean cloudStore(HttpServletRequest request, HttpServletResponse response, @RequestBody HashMap<String, Map<String, Object>> mapToStore) throws IOException
		{
			////System.out.println(mapToStore);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			Iterator it = mapToStore.entrySet().iterator();
			
			while(it.hasNext()){
				////System.out.println("In loop");
				Map.Entry mapEntryForMap = (Map.Entry) it.next();
				HashMap<String, Object> eachValueMap = (HashMap<String, Object>) mapEntryForMap.getValue();
				String eachKeyForMap = (String) mapEntryForMap.getKey();
				HashMap<String, Map<String, Object>> mapWithSingleRow = new HashMap<String, Map<String, Object>>();
				mapWithSingleRow.put(eachKeyForMap, eachValueMap);
				String invoiceDate = (String)eachValueMap.get("invoiceDate");
				baos = generatePdf(mapWithSingleRow);
				uploadFileToGCS(baos.toByteArray(), eachKeyForMap,invoiceDate);
				////System.out.println("in cloud Store");
			}
			
			//uploadFileToGCS(request, response);
			return true;
		}
		
		private void uploadFileToGCS(byte[] bytesToPush, String fileName,String invoiceDate) throws IOException{

			//System.out.println("Date String:"+invoiceDate);
			String ACL_BUCKET_ACCESS="bucket-owner-read";
			String BUCKET = "fs_invoice";
			String OBJECT = invoiceDate.replaceAll("/","_")+"/"+fileName;
			//response.setContentType("text/plain");
		    //response.getWriter().println("Hello, world from java");
		    GcsService gcsService = GcsServiceFactory.createGcsService();
		    GcsFilename filePath = new GcsFilename(BUCKET,OBJECT );
		    log.info("filePath::"+filePath);
		    //System.out.println("filePath::"+filePath);
		    GcsFileOptions options = new GcsFileOptions.Builder().mimeType("application/pdf").acl(ACL_BUCKET_ACCESS).cacheControl("public,max-age=60,no-transform").build();
		    GcsOutputChannel writeChannel = gcsService.createOrReplace(filePath, options);

		   /* PrintWriter out = new PrintWriter(Channels.newWriter(writeChannel, "UTF8"));
		    out.println("The woods are lovely dark and deep.");
		    out.println("But I have promises to keep.");
		    out.flush();
		    */
		    writeChannel.waitForOutstandingWrites();
		    writeChannel.write(ByteBuffer.wrap(bytesToPush));
		    writeChannel.close();
		    //System.out.println("Done successfully");
		    
		    //response.getWriter().println("Done writing...");


		    /*GcsInputChannel readChannel = gcsService.openReadChannel(filePath, 0);
		    BufferedReader reader = new BufferedReader(Channels.newReader(readChannel, "UTF8"));
		    String line;
		    while ((line = reader.readLine()) != null) {
		      //response.getWriter().println("READ:" + line);
		    }
		    readChannel.close();*/
			 }
		
}