package com.capgemini.restdemo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.capgemini.restdemo.entity.Product;

@Repository
public interface ProductDao extends JpaRepository<Product, Integer>,CrudRepository<Product,Integer> {

}
