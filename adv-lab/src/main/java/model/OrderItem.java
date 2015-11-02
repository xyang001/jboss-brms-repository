package model;

import java.io.Serializable;
import java.math.BigDecimal;

public class OrderItem implements Serializable {

	private static final long serialVersionUID = 1L;

	private String inventoryId;
	private String description;
	private BigDecimal price;
	private Integer quantity = 0;
	private BigDecimal extPrice;
	private Integer backorderedQuantity = 0;

	public OrderItem() {

	}

	public OrderItem(String inventoryId, Integer quantity) {

		this.inventoryId = inventoryId;
		this.quantity = quantity;
	}

	public String getInventoryId() {
		return inventoryId;
	}

	public void setInventoryId(String inventoryId) {
		this.inventoryId = inventoryId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getExtPrice() {
		return extPrice;
	}

	public void setExtPrice(BigDecimal extPrice) {
		this.extPrice = extPrice;
	}

	public Integer getBackorderedQuantity() {
		return backorderedQuantity;
	}

	public void setBackorderedQuantity(Integer backorderedQuantity) {
		this.backorderedQuantity = backorderedQuantity;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer(100);
		
		sb.append("  Item " + inventoryId + ":" + description + ": " + quantity + "@" + price);
		sb.append("=" + extPrice);
		if (backorderedQuantity > 0)
			sb.append(": BO=" + backorderedQuantity);
		sb.append("\n");
		
		return sb.toString();
	}

}
