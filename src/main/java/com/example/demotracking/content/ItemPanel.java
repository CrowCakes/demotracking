package com.example.demotracking.content;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.dialogs.ConfirmDialog;

import com.example.demotracking.classes.ConnectionManager;
import com.example.demotracking.classes.DemoOrder;
import com.example.demotracking.classes.ObjectConstructor;
import com.example.demotracking.classes.OrderItem;
import com.example.demotracking.classes.OrderItemPart;
import com.vaadin.data.Binder;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class ItemPanel extends Panel {
	protected TextField name = new TextField("Item Name");
	protected TextField quantity = new TextField("Quantity");
	protected TextArea serial = new TextArea("Serial#");
	protected TextField source = new TextField("Source");
	protected TextArea itemRemarks = new TextArea("Item Remarks");
	protected NativeSelect<String> itemStatus = new NativeSelect<>("Item Status");
	protected VerticalLayout itemLayout = new VerticalLayout(
			new HorizontalLayout(name),
			new HorizontalLayout(quantity, serial),
			new HorizontalLayout(source, itemStatus),
			itemRemarks);
	
	protected Button save = new Button("Save");
	protected Button cancel = new Button("Cancel");
	protected Button delete = new Button("Delete");
	protected HorizontalLayout orderButtons = new HorizontalLayout(save, cancel, delete);
	
	private Binder<OrderItem> binder = new Binder<>(OrderItem.class);
	private ConnectionManager manager;
	private ObjectConstructor constructor;
	private OrderForm parent;

	private OrderItem orderItem;
	
	/**
	 * Prepares the UI elements for this form.
	 * @param parent
	 * @param manager
	 * @param constructor
	 */
	public ItemPanel(OrderForm parent, ConnectionManager manager, ObjectConstructor constructor) {
		this.parent = parent;
		this.manager = manager;
		this.constructor = constructor;
		
		bind_fields();
		prepare_buttons();

		this.save.setStyleName("primary");
		
		setContent(new VerticalLayout(itemLayout, orderButtons));
		
		setVisible(false);
	}
	
	/**
	 * Sets the given OrderItem to be edited by this form.
	 * @param item
	 */
	public void setItem(OrderItem item) {
		this.orderItem = item;
		
		binder.setBean(this.orderItem);
		save.setEnabled(true);
		delete.setEnabled(true);
		setVisible(true);
	}
	
	/**
	 * Prepares the UI elements to edit OrderItems.
	 */
	private void bind_fields() {
		itemStatus.setItems("Active", "Returned", "Purchased");
		
		binder.bind(name, OrderItem::getName, OrderItem::setName);
		binder.bind(quantity, OrderItem::getQuantityStr, OrderItem::setQuantity);
		binder.bind(serial, OrderItem::getSerial, OrderItem::setSerial);
		binder.bind(source, OrderItem::getSource, OrderItem::setSource);
		binder.bind(itemRemarks, OrderItem::getRemarks, OrderItem::setRemarks);
		binder.bind(itemStatus, OrderItem::getStatus, OrderItem::setStatus);
		this.binder.bindInstanceFields(this);
	}
	
	/**
	 * Prepares the functions triggered by pressing the various buttons on the form.
	 */
	private void prepare_buttons() {
		save.addClickListener(e -> {
			delete.setEnabled(false);
			save();
		});
		delete.addClickListener(e -> {
			save.setEnabled(false);
			
			ConfirmDialog.show(this.getUI(), 
					"Confirmation", "Delete this Schedule?", "Yes", "No",
					new ConfirmDialog.Listener() {
						public void onClose(ConfirmDialog dialog) {
			        		if (dialog.isConfirmed()) {
			        			delete();
			        		}
			        		else {
			        			save.setEnabled(true);
			        			delete.setEnabled(true);
			        		}
						}
					}
				);
		}
		);
		cancel.addClickListener(e -> cancel());
		
		save.setDisableOnClick(true);
		delete.setDisableOnClick(true);
	}
	
	/**
	 * Sends the entered OrderItem data to be inserted/edited into the database.
	 */
	private void save() {
		if (!validate()) Notification.show("Error", "Quantity must be a whole number", Notification.Type.ERROR_MESSAGE);
		
		//a new item to be added
		if (orderItem.getItemID() == 0) {
			List<String> parameters = new ArrayList<>();
			parameters.add(name.getValue());
			parameters.add(quantity.getValue());
			parameters.add(serial.getValue());
			parameters.add(source.getValue());
			parameters.add(itemRemarks.getValue());
			parameters.add(itemStatus.getValue());
			parameters.add(String.valueOf(parent.getOrderID()));
			
			String query = constructor.constructMessage("InsertNewItem", parameters);
			
			manager.connect();
			String result = manager.send(query);
			manager.disconnect();
			
			Notification.show("Create New", result, Notification.Type.HUMANIZED_MESSAGE);
		}
		//an existing item to be edited
		else {
			List<String> parameters = new ArrayList<>();
			parameters.add(orderItem.getItemIDStr());
			parameters.add(name.getValue());
			parameters.add(quantity.getValue());
			parameters.add(serial.getValue());
			parameters.add(source.getValue());
			parameters.add(itemRemarks.getValue());
			parameters.add(itemStatus.getValue());
			
			String query = constructor.constructMessage("EditItem", parameters);
			
			manager.connect();
			String result = manager.send(query);
			manager.disconnect();
			
			Notification.show("Edit Item", result, Notification.Type.HUMANIZED_MESSAGE);
		}
		
		parent.refreshForm();
		setVisible(false);
	}
	
	/**
	 * Hides the form.
	 */
	private void cancel() {
		setVisible(false);
	}
	
	/**
	 * Sends the OrderItem's ID for deletion from database.
	 */
	private void delete() {
		List<String> parameters = new ArrayList<>();
		parameters.add(orderItem.getItemIDStr());
		String query = constructor.constructMessage("DeleteItem", parameters);
		
		manager.connect();
		String result = manager.send(query);
		manager.disconnect();
		
		Notification.show("Delete Item", result, Notification.Type.HUMANIZED_MESSAGE);
		
		parent.refreshForm();
		setVisible(false);
	}
	
	/**
	 * Checks if the given String contains only digits 0 to 9.
	 * @param s
	 * @return
	 */
	private boolean isDigit(String s) {
		if (s.matches("[0-9]+")) return true;
		else return false;
	}
	
	/**
	 * Checks if the Quantity field contains only digits.
	 * @return
	 */
	private boolean validate() {
		if (!isDigit(quantity.getValue())) return false;
		return true;
	}
}
