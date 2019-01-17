package com.aop.aspecto;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.aop.anotaciones.ValidarUsuario;
import com.aop.entity.BaseRequest;
import com.aop.entity.BaseResponse;

@Aspect
@Component
@Scope("prototype")
public class ValidarUsuarioAspecto {
	private static final Logger log = LoggerFactory.getLogger(ValidarUsuarioAspecto.class);
	
	@Around("execution(* com.aop..*(..)) && @annotation(valida)")
	public ResponseEntity<?> validaCredenciales(ProceedingJoinPoint point, ValidarUsuario valida) throws Throwable {
		log.info("Inica el aspecto");
		for (Object item : point.getArgs()) {
			if(item instanceof BaseRequest) {
				BaseRequest request = (BaseRequest) item;
				if(validaCredenciales(request)) {					
					return (ResponseEntity<?>) point.proceed(new Object[]{request});
				}else {
					return (ResponseEntity<?>)failAuthentication();
				}
			}
		}		
		return null;
	}
	
	private boolean validaCredenciales(BaseRequest request) {
		log.info("Inicia validacion de credenciales.");
		return request.getUser().equals("admin") && request.getPassword().equals("admin");
	}
	
	private ResponseEntity<?> failAuthentication() {
		BaseResponse response = new BaseResponse();
		response.setDescripcion("Credenciales invalidas.");
		return new ResponseEntity(response, HttpStatus.OK);
	}
}
