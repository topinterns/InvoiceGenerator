package com.invoicgenerator;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.appengine.api.memcache.ErrorHandlers;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsOutputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

@Controller
public class PdfCloudStorageController {
	
	public static final Logger log = Logger.getLogger(AdminController.class.getName());
	static Integer count=0;
	
	@RequestMapping("/uploadGCS")
	public void  uploadCsvFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String fileName = request.getParameter("fileName");
			Queue queue 			= QueueFactory.getQueue("generatePdfAndPushToCloudStorage");
			queue.add(withUrl("/generatePdfAndPushToCloudStorage").param("fileName",fileName));
		}
	
	@RequestMapping(value = "/generatePdfAndPushToCloudStorage")
	public void generatePdfAndPushToCloudStorage(HttpServletRequest request, HttpServletResponse response,@RequestParam("fileName") String fileName) throws IOException, JSONException {
		ByteArrayOutputStream baos = null;
		long TIME_LIMIT = 1000;
		HashMap<String, Map<String, Object>> lReturnUserObjFromCache = new HashMap<String, Map<String, Object>>();
		
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
	    syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
	   /* lReturnUserObjFromCache = (HashMap<String, Map<String, Object>>) syncCache.get(fileName);*/
		AdminController ac = new AdminController();
		lReturnUserObjFromCache = ac.readParsedObjectFromGCS(fileName);
	   
	    if(lReturnUserObjFromCache == null){
	    	System.out.println("NOthing in memcache");
	    	return;
	    }
		
	    System.out.println("In upload GCS::"+lReturnUserObjFromCache.size());
	    Iterator it = lReturnUserObjFromCache.entrySet().iterator();
		long startTime = System.currentTimeMillis();
		long currentTime = System.currentTimeMillis();
	    try {
	    	HashMap<String, Boolean> pdfGenerationStatus = (HashMap<String, Boolean>) syncCache.get("isPdfGenerated");
	    	if(pdfGenerationStatus == null){
				pdfGenerationStatus = new HashMap<String, Boolean>();
				System.out.println("memcache entry created");
			}
			//main while
			while(it.hasNext() && currentTime-startTime <= TIME_LIMIT) {
				
				String accountName = "";
				String accountNumber = "";
				String invoiceDate = "";
				String dateDue = "";
				String phoneNumber = "";
				String[] TotalDue = new String[10];
				String addressString = "";
				String fromAddressString = "";
				String description = "";
				String noOfCalls = "";
				String minutes = "";
				String amount = "";
				String datePage2 = "";
				String summary = "";
				String nom = "";
				String charges = "";
				Double totalCurrentCharges = 0.0;
				String planOverageCharges = "0.00";
				String otherCharges = "0.00";
				String inbound = "";
				String inboundCalls = "";
				Double sumOfCharges = 0.0;
				
				Map.Entry mapEntryForMap = (Map.Entry) it.next();
				HashMap<String, Object> eachValueMap = (HashMap<String, Object>) mapEntryForMap
						.getValue();
				String eachKeyForMap = (String) mapEntryForMap.getKey();
				Iterator it1 = eachValueMap.entrySet().iterator();
				
				if(pdfGenerationStatus.get(eachKeyForMap) != null)
					continue;
				//boolean statusForPdfGeneration = (boolean)lReturnUserObjFromCache.get(eachKeyForMap).get("isPdfGenerated");
				//if(!statusForPdfGeneration){
				
				//Inner while
				while (it1.hasNext()) {
					Map.Entry mapEntry = (Map.Entry) it1.next();
					Object eachValue = mapEntry.getValue();
					String eachKey = (String) mapEntry.getKey();
					/*if( eachKey.equals("isPdfGenerated"))
						isPdfGenerated = (boolean)mapEntry.getValue();*/
					//if(isPdfGenerated == false){
						
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
						// //System.out.println("accountnumber::" +
						// accountNumber);
					} else if (eachValue instanceof String[]) {
						String[] stringArray = (String[]) eachValue;
						if (eachKey.equals("TotalDue")) {
							for (int i = 0; i < stringArray.length; i++) {
								TotalDue[i] = stringArray[i];
							}
						} else if (eachKey.equals("address")) {
							for (int i = 0; i < stringArray.length - 1; i++) {
								addressString += stringArray[i] + "\n";
							}
						} else if (eachKey.equals("fromAddress")) {
							for (int i = 0; i < stringArray.length; i++) {
								fromAddressString += stringArray[i] + "\n";
							}
						}
					} else if (eachValue instanceof ArrayList) {
						ArrayList<Object> list = (ArrayList<Object>) eachValue;
						if (eachKey.equals("TotalDue")) {
							int i = 0;
							for (Object o : list) {
								TotalDue[i] = (String) o;
								i++;
							}
						} else if (eachKey.equals("address")) {
							String country = (String) list.get(list.size() - 1);
							for (Object o : list) {
								String lastIndexCheck = (String) o;
								if (!(lastIndexCheck.equals(country)))
									addressString += (String) o + "\n";
							}
						} else if (eachKey.equals("fromAddress")) {
							for (Object o : list)
								fromAddressString += (String) o + "\n";
						} else if (eachKey.equals("summaryOfCharges")) {
							for (Object o : list) {
								if (o instanceof String[]) {
									String[] stringArray = (String[]) o;
									if (stringArray.length != 0) {
										if (stringArray.length == 2) {
											if (stringArray[0].trim().equals(
													"Plan Overage Charges")) {
												planOverageCharges = Double
														.toString(Double
																.parseDouble(stringArray[1]));
											} else if (stringArray[0].trim()
													.equals("Other Charges")) {
												otherCharges = Double
														.toString(Double
																.parseDouble(stringArray[1]));
											} else if (stringArray[0]
													.trim()
													.equals("INBOUND 8XX CALLS")) {
												inbound = stringArray[0];
												inboundCalls = stringArray[1];
												otherCharges = "0.00";
												planOverageCharges = "0.00";
											} else {
												planOverageCharges = "0.00";
												otherCharges = "0.00";
											}
										}
									}
								}
								if (o instanceof ArrayList) {
									ArrayList<Object> eachSummary = (ArrayList<Object>) o;
									if (eachSummary != null
											&& eachSummary.size() != 0) {
										if (eachSummary.size() == 2) {
											String eachSummary0 = (String) eachSummary
													.get(0);
											String eachSummary1 = (String) eachSummary
													.get(1);
											if (eachSummary0.trim().equals(
													"Plan Overage Charges")) {
												planOverageCharges = Double
														.toString(Double
																.parseDouble(eachSummary1));
											} else if (eachSummary0.trim()
													.equals("Other Charges")) {
												otherCharges = Double
														.toString(Double
																.parseDouble(eachSummary1));
											} else if (eachSummary1
													.trim()
													.equals("INBOUND 8XX CALLS")) {
												inbound = eachSummary0;
												inboundCalls = eachSummary1;
												otherCharges = "0.00";
												planOverageCharges = "0.00";
											}
										}
									} else {
										planOverageCharges = "0.00";
										otherCharges = "0.00";
									}
								}

							}
						} else if (eachKey.equals("summaryOfCharges2")) {
							for (Object o : list) {
								if (o instanceof ArrayList) {
									ArrayList<Object> each4100 = (ArrayList<Object>) o;
									if (each4100.size() == 4) {
										if (each4100.get(0).toString().length() > 0)
											datePage2 += each4100.get(0)
													.toString().substring(0, 5)
													+ "\n";
										summary += each4100.get(1).toString()
												.trim()
												+ "\n";
										nom += each4100.get(2).toString()
												+ "\n";
										Double charge = Double
												.parseDouble(each4100.get(3)
														.toString());
										charges += charge + "\n";
										sumOfCharges += (Double) charge;
									} else if (each4100.size() == 1) {
										datePage2 += "\n";
										summary += each4100.get(0).toString()
												.trim()
												+ "\n";
										nom += "\n";
										charges += "\n";

									}
								}

							}
						} else if (eachKey.equals("userIdSummary")) {
							for (Object o : list) {
								if (o instanceof ArrayList) {
									ArrayList<Object> each9200 = (ArrayList<Object>) o;
									if (each9200.size() == 4) {
										description += "     "
												+ each9200.get(0).toString()
												+ "\n";
										noOfCalls += each9200.get(1).toString()
												+ "\n";
										minutes += each9200.get(2).toString()
												+ "\n";
										amount += each9200.get(3).toString()
												+ "\n";
									}
								}
							}
						}
					} else if (eachValue instanceof Object) {
						Double floatValue;
						if (eachKey.equals("totalCurrentCharges")) {
							try {
								floatValue = (Double) eachValue;
								totalCurrentCharges = floatValue;
							} catch (Exception e) {
								floatValue = Double.parseDouble(eachValue
										.toString());
								totalCurrentCharges = floatValue;
							}
						}
					}
					//}//if for isPdf flag
					//else
					//{
						//if(it.hasNext()){
							//it.next();
						//continue;
						//}
					//}
				}//inner while ended
				//log.info("b4 try");
				//log.info("entering try");
				PdfReader reader = null;
				try {
					// We get a resource from our web app
					if (description.equals("")) {
						if (fromAddressString.trim().contains("AnswerConnect")
								|| fromAddressString.trim().contains(
										"CTI Long Distance")
								|| fromAddressString.trim().contains(
										"VoiceCurve")
								|| fromAddressString.trim().contains("Synclio"))
							reader = new PdfReader(
									"templates/TemplateWith2pagesModifiedAC.pdf");
						else if (fromAddressString.trim().contains(
								"Answer Force"))
							reader = new PdfReader(
									"templates/TemplateWith2pagesModifiedAF.pdf");
						else if (fromAddressString.trim().contains(
								"Lex Reception"))
							reader = new PdfReader(
									"templates/TemplateWith2pagesModifiedLR.pdf");
						else if (fromAddressString.trim().contains("Memosent"))
							reader = new PdfReader(
									"templates/TemplateWith2pagesModifiedMS.pdf");
						else
								reader = new PdfReader("templates/TemplateWith2pagesModifiedAC.pdf");
					} else {
						if (fromAddressString.trim().contains("AnswerConnect")
								|| fromAddressString.trim().contains(
										"CTI Long Distance")
								|| fromAddressString.trim().contains(
										"VoiceCurve")
								|| fromAddressString.trim().contains("Synclio"))
							reader = new PdfReader(
									"templates/TemplateWith3pagesModifiedAC.pdf");
						else if (fromAddressString.trim().contains(
								"Answer Force"))
							reader = new PdfReader(
									"templates/TemplateWith3pagesModifiedAF.pdf");
						else if (fromAddressString.trim().contains(
								"Lex Reception"))
							reader = new PdfReader(
									"templates/TemplateWith3pagesModifiedLR.pdf");
						else if (fromAddressString.trim().contains("Memosent"))
							reader = new PdfReader(
									"templates/TemplateWith3pagesModifiedMS.pdf");
						else
							reader = new PdfReader("templates/TemplateWith2pagesModifiedAC.pdf");
					}
					
					// reader = new
					// PdfReader("templates/TemplateWith2pagesModifiedAC.pdf");
					//log.info("Read file successfully");
					//Inner try
					try {
						// We create an OutputStream for the new PDF
						baos = new ByteArrayOutputStream();
					} catch (Exception e) {
						System.out.println("Exception in creating stream");
						e.printStackTrace();
					}
					// Now we create the PDF
					PdfStamper stamper = new PdfStamper(reader, baos);
					//log.info("Created file successfully");
					// We alter the fields of the existing PDF
					AcroFields form = stamper.getAcroFields();
					form.setField("accountNumber", accountNumber);
					form.setField("phoneNumber", phoneNumber);
					form.setField("invoiceDate", invoiceDate);
					form.setField("dueDate", dateDue);
					form.setField("fromAddress", fromAddressString);
					String totalDue = TotalDue[0].substring(1);
					form.setField("totalDue",
							(TotalDue[0].contains("-")) ? "-$" + totalDue : "$"
									+ TotalDue[0]);
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
					form.setField("inbound", inbound);
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
					charges += "\n"
							+ Math.round((sumOfCharges.doubleValue()) * 100.0)
							/ 100.0;
					// System.out.println("In pdf generation:"+sumOfCharges);
					form.setField("datePage2", datePage2);
					form.setField("summary", summary);
					form.setField("nom", nom);
					form.setField("totals", charges);

					form.setField("Description3_1", description);
					form.setField("Calls3_1", noOfCalls);
					form.setField("Minutes3_1", minutes);
					form.setField("Amount3_1", amount);
					// page 3 completed
					stamper.setFormFlattening(true);
					stamper.close();
					reader.close();
					//log.info("altered successfully");
					//System.out.println("before generation"+lReturnUserObjFromCache.get(eachKeyForMap).get("isPdfGenerated"));
					/*boolean operationStatus = uploadFileToGCS(baos.toByteArray(), accountNumber, invoiceDate);
					if(operationStatus){
						lReturnUserObjFromCache.get(eachKeyForMap).put("isPdfGenerated",true);
					}*/
					//System.out.println("After generation:"+lReturnUserObjFromCache.get(eachKeyForMap).get("isPdfGenerated"));
					uploadFileToGCS(baos.toByteArray(), accountNumber, invoiceDate);
					pdfGenerationStatus.put(eachKeyForMap, true);
					syncCache.put("isPdfGenerated", pdfGenerationStatus);
					currentTime = System.currentTimeMillis();
				}//Inner try ended 
				catch (DocumentException | IOException e) {
					System.out.println("In Exception of generating pdf::");
					e.printStackTrace();
					throw new IOException(e.getMessage());
				}
				/*}
				/*else{
					if(it.hasNext())
					{
						it.next();
						continue;
					}*/
				//}
				//}//if ended (to restart task queue
				/*else{
					System.out.println("Time out.... so Queue started again");
					//System.out.println("started::"+startTime);
					startTime = currentTime;
					Queue queue 			= QueueFactory.getQueue("generatePdfAndPushToCloudStorage");
					queue.add(withUrl("/generatePdfAndPushToCloudStorage").param("count",count.toString()));
				}*/
			}//Main while ended
			
			if(currentTime - startTime > TIME_LIMIT && count <= lReturnUserObjFromCache.size()){
				System.out.println("Queue Restated");
				Queue queue 			= QueueFactory.getQueue("generatePdfAndPushToCloudStorage");
				queue.add(withUrl("/generatePdfAndPushToCloudStorage").param("fileName",fileName));
			}
			
			System.out.println("count value::"+count);
		}//main try ended
		catch (Exception e) {
			e.printStackTrace();
			
		}
		
	}
	
	
	private void uploadFileToGCS(byte[] bytesToPush, String fileName,
			String invoiceDate) throws IOException {

		String ACL_BUCKET_ACCESS = "bucket-owner-read";
		String BUCKET = "fs_invoice";
		String OBJECT = invoiceDate.replaceAll("/", "_") + "/" + fileName;
		GcsService gcsService = GcsServiceFactory.createGcsService();
		GcsFilename filePath = new GcsFilename(BUCKET, OBJECT);
		
		GcsFileOptions options = new GcsFileOptions.Builder()
				.mimeType("application/pdf").acl(ACL_BUCKET_ACCESS)
				.cacheControl("public,max-age=60,no-transform").build();
		GcsOutputChannel writeChannel = gcsService.createOrReplace(filePath,
				options);
		writeChannel.waitForOutstandingWrites();
		writeChannel.write(ByteBuffer.wrap(bytesToPush));
		writeChannel.close();
		count++;
		log.info("filePath::" + filePath + "-----------"+count+"-----------");
		return;
	}
	
	
	@RequestMapping("/fileUploadHandlerService")
	public @ResponseBody String fileUploadMethod(@RequestBody Map<String,Object>request) {
		System.out.println("In handler method");
				return "Successfully Uploaded";
	}

}
