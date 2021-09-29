package com.csk.msscbeerservice.service.inventory;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.csk.msscbeerservice.service.inventory.model.BeerInventoryDto;

import lombok.extern.slf4j.Slf4j;

@Service
@ConfigurationProperties(prefix = "csk.beer", ignoreUnknownFields = false)
@Slf4j
public class BeerInventoryServiceRestTemplateImpl implements BeerInventoryService {

	private static final String INVENTORY_PATH = "/api/v1/beer/{beerId}/inventory";

	private String beerInventoryServiceHost;

	public void setBeerInventoryServiceHost(String beerInventoryServiceHost) {
		this.beerInventoryServiceHost = beerInventoryServiceHost;
	}

	@Override
	public Integer getOnhandInventory(UUID beerId) {
		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<List<BeerInventoryDto>> responseEntity = restTemplate.exchange(
				beerInventoryServiceHost + INVENTORY_PATH, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<BeerInventoryDto>>() {
				}, beerId);
		log.info("Beer Id = " + beerId);
		
		for (BeerInventoryDto dto: responseEntity.getBody()) {
			log.info("Quantity = " + dto.getQuantityOnHand());
		}
		int beerQuantity = Objects.requireNonNull(responseEntity.getBody())
				.stream()
				.mapToInt(BeerInventoryDto::getQuantityOnHand)
				.sum();
		return beerQuantity;
	}

}
