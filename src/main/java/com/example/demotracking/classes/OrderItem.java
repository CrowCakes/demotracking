package com.example.demotracking.classes;

import java.util.List;

public class OrderItem {
	private int itemID;
	private String name;
	private int quantity;
	private String serial;
	private String source;
	private String remarks;
	private String status; //"Leased", "Returned"
	private List<OrderItemPart> parts = null;
	
	public OrderItem(int itemID, String name, int quantity, String serial, String source, String remarks,
			String status) {
		super();
		this.itemID = itemID;
		this.name = name;
		this.quantity = quantity;
		this.serial = serial;
		this.source = source;
		this.remarks = remarks;
		this.status = status;
		this.parts = null;
	}

	public OrderItem(int itemID, String name, int quantity, String serial, String source, String remarks, String status,
			List<OrderItemPart> parts) {
		super();
		this.itemID = itemID;
		this.name = name;
		this.quantity = quantity;
		this.serial = serial;
		this.source = source;
		this.remarks = remarks;
		this.status = status;
		this.parts = parts;
	}
	
	@Override
	/***
	 * Returns the quantity and name of the OrderItem as a String
	 */
	public String toString() {
		return String.format("%s x %s", quantity, name);
	}

	public int getItemID() {
		return itemID;
	}

	public void setItemID(int itemID) {
		this.itemID = itemID;
	}
	
	public String getItemIDStr() {
		return String.valueOf(itemID);
	}

	public void setItemID(String itemID) {
		this.itemID = Integer.parseInt(itemID);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public String getQuantityStr() {
		return String.valueOf(quantity);
	}

	public void setQuantity(String quantity) {
		this.quantity = Integer.parseInt(quantity);
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<OrderItemPart> getParts() {
		return parts;
	}

	public void setParts(List<OrderItemPart> parts) {
		this.parts = parts;
	}
	
	/***
	 * Attempts to find the OrderItemPart with specified listID.
	 * @param listID
	 * @return null if OrderItemPart is not found, and the corresponding OrderItemPart otherwise
	 */
	public OrderItemPart getPartByListID(int listID) {
		for (OrderItemPart part : parts) {
			if (part.getListID() == listID) return part;
		}
		return null;
	}
	
	public void insertPart(OrderItemPart part) {
		this.parts.add(part);
	}
	
	public void deletePart(int listID) {
		for (OrderItemPart part : parts) {
			if (part.getListID() == listID) {
				this.parts.remove(part);
				break;
			}
		}
	}
	
	public void deletePart(OrderItemPart part) {
		this.parts.remove(part);
	}
	
	/***
	 * Returns whether or not the OrderItem has OrderItemParts.
	 * @return False if the list of OrderItemParts is null, True if it is not null
	 */
	public boolean isUnit() {
		return (this.parts == null) ? false : true;
	}
}
