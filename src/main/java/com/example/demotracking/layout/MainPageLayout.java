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
	
	protected Button refresh = new Button("Refresh");
	protected Button addOrder = new Button("Add New");
	
	protected HorizontalLayout buttons = new HorizontalLayout(serialFilter, refresh, addOrder);
	protected VerticalLayout layout = new VerticalLayout(buttons, display_orders);
}
