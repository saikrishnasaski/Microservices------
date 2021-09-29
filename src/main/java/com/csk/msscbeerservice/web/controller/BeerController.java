package com.csk.msscbeerservice.web.controller;

import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.csk.msscbeerservice.service.BeerService;
import com.csk.msscbeerservice.web.model.BeerDto;
import com.csk.msscbeerservice.web.model.BeerPagedList;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class BeerController {
	
	private static final int DEFAULT_PAGE_NUMBER = 0;
	private static final int DEFAULT_PAGE_SIZE = 25;

	private final BeerService beerService;

	@GetMapping("/beer/{beerId}")
	public ResponseEntity<BeerDto> getBeerById(@PathVariable("beerId") UUID beerId,
			@RequestParam(value = "showInventoryOnHand", required = false, defaultValue = "false") boolean showInventoryOnHand) {

		return new ResponseEntity<BeerDto>(beerService.getById(beerId, showInventoryOnHand), HttpStatus.OK);
	}
	
	@GetMapping("/beerUpc/{upc}")
	public ResponseEntity<BeerDto> getBeerByUpc(@PathVariable("upc") String upc) {
		
		return new ResponseEntity<BeerDto>(beerService.getByUpc(upc), HttpStatus.OK);
	}
	
	@GetMapping("/beer")
	public ResponseEntity<BeerPagedList> getBeers(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
												  @RequestParam(value = "pageSize", defaultValue = "25", required = false) int pageSize,
												  @RequestParam(value = "beerName", required = false) String beerName,
												  @RequestParam(value = "beerStyle", required = false) String beerStyle,
												  @RequestParam(value = "showInventoryOnHand", required = false, defaultValue = "false") boolean showInventoryOnHand) {
		
		if (pageNumber < 0) {
			pageNumber = DEFAULT_PAGE_NUMBER;
		}
		if (pageSize < 1) {
			pageSize = DEFAULT_PAGE_SIZE;
		}
		@SuppressWarnings("deprecation")
		PageRequest pageRequest = new PageRequest(pageNumber, pageSize);
		return new ResponseEntity<BeerPagedList>(beerService.listBeers(beerName, beerStyle, pageRequest, showInventoryOnHand), HttpStatus.OK);
	}

	@PostMapping("/beer")
	public ResponseEntity<BeerDto> postBeer(@RequestBody @Validated BeerDto beerDto) {

		return new ResponseEntity<BeerDto>(beerService.saveNewBeer(beerDto), HttpStatus.CREATED);
	}

	@PutMapping("/beer/{beerId}")
	public ResponseEntity<BeerDto> updateBeer(@PathVariable("beerId") UUID beerId,
			@RequestBody @Validated BeerDto beerDto) {

		return new ResponseEntity<BeerDto>(beerService.updateBeer(beerId, beerDto), HttpStatus.NO_CONTENT);
	}

}
