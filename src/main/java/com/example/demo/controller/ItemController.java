package com.example.demo.controller;


import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.comparator.ItemListComparatorAsc;
import com.example.demo.comparator.ItemListComparatorDesc;
import com.example.demo.entity.Category;
import com.example.demo.entity.Item;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ItemRepository;
import com.example.demo.specification.ItemSpecification;

@Controller
public class ItemController {
	
	@Autowired
	CategoryRepository categoryRepository;
	
	@Autowired
	ItemRepository itemRepository;
	
	@GetMapping("/items")
	public String index(
			@RequestParam(value = "categoryId", defaultValue="") Integer categoryId,
			@RequestParam(value = "name", defaultValue="") String name,
			@RequestParam(value = "priceL", defaultValue="") Integer priceL,
			@RequestParam(value = "priceH", defaultValue="") Integer priceH,
			@RequestParam(value = "priceSort", defaultValue="") Integer priceSort,
			Model model
			) {
		
		List<Category> categoryList = categoryRepository.findAll();
		model.addAttribute("categories", categoryList);
		
//		System.out.print(categoryList.get(0).getItem().get(0).);
		
		return "items";
		
	}
	
	@GetMapping("/search")
	public String search(
			@RequestParam(value = "categoryId", defaultValue="") Category category,
			@RequestParam(value = "name", defaultValue="") String name,
			@RequestParam(value = "priceL", defaultValue="") Integer priceL,
			@RequestParam(value = "priceH", defaultValue="") Integer priceH,
			@RequestParam(value = "priceSort", defaultValue="") Integer priceSort,
			Model model
			) {
		
		List<Category> cateoryList = categoryRepository.findAll();
		model.addAttribute("categories", cateoryList);
		
		List<Item> itemList = null;
		
		itemList = findItems(name, category, priceL, priceH);
		model.addAttribute("priceL", priceL);
		model.addAttribute("priceH", priceH);
		model.addAttribute("inputName", name);
		model.addAttribute("cate", category);
		model.addAttribute("sort", priceSort);
		
		if(!(priceSort == null)) {
			if(priceSort == 0) {
				Collections.sort(itemList, new ItemListComparatorAsc());
			}else {
				Collections.sort(itemList, new ItemListComparatorDesc());
			}
		}
		
		model.addAttribute("items",itemList);
		
		return "s";
		
	}
	
	@GetMapping("/items/{itemCode}")
	public String itemDetail(
			@PathVariable("itemCode") Integer itemCode,
			Model model
			) {
		Optional<Item> detailItem = Optional.ofNullable(new Item());
		detailItem = itemRepository.findById(itemCode);
		List<Category> categoryList = categoryRepository.findAll();
		model.addAttribute("categories", categoryList);
		
		System.out.println(detailItem);
		
		model.addAttribute("detailItem", detailItem.get());
		
		return "itemDetail";
	}

	public List<Item> findItems(String name, Category category, Integer priceL, Integer priceH) {
		 ItemSpecification spec = new ItemSpecification();
		
	    return itemRepository.findAll(Specification
	        .where(spec.nameContains(name))
	        .and(spec.findByCategoryId(category))
	        .and(spec.priceBetween(priceL, priceH))
	    );
	}
	
	@GetMapping("/create/item")
	public String createItem(Model model) {
		List<Category> cateList = categoryRepository.findAll();
		model.addAttribute("cates", cateList);
		List<Category> categoryList = categoryRepository.findAll();
		model.addAttribute("categories", categoryList);
		return "createItem";
	}
	
	@PostMapping("create/item")
	public String uploadCreateItem(
			@RequestParam("img") MultipartFile file,
			@RequestParam("name") String name,
			@RequestParam("price") Integer price,
			@RequestParam("cate") Category category
			) throws IOException {
	    Path dst = Path.of("src\\main\\resources\\static\\img", file.getOriginalFilename());
	    try {
	    	 Files.copy(file.getInputStream(), dst);
	    }catch(FileAlreadyExistsException e) {
	    	String fileName = file.getOriginalFilename();
		    
		    Item item = new Item(name, category, price, fileName);
		    
		    itemRepository.save(item);
		    
		    return "redirect:/items";
	    }
	    
	    String fileName = file.getOriginalFilename();
	    
	    Item item = new Item(name, category, price, fileName);
	    
	    itemRepository.save(item);
	    
	    return "redirect:/items";
	  }

}
