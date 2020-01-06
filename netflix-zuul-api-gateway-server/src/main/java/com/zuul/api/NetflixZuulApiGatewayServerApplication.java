package com.zuul.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

import com.netflix.eureka.registry.rule.AlwaysMatchInstanceStatusRule;

import brave.sampler.Sampler;

@EnableZuulProxy
@SpringBootApplication
@EnableDiscoveryClient
public class NetflixZuulApiGatewayServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(NetflixZuulApiGatewayServerApplication.class, args);
	}

	@Bean
	public Sampler defaSampler() {
		return Sampler.ALWAYS_SAMPLE;
	}
}
