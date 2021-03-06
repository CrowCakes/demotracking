package com.example.demotracking.content;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.dialogs.ConfirmDialog;

import com.example.demotracking.classes.ConnectionManager;
import com.example.demotracking.classes.ObjectConstructor;
import com.example.demotracking.classes.OrderItemPart;
import com.vaadin.data.Binder;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class PartPanel extends Panel {
	protected TextField partName = new TextField("Part");
	protected TextArea serial = new TextArea("Serial#");
	
	protected Button save = new Button("Save");
	protected Button cancel = new Button("Cancel");
	protected Button delete = new Button("Delete");
	protected HorizontalLayout orderButtons = new HorizontalLayout(save, cancel, delete);
	
	private Binder<OrderItemPart> binder = new Binder<>(OrderItemPart.class);
	private ConnectionManager manager;
	private ObjectConstructor constructor;
	private OrderForm parent;
	
	private OrderItemPart orderItemPart;
	
	/**
	 * Prepares the UI elements for this form.
	 * @param parent
	 * @param manager
	 * @param constructor
	 */
	public PartPanel(OrderForm parent, ConnectionManager manager, ObjectConstructor constructor) {
		this.parent = parent;
		this.manager = manager;
		this.constructor = constructor;
		
		bind_fields();
		prepare_buttons();

		this.save.setStyleName("primary");
		
		setContent(new VerticalLayout(partName, serial, orderButtons));
		
		setVisible(false);
	}
	
	/**
	 * Sets the given ItemPart to be edited by this form.
	 * @param part
	 */
	public void setPart(OrderItemPart part) {
		this.orderItemPart = part;
		
		binder.setBean(this.orderItemPart);
		save.setEnabled(true);
		delete.setEnabled(this.orderItemPart.getListID() != 0);
		setVisible(true);
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
	 * Prepares the UI elements to edit ItemParts.
	 */
	private void bind_fields() {
		binder.bind(partName, OrderItemPart::getName, OrderItemPart::setName);
		binder.bind(serial, OrderItemPart::getSerial, OrderItemPart::setSerial);
		this.binder.bindInstanceFields(this);
	}
	
	/**
	 * Sends the entered ItemPart data to be inserted/edited into the database.
	 */
	private void save() {
		//a new part to add
		if (orderItemPart.getListID() == 0) {
			List<String> parameters = new ArrayList<>();
			parameters.add(orderItemPart.getItemIDStr());
			parameters.add(partName.getValue());
			parameters.add((serial.getValue().isEmpty()) ? "n/a" : serial.getValue());
			
			String query = constructor.constructMessage("InsertNewUnitPart", parameters);
			
			manager.connect();
			String result = manager.send(query);
			manager.disconnect();
			
			Notification.show("Create New", result, Notification.Type.HUMANIZED_MESSAGE);
		}
		//an existing part to edit
		else {
			List<String> parameters = new ArrayList<>();
			parameters.add(orderItemPart.getListIDStr());
			parameters.add(orderItemPart.getItemIDStr());
			parameters.add(partName.getValue());
			parameters.add((serial.getValue().isEmpty()) ? "n/a" : serial.getValue());
			
			String query = constructor.constructMessage("EditUnitPart", parameters);
			
			manager.connect();
			String result = manager.send(query);
			manager.disconnect();
			
			Notification.show("Edit Part", result, Notification.Type.HUMANIZED_MESSAGE);
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
	 * Sends the ItemPart's ID for deletion from database.
	 */
	private void delete() {
		List<String> parameters = new ArrayList<>();
		parameters.add(orderItemPart.getListIDStr());
		String query = constructor.constructMessage("DeleteUnitPart", parameters);
		
		manager.connect();
		String result = manager.send(query);
		manager.disconnect();
		
		Notification.show("Delete Part", result, Notification.Type.HUMANIZED_MESSAGE);
		
		parent.refreshForm();
		setVisible(false);
	}
}
