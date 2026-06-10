package ru.adel.locationtracker.core.service.analysis.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.adel.locationtracker.core.service.analysis.category.db.CategoryRepository;
import ru.adel.locationtracker.core.service.analysis.category.db.entity.Category;
import ru.adel.locationtracker.public_interface.analysis.CategoryDto;
import ru.adel.locationtracker.public_interface.analysis.CategoryUpsertRequest;
import ru.adel.locationtracker.public_interface.exception.CategoryAlreadyExistsException;
import ru.adel.locationtracker.public_interface.exception.CategoryNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Administrative CRUD over categories. Every mutation refreshes the in-memory {@link CategoryCache}
 * (recomputing embeddings), so the classifier immediately reflects the new category set without retraining.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryCache categoryCache;

    @Transactional(readOnly = true)
    public List<CategoryDto> getAll() {
        return categoryRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional
    public CategoryDto create(CategoryUpsertRequest request) {
        if (categoryRepository.existsByCode(request.code())) {
            throw new CategoryAlreadyExistsException("Category already exists with code " + request.code());
        }
        LocalDateTime now = LocalDateTime.now();
        Category category = Category.builder()
                .code(request.code())
                .name(request.name())
                .description(request.description())
                .baseDangerLevel(request.baseDangerLevel())
                .active(request.active() == null || request.active())
                .createdAt(now)
                .updatedAt(now)
                .build();

        Category saved = categoryRepository.save(category);
        categoryCache.refresh();
        log.info("Created category {}", saved.getCode());
        return toDto(saved);
    }

    @Transactional
    public CategoryDto update(Long id, CategoryUpsertRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id " + id));

        if (request.code() != null && !request.code().equals(category.getCode())
                && categoryRepository.existsByCode(request.code())) {
            throw new CategoryAlreadyExistsException("Category already exists with code " + request.code());
        }

        if (request.code() != null) {
            category.setCode(request.code());
        }
        if (request.name() != null) {
            category.setName(request.name());
        }
        if (request.description() != null) {
            category.setDescription(request.description());
        }
        if (request.baseDangerLevel() != null) {
            category.setBaseDangerLevel(request.baseDangerLevel());
        }
        if (request.active() != null) {
            category.setActive(request.active());
        }
        category.setUpdatedAt(LocalDateTime.now());

        Category saved = categoryRepository.save(category);
        categoryCache.refresh();
        log.info("Updated category {}", saved.getCode());
        return toDto(saved);
    }

    /**
     * Soft delete: the category is deactivated so historical incidents keep their foreign key.
     */
    @Transactional
    public void deactivate(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id " + id));
        category.setActive(false);
        category.setUpdatedAt(LocalDateTime.now());
        categoryRepository.save(category);
        categoryCache.refresh();
        log.info("Deactivated category {}", category.getCode());
    }

    @Transactional(readOnly = true)
    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    private CategoryDto toDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .code(category.getCode())
                .name(category.getName())
                .description(category.getDescription())
                .baseDangerLevel(category.getBaseDangerLevel())
                .active(category.isActive())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }
}
