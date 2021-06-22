package com.davalosh.taco.data;

import com.davalosh.taco.model.Order;

public interface OrderRepository {

  Order save(Order order);
  
}
