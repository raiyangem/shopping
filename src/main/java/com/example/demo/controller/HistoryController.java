package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.entity.Category;
import com.example.demo.entity.OrderDetail;
import com.example.demo.model.AccountModel;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.OrderDetailRepository;

@Controller
public class HistoryController {
	
	@Autowired
	OrderDetailRepository orderDetailRepository;
	
	@Autowired
	AccountModel accountModel;
	
	@Autowired
	CategoryRepository categoryRepository;
	
	@GetMapping("/history")
	public String history(
			Model model
			) {
		List<Category> categoryList = categoryRepository.findAll();
		model.addAttribute("categories", categoryList);
		List<OrderDetail> historyList = orderDetailRepository.findByAccountId(accountModel.getId());
		model.addAttribute("hList", historyList);
		
		return "history";
	}

}
