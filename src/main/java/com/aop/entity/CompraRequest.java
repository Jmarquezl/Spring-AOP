package com.aop.entity;

import com.aop.aspecto.MyNotNull;

public class CompraRequest extends BaseRequest{
	@MyNotNull
	private String producto;

	public String getProducto() {
		return producto;
	}

	public void setProducto(String producto) {
		this.producto = producto;
	}
}
