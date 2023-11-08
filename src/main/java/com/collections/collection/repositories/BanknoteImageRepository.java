package com.collections.collection.repositories;

import com.collections.collection.entities.BanknoteImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BanknoteImageRepository extends JpaRepository<BanknoteImage, Long> {
}
