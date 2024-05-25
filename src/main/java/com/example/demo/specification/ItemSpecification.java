package com.example.demo.specification;

import org.springframework.data.jpa.domain.Specification;

import com.example.demo.entity.Category;
import com.example.demo.entity.Item;

import io.micrometer.common.util.StringUtils;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class ItemSpecification {
	
	public Specification<Item> nameContains(String name) {
        return StringUtils.isEmpty(name) ? null : new Specification<Item>() {
            @Override
            public Predicate toPredicate(Root<Item> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.like(root.get("name"), "%" + name + "%");
            }
        };
    }

	public Specification<Item> findByCategoryId(Category category) {
        return isEmptyNumber(category) ? null : new Specification<Item>() {
            @Override
            public Predicate toPredicate(Root<Item> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.equal(root.get("category"), category);
            }
        };
    }

	public Specification<Item> priceBetween(Integer priceL, Integer priceH) {
        return isEmptyNumber(priceL, priceH) ? null : new Specification<Item>() {
            @SuppressWarnings("unused")
			@Override
            public Predicate toPredicate(Root<Item> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            	Predicate obj = null;

            	if((priceL == null) && (priceH != null)) {
            		obj = cb.lessThanOrEqualTo(root.get("price"), priceH);
            	}else if((priceL != null) && (priceH == null)) {
            		obj = cb.greaterThanOrEqualTo(root.get("price"), priceL);
            	}else {
            		obj = cb.between(root.get("price"), priceL, priceH );
            	}

            	return obj;
            }
        };
    }

	public static boolean isEmptyNumber(Category category) {
		return category == null;
	}
	
	public static boolean isEmptyNumber(Integer priceL, Integer priceH) {
		boolean a = false;
		if((priceL == null) && (priceH == null)) {
			a = true;
		}
		return a;
	}

}
