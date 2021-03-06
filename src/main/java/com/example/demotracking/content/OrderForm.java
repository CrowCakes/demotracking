package com.example.demotracking.content;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.vaadin.dialogs.ConfirmDialog;

import java.sql.Date;

import com.example.demotracking.classes.ConnectionManager;
import com.example.demotracking.classes.DemoOrder;
import com.example.demotracking.classes.ObjectConstructor;
import com.example.demotracking.classes.OrderDuration;
import com.example.demotracking.classes.OrderItem;
import com.example.demotracking.classes.OrderItemPart;
import com.example.demotracking.layout.OrderFormLayout;
import com.vaadin.data.Binder;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

public class OrderForm extends OrderFormLayout {
	
	private DemoOrder order;
	
	private ConnectionManager manager = new ConnectionManager();
	private ObjectConstructor constructor;
	private Binder<DemoOrder> binder = new Binder<>(DemoOrder.class);
	private MainPage parent;
	
	/**
	 * Prepares the UI elements for this form.
	 * @param parent
	 * @param constructor
	 */
	public OrderForm(MainPage parent, ObjectConstructor constructor) {
		this.parent = parent;
		this.constructor = constructor;
		
		schedulePanel = new SchedulePanel(this, manager, this.constructor);
		itemPanel = new ItemPanel(this, manager, this.constructor);
		partPanel = new PartPanel(this, manager, this.constructor);
		
		layout = new VerticalLayout(orderInfo, 
				new HorizontalLayout(
						new VerticalLayout(schedule, addSchedule, schedulePanel), 
						new VerticalLayout(items, addItem, itemPanel), 
						new VerticalLayout(parts, addPart, partPanel)
						), 
				orderButtons);
		
		prepare_grids();
		bind_fields();
		prepare_buttons();
		
		layout.setSizeUndefined();
		setHeight("600px");
		setWidth("1125px");
		setContent(layout);

		this.save.setStyleName("primary");
		
		setVisible(false);
	}

	/**
	 * Sets the given DemoOrder to be edited by this form.
	 * @param order
	 */
	public void setOrder(DemoOrder order) {
		this.order = order;
		
		schedule.setItems(this.order.getSchedule());

		if (this.order.getOrderID() != 0) {
			// retrieve this order's items and unit parts
			this.order.setItems(constructor.constructItems(manager, this.order.getOrderID()));
			items.setItems(this.order.getItems());
		}
		else {
			this.order.setItems(Collections.emptyList());
			items.setItems(Collections.emptyList());
		}
		
		binder.setBean(this.order);
		
		//if the order is new do not let user add schedules, parts, or items
		addSchedule.setEnabled(this.order.getOrderID() != 0);
		addItem.setEnabled(this.order.getOrderID() != 0);
		addPart.setEnabled(this.order.getOrderID() != 0);
		
		schedulePanel.setVisible(false);
		itemPanel.setVisible(false);
		partPanel.setVisible(false);
		
		schedule.deselectAll();
		items.deselectAll();
		parts.deselectAll();
		
		save.setEnabled(true);
		delete.setEnabled(true);
		
		setVisible(true);
	}
	
	/**
	 * Prepares the UI elements to edit DemoOrders.
	 */
	private void bind_fields() {
		orderID.setEnabled(false);
		status.setItems("Active", "Internal", "Pullout", "Returned");
		
		binder.bind(orderID, DemoOrder::getOrderIDStr, DemoOrder::setOrderID);
		binder.bind(client, DemoOrder::getClient, DemoOrder::setClient);
		binder.bind(rfd, DemoOrder::getRfd, DemoOrder::setRfd);
		binder.bind(ard, DemoOrder::getArd, DemoOrder::setArd);
		binder.bind(accountManager, DemoOrder::getAccountManager, DemoOrder::setAccountManager);
		binder.bind(po, DemoOrder::getPo, DemoOrder::setPo);
		binder.bind(rr, DemoOrder::getRr, DemoOrder::setRr);
		binder.bind(rts, DemoOrder::getRts, DemoOrder::setRts);
		binder.bind(status, DemoOrder::getStatus, DemoOrder::setStatus);
		
		this.binder.bindInstanceFields(this);
	}
	
	/**
	 * Prepares the functions triggered by pressing the various buttons on the form.
	 */
	private void prepare_buttons() {
		save.addClickListener(e -> save());
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
		
		addSchedule.addClickListener(e -> {
			schedule.deselectAll();
			itemPanel.setVisible(false);
			partPanel.setVisible(false);
			
			schedulePanel.setSchedule(
					new OrderDuration(
							0, 
							order.getOrderID(), 
							Date.valueOf(LocalDate.now()), 
							Date.valueOf(LocalDate.now()), 
							"n/a")
					);
		});
		addItem.addClickListener(e -> {
			items.deselectAll();
			schedulePanel.setVisible(false);
			partPanel.setVisible(false);
			
			itemPanel.setItem(new OrderItem(0, "", 0, "", "", "", "Active"));
		});
		addPart.addClickListener(e -> {
			parts.deselectAll();
			schedulePanel.setVisible(false);
			itemPanel.setVisible(false);
			
			partPanel.setPart(
					new OrderItemPart(
							0, 
							items.asSingleSelect().getSelectedItem().get().getItemID(), 
							"",
							"n/a"));
		});
		
		addPart.setEnabled(false);
		
		save.setDisableOnClick(true);
		delete.setDisableOnClick(true);
	}
	
