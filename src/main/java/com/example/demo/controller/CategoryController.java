package com.example.demo.controller;


import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Category;
import com.example.demo.repository.CategoryRepository;

@Controller
public class CategoryController {
	@Autowired
	CategoryRepository categoryRepository;

	@GetMapping("/category")
	public String category(
		Model model
	) {
		List<Category> c = categoryRepository.findAll();
		model.addAttribute("categories", c);
		return "category";
	}

	@GetMapping("/category/add")
	public String addCategoryGet(
		Model model
			) {
		List<Category> c = categoryRepository.findAll();
		model.addAttribute("categories", c);
		return "categoryCreate";
	}

	@PostMapping("/category/add")
	public String addcate(
			@RequestParam("cate") String cate,
			@RequestParam("img") MultipartFile file,
			Model model
			) throws IOException {
				Path dst = Path.of("src\\main\\resources\\static\\img", file.getOriginalFilename());
				try {
					Files.copy(file.getInputStream(), dst);
				}catch(FileAlreadyExistsException e) {
					String fileName = file.getOriginalFilename();

					Category cateSet = new Category(cate, fileName);
					categoryRepository.save(cateSet);
					return "redirect:/category";
				}
	    		String fileName = file.getOriginalFilename();
				Category cateSet = new Category(cate, fileName);
				categoryRepository.save(cateSet);
				return "redirect:/category";
	}

	@PostMapping("/category/delete")
	public String deleteCategory(@RequestParam("cateId") Integer cateId) {
		categoryRepository.deleteById(cateId);
		return "redirect:/category";
	}

}
