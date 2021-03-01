package com.npw.app.manager;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.npw.app.DTO.Item;
import com.npw.app.DTO.OrderDetail;
import com.npw.app.DTO.OrderSummary;
import com.npw.app.DTO.SKU;
import com.npw.app.mock.WireMock;

@Service
public class OrderManager {

	@Value("${order.service.url}")
	private String url;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	CatalogManager catalogManager;

	@Autowired
	WireMock wireMockUtil;

	public OrderSummary getOrderSummary(Long orderId) {

		return buildOrderSummary(orderId);

	}

	public List<OrderSummary> getOrderSummary(String customerId) {

		List<OrderSummary> response = new ArrayList<>();

		List<Long> orders = wireMockUtil.getCustIdToOrderId().get(customerId);

		if (!CollectionUtils.isEmpty(orders)) {

			for (Long id : orders) {
				response.add(buildOrderSummary(id));
			}
		}

		return response;

	}

	public OrderDetail getOrderDetails(Long orderId) {

		String orderUrl = url + orderId;
		OrderDetail orderDetail = restTemplate.getForObject(orderUrl, OrderDetail.class);

		for (Item item : orderDetail.getItems()) {

			SKU sku = catalogManager.getSKU(item.getSkuId());
			orderDetail.getSkus().add(sku);
		}

		return orderDetail;

	}

	private OrderSummary buildOrderSummary(Long id) {

		String orderUrl = url + id;
		OrderSummary orderSummary = restTemplate.getForObject(orderUrl, OrderSummary.class);
		return orderSummary;
	}

}
