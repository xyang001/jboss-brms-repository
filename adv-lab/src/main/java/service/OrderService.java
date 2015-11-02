package service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import model.Order;
import model.OrderItem;

public class OrderService {

	// Dummy inventory database
	private List<OrderService.Inventory> inventory_database = new ArrayList<OrderService.Inventory>(
			5);

	public OrderService() {
		
		// build a dummy inventory database
		inventory_database.add(new Inventory("JY8075", "pearl earrings", 4,
				new BigDecimal("895.00")));
		inventory_database.add(new Inventory("FD1925", "peanut butter - 24oz",
				15, new BigDecimal("3.45")));
		inventory_database.add(new Inventory("JY7128", "jade necklace", 0,
				new BigDecimal("2385.00")));
		inventory_database.add(new Inventory("CL1144", "men's rain jacket", 2,
				new BigDecimal("44.95")));
		inventory_database.add(new Inventory("CL1256",
				"women's pumps - size 8US", 21, new BigDecimal("31.25")));
	}

	public List<Order> processInventory(Order order) {
		List<Order> orders = new ArrayList<Order>();
		
		// add original order
		orders.add(order);

		// just in case - create a back order
		Order backorder = createBackOrder(order);

		for (OrderItem item : order.getItems()) {
			processItem(item, backorder);
		}

		// back order has items - add it to order list
		if (backorder.getItems().size() > 0)
			orders.add(backorder);

		return orders;
	}
	
	public void calcSalesTax(Order order) {
		// This method handles states {NC, CA}
		
		BigDecimal rate = new BigDecimal("0.00"); // for other states
		if (order.isExempt())
			; // rate is 0
		else
		if ("NC".equals(order.getStateCode()))
			rate = new BigDecimal("0.06");
		else
		if ("CA".equals(order.getStateCode()))
			rate = new BigDecimal("0.09");
		
		if (order.isOnline())
			rate = (rate.divide(new BigDecimal("0.5")));
		
		order.setSalesTax(order.getOrderTotal().multiply(rate).setScale(2, RoundingMode.HALF_UP));
	}

	private Order createBackOrder(Order originalOrder) {
		Order backorder = new Order();

		backorder.setExempt(originalOrder.isExempt());
		backorder.setOnline(originalOrder.isOnline());
		backorder.setStateCode(originalOrder.getStateCode());
		backorder.setStatus("BACKORDER");

		return backorder;
	}

	private void processItem(OrderItem item, Order backorder) {
		Inventory inventory = findInventoryItem(item.getInventoryId());
		/**
		if (inventory == null)
			throw new OrderException("Item not found in inventory:  "
					+ item.getInventoryId());**/

		// enhance item line with details
		item.setPrice(inventory.price);
		item.setDescription(inventory.description);

		if (inventory.quantity >= item.getQuantity()) {
			// reduce inventory database
			inventory.quantity += (item.getQuantity() * -1);
			item.setExtPrice(item.getPrice().multiply(
					new BigDecimal(item.getQuantity())));
		} else {
			// how many items will be backordered?
			int backOrderQuantity = item.getQuantity() - inventory.quantity;
			item.setBackorderedQuantity(backOrderQuantity);
			// see if there is already an item of this type
			OrderItem backOrderItem = findBackOrderItem(item.getInventoryId(),
					backorder);
			if (backOrderItem == null) {
				// initialize a new back order item
				backOrderItem = new OrderItem();
				backOrderItem.setInventoryId(item.getInventoryId());
				backOrderItem.setPrice(inventory.price);
				backOrderItem.setDescription(inventory.description);
				// add item to back order
				backorder.getItems().add(backOrderItem);
			}
			// update quantity to include additional quantity
			backOrderItem.setQuantity(backOrderItem.getQuantity()
					+ backOrderQuantity);
			// extend price by back order quantity only
			backOrderItem.setExtPrice(item.getPrice().multiply(
					new BigDecimal(backOrderItem.getQuantity())));
			// reduce inventory database by quantity fulfilled
			inventory.quantity += ((item.getQuantity() - backOrderQuantity) * -1);
			
			// only charge for fulfilled items - backorder charges later
			item.setExtPrice(item.getPrice().multiply(
					new BigDecimal(item.getQuantity() - item.getBackorderedQuantity())));
		}

	}

	private Inventory findInventoryItem(String id) {
		for (Inventory inventory : inventory_database) {
			if (inventory.id.equals(id))
				return inventory;
		}
		return null;
	}

	private OrderItem findBackOrderItem(String id, Order backorder) {
		for (OrderItem item : backorder.getItems()) {
			if (item.getInventoryId().equals(id))
				return item;
		}

		return null;
	}

	private class Inventory {
		public String id;
		public String description;
		public Integer quantity; // quantity on hand
		public BigDecimal price;

		public Inventory(String id, String description, Integer quantity,
				BigDecimal price) {
			this.id = id;
			this.description = description;
			this.quantity = quantity;
			this.price = price;
		}
	}

}
