package com.aop.business;

import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.aop.anotaciones.ValidarUsuario;
import com.aop.entity.CompraRequest;
import com.aop.entity.CompraResponse;

@Component
@Scope("prototype")
public class POS {
	public POS() {
		super();
	}
	
	@ValidarUsuario
	public ResponseEntity<?> comprar(CompraRequest request) {
		CompraResponse response = new CompraResponse();		
		response.setFolioCompra("1235421");
		response.setDescripcion("OK");
		return new ResponseEntity(response, HttpStatus.OK);
	}
}
