package com.invoicgenerator;

import java.io.Serializable;
import java.util.ArrayList;
import javax.jdo.annotations.*;
@PersistenceCapable
public class InvoicesJDO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;





		//page 1
	    @Persistent
	    @PrimaryKey
	    private String Invoicekey;
	 
	 
	 
	  
	 	
	      @Persistent
		  private String totalDueIn;
		  @Persistent 
		  private String paymentsReceivedIn;
		  @Persistent 
		  private String debitsIn;
		  @Persistent
		  private String balanceForwardIn;
		  @Persistent
		  private String currentChargesIn;
		  @Persistent
		  private String totalDueByIn;
		  @Persistent
		  private String totalDueAfterIn;
		  
		 
		  
		/*  @Persistent
		  private ArrayList summaryChargesIn = new ArrayList();
		
		  
		public ArrayList getSummaryChargesIn() {
			return summaryChargesIn;
		}


		public void setSummaryChargesIn(ArrayList summaryChargesIn) {
			this.summaryChargesIn = summaryChargesIn;
		}

*/		  @Persistent
		  private String phoneNumberIn;
			 
		  @Persistent
		  private String invoiceDateIn;
			  
		  @Persistent
		  private String dateDueIn;
		  
		  @Persistent
		  private Double totalCurrentChargesIn;
			 
			 
	 	public Double getTotalCurrentChargesIn() {
			return totalCurrentChargesIn;
		}


		public void setTotalCurrentChargesIn(Double totalCurrentChargesIn) {
			this.totalCurrentChargesIn = totalCurrentChargesIn;
		}


		public String getInvoicekey() {
			return Invoicekey;
		}


		public void setInvoicekey(String invoicekey) {
			Invoicekey = invoicekey;
		}


		

		public String getPhoneNumberIn() {
			return phoneNumberIn;
		}


		public void setPhoneNumberIn(String phoneNumberIn) {
			this.phoneNumberIn = phoneNumberIn;
		}


		public String getInvoiceDateIn() {
			return invoiceDateIn;
		}


		public void setInvoiceDateIn(String invoiceDateIn) {
			this.invoiceDateIn = invoiceDateIn;
		}


		public String getDateDueIn() {
			return dateDueIn;
		}


		public void setDateDueIn(String dateDueIn) {
			this.dateDueIn = dateDueIn;
		}


		public String getTotalDueIn() {
			return totalDueIn;
		}


		public void setTotalDueIn(String totalDueIn) {
			this.totalDueIn = totalDueIn;
		}


		public String getPaymentsReceivedIn() {
			return paymentsReceivedIn;
		}


		public void setPaymentsReceivedIn(String paymentsReceivedIn) {
			this.paymentsReceivedIn = paymentsReceivedIn;
		}


		public String getDebitsIn() {
			return debitsIn;
		}


		public void setDebitsIn(String debitsIn) {
			this.debitsIn = debitsIn;
		}


		public String getBalanceForwardIn() {
			return balanceForwardIn;
		}


		public void setBalanceForwardIn(String balanceForwardIn) {
			this.balanceForwardIn = balanceForwardIn;
		}


		public String getCurrentChargesIn() {
			return currentChargesIn;
		}


		public void setCurrentChargesIn(String currentChargesIn) {
			this.currentChargesIn = currentChargesIn;
		}


		public String getTotalDueByIn() {
			return totalDueByIn;
		}


		public void setTotalDueByIn(String totalDueByIn) {
			this.totalDueByIn = totalDueByIn;
		}


		public String getTotalDueAfterIn() {
			return totalDueAfterIn;
		}


		public void setTotalDueAfterIn(String totalDueAfterIn) {
			this.totalDueAfterIn = totalDueAfterIn;
		}


		
		


	
	 
	
	
}
