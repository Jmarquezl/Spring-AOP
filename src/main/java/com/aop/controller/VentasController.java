package com.aop.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aop.business.POS;
import com.aop.entity.CompraRequest;
import com.aop.entity.CompraResponse;

@RestController
@RequestMapping("/pos")
public class VentasController {
	private static final Logger log = LoggerFactory.getLogger(VentasController.class);
	
	@Autowired
	private POS pos;

	@PostMapping(value = "/comprar", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> compra(@RequestBody CompraRequest request) {
		log.info("Inicia el servicio de compras.");
		return pos.comprar(request);
	}
}
