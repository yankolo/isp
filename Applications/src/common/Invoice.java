package common;

import java.sql.*;

public class Invoice {
	private String invoiceId;
	private String customerName;
	private double costPlan;
	private double overage;
	private double otherServices;
	private double gst;
	private double qst;
	private double previousBalance;
	private double newBalance;
	private double total;
	private Date dueDate;
	private Date startInvoicingPeriod;
	private Date endInvoicingPeriod;

	public String getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerId) {
		this.customerName = customerId;
	}

	public double getCostPlan() {
		return costPlan;
	}

	public void setCostPlan(double costPlan) {
		this.costPlan = costPlan;
	}

	public double getOverage() {
		return overage;
	}

	public void setOverage(double overage) {
		this.overage = overage;
	}

	public double getOtherServices() {
		return otherServices;
	}

	public void setOtherServices(double otherServices) {
		this.otherServices = otherServices;
	}

	public double getGst() {
		return gst;
	}

	public void setGst(double gst) {
		this.gst = gst;
	}

	public double getQst() {
		return qst;
	}

	public void setQst(double qst) {
		this.qst = qst;
	}

	public double getPreviousBalance() {
		return previousBalance;
	}

	public void setPreviousBalance(double previousBalance) {
		this.previousBalance = previousBalance;
	}

	public double getNewBalance() {
		return newBalance;
	}

	public void setNewBalance(double newBalance) {
		this.newBalance = newBalance;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public Date getStartInvoicingPeriod() {
		return startInvoicingPeriod;
	}

	public void setStartInvoicingPeriod(Date startInvoicingPeriod) {
		this.startInvoicingPeriod = startInvoicingPeriod;
	}

	public Date getEndInvoicingPeriod() {
		return endInvoicingPeriod;
	}

	public void setEndInvoicingPeriod(Date endInvoicingPeriod) {
		this.endInvoicingPeriod = endInvoicingPeriod;
	}
}
