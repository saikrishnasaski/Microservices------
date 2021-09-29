package com.csk.msscbeerservice;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.csk.msscbeerservice.domain.Beer;
import com.csk.msscbeerservice.repository.BeerRepository;

@SpringBootApplication
public class MsscBeerServiceApplication implements CommandLineRunner {
	
	@Autowired
	private BeerRepository beerRepository;

	public static void main(String[] args) {
		SpringApplication.run(MsscBeerServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Beer beer = null;
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String beerName1 = "Mango Bobs";
		String beerName2 = "Galaxy Cat";
		String beerName3 = "Pinball Porter";
		
		String type1 = "IPA";
		String type2 = "PALE_ALE";
		String type3 = "PORTER";
		
		String upc1 = "0631234200036";
		String upc2 = "0631234300019";
		String upc3 = "0083783375213";
		
		BigDecimal price = new BigDecimal(12.95);
		
		beer = new Beer(beerName1, type1, timestamp, timestamp, upc1, price, 12, 200, 1l);
		beerRepository.save(beer);
		
		beer = new Beer(beerName2, type2, timestamp, timestamp, upc2, price, 12, 200, 1l);
		beerRepository.save(beer);
		
		beer = new Beer(beerName3, type3, timestamp, timestamp, upc3, price, 12, 200, 1l);
		beerRepository.save(beer);
		
		
	}

}
