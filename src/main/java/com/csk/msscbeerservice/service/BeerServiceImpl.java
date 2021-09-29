package com.csk.msscbeerservice.service;

import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.csk.msscbeerservice.domain.Beer;
import com.csk.msscbeerservice.exception.NotFoundException;
import com.csk.msscbeerservice.repository.BeerRepository;
import com.csk.msscbeerservice.web.mapper.BeerMapper;
import com.csk.msscbeerservice.web.model.BeerDto;
import com.csk.msscbeerservice.web.model.BeerPagedList;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class BeerServiceImpl implements BeerService {

	private final BeerRepository beerRepo;

	private final BeerMapper beerMapper;

	@Cacheable(cacheNames = "beerCache", key = "#beerId", condition = "#showInventoryOnHand == false")
	@Override
	public BeerDto getById(UUID beerId, boolean showInventoryOnHand) {
		if (showInventoryOnHand) {
			return beerMapper.beerToBeerDtoWithInventory(beerRepo.findById(beerId).orElseThrow(NotFoundException::new));
		} else {
			return beerMapper.beerToBeerDto(beerRepo.findById(beerId).orElseThrow(NotFoundException::new));
		}

	}

	@Cacheable(cacheNames = "beerUpcCache", key = "#upc", condition = "#showInventoryOnHand == false")
	@Override
	public BeerDto getByUpc(String upc) {
		return beerMapper.beerToBeerDto(beerRepo.findByUpc(upc).orElseThrow(NotFoundException::new));
	}

	@Override
	public BeerDto saveNewBeer(BeerDto beerDto) {
		return beerMapper.beerToBeerDto(beerRepo.save(beerMapper.beerDtoToBeer(beerDto)));
	}

	@Override
	public BeerDto updateBeer(UUID beerId, BeerDto beerDto) {
		Beer beer = beerRepo.findById(beerId).orElseThrow(NotFoundException::new);

		beer.setBeerName(beerDto.getBeerName());
		beer.setBeerStyle(beerDto.getBeerStyle());
		beer.setPrice(beerDto.getPrice());

		return beerMapper.beerToBeerDto(beerRepo.save(beer));
	}

	@Cacheable(cacheNames = "beerListCache", condition = "#showInventoryOnHand == false")
	@Override
	public BeerPagedList listBeers(String beerName, String beerStyle, PageRequest pageRequest,
			final boolean showInventoryOnHand) {

		System.out.println("I was called....");
		BeerPagedList pagedList = null;
		Page<Beer> beers = null;

		if (!StringUtils.isEmpty(beerName) && !StringUtils.isEmpty(beerStyle)) {
			beers = beerRepo.findAllByBeerNameAndBeerStyle(beerName, beerStyle, pageRequest);
		} else if (StringUtils.isEmpty(beerName) && !StringUtils.isEmpty(beerStyle)) {
			beers = beerRepo.findAllByBeerStyle(beerStyle, pageRequest);
		} else {
			beers = beerRepo.findAll(pageRequest);
		}

		log.info("printing beer info...");
		beers.forEach(b -> System.out.println(b));

		if (showInventoryOnHand) {
			pagedList = new BeerPagedList(
					beers.stream().map(beerMapper::beerToBeerDtoWithInventory).collect(Collectors.toList()),
					pageRequest, beers.getTotalElements());
		} else {
			pagedList = new BeerPagedList(beers.stream().map(beerMapper::beerToBeerDto).collect(Collectors.toList()),
					pageRequest, beers.getTotalElements());
		}

		return pagedList;
	}

}
