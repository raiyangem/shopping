package com.example.demo.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import com.example.demo.entity.Item;

@Component
@SessionScope
public class CartModel {

	private List<Item> itemList = new ArrayList<>();

	public List<Item> getItemList() {
		return itemList;
	}

	public void setItemList(List<Item> itemList) {
		this.itemList = itemList;
	}
	
	public int getTotalPrice() {
		int total = 0;
		for(Item item : itemList) {
			total += item.getPrice() * item.getQuantity();
		}
		return total;
	}

	public void add(Item newItem) {
		Item existItem = null;
		for(Item item : itemList) {
			if(item.getId() == newItem.getId()) {
				existItem = item;
				break;
			}
		}
		
		if(existItem == null) {
			itemList.add(newItem);
		}else {
			existItem.setQuantity(existItem.getQuantity() + newItem.getQuantity());
		}
	}
	
	public void delete(int itemId) {
		for(Item item : itemList) {
			if(item.getId() == itemId) {
				itemList.remove(item);
				break;
			}
		}
	}
	public void clear() {
		itemList = new ArrayList<>();
	}
}
