package com.example.demotracking.layout;

import com.example.demotracking.classes.OrderDuration;
import com.example.demotracking.classes.OrderItem;
import com.example.demotracking.classes.OrderItemPart;
import com.example.demotracking.content.ItemPanel;
import com.example.demotracking.content.PartPanel;
import com.example.demotracking.content.SchedulePanel;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class OrderFormLayout extends Panel {
	protected TextField orderID = new TextField("Order ID#");
	protected TextField client = new TextField("Client");
	protected TextField rfd = new TextField("RFD#");
	protected TextField ard = new TextField("ARD#");
	protected TextField accountManager = new TextField("Account Manager");
	protected NativeSelect<String> status = new NativeSelect<>("Status");
	protected HorizontalLayout orderInfo = new HorizontalLayout(orderID, client, rfd, ard, accountManager, status);
	
	protected Grid<OrderDuration> schedule = new Grid<>();
	protected Grid<OrderItem> items = new Grid<>();
	protected Grid<OrderItemPart> parts = new Grid<>();
	
	protected Button save = new Button("Save");
	protected Button cancel = new Button("Cancel");
	protected Button delete = new Button("Delete");
	protected HorizontalLayout orderButtons = new HorizontalLayout(save, cancel, delete);
	
	protected Button addSchedule = new Button("Add New Schedule");
	protected SchedulePanel schedulePanel;
	
	protected Button addItem = new Button("Add Unit");
	protected ItemPanel itemPanel;
	
	protected Button addPart = new Button("Add Unit Part");
	protected PartPanel partPanel;
	
	protected VerticalLayout layout;
}
