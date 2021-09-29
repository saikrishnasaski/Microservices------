package com.csk.msscbeerservice.web.mapper;

import org.springframework.beans.factory.annotation.Autowired;

import com.csk.msscbeerservice.domain.Beer;
import com.csk.msscbeerservice.service.inventory.BeerInventoryService;
import com.csk.msscbeerservice.web.model.BeerDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BeerMapperDecorator implements BeerMapper {
	private BeerInventoryService beerInventoryService;
	private BeerMapper mapper;

	@Autowired
	public void setBeerInventoryService(BeerInventoryService beerInventoryService) {
		this.beerInventoryService = beerInventoryService;
	}

	@Autowired
	public void setMapper(BeerMapper mapper) {
		this.mapper = mapper;
	}

	@Override
	public BeerDto beerToBeerDto(Beer beer) {
		BeerDto dto = mapper.beerToBeerDto(beer);
		return dto;
	}

	@Override
	public BeerDto beerToBeerDtoWithInventory(Beer beer) {
		BeerDto dto = mapper.beerToBeerDto(beer);
		int quantity = beerInventoryService.getOnhandInventory(beer.getId());
		log.info("setting quantity=" + quantity);
		dto.setQuantityOnHand(quantity);
		return dto;
	}

	@Override
	public Beer beerDtoToBeer(BeerDto beerDto) {
		return mapper.beerDtoToBeer(beerDto);
	}
}
