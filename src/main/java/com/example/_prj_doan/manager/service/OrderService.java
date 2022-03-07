package com.example._prj_doan.manager.service;

import com.example._prj_doan.entity.Order;
import com.example._prj_doan.entity.OrderRepo;
import com.example._prj_doan.manager.exception.OrderNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class OrderService {
    @Autowired
    private OrderRepo orderRepository;
    public static final int ORDER_PER_PAGE = 10;

    public Page<Order> listByPage(int pageNum, String sortField, String sortDir, String keyword) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        PageRequest page = PageRequest.of(pageNum - 1, ORDER_PER_PAGE, sort);

        if (keyword != null) {
            return orderRepository.findAll(keyword, page);
        }
        return orderRepository.findAll(page);
    }

    public Order get(Integer id) throws OrderNotFoundException {
        try {
            return orderRepository.findById(id).get();
        } catch (NoSuchElementException e) {
            throw new OrderNotFoundException("Could not find any orders with id: " + id);
        }
    }

    public void delete(Integer id) throws OrderNotFoundException {
        try {
            orderRepository.deleteById(id);
        }catch (NoSuchElementException e) {
            throw new OrderNotFoundException("Could not find any orders with id: " + id);
        }
    }
}
