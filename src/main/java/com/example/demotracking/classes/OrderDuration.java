package com.example.demotracking.classes;

import java.sql.Date;
import java.time.LocalDate;

public class OrderDuration {
	private int listID;
	private int orderID;
	private Date startDate;
	private Date endDate;
	private String remarks = "n/a";
	
	public OrderDuration(int listID, int orderID, Date startDate, Date endDate) {
		super();
		this.listID = listID;
		this.orderID = orderID;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public OrderDuration(int listID, int orderID, Date startDate, Date endDate, String remarks) {
		super();
		this.listID = listID;
		this.orderID = orderID;
		this.startDate = startDate;
		this.endDate = endDate;
		this.remarks = (remarks.isEmpty()) ? "n/a" : remarks;
	}
	
	public int getOrderID() {
		return orderID;
	}

	public void setOrderID(int orderID) {
		this.orderID = orderID;
	}
	
	public String getOrderIDStr() {
		return String.valueOf(orderID);
	}

	public void setOrderID(String orderID) {
		this.orderID = Integer.parseInt(orderID);
	}

	public int getListID() {
		return listID;
	}

	public void setListID(int listID) {
		this.listID = listID;
	}
	
	public String getListIDStr() {
		return String.valueOf(listID);
	}

	public void setListID(String listID) {
		this.listID = Integer.parseInt(listID);
	}

	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public LocalDate getStartDateLocal() {
		return startDate.toLocalDate();
	}
	public void setStartDate(LocalDate startDate) {
		this.startDate = Date.valueOf(startDate);
	}
	public LocalDate getEndDateLocal() {
		return endDate.toLocalDate();
	}
	public void setEndDate(LocalDate endDate) {
		this.endDate = Date.valueOf(endDate);
	}
	
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
}
