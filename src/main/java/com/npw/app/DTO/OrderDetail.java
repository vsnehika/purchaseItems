package com.npw.app.DTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OrderDetail  extends Order implements Serializable{

	
	
	private static final long serialVersionUID = 1L;
	
	List<SKU> skus = new ArrayList<SKU>();

	public List<SKU> getSkus() {
		return skus;
	}

	public void setSkus(List<SKU> skus) {
		this.skus = skus;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
