package com.npw.app.mock;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.matching.UrlPattern;
import com.npw.app.DTO.Address;
import com.npw.app.DTO.DateRange;
import com.npw.app.DTO.Item;
import com.npw.app.DTO.Order;
import com.npw.app.DTO.SKU;
import com.npw.app.DTO.Shipment;
import com.npw.app.DTO.Status;
import com.npw.app.DTO.Type;

@Component
public class WireMock {

	WireMockServer wireMockServer = new WireMockServer(options().port(8080));;
	
	Map<String, List<Long>> custIdToOrderId = new HashMap<>();

	// OrderId to Order mapping
	Map<Long, Order> orderIdToOrder = new HashMap<>();
	
	ObjectMapper mapper = new ObjectMapper();


	@PostConstruct
	public void init() throws JsonProcessingException {
		createOrders();
		wireMockServer.start();
		mockOrderService();
		mockCatelogService();

	}

	public void mockOrderService() throws JsonProcessingException {
		
		// url -- http:localhost:8080/order/{orderId}
		UrlPattern externalUrl = urlPathMatching("/order/[1-9]+");
		configureFor("localhost", 8080);
		stubFor(get(externalUrl).willReturn(aResponse().withStatus(200)
				.withHeader("Content-Type", "application/json").withBody(mapper.writeValueAsString(orderIdToOrder.get(1L)))));
	}
	
	public void mockCatelogService() throws JsonProcessingException {
		
		// url -- http:localhost:8080/catalog/sku/{skuId}
		UrlPattern externalUrl = urlPathMatching("/catalog/sku/sku1");
		configureFor("localhost", 8080, "/");
		stubFor(get(externalUrl).willReturn(aResponse().withStatus(200)
				.withHeader("Content-Type", "application/json").withBody(mapper.writeValueAsString(new SKU("sku1",Type.plan,"At & T Gold Plan")))));
		
		externalUrl = urlPathMatching("/catalog/sku/sku2");

		wireMockServer.stubFor(get(externalUrl).willReturn(aResponse().withStatus(200)
				.withHeader("Content-Type", "application/json").withBody(mapper.writeValueAsString(new SKU("sku2",Type.hardgood,"Mobile")))));
	}

	private void createOrders() {

		DateRange estimatedShipDateRangeItem1 = new DateRange("Estimated ship date for item 1 - Iphone 11",
				"2021-03-06T13:39:52.774-05:00", "2021-03-011T11:00:52.774-04:00");
		DateRange newEstimatedShipDateRangeItem1 = new DateRange("New estimated ship Date Range for item 1 - Iphone 11",
				"2021-03-010T13:39:52.774-05:00", "2021-03-151T11:00:52.774-04:00");

		// Item 1 is in Ordered status
		Item item1 = new Item(101L, "IPhone 11", "plan123", "sku1", 1, "409.406.3322", Status.ordered, false,
				estimatedShipDateRangeItem1, newEstimatedShipDateRangeItem1, true, "2021-03-07T13:39:52.774-05:00");

		// Item 2 is in Shipped Status
		DateRange estimatedShipDateRangeItem2 = new DateRange("Estimated ship date for item 2 - Iphone 12",
				"2021-02-04T13:39:52.774-05:00", "2021-02-08T11:00:52.774-04:00");
		Item item2 = new Item(102L, "IPhone 12", "plan113", "sku2", 1, "449.506.9922", Status.shipped, false,
				estimatedShipDateRangeItem2, null, false, null);

		ArrayList<Item> listOfAllItems = new ArrayList<>();
		listOfAllItems.add(item1);
		listOfAllItems.add(item2);

		// List of item shipped with the Shipment below
		List<Long> items = new ArrayList<>();
		items.add(item1.getId());
		items.add(item2.getId());
		Shipment shipment = new Shipment(1001L, items, "USPS", "9400100000000000000000",
				"https://tools.usps.com/go/TrackConfirmAction?tRef=fullpage&tLc=2&text28777=&tLabels=9400100000000000000000%2C",
				"2021-02-08T13:39:52.774-05:00", "2021-02-04T13:39:52.774-05:00"); // shipment with one item

		Address address = new Address("437 Lytton", "Palo Alto", "CA", "94301");

		ArrayList<Shipment> shipments = new ArrayList<>();
		shipments.add(shipment);

		Order order = new Order(100001L, 1L, "cust01", listOfAllItems, Status.shipped, false, address, shipments);
		orderIdToOrder.put(1L, order);
		List<Long> orders = new ArrayList<>();
		orders.add(1L);
		custIdToOrderId.put("cust01", orders);

	}
	
	

	public Map<String, List<Long>> getCustIdToOrderId() {
		return custIdToOrderId;
	}

	public void setCustIdToOrderId(Map<String, List<Long>> custIdToOrderId) {
		this.custIdToOrderId = custIdToOrderId;
	}

	public Map<Long, Order> getOrderIdToOrder() {
		return orderIdToOrder;
	}

	public void setOrderIdToOrder(Map<Long, Order> orderIdToOrder) {
		this.orderIdToOrder = orderIdToOrder;
	}

}
