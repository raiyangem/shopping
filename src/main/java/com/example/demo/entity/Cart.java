package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="carts")
public class Cart {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String name;
	
	private Integer price;
	
	private Integer quantity;
	
	private Integer accountId;
	
	private Integer itemId;
	
	private Integer totalPrice;

	private String img;
	
	public Cart() {
		
	}

	public Cart(String name, Integer price, Integer quantity, Integer accountId, Integer itemId, Integer totalPrice, String img) {
		this.name = name;
		this.price = price;
		this.quantity = quantity;
		this.accountId = accountId;
		this.itemId = itemId;
		this.totalPrice = totalPrice;
		this.img = img;
	}
	
	public Cart(Integer id, String name, Integer price, Integer quantity, Integer accountId, Integer itemId, Integer totalPrice, String img) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.quantity = quantity;
		this.accountId = accountId;
		this.itemId = itemId;
		this.totalPrice = totalPrice;
		this.img = img;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}
	
	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Integer totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

}
