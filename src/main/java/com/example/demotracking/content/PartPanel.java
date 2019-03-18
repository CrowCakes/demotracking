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
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class PartPanel extends Panel {
	protected TextField partName = new TextField("Part");
	protected TextField serial = new TextField("Serial#");
	
	protected Button save = new Button("Save");
	protected Button cancel = new Button("Cancel");
	protected Button delete = new Button("Delete");
	protected HorizontalLayout orderButtons = new HorizontalLayout(save, cancel, delete);
	
	private Binder<OrderItemPart> binder = new Binder<>(OrderItemPart.class);
	private ConnectionManager manager;
	private ObjectConstructor constructor;
	private OrderForm parent;
	
	private OrderItemPart orderItemPart;
	
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
	
	public void setPart(OrderItemPart part) {
		this.orderItemPart = part;
		
		binder.setBean(this.orderItemPart);
		delete.setEnabled(this.orderItemPart.getListID() != 0);
		setVisible(true);
	}
	
	private void prepare_buttons() {
		save.addClickListener(e -> save());
		delete.addClickListener(e -> ConfirmDialog.show(this.getUI(), 
				"Confirmation", "Delete this Schedule?", "Yes", "No",
				new ConfirmDialog.Listener() {
					public void onClose(ConfirmDialog dialog) {
		        		if (dialog.isConfirmed()) {
		        			delete();
		        		}
		        		else {
		        			
		        		}
					}
				}
			)
		);
		cancel.addClickListener(e -> cancel());
	}

	private void bind_fields() {
		binder.bind(partName, OrderItemPart::getName, OrderItemPart::setName);
		binder.bind(serial, OrderItemPart::getSerial, OrderItemPart::setSerial);
		this.binder.bindInstanceFields(this);
	}
	
	private void save() {
		//a new part to add
		if (orderItemPart.getListID() == 0) {
			List<String> parameters = new ArrayList<>();
			parameters.add(orderItemPart.getItemIDStr());
			parameters.add(partName.getValue());
			
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
			
			String query = constructor.constructMessage("EditUnitPart", parameters);
			
			manager.connect();
			String result = manager.send(query);
			manager.disconnect();
			
			Notification.show("Edit Part", result, Notification.Type.HUMANIZED_MESSAGE);
		}
		
		parent.refreshForm();
		setVisible(false);
	}
	
	private void cancel() {
		setVisible(false);
	}
	
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
	
	private boolean isDigit(String s) {
		if (s.matches("[0-9]+")) return true;
		else return false;
	}
}
