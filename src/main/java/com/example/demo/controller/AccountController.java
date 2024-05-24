package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Account;
import com.example.demo.entity.Category;
import com.example.demo.model.AccountModel;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.CategoryRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class AccountController {
	
	@Autowired
	HttpSession session;
	
	@Autowired
	Account account;
	
	@Autowired
	AccountModel accountModel;
	
	@Autowired
    AccountRepository accountRepository;
	
	@Autowired
	CategoryRepository categoryRepository;
	
	@GetMapping({"/", "/login", "/logout"})
	public String index(
			@RequestParam(name = "error", defaultValue="") String error,
			Model model
			) {
		List<Category> categoryList = categoryRepository.findAll();
		model.addAttribute("categories", categoryList);
		session.invalidate();
		if(error.equals("notLoggedIn")) {
			model.addAttribute("message", "ログインしてください");
		}
		
		return "login";
		
	}
	
	@PostMapping("/login")
	public String login(
			@RequestParam("email") String email,
			Model model
			) {
		//第一段階
		if(email == null || email.length() == 0) {
			model.addAttribute("message", "メールアドレスを入力してください");
			return "login";
		}
		
		Account accountNameList = null;
		
		accountNameList = accountRepository.findByEmail(email);
		
		if(accountNameList == null) {
			model.addAttribute("message", "メールアドレスが間違っています");
			return "login";
		}
			
		
		accountModel.setName(accountNameList.getName());
		accountModel.setId(accountNameList.getId());
		accountModel.setEmail(accountNameList.getEmail());
		accountModel.setAddress(accountNameList.getAddress());
		accountModel.setTel(accountNameList.getTel());
		
		return "redirect:/items";
	}
	
	@GetMapping("/join/member")
    public String input(Model model) {
		List<Category> categoryList = categoryRepository.findAll();
		model.addAttribute("categories", categoryList);

        return "joinMember";
    }
	
	@PostMapping("/join/member")
    public String form(@ModelAttribute("account") Account accountSet, Model model) {
		List<Account>accountList = accountRepository.findAll();
		
		for(Account a : accountList) {
			if(a.getEmail().equals(accountSet.getEmail())) {
				model.addAttribute("message", "すでに同じ名前が使用されています。");
				return "joinMember";
			}
		}
		
		accountModel.setName(accountSet.getName());
		accountModel.setId(accountSet.getId());
		accountModel.setEmail(accountSet.getEmail());
		accountModel.setAddress(accountSet.getAddress());
		accountModel.setTel(accountSet.getTel());
		
		accountRepository.saveAndFlush(accountSet);
		
        return "redirect:/items";
        
    }
	
	@PostMapping("/delete/account")
	public String deleteAccount(@RequestParam("accountId") Integer accountId) {
		accountRepository.deleteById(accountId);
		return "redirect:/login";
	}
}