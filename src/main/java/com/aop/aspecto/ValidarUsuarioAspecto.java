package com.aop.aspecto;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

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
				validaCampos(request);
				if(validaCredenciales(request)) {					
					return (ResponseEntity<?>) point.proceed(new Object[]{request});
				}else {
					return (ResponseEntity<?>)failAuthentication();
				}
			}
		}		
		return null;
	}

	private String validaCampos(BaseRequest request) {
		log.info("Inicia la validacion de campos");
		try {			
			Class clazz = request.getClass();
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				Annotation[] anotaciones = field.getAnnotations();
				if(anotaciones.length > 0) {
					for (Annotation anotacion : anotaciones) {
						String value = field.get(request).toString();
						log.info("Valor:" + value);
						if(anotacion instanceof MyNotNull && (value == null | value.equals(""))) {
							log.info("Campos NULOS o VACIOS.");
							return String.format("Campo: %s no debe ser nulo o vacio.", field.getName());
						}
					}
				}
			}
		} catch (Exception e) {
			log.info("ERROR: " +e.getMessage());
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
