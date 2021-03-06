package com.example.demotracking.content;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.joda.time.DateTime;

import com.example.demotracking.classes.ConnectionManager;
import com.example.demotracking.classes.DemoOrder;
import com.example.demotracking.classes.ObjectConstructor;
import com.example.demotracking.classes.ReportGenerator;
import com.example.demotracking.layout.MainPageLayout;
import com.example.demotracking.classes.OnDemandFileDownloader;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

public class MainPage extends MainPageLayout {
	private ConnectionManager manager = new ConnectionManager();
	private ObjectConstructor constructor = null;
	private ReportGenerator generator = new ReportGenerator();
	
	private OrderForm orderForm;
	private VerticalLayout finalLayout;
	
	/*
	 * 0 = Outstanding
	 * 1 = Internal
	 * 2 = Due
	 * 3 = Pullout
	 * 4 = Returned
	 */
	int current_view = 0;
	
	int count = 0;
	int MAX_LIMIT = 20;
	int limit = MAX_LIMIT;
	int offset = 0;

	/**
	 * Prepares the UI elements for the page.
	 * @param user
	 * @param constructor
	 */
	public MainPage(String user, ObjectConstructor constructor) {
		this.constructor = constructor;
		orderForm = new OrderForm(this, this.constructor);
		
		prepareButtons();
		prepareGrid(user);
		preparePagination();
		
		finalLayout = new VerticalLayout(layout, new VerticalLayout(orderForm));
		finalLayout.setMargin(false);
		addComponents(finalLayout);
		refreshView(0);
	}
	
	/**
	 * Prepares the buttons and display to be used for the page display system.
	 */
	private void preparePagination() {
		Button previous = new Button(String.format("Previous %d", MAX_LIMIT)); 
		Button next = new Button(String.format("Next %d", MAX_LIMIT));
		
		previous.addClickListener(e -> {
        	offset = (offset - limit < 0) ? 0 : offset - limit;
        	limit = (offset + limit > count) ? count - offset : limit;
        	displayNew(offset, limit, current_view);
        	
        	display_count.setValue(String.format("%d-%d of %d", offset, offset+limit, count));
        	limit = MAX_LIMIT;
        });
		next.addClickListener(e -> {
        	offset = (offset + limit > count) ? offset : offset + limit;
        	limit = (offset + limit > count) ? count - offset : limit;
        	displayNew(offset, limit, current_view);
        	
        	display_count.setValue(String.format("%d-%d of %d", offset, offset+limit, count));
        	limit = MAX_LIMIT;
        });
		
		pagination.addComponents(previous, display_count, next);
	}
	
	/**
	 * Prepares the filter and the display grid, as well as grid interaction with the OrderForm.
	 * @param user
	 */
	private void prepareGrid(String user) {
		display_orders.addColumn(DemoOrder::getClient).setCaption("Client");
		display_orders.addColumn(DemoOrder::getRfd).setCaption("RFD");
		display_orders.addColumn(DemoOrder::getArd).setCaption("ARD");
		display_orders.addColumn(DemoOrder::getAccountManager).setCaption("AccountManager");
		display_orders.addColumn(DemoOrder::getStatus).setCaption("Status");
		display_orders.addColumn(DemoOrder::getLatestStartDate).setCaption("Start Date");
		display_orders.addColumn(DemoOrder::getLatestDueDate).setCaption("Due Date");
		
		display_orders.setHeight("475px");
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
		serialFilter.addValueChangeListener(e -> {
			if (!serialFilter.getValue().isEmpty()) filterView();
			else refreshView(current_view);
		});
		serialFilter.setValueChangeMode(ValueChangeMode.LAZY);
	}
	
