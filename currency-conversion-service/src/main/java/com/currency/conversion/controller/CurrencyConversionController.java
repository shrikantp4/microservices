package com.currency.conversion.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.currency.conversion.bean.CurrencyConversionBean;
import com.currency.conversion.proxy.CurrencyExchangeProxy;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
public class CurrencyConversionController {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private CurrencyExchangeProxy proxy;

	@GetMapping("/currency-converter/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversionBean convertCurrency(@PathVariable String from,
			@PathVariable String to,
			@PathVariable BigDecimal quantity) {
		
		Map<String,String> uriVariable =  new HashMap<String,String>();
		uriVariable.put("from", from);
		uriVariable.put("to", to);
		ResponseEntity<CurrencyConversionBean> responseEntity = new RestTemplate().getForEntity("http://localhost:8000/currency-exchange/from/{from}/to/{to}",
				CurrencyConversionBean.class,uriVariable);
		CurrencyConversionBean response = responseEntity.getBody();
		
				return new CurrencyConversionBean(response.getId()
						,response.getFrom(),response.getTo(),
						response.getConversionMultiple(),
						quantity,
						quantity.multiply(response.getConversionMultiple()),
						8100);
		
	}
	
	@GetMapping("/currency-converter-feignClient/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversionBean convertCurrencyFeign(@PathVariable String from,
			@PathVariable String to,
			@PathVariable BigDecimal quantity) {
		
		
		CurrencyConversionBean response = proxy.getExchangeValue(from, to);
		
		logger.info("response :::::::::: ->  "+response);
				return new CurrencyConversionBean(response.getId()
						,response.getFrom(),response.getTo(),
						response.getConversionMultiple(),
						quantity,
						quantity.multiply(response.getConversionMultiple()),
						response.getPort());
		
	}
	
	@HystrixCommand(fallbackMethod = "fallbackConvertCurrencyFail")
	@GetMapping("/currency-converter-convertCurrencyFail/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversionBean convertCurrencyFail(@PathVariable String from,
			@PathVariable String to,
			@PathVariable BigDecimal quantity) {
		logger.info("calling convertCurrencyFail");
		int num1=30, num2=0;
        int output=num1/num2;
		
		CurrencyConversionBean response = proxy.getExchangeValue(from, to);
		
		logger.info("response :::::::::: ->  "+response);
				return new CurrencyConversionBean(response.getId()
						,response.getFrom(),response.getTo(),
						response.getConversionMultiple(),
						quantity,
						quantity.multiply(response.getConversionMultiple()),
						response.getPort());
		
	}
	
	
	public CurrencyConversionBean fallbackConvertCurrencyFail(@PathVariable String from,
			@PathVariable String to,
			@PathVariable BigDecimal quantity) {
		logger.info("calling fallbackConvertCurrencyFail ::::::::::: 9333333333");
		
		CurrencyConversionBean response = proxy.getExchangeValue(from, to);
		
		logger.info("response :::::::::: ->  "+response);
				return new CurrencyConversionBean(response.getId()
						,response.getFrom(),response.getTo(),
						response.getConversionMultiple(),
						quantity,
						quantity.multiply(response.getConversionMultiple()),
						response.getPort());
		
	}
}
