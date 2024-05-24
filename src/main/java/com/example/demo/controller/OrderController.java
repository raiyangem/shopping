package com.example.demo.controller;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entity.Cart;
import com.example.demo.entity.Category;
import com.example.demo.entity.Item;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderDetail;
import com.example.demo.model.AccountModel;
import com.example.demo.model.CartModel;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.OrderDetailRepository;
import com.example.demo.repository.OrderRepository;

@Controller
public class OrderController {
	
	@Autowired
	CartModel cart;
	
	@Autowired
	AccountModel accountModel;
	
	@Autowired
	CartRepository cartRepository;
	
	@Autowired
	CustomerRepository customerRepository;
	
	@Autowired
	OrderRepository orderRepository;
	
	@Autowired
	OrderDetailRepository orderDetailRepository;
	
	@Autowired
	CategoryRepository categoryRepository;
	
	@GetMapping("/order")
	public String index() {
		return "customerForm";
	}
	
	@PostMapping("/order/confirm")
	public String confirm(
			Model model
			) {
		
		Integer accountId = accountModel.getId();
		
		List<Cart> cartList = cartRepository.findAllByAccountId(accountId);
		List<Category> categoryList = categoryRepository.findAll();
		model.addAttribute("categories", categoryList);
		
		model.addAttribute("items", cartList);
		
		return "orderConfirm";
		
	}
	
	@PostMapping("/order")
	public String order(
			Model model
			) {
		List<Category> categoryList = categoryRepository.findAll();
		model.addAttribute("categories", categoryList);
		//ログインユーザーのIDを取得
		Integer accountId = accountModel.getId();
		
		List<OrderDetail> orderDetails = new ArrayList<>();
		List<Cart> cartList = cartRepository.findAllByAccountId(accountId);
		Integer allTotalPrice = 0;
		//合計金額計算
		for(Cart cart : cartList) {
			allTotalPrice += cart.getTotalPrice();
		}
		
		//注文情報概要をorderに登録
		Order order = new Order(accountModel.getId(), LocalDate.now(), allTotalPrice);
		orderRepository.save(order);
		
		Order orderItem = new Order();
		
		orderItem.setId(order.getId());
		
		//注文情報詳細を登録
		for(Cart cart : cartList) {
			allTotalPrice += cart.getTotalPrice();
			Item item = new Item();
			
			item.setId(cart.getItemId());
			orderDetails.add(
					new OrderDetail(orderItem, item, cart.getQuantity(), accountModel.getId(), cart.getTotalPrice())
					);
		}
		orderDetailRepository.saveAll(orderDetails);
		
		//カート中身削除
		cartRepository.deleteAllByAccountId(accountId);
		
		model.addAttribute("orderNumber", order.getId());
		
		return "ordered";
	}

}