	/**
	 * Prepares the functions that will trigger when the various UI buttons are pressed.
	 */
	private void prepareButtons() {
		outstandingOrders.addClickListener(e -> refreshView(0));
		internalOrders.addClickListener(e -> refreshView(1));
		dueOrders.addClickListener(e -> refreshView(2));
		pullOutOrders.addClickListener(e -> refreshView(3));
		returnedOrders.addClickListener(e -> refreshView(4));
		
		addOrder.addClickListener(e -> {
			layout.setVisible(false);
			orderForm.setOrder(newOrder());
			}
		);
		
		openReport.addClickListener(e -> {
			openReport.setEnabled(false);
			Notification.show("Notice", "Please refresh the page after the report is generated", Notification.Type.TRAY_NOTIFICATION);
		});
		OnDemandFileDownloader.OnDemandStreamResource resource = new  OnDemandFileDownloader.OnDemandStreamResource()
        {
            @Override
            public String getFilename()
            {
            	return String.format("Due Outstanding Demo - %s.xls", new Date(DateTime.now().getMillis()));
            }

            @Override
            public InputStream getStream()
            {	
                return new ByteArrayInputStream(
                		generator.generateReport(manager)
                		);
            }
         };

         OnDemandFileDownloader downloader = new OnDemandFileDownloader(
        		 resource);
         downloader.extend(openReport);
	}
	
	/**
	 * Refreshes the display grid with DemoOrders whose Serial# contains the user input.
	 */
	private void filterView() {
		List<String> foo = Arrays.asList(serialFilter.getValue().split("\\s*/\\s*"));
		foo.replaceAll(string -> {
			String temp = String.format(".*%s.*", string);
			return temp;
		});
		String parameter = String.join("|", foo);
		
		List<DemoOrder> update = new ArrayList<>();
		update = constructor.filterOrders(manager, parameter);
		
		display_orders.setItems(update);
		pagination.setVisible(false);
	}
	
	/**
	 * Refreshes the display grid with the list of active DemoOrders.
	 */
	public void refreshView(int option) {
		List<DemoOrder> update = new ArrayList<>();
		current_view = option;
		offset = 0;
		
		count = constructor.getOrderCount(manager, option);
		
		limit = (offset + limit > count) ? count - offset : limit;
		
		switch (option) {
		case 0:
			update = constructor.constructOrders(manager, offset, limit);
			break;
		case 1:
			update = constructor.constructInHouseOrders(manager, offset, limit);
			break;
		case 2:
			update = constructor.constructDueOrdersNoItems(manager, offset, limit);
			break;
		case 3:
			update = constructor.constructPullOutOrders(manager, offset, limit);
			break;
		case 4:
			update = constructor.constructReturnedOrders(manager, offset, limit);
			break;
		}
		
		display_orders.setItems(update);
		display_count.setValue(String.format("%d-%d of %d", offset, offset+limit, count));
		pagination.setVisible(true);
		limit = MAX_LIMIT;
	}
	
	/** 
	 * Displays the next/previous page of active DemoOrders.
	 * @param offset
	 * @param limit
	 */
	private void displayNew(int offset, int limit, int option) {
		List<DemoOrder> update = new ArrayList<>();
		
		switch (option) {
		case 0:
			update = constructor.constructOrders(manager, offset, limit);
			break;
		case 1:
			update = constructor.constructInHouseOrders(manager, offset, limit);
			break;
		case 2:
			update = constructor.constructDueOrdersNoItems(manager, offset, limit);
			break;
		case 3:
			update = constructor.constructPullOutOrders(manager, offset, limit);
			break;
		case 4:
			update = constructor.constructReturnedOrders(manager, offset, limit);
			break;
		}
		
		display_orders.setItems(update);
	}
	
	/**
	 * Sets the current page to be visible.
	 */
	public void setLayoutVisible() {
		layout.setVisible(true);
	}
	
	/**
	 * Returns a dummy DemoOrder. To be used when making a new DemoOrder.
	 * @return
	 */
	private DemoOrder newOrder() {
		return new DemoOrder(0, "", "", "", "", "", "", "", "Active");
	}
}
