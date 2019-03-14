package com.example.demotracking.content;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.dialogs.ConfirmDialog;

import com.example.demotracking.classes.ConnectionManager;
import com.example.demotracking.classes.ObjectConstructor;
import com.example.demotracking.classes.OrderDuration;
import com.vaadin.data.Binder;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;

public class SchedulePanel extends Panel {
	protected DateField startDate = new DateField("Start Date");
	protected DateField endDate = new DateField("End Date");
	protected TextArea scheduleRemarks = new TextArea("Remarks");
	protected FormLayout scheduleForm = new FormLayout(startDate, endDate, scheduleRemarks);
	
	protected Button save = new Button("Save");
	protected Button cancel = new Button("Cancel");
	protected Button delete = new Button("Delete");
	protected HorizontalLayout orderButtons = new HorizontalLayout(save, cancel, delete);
	
	private Binder<OrderDuration> binder = new Binder<>(OrderDuration.class);
	private ConnectionManager manager;
	private ObjectConstructor constructor;
	private OrderForm parent;
	
	private OrderDuration orderDuration;
	
	public SchedulePanel(OrderForm parent, ConnectionManager manager, ObjectConstructor constructor) {
		this.parent = parent;
		this.manager = manager;
		this.constructor = constructor;
		
		bind_fields();
		prepare_buttons();

		this.save.setStyleName("primary");
		
		setContent(new VerticalLayout(scheduleForm, orderButtons));
		
		setVisible(false);
	}

	public void setSchedule(OrderDuration schedule) {
		this.orderDuration = schedule;
		binder.setBean(this.orderDuration);
		
		delete.setEnabled(this.orderDuration.getListID() != 0);
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
		binder.bind(startDate, OrderDuration::getStartDateLocal, OrderDuration::setStartDate);
		binder.bind(endDate, OrderDuration::getEndDateLocal, OrderDuration::setEndDate);
		binder.bind(scheduleRemarks, OrderDuration::getRemarks, OrderDuration::setRemarks);
		this.binder.bindInstanceFields(this);
	}
	
	private void save() {
		//a new schedule
		if (orderDuration.getListID() == 0) {
			List<String> parameters = new ArrayList<>();
			parameters.add(orderDuration.getOrderIDStr());
			parameters.add(startDate.getValue().toString());
			parameters.add(endDate.getValue().toString());
			parameters.add(scheduleRemarks.getValue());
			
			String query = constructor.constructMessage("InsertNewOrderDate", parameters);
			
			manager.connect();
			String result = manager.send(query);
			manager.disconnect();
			
			Notification.show("Create New", result, Notification.Type.HUMANIZED_MESSAGE);
		}
		//an existing schedule
		else {
			List<String> parameters = new ArrayList<>();
			parameters.add(orderDuration.getListIDStr());
			parameters.add(orderDuration.getOrderIDStr());
			parameters.add(startDate.getValue().toString());
			parameters.add(endDate.getValue().toString());
			parameters.add(scheduleRemarks.getValue());
			
			String query = constructor.constructMessage("EditOrderDate", parameters);
			
			manager.connect();
			String result = manager.send(query);
			manager.disconnect();
			
			Notification.show("Edit Schedule", result, Notification.Type.HUMANIZED_MESSAGE);
		}

		parent.refreshForm();
		setVisible(false);
	}
	
	private void cancel() {
		setVisible(false);
	}
	
	private void delete() {
		List<String> parameters = new ArrayList<>();
		parameters.add(orderDuration.getListIDStr());
		String query = constructor.constructMessage("DeleteOrderDate", parameters);
		
		manager.connect();
		String result = manager.send(query);
		manager.disconnect();
		
		Notification.show("Delete Schedule", result, Notification.Type.HUMANIZED_MESSAGE);
		
		parent.refreshForm();
		setVisible(false);
	}
	
	private boolean isDigit(String s) {
		if (s.matches("[0-9]+")) return true;
		else return false;
	}
}
