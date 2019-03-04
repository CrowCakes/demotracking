package com.example.demotracking.classes;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ObjectConstructor {
	
	private List<DemoOrder> parseOrders(String foo) {
		List<DemoOrder> parsed_data = new ArrayList<>();
		List<String> bar = new ArrayList<String>(Arrays.asList(foo.split("::\n")));
		
		List<String> foobar;
		DemoOrder previous = null;
		for (int i=0; i<bar.size(); i++) {
			foobar = new ArrayList<>(Arrays.asList(bar.get(i).split("\\s*::,\\s*")));

			//System.out.println("bar:");
			//System.out.println(bar.get(i));
			
			//System.out.println("foobar:");
			//System.out.println(foobar);
			
			if (i==0 && foobar.size() == 1) {
				// no results
				break;
			}
			
			/*
			System.out.println("-- parseOrders --");
			System.out.println("before:");
			System.out.println(bar.get(i));
			System.out.println("after:");
			System.out.println(foobar);
			System.out.println("-- nothing follows --");
			*/
			
			//initial condition
			if (previous == null) {
				previous = new DemoOrder(
						Integer.parseInt(foobar.get(0)),
						foobar.get(1),
						foobar.get(2),
						foobar.get(3),
						foobar.get(4),
						foobar.get(5)
						);
				previous.addSchedule(
						new OrderDuration(
								Integer.parseInt(foobar.get(6)),
								previous.getOrderID(),
								Date.valueOf(foobar.get(7)),
								Date.valueOf(foobar.get(8)),
								foobar.get(9)
								)
						);
			}
			//since the entries are sorted by orderID and come one after the other,
			//we can simply check if the previous and the current order is the same
			//if it is the same, just add schedule to the order to be added
			else if (previous.getOrderID() == Integer.parseInt(foobar.get(0))) {
				previous.addSchedule(
						new OrderDuration(
								Integer.parseInt(foobar.get(6)),
								previous.getOrderID(),
								Date.valueOf(foobar.get(7)),
								Date.valueOf(foobar.get(8)),
								foobar.get(9)
								)
						);
			}
			//if they are not the same, finish creating the previous order and insert it into the result array
			//also prep the new one
			else if (previous.getOrderID() != Integer.parseInt(foobar.get(0))) {
				/*
				System.out.println("-- parseOrders --");
				System.out.println(previous.getSchedule());
				System.out.println("-- nothing follows --\n");
				*/
				parsed_data.add(previous);
				
				/*
				System.out.println("-- parseOrders --");
				System.out.println("Creating a new order");
				System.out.println("-- nothing follows --\n");
				*/
				previous = new DemoOrder(
						Integer.parseInt(foobar.get(0)),
						foobar.get(1),
						foobar.get(2),
						foobar.get(3),
						foobar.get(4),
						foobar.get(5)
						);
				previous.addSchedule(
						new OrderDuration(
								Integer.parseInt(foobar.get(6)),
								previous.getOrderID(),
								Date.valueOf(foobar.get(7)),
								Date.valueOf(foobar.get(8)),
								foobar.get(9)
								)
						);
			}
			
			//if end of the list is reached, add the final order
			if (i == bar.size()-1) {
				/*
				System.out.println("-- parseOrders --");
				System.out.println(previous.getSchedule());
				System.out.println("-- nothing follows --\n");
				*/
				parsed_data.add(previous);
			}
		}
		
		return parsed_data;
	}
	
	private List<OrderItem> parseItems(String result, String resultParts) {
		List<OrderItem> parsed_data = new ArrayList<>();
		List<String> bar = new ArrayList<String>(Arrays.asList(result.split("::\n")));
		List<String> bar2 = new ArrayList<String>(Arrays.asList(resultParts.split("::\n")));
		
		List<String> foobar;
		for (int i=0; i<bar.size(); i++) {
			foobar = new ArrayList<>(Arrays.asList(bar.get(i).split("\\s*::,\\s*")));
			
			if (i==0 && foobar.size() == 1) {
				// no results
				break;
			}
			
			//at first, all items will be assumed not to be units
			parsed_data.add(
					new OrderItem(
							Integer.parseInt(foobar.get(0)),
							foobar.get(1),
							Integer.parseInt(foobar.get(2)),
							foobar.get(3),
							foobar.get(4),
							foobar.get(5),
							foobar.get(6)
							)
					);
		}
		
		foobar = null;
		for (int i=0; i<bar2.size(); i++) {
			foobar = new ArrayList<>(Arrays.asList(bar2.get(i).split("\\s*::,\\s*")));
			
			if (i==0 && foobar.size() == 1) {
				// no results
				break;
			}
			
			//add the part to its respective item
			for (OrderItem j : parsed_data) {
				if (j.getItemID() == Integer.parseInt(foobar.get(1))) {
					//make sure to give the item a list of parts to put its parts in
					if (!j.isUnit()) j.setParts(new ArrayList<>());
					//finally put the part in
					j.insertPart(
							new OrderItemPart(
									Integer.parseInt(foobar.get(0)),
									Integer.parseInt(foobar.get(1)),
									foobar.get(2)
									)
							);
				}
			}
		}
		
		return parsed_data;
	}

	public List<DemoOrder> constructOrders(ConnectionManager manager) {
		manager.connect();
		String result = manager.send(constructMessage("ViewOrders", new ArrayList<String>()));
		manager.disconnect();
		
		return parseOrders(result);
	}
	
	/***
	 * Attempts to find a DemoOrder with the specified orderID.
	 * @param manager
	 * @param orderID
	 * @return An empty list if no such DemoOrder is found, or a list containing exactly one matching DemoOrder
	 */
	public List<DemoOrder> findOrder(ConnectionManager manager, int orderID) {
		List<String> parameters = new ArrayList<>();
		parameters.add(String.valueOf(orderID));
		
		manager.connect();
		String result = manager.send(constructMessage("FindOrder", parameters));
		manager.disconnect();
		
		return parseOrders(result);
	}
	
	public List<DemoOrder> filterOrders(ConnectionManager manager, String filter) {
		List<String> parameters = new ArrayList<>();
		parameters.add(filter);
		
		manager.connect();
		String result = manager.send(constructMessage("FilterOrder", parameters));
		manager.disconnect();
		
		return parseOrders(result);
	}
	
	/***
	 * Constructs the list of OrderItems belonging to a DemoOrder with the specified orderID.
	 * @param manager
	 * @param orderID
	 * @return
	 */
	public List<OrderItem> constructItems(ConnectionManager manager, int orderID) {
		List<String> parameters = new ArrayList<>();
		parameters.add(String.valueOf(orderID));
		
		manager.connect();
		String result = manager.send(constructMessage("ViewItemsSingle", parameters));
		manager.disconnect();
		
		manager.connect();
		String resultParts = manager.send(constructMessage("ViewUnits", parameters));
		manager.disconnect();
		
		return parseItems(result, resultParts);
	}
	
	/*
	public List<OrderItemPart> constructParts(ConnectionManager manager, int itemID) {
		List<String> parameters = new ArrayList<>();
		parameters.add(String.valueOf(itemID));
		
		manager.connect();
		String result = manager.send(constructMessage("ViewUnits"));
		manager.disconnect();
		
		return parseParts(result);
	}
	*/
	
	public String constructMessage(String query, List<String> parameters) {
		String result = "";
		
		result = result.concat("STARTMESSAGE\f");
		
		result = result.concat("STARTCOMMAND\f");
		
		result = result.concat(query.concat("\f"));

		if (!parameters.isEmpty()) {
			result = result.concat("WITHINPUT\f");
			
			for (String input : parameters) {
				result = result.concat(input.concat("\f"));
			}
			
			result = result.concat("ENDINPUT\f");
		}
		
		result = result.concat("ENDCOMMAND\f");
		
		result = result.concat("ENDMESSAGE\f\n");
		
		//System.out.println("-- constructMessage --");
		//System.out.println(result);
		//System.out.println("-- nothing follows --");
		return result;
	}
}
