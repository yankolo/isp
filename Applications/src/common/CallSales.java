package common;

import java.sql.*;

public class CallSales {
	private String callId;
	private String customerId;
	private String representativeId;
	private Date date;
	private String sale;
	
	private Customer customer;
	private Employee representative;
	
	public String getCallId() {
		return callId;
	}
	public void setCallId(String callId) {
		this.callId = callId;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getRepresentativeId() {
		return representativeId;
	}
	public void setRepresentativeId(String representativeId) {
		this.representativeId = representativeId;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getSale() {
		return sale;
	}
	public void setSale(String sale) {
		this.sale = sale;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public Employee getRepresentative() {
		return representative;
	}
	public void setRepresentative(Employee representative) {
		this.representative = representative;
	}
}
