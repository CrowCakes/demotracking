package com.example.demotracking.classes;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ObjectConstructor {
	
	/**
	 * Returns the number of DemoOrders represented by the given String.
	 * @param foo
	 * @return
	 */
	private int orderCount(String foo) {
		List<String> bar = new ArrayList<String>(Arrays.asList(foo.split("::\n")));
		return bar.size();
	}
	
	/**
	 * Returns the list of DemoOrders represented by the given String.
	 * @param foo
	 * @return
	 */
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
						Integer.parseInt(foobar.get(0)), // orderid
						foobar.get(1),	//client
						foobar.get(2),	//rfd
						foobar.get(3),	//ard
						foobar.get(4),	//am
						foobar.get(5),	//po
						foobar.get(6),	//rr
						foobar.get(7),	//rts
						foobar.get(8)	//status
						);
				previous.addSchedule(
						new OrderDuration(
								Integer.parseInt(foobar.get(9)),
								previous.getOrderID(),
								Date.valueOf(foobar.get(10)),
								Date.valueOf(foobar.get(11)),
								foobar.get(12)
								)
						);
			}
			//since the entries are sorted by orderID and come one after the other,
			//we can simply check if the previous and the current order is the same
			//if it is the same, just add schedule to the order to be added
			else if (previous.getOrderID() == Integer.parseInt(foobar.get(0))) {
				previous.addSchedule(
						new OrderDuration(
								Integer.parseInt(foobar.get(9)),
								previous.getOrderID(),
								Date.valueOf(foobar.get(10)),
								Date.valueOf(foobar.get(11)),
								foobar.get(12)
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
						Integer.parseInt(foobar.get(0)), // orderid
						foobar.get(1),	//client
						foobar.get(2),	//rfd
						foobar.get(3),	//ard
						foobar.get(4),	//am
						foobar.get(5),	//po
						foobar.get(6),	//rr
						foobar.get(7),	//rts
						foobar.get(8)	//status
						);
				previous.addSchedule(
						new OrderDuration(
								Integer.parseInt(foobar.get(9)),
								previous.getOrderID(),
								Date.valueOf(foobar.get(10)),
								Date.valueOf(foobar.get(11)),
								foobar.get(12)
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
	
	/**
	 * Returns the list of DemoOrders represented by the given String, from the given index until the given number of entries.
	 * @param foo
	 * @param offset
	 * @param limit
	 * @return
	 */
	private List<DemoOrder> parseOrders(String foo, int offset, int limit) {
		List<DemoOrder> parsed_data = new ArrayList<>();
		List<String> bar = new ArrayList<String>(Arrays.asList(foo.split("::\n")));
		bar = bar.subList(offset, offset+limit);
		
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
						Integer.parseInt(foobar.get(0)), // orderid
						foobar.get(1),	//client
						foobar.get(2),	//rfd
						foobar.get(3),	//ard
						foobar.get(4),	//am
						foobar.get(5),	//po
						foobar.get(6),	//rr
						foobar.get(7),	//rts
						foobar.get(8)	//status
						);
				previous.addSchedule(
						new OrderDuration(
								Integer.parseInt(foobar.get(9)),
								previous.getOrderID(),
								Date.valueOf(foobar.get(10)),
								Date.valueOf(foobar.get(11)),
								foobar.get(12)
								)
						);
			}
			//since the entries are sorted by orderID and come one after the other,
			//we can simply check if the previous and the current order is the same
			//if it is the same, just add schedule to the order to be added
			else if (previous.getOrderID() == Integer.parseInt(foobar.get(0))) {
				previous.addSchedule(
						new OrderDuration(
								Integer.parseInt(foobar.get(9)),
								previous.getOrderID(),
								Date.valueOf(foobar.get(10)),
								Date.valueOf(foobar.get(11)),
								foobar.get(12)
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
						Integer.parseInt(foobar.get(0)), // orderid
						foobar.get(1),	//client
						foobar.get(2),	//rfd
						foobar.get(3),	//ard
						foobar.get(4),	//am
						foobar.get(5),	//po
						foobar.get(6),	//rr
						foobar.get(7),	//rts
						foobar.get(8)	//status
						);
				previous.addSchedule(
						new OrderDuration(
								Integer.parseInt(foobar.get(9)),
								previous.getOrderID(),
								Date.valueOf(foobar.get(10)),
								Date.valueOf(foobar.get(11)),
								foobar.get(12)
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
	
	/**
	 * Returns the list of OrderItems complete with their corresponding ItemParts represented by the given Strings, if any.
	 * @param result
	 * @param resultParts
	 * @return
	 */
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
									foobar.get(2),
									foobar.get(3)
									)
							);
				}
			}
		}
		
		return parsed_data;
	}

	/**
	 * Queries for the list of DemoOrders, then returns the number of DemoOrders therein.
	 * @param manager
	 * @return
	 */
	public int getOrderCount(ConnectionManager manager) {
		manager.connect();
		String result = manager.send(constructMessage("ViewOrders", new ArrayList<String>()));
		manager.disconnect();
		
		return orderCount(result);
	}
	
	/***
	 * Constructs the list of active DemoOrders that are not yet due, not including their OrderItems.
	 * @param manager
	 * @return
	 */
	public List<DemoOrder> constructOrders(ConnectionManager manager) {
		manager.connect();
		String result = manager.send(constructMessage("ViewOrders", new ArrayList<String>()));
		manager.disconnect();
		
		return parseOrders(result);
	}
	
	/***
	 * Constructs the list of active DemoOrders that are not yet due, not including their OrderItems.
	 * Includes only a specified number of Orders based on their position in the query result.
	 * @param manager
	 * @return
	 */
	public List<DemoOrder> constructOrders(ConnectionManager manager, int offset, int limit) {
		manager.connect();
		String result = manager.send(constructMessage("ViewOrders", new ArrayList<String>()));
		manager.disconnect();
		
		return parseOrders(result, offset, limit);
	}
	
	/**
	 * Constructs the list of internal DemoOrders.
	 * @param manager
	 * @return
	 */
	public List<DemoOrder> constructInHouseOrders(ConnectionManager manager) {
		manager.connect();
		String result = manager.send(constructMessage("ViewInHouseOrders", new ArrayList<String>()));
		manager.disconnect();
		
		return parseOrders(result);
	}
	
	/***
	 * Constructs the list of DemoOrders, without their items, that are due in 7 days
	 * @param manager
	 * @return
	 */
	public List<DemoOrder> constructDueOrdersNoItems(ConnectionManager manager) {
		manager.connect();
		String result = manager.send(constructMessage("ViewDueOrders", new ArrayList<String>()));
		manager.disconnect();
		
		return parseOrders(result);
	}
	
	/**
	 * Constructs the list of DemoOrders whose status is "Pullout."
	 * @param manager
	 * @return
	 */
	public List<DemoOrder> constructPullOutOrders(ConnectionManager manager) {
		manager.connect();
		String result = manager.send(constructMessage("ViewPullOutOrders", new ArrayList<String>()));
		manager.disconnect();
		
		return parseOrders(result);
	}
	
	/**
	 * Constructs the list of DemoOrders whose status is "Returned."
	 * @param manager
	 * @return
	 */
	public List<DemoOrder> constructReturnedOrders(ConnectionManager manager) {
		manager.connect();
		String result = manager.send(constructMessage("ViewReturnedOrders", new ArrayList<String>()));
		manager.disconnect();
		
		return parseOrders(result);
	}
	
	/***
	 * Constructs the list of DemoOrders, complete with their items, that are due in 7 days
	 * @param manager
	 * @return
	 */
	public List<DemoOrder> constructDueOrders(ConnectionManager manager) {
		manager.connect();
		String result = manager.send(constructMessage("ViewDueOrders", new ArrayList<String>()));
		manager.disconnect();
		
		List<DemoOrder> foo = parseOrders(result);
		for (DemoOrder bar : foo) {
			manager.connect();
			bar.setItems(this.constructItems(manager, bar.getOrderID()));
			manager.disconnect();
		}
		
		return foo;
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
	
	/***
	 * Finds all DemoOrders that have an OrderItem 
	 * whose serial number contains the substring specified by user input
	 * @param manager
	 * @param filter
	 * @return
	 */
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
	
	/**
	 * Assembles a server-readable message containing the desired query to be run and a list of parameters
	 * to insert into the query. An empty list may be passed if parameters are not needed.
	 * @param query
	 * @param parameters
	 * @return
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
