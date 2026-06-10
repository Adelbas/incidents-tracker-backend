package ru.adel.apigateway.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.adel.apigateway.core.service.category.client.CategoryRestClient;
import ru.adel.apigateway.public_interface.category.dto.CategoryDto;
import ru.adel.apigateway.public_interface.category.dto.CategoryUpsertRequest;

import java.util.List;

/**
 * Administrative management of event categories. Access is restricted to users with the ADMIN permission
 * (enforced in {@code SecurityConfiguration}). Requests are proxied to the location-tracker service.
 */
@Slf4j
@RestController
@RequestMapping("/api/admin/category")
@RequiredArgsConstructor
public class AdminCategoryController {

    private final CategoryRestClient categoryRestClient;

    @GetMapping
    public List<CategoryDto> getAll() {
        log.info("Admin: handle get all categories request");
        return categoryRestClient.getAll();
    }

    @PostMapping
    public CategoryDto create(@RequestBody CategoryUpsertRequest request) {
        log.info("Admin: handle create category request: {}", request);
        return categoryRestClient.create(request);
    }

    @PutMapping("/{id}")
    public CategoryDto update(@PathVariable Long id, @RequestBody CategoryUpsertRequest request) {
        log.info("Admin: handle update category <{}> request: {}", id, request);
        return categoryRestClient.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        log.info("Admin: handle delete category <{}> request", id);
        categoryRestClient.delete(id);
    }
}
