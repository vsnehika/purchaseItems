package com.npw.app.controller;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.npw.app.DTO.OrderDetail;
import com.npw.app.DTO.OrderSummary;
import com.npw.app.manager.OrderManager;

@RestController
public class AppController {


	@Autowired
	OrderManager orderManager;

	@GetMapping("customer/{custId}/order/summary")
	public List<OrderSummary> getCustomerSummary(@PathVariable(value = "custId") String custId)
			throws ClientProtocolException, IOException {

		return orderManager.getOrderSummary(custId);

	}

	@GetMapping("order/{orderId}/summary")
	public OrderSummary getOrderSummary(@PathVariable(value = "orderId") Long orderId)
			throws ClientProtocolException, IOException {

		return orderManager.getOrderSummary(orderId);

	}

	@GetMapping("/order/{orderId}/detail")
	public OrderDetail getDetail(@PathVariable(value = "orderId") Long orderId)
			throws ClientProtocolException, IOException {

		return orderManager.getOrderDetails(orderId);

	}

}
