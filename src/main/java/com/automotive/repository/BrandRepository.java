package com.automotive.repository;

import com.automotive.model.Brand;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Integer> {

    @Query("SELECT b FROM Brand b JOIN FETCH b.models")
    List<Brand> findAllByJoinModel();

    @Override
    @EntityGraph(value = "Brand.models", type = EntityGraph.EntityGraphType.FETCH)
    List<Brand> findAll();
}
