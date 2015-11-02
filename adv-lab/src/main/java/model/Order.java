package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Order implements Serializable {

	private static final long serialVersionUID = 1L;

	private BigDecimal orderTotal = new BigDecimal("0.00");
	private BigDecimal salesTax = new BigDecimal("0.00");
	private String stateCode;
	private boolean exempt = false;
	private boolean online = false;
	private String status = "NEW";
	private Integer backorderedItems = 0;

	private List<OrderItem> items = new ArrayList<OrderItem>();

	public Order() {

	}

	public Order(String stateCode, boolean exempt, boolean online) {
		this.stateCode = stateCode;
		this.exempt = exempt;
		this.online = online;
	}

	public BigDecimal getOrderTotal() {
		return orderTotal;
	}

	public void setOrderTotal(BigDecimal orderTotal) {
		this.orderTotal = orderTotal;
	}

	public BigDecimal getSalesTax() {
		return salesTax;
	}

	public void setSalesTax(BigDecimal salesTax) {
		this.salesTax = salesTax;
	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public boolean isExempt() {
		return exempt;
	}

	public void setExempt(boolean exempt) {
		this.exempt = exempt;
	}

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getBackorderedItems() {
		return backorderedItems;
	}

	public void setBackorderedItems(Integer backorderedItems) {
		this.backorderedItems = backorderedItems;
	}

	public List<OrderItem> getItems() {
		return items;
	}

	public void setItems(List<OrderItem> items) {
		this.items = items;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer(200);
		
		sb.append("Order " + status);
		sb.append(" ST=" + stateCode);
		sb.append(" EX=" + (exempt ? "Y" : "N"));
		sb.append(" OL=" + (online ? "Y" : "N"));
		sb.append(" TX=" + salesTax);
		sb.append(" TOT=" + orderTotal);
		if (backorderedItems > 0)
			sb.append(" BO=" + backorderedItems);
		sb.append("\n");
		for (OrderItem item : items)
			sb.append(item);
		sb.append("\n");
		
		return sb.toString();
	}
}
