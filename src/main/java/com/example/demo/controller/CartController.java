package com.example.demo.controller;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Cart;
import com.example.demo.entity.Category;
import com.example.demo.entity.Item;
import com.example.demo.model.AccountModel;
import com.example.demo.model.CartModel;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ItemRepository;

@Controller
public class CartController {
	
	@Autowired
	CartModel cartModel;
	
	@Autowired
	AccountModel accountModel;
	
	@Autowired
	ItemRepository itemRepository;
	
	@Autowired
	CartRepository cartRepository;
	
	@Autowired
	CategoryRepository categoryRepository;
	
	@GetMapping("/cart")
	public String index(
			Model model
			) {
		List<Category> categoryList = categoryRepository.findAll();
		model.addAttribute("categories", categoryList);
		Integer accountId = accountModel.getId();
		
		List<Cart> cartList = cartRepository.findAllByAccountId(accountId);
		
		model.addAttribute("items", cartList);
		
		return "cart";
		
	}
	
	@PostMapping("/cart/add")
	public String addcart(
			@RequestParam("itemId") int itemId,
			@RequestParam(name = "quantity", defaultValue = "1") Integer quantity
			) {
		Optional<Item> item = itemRepository.findById(itemId);
		Cart cartItem = cartRepository.findByItemId(itemId);
		Integer bQ;
		Integer nQ;
		Integer setId;
		try {
			 bQ =cartItem.getQuantity();
			 nQ = bQ + quantity;
		}catch(NullPointerException e) {
			 bQ = 0;
			 nQ = quantity;
		}
		try {
			setId = cartItem.getId();
		}catch(NullPointerException e) {
			setId = null;
		}
		
		Integer idItem = setId;
		String nameItem = item.get().getName();
		Integer priceItem = item.get().getPrice();
		Integer quantityItem = nQ;
		Integer itemIdItem = item.get().getId();
		Integer accountIdItem = accountModel.getId();
		Integer totalPriceItem = item.get().getPrice() * nQ;
		String imgItem = item.get().getImg();

		System.out.println("実行"+imgItem);
		
		Cart cart = null;
		
		if(idItem == null) {
			cart = new Cart(nameItem, priceItem, quantityItem, accountIdItem, itemIdItem, totalPriceItem, imgItem);
		}else {
			cart = new Cart(idItem, nameItem, priceItem, quantityItem, accountIdItem, itemIdItem, totalPriceItem, imgItem);
		}
		
		cartRepository.save(cart);
		return "redirect:/cart";
	}
	
	@PostMapping("/cart/delete")
	public String deleteCart(@RequestParam("itemId") Integer itemId) {
		cartRepository.deleteById(itemId);
		return "redirect:/cart";
	}

}
