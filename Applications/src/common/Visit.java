package common;

import java.sql.*;
import java.text.SimpleDateFormat;

public class Visit {
	private String visitId;
	private String customerId;
	private String technicianId;
	private java.util.Date start;
	private java.util.Date end;
	private String notes;
	
	public String getVisitId() {
		return visitId;
	}
	public void setVisitId(String visitId) {
		this.visitId = visitId;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getTechnicianId() {
		return technicianId;
	}
	public void setTechnicianId(String technicianId) {
		this.technicianId = technicianId;
	}
	public java.util.Date getStart() {
		return start;
	}
	public void setStart(Timestamp start) {
		this.start = start;
	}
	public java.util.Date getEnd() {
		return end;
	}
	public void setEnd(Timestamp end) {
		this.end = end;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	@Override
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MMM.dd 'at' HH:mm");
		return "id: " + visitId + "\tStart: " + sdf.format(start) + "\tEnd: " + sdf.format(end);
	}
}
