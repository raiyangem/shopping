package com.example.demo.controller;


import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

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


	@GetMapping("/category/edit")
	public String editCategoryGet(
		@RequestParam("cateId") Integer cateId,
		Model model
			) {
		List<Category> c = categoryRepository.findAll();
		model.addAttribute("categories", c);
		Optional<Category> ci = categoryRepository.findById(cateId);
		model.addAttribute("ci", ci);
		return "categoryCreate";
	}

	@PostMapping("/category/edit")
	public String editcate(
			@RequestParam("cateId") Integer cateId,
			@RequestParam("cate") String cate,
			@RequestParam("img") MultipartFile file,
			Model model
			) throws IOException {
				Optional<Category> cs = null;
				Path dst = null;
				String fileName;
				if (!file.isEmpty()) {
					// 新しい画像がアップロードされた場合のみ保存処理を行う
					dst = Path.of("src\\main\\resources\\static\\img", file.getOriginalFilename());
					try {
						Files.copy(file.getInputStream(), dst);
					}catch(FileAlreadyExistsException e) {
					}finally {
						fileName = file.getOriginalFilename();
					}
				}else {
					cs  = categoryRepository.findById(cateId);
					fileName = cs.get().getImg();
				}
					Category cateSet = new Category(cateId, cate, fileName);
					categoryRepository.save(cateSet);
				return "redirect:/category";
	}

	@PostMapping("/category/delete")
	public String deleteCategory(@RequestParam("cateId") Integer cateId) {
		categoryRepository.deleteById(cateId);
		return "redirect:/category";
	}

}
