package com.example.bookmyshow.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@SpringBootApplication
@RestController
@EnableHystrix
public class BookMyShowApplication {

	@Autowired
	private RestTemplate restTemplate;

	@HystrixCommand(groupKey="java learning",commandKey="java learning",fallbackMethod="bookMyShowFallbak")
	@GetMapping("/booknow")
	public String bookShow() {
		String emailServiceResponse = restTemplate.getForObject("http://localhost:8787/emailservice/send", String.class);
		String paymentServiceResponse = restTemplate.getForObject("http://localhost:8788/paytmservice/pay", String.class);
		return emailServiceResponse + "\n" + paymentServiceResponse;
	}
	
	@GetMapping("/booknowWithoutHystrix")
	public String bookShowWithoutHystrix() {
		String emailServiceResponse = restTemplate.getForObject("http://localhost:8787/emailservice/send", String.class);
		String paymentServiceResponse = restTemplate.getForObject("http://localhost:8788/paytmservice/pay", String.class);
		return emailServiceResponse + "\n" + paymentServiceResponse;
	}
	
	public static void main(String[] args) {
		SpringApplication.run(BookMyShowApplication.class, args);
	}
	
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	public String bookMyShowFallbak() {
		return "Service getway falied";
	}

}
