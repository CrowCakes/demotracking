package com.example.demotracking.content;

import java.util.ArrayList;
import java.util.List;

import com.example.demotracking.classes.ConnectionManager;
import com.example.demotracking.classes.DemoOrder;
import com.example.demotracking.classes.ObjectConstructor;
import com.example.demotracking.layout.MainPageLayout;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;

public class MainPage extends MainPageLayout {
	private ConnectionManager manager = new ConnectionManager();
	private ObjectConstructor constructor = null;
	
	private OrderForm orderForm;
	private VerticalLayout finalLayout;

	public MainPage(String user, ObjectConstructor constructor) {
		this.constructor = constructor;
		orderForm = new OrderForm(this, this.constructor);
		
		prepareButtons();
		prepareGrid(user);
		
		finalLayout = new VerticalLayout(layout, new VerticalLayout(orderForm));
		finalLayout.setMargin(false);
		addComponents(finalLayout);
		refreshView();
	}
	
	private void prepareGrid(String user) {
		display_orders.addColumn(DemoOrder::getClient).setCaption("Client");
		display_orders.addColumn(DemoOrder::getRfd).setCaption("RFD");
		display_orders.addColumn(DemoOrder::getArd).setCaption("ARD");
		display_orders.addColumn(DemoOrder::getAccountManager).setCaption("AccountManager");
		display_orders.addColumn(DemoOrder::getStatus).setCaption("Status");
		display_orders.addColumn(DemoOrder::getLatestStartDate).setCaption("Start Date");
		display_orders.addColumn(DemoOrder::getLatestDueDate).setCaption("Due Date");
		
		display_orders.setHeight("550px");
		display_orders.setWidth("1125px");
		display_orders.setSelectionMode(Grid.SelectionMode.SINGLE);
		
		if (user.equals("Admin")) {
            this.display_orders.asSingleSelect().addValueChangeListener(event -> {
                if (event.getValue() == null) {
                	orderForm.setVisible(false);
                	layout.setVisible(true);
                } else {
                	layout.setVisible(false);
                	orderForm.setOrder(event.getValue());
                }
            });
        }
		
		serialFilter.setPlaceholder("Search by Unit Serial#");
		serialFilter.addValueChangeListener(e -> filterView());
		serialFilter.setValueChangeMode(ValueChangeMode.LAZY);
	}
	
	private void prepareButtons() {
		refresh.addClickListener(e -> refreshView());
		addOrder.addClickListener(e -> {
			layout.setVisible(false);
			orderForm.setOrder(newOrder());
			}
		);
	}
	
	private void filterView() {
		List<DemoOrder> update = new ArrayList<>();
		update = constructor.filterOrders(manager, serialFilter.getValue());
		
		display_orders.setItems(update);
	}
	
	public void refreshView() {
		List<DemoOrder> update = new ArrayList<>();
		update = constructor.constructOrders(manager);
		
		display_orders.setItems(update);
	}
	
	public void setLayoutVisible() {
		layout.setVisible(true);
	}
	
	private DemoOrder newOrder() {
		return new DemoOrder(0, "", "", "", "", "");
	}
}
