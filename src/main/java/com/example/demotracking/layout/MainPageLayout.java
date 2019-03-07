package com.example.demotracking.layout;

import com.example.demotracking.classes.DemoOrder;
import com.vaadin.navigator.View;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class MainPageLayout extends CssLayout implements View {
	protected Grid<DemoOrder> display_orders = new Grid<>();
	
	protected TextField serialFilter = new TextField();
	
	protected Button outstandingOrders = new Button("Outstanding Orders");
	protected Button internalOrders = new Button("In House Orders");
	protected Button dueOrders = new Button("Due Orders");
	protected Button pullOutOrders = new Button("Pulling-out Orders");
	protected Button returnedOrders = new Button("Returned Orders");
	protected Button addOrder = new Button("Add New");
	protected Button openReport = new Button("Generate Due Date Report");
	
	protected HorizontalLayout buttons = new HorizontalLayout(serialFilter, addOrder, openReport);
	protected HorizontalLayout viewButtons = new HorizontalLayout(outstandingOrders,
			internalOrders, dueOrders, pullOutOrders, returnedOrders);
	protected VerticalLayout layout = new VerticalLayout(buttons, display_orders, viewButtons);
}