	/**
	 * Prepares the display grids and their interactions with each other and the other subforms.
	 */
	private void prepare_grids() {
		schedule.setWidth("300px");
		schedule.addColumn(OrderDuration::getStartDate).setCaption("Start Date");
		schedule.addColumn(OrderDuration::getEndDate).setCaption("End Date");
		
		items.addColumn(OrderItem::getSerial).setCaption("Serial#");
		items.addColumn(OrderItem::getName).setCaption("Item Name");
		
		parts.addColumn(OrderItemPart::getName).setCaption("Part Name");
		
		schedule.asSingleSelect().addValueChangeListener(event -> {
			if (event.getValue() == null) {
            	schedulePanel.setVisible(false);
            } else {
            	itemPanel.setVisible(false);
            	partPanel.setVisible(false);
            	schedulePanel.setSchedule(event.getValue());
            }
		});
		
		items.asSingleSelect().addValueChangeListener(event -> {
			if (event.getValue() == null) {
            	itemPanel.setVisible(false);
            	parts.setItems(Collections.emptyList());
            	
            	//hide parts since no item is selected
            	addPart.setEnabled(false);
            	partPanel.setVisible(false);
            } else {
            	schedulePanel.setVisible(false);
            	partPanel.setVisible(false);
            	itemPanel.setItem(event.getValue());
            	
            	//populate the grid of parts for the selected item
            	if (event.getValue().getParts() != null) parts.setItems(event.getValue().getParts());
            	else parts.setItems(Collections.emptyList());
            	
            	addPart.setEnabled(true);
            	//keep parts hidden still
            }
		});
		
		parts.asSingleSelect().addValueChangeListener(event -> {
			if (event.getValue() == null) {
            	partPanel.setVisible(false);
            } else {
            	itemPanel.setVisible(false);
            	schedulePanel.setVisible(false);
            	partPanel.setPart(event.getValue());
            }
		});
	}
	
	/**
	 * Sends the entered DemoOrder data to be inserted/edited into the database.
	 */
	private void save() {
		//it's a new order
		if (order.getOrderID() == 0) {
			List<String> parameters = new ArrayList<>();
			parameters.add(client.getValue());
			parameters.add(rfd.getValue());
			parameters.add(ard.getValue());
			parameters.add(accountManager.getValue());
			parameters.add(po.getValue());
			parameters.add(rr.getValue());
			parameters.add(rts.getValue());
			parameters.add(status.getValue());
			
			String query = constructor.constructMessage("InsertNewOrder", parameters);
			
			manager.connect();
			String result = manager.send(query);
			manager.disconnect();
			
			Notification.show("Create New", result, Notification.Type.HUMANIZED_MESSAGE);
		}
		//it's an existing order
		else {
			List<String> parameters = new ArrayList<>();
			parameters.add(orderID.getValue());
			parameters.add(client.getValue());
			parameters.add(rfd.getValue());
			parameters.add(ard.getValue());
			parameters.add(accountManager.getValue());
			parameters.add(po.getValue());
			parameters.add(rr.getValue());
			parameters.add(rts.getValue());
			parameters.add(status.getValue());
			
			String query = constructor.constructMessage("EditOrder", parameters);
			
			manager.connect();
			String result = manager.send(query);
			manager.disconnect();
			
			Notification.show("Edit Order", result, Notification.Type.HUMANIZED_MESSAGE);
		}
		
		parent.refreshView(0);
		setVisible(false);
		parent.setLayoutVisible();
	}
	
	/**
	 * Hides the form and reveals the main page.
	 */
	private void cancel() {
		setVisible(false);
		parent.setLayoutVisible();
	}
	
	/**
	 * Sends the DemoOrder's ID for deletion from database.
	 */
	private void delete() {
		List<String> parameters = new ArrayList<>();
		parameters.add(order.getOrderIDStr());
		String query = constructor.constructMessage("DeleteOrder", parameters);
		
		manager.connect();
		String result = manager.send(query);
		manager.disconnect();
		
		Notification.show("Delete Order", result, Notification.Type.HUMANIZED_MESSAGE);
		
		parent.refreshView(0);
		setVisible(false);
		parent.setLayoutVisible();
	}
	
	/**
	 * Checks if the given string contains only digits 0 to 9.
	 * @param s
	 * @return
	 */
	private boolean isDigit(String s) {
		if (s.matches("[0-9]+")) return true;
		else return false;
	}

	/**
	 * Refreshes the given Order's data.
	 */
	public void refreshForm() {
		this.setOrder(
				constructor.findOrder(manager, order.getOrderID()).get(0)
				);
	}
	
	/**
	 * Returns the form's current Order.
	 * @return
	 */
	public int getOrderID() {
		return order.getOrderID();
	}
	
}
