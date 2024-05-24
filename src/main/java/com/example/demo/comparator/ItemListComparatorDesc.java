package com.example.demo.comparator;

import java.util.Comparator;

import com.example.demo.entity.Item;

public class ItemListComparatorDesc implements Comparator<Item> {

	@Override
	public int compare(Item o1, Item o2) {
		if(o1.getPrice() > o2.getPrice()) {
			return -1;
		}else {
			return 1;
		}
	}
}
