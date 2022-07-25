package com.chayxana.chayxana.repo;

import com.chayxana.chayxana.entity.Chayxana;
import com.chayxana.chayxana.entity.ChayxanaImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChayxanaImageRepo extends JpaRepository<ChayxanaImage, UUID> {

      List<ChayxanaImage> findChayxanaImagesByChayxanasId(UUID chayxanas_id);

     Optional<ChayxanaImage> findByMainImageTrueAndChayxanasId(UUID chayxana_id);

      boolean existsByChayxanasId(UUID chayxanaId);

//      boolean findChayxanaImageByMainImageTrue();




      ChayxanaImage getChayxanaImageByMainImage(boolean mainImage);

}
