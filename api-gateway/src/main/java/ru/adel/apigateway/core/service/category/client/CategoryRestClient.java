package ru.adel.apigateway.core.service.category.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.adel.apigateway.core.service.incident.client.rest.IncidentRestClientProperty;
import ru.adel.apigateway.public_interface.category.dto.CategoryDto;
import ru.adel.apigateway.public_interface.category.dto.CategoryUpsertRequest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * REST client to the category CRUD endpoints owned by the location-tracker service.
 * Reuses the existing location-tracker host/port configuration ({@code api.client.incident.rest.*}).
 */
@Slf4j
@Component
public class CategoryRestClient {

    private final String basePath;

    private final RestTemplate restTemplate;

    public CategoryRestClient(IncidentRestClientProperty property, RestTemplate restTemplate) {
        this.basePath = "http://" + property.host() + ":" + property.port() + "/api/category";
        this.restTemplate = restTemplate;
    }

    public List<CategoryDto> getAll() {
        CategoryDto[] response = restTemplate.getForObject(basePath, CategoryDto[].class);
        if (response == null || response.length == 0) {
            return Collections.emptyList();
        }
        return Arrays.asList(response);
    }

    public CategoryDto create(CategoryUpsertRequest request) {
        return restTemplate.postForObject(basePath, request, CategoryDto.class);
    }

    public CategoryDto update(Long id, CategoryUpsertRequest request) {
        HttpEntity<CategoryUpsertRequest> httpEntity = new HttpEntity<>(request);
        return restTemplate.exchange(basePath + "/" + id, HttpMethod.PUT, httpEntity, CategoryDto.class).getBody();
    }

    public void delete(Long id) {
        restTemplate.delete(basePath + "/" + id);
    }
}
