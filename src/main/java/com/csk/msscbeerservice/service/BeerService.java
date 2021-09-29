package com.csk.msscbeerservice.service;

import java.util.UUID;

import org.springframework.data.domain.PageRequest;

import com.csk.msscbeerservice.web.model.BeerDto;
import com.csk.msscbeerservice.web.model.BeerPagedList;

public interface BeerService {
	
	BeerDto getById(UUID beerId, boolean showInventoryOnHand);
	
	BeerDto getByUpc(String upc);
	
	BeerDto saveNewBeer(BeerDto beerDto);
	
	BeerDto updateBeer(UUID beerId, BeerDto beerDto);
	
	BeerPagedList listBeers(String beerName, String beerStyle, PageRequest pageRequest, boolean showInventoryOnHand);
	
}
