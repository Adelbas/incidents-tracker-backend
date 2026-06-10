package ru.adel.locationtracker.controller;

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
import ru.adel.locationtracker.core.service.analysis.category.CategoryService;
import ru.adel.locationtracker.public_interface.analysis.CategoryDto;
import ru.adel.locationtracker.public_interface.analysis.CategoryUpsertRequest;

import java.util.List;

/**
 * Internal CRUD over categories. Exposed to administrators through the api-gateway under
 * {@code /api/admin/category}; access control is enforced at the gateway.
 */
@Slf4j
@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> getAll() {
        log.info("Handle get all categories request");
        return categoryService.getAll();
    }

    @PostMapping
    public CategoryDto create(@RequestBody CategoryUpsertRequest request) {
        log.info("Handle create category request: {}", request);
        return categoryService.create(request);
    }

    @PutMapping("/{id}")
    public CategoryDto update(@PathVariable Long id, @RequestBody CategoryUpsertRequest request) {
        log.info("Handle update category <{}> request: {}", id, request);
        return categoryService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        log.info("Handle delete (deactivate) category <{}> request", id);
        categoryService.deactivate(id);
    }
}
