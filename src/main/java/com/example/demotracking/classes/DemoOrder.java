package com.example.demotracking.classes;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

public class DemoOrder {
	private int orderID;
	private String client;
	private String rfd;
	private String ard;
	private String accountManager;
	private String po;
	private String rr;
	private String rts;
	private String status; //"Active", "Internal", "Pullout", "Returned"
	private List<OrderDuration> schedule;
	private List<OrderItem> items;
	
	/***
	 * Creates a new DemoOrder with an empty schedule and no ordered items.
	 * @param orderID
	 * @param client
	 * @param rfd
	 * @param ard
	 * @param accountManager
	 * @param status
	 */
	public DemoOrder(int orderID, String client, String rfd, String ard, String accountManager, String po, String rr, 
			String rts, String status) {
		super();
		this.orderID = orderID;
		this.client = client;
		this.rfd = rfd;
		this.ard = ard;
		this.accountManager = accountManager;
		this.po = po;
		this.rr = rr;
		this.rts = rts;
		this.status = status;
		this.schedule = new ArrayList<>();
		this.items = new ArrayList<>();
	}

	public DemoOrder(int orderID, String client, String rfd, String ard, String accountManager, String po, String rr, 
			String rts, String status,
			List<OrderDuration> schedule, List<OrderItem> items) {
		super();
		this.orderID = orderID;
		this.client = client;
		this.rfd = rfd;
		this.ard = ard;
		this.accountManager = accountManager;
		this.po = po;
		this.rr = rr;
		this.rts = rts;
		this.status = status;
		this.schedule = schedule;
		this.items = items;
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

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String getRfd() {
		return rfd;
	}

	public void setRfd(String rfd) {
		this.rfd = rfd;
	}

	public String getArd() {
		return ard;
	}

	public void setArd(String ard) {
		this.ard = ard;
	}

	public String getAccountManager() {
		return accountManager;
	}

	public void setAccountManager(String accountManager) {
		this.accountManager = accountManager;
	}

	public String getPo() {
		return po;
	}

	public void setPo(String po) {
		this.po = po;
	}

	public String getRr() {
		return rr;
	}

	public void setRr(String rr) {
		this.rr = rr;
	}

	public String getRts() {
		return rts;
	}

	public void setRts(String rts) {
		this.rts = rts;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<OrderDuration> getSchedule() {
		return schedule;
	}

	public void setSchedule(List<OrderDuration> schedule) {
		this.schedule = schedule;
	}
	
	public void addSchedule(OrderDuration newSchedule) {
		this.schedule.add(newSchedule);
	}
	
	/***
	 * Attempts to find an OrderDuration of listID specified.
	 * @param listID - listID of the desired OrderDuration
	 * @return null if OrderDuration is not found, and the corresponding OrderDuration otherwise
	 */
	public OrderDuration getScheduleByListID(int listID) {
		for (OrderDuration od : schedule) {
			if (od.getListID() == listID) return od;
		}
		return null;
	}
	
	/***
	 * Returns the latest schedule's start date.
	 * @return
	 */
	public Date getLatestStartDate() {
		// assume the last schedule in the list is the latest one
		return this.schedule.get(this.schedule.size() - 1).getStartDate();
	}
	
	/***
	 * Returns the latest schedule's end date.
	 * @return
	 */
	public Date getLatestDueDate() {
		// assume the last schedule in the list is the latest one
		return this.schedule.get(this.schedule.size() - 1).getEndDate();
	}
	
	/***
	 * Removes the OrderDuration at index specified.
	 * @param index - Index between 0 and the length of the DemoOrder's OrderDuration list
	 */
	public void removeSchedule(int index) {
		this.schedule.remove(index);
	}
	
	/***
	 * Attempts to remove an OrderDuration with the listID specified.
	 * @param listID
	 */
	public void removeScheduleByListID(int listID) {
		for (OrderDuration od : schedule) {
			if (od.getListID() == listID) {
				this.schedule.remove(od);
				break;
			}
		}
	}
	
	/***
	 * Removes the OrderDuration specified. Parameter must be supplied using this class's getScheduleByListID function.
	 * @param index - an OrderDuration from the Order's OrderDuration list
	 */
	public void removeSchedule(OrderDuration index) {
		this.schedule.remove(index);
	}
	
	public List<OrderItem> getItems() {
		return items;
	}

	public void setItems(List<OrderItem> items) {
		this.items = items;
	}
	
	public void addItem(OrderItem newItem) {
		this.items.add(newItem);
	}
	
	/***
	 * Attempts to find an OrderItem of itemID specified.
	 * @param itemID - itemID of the desired OrderItem
	 * @return null if OrderItem is not found, and the corresponding OrderItem otherwise
	 */
	public OrderItem getItemByItemID(int itemID) {
		for (OrderItem item : items) {
			if (item.getItemID() == itemID) return item;
		}
		return null;
	}
	
	/***
	 * Removes the OrderItem at index specified.
	 * @param index - Index between 0 and the length of the DemoOrder's OrderItem list
	 */
	public void removeItem(int index) {
		this.items.remove(index);
	}
	
	/***
	 * Attempts to remove an OrderItem with the itemID specified.
	 * @param itemID
	 */
	public void removeItemByItemID(int itemID) {
		for (OrderItem item : items) {
			if (item.getItemID() == itemID) {
				this.items.remove(item);
				break;
			}
		}
	}
	
	/***
	 * Removes the OrderItem specified. Parameter must be supplied using this class's getItemByItemID function.
	 * @param index - an OrderItem from the Order's OrderItem list
	 */
	public void removeItem(OrderItem index) {
		this.items.remove(index);
	}
}
