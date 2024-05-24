package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Cart;

import jakarta.transaction.Transactional;
public interface CartRepository extends JpaRepository<Cart, Integer> {

	List<Cart> findAllByAccountId(Integer accountId);
	@Transactional
	void deleteAllByAccountId(Integer accountId);
	Cart findByItemId(Integer itemId);
}
