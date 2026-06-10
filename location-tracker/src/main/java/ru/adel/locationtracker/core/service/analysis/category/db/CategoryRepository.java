package ru.adel.locationtracker.core.service.analysis.category.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.adel.locationtracker.core.service.analysis.category.db.entity.Category;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByActiveTrue();

    Optional<Category> findByCode(String code);

    boolean existsByCode(String code);
}
