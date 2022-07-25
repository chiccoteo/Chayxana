package com.chayxana.chayxana.repo;

import com.chayxana.chayxana.entity.Chayxana;
import com.chayxana.chayxana.entity.ChayxanaDetail;
import com.chayxana.chayxana.payload.SearchChayxanaDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChayxanaRepo extends JpaRepository<Chayxana, UUID> {

    List<Chayxana> findAllByUserId(UUID uuid);

    List<Chayxana> getAllByUserDisabledAndUser_Id(boolean user_disabled, UUID user_id);

    @Query(value = "WITH al AS (  SELECT  c.id as chayxan , ( 3959 * acos( cos( radians(?2) )\n" +
            "                                   * cos( radians( ad.lat ) ) * cos( radians( ad.lan )\n" +
            "                                  - radians(?1) ) + sin( radians(?2) )\n" +
            "                                   * sin( radians( ad.lat ) ) ) ) AS distance\n" +
            "    FROM address ad\n" +
            "             join chayxana c on ad.id = c.address_id " +
            "where c.is_delete = false )\n" +
            "\n" +
            "select  ch.*\n" +
            "from chayxana ch\n" +
            "join al a on ch.id = a.chayxan\n" +
            "ORDER BY a.distance\n" +
            "LIMIT 10;\n", nativeQuery = true)
    List<Chayxana> getALlByLonAndLat(@Param("lon") double lon,
                                     @Param("lat") double lat);
//    @Query(value = "SELECT ch_d.* FROM chayxana_detail ch_d join chayxana_detail_detail ch_d_d on ch_d.id=ch_d_d.chayxana_detail_id join chayxana ch on ch_d_d.chayxana_id = ch.id where ch_d.chayxana_detail_id=:?", nativeQuery = true)
//    List<Chayxana> findAllByChayxanaDetailId(Long chayxana_detail_id);

    @Query(value = "SELECT ch_d.* FROM chayxana_detail ch_d join chayxana_detail_detail ch_d_d on ch_d.id=ch_d_d.chayxana_detail_id join chayxana ch on ch_d_d.chayxana_id = ch.id where ch_d.chayxana_detail_id=:?", nativeQuery = true)
    List<Chayxana> findAllByChayxanaDetailId(Long chayxana_detail_id);

    @Query(value = "SELECT * FROM chayxana ch  where ch.is_delete = false", nativeQuery = true)
    Page<Chayxana> findAllByDelete(Pageable pageable);

    @Query(value = "SELECT * FROM chayxana ch  where ch.is_delete = false ", nativeQuery = true)
    Page<Chayxana> findAllforClient(Pageable pageable);

    @Query(value = "select cast(c.id as varchar ) as id, c.name as name, a.street_name as streetName, d.name as districtName\n" +
            "from chayxana c\n" +
            "left join address a on a.id = c.address_id\n" +
            "left join district d on d.id = a.district_id\n" +
            "where c.name  ILIKE CONCAT('%',:name,'%')", nativeQuery = true)
    @Modifying
    List<SearchChayxanaDto> findAllByNameContaining(@Param("name") String name);

//    @Query(value = "SELECT ch_d.* FROM chayxana_detail ch_d join chayxana_detail_detail ch_d_d on ch_d.id=ch_d_d.chayxana_detail_id join chayxana ch on ch_d_d.chayxana_id = ch.id where ch_d.chayxana_detail_id=:?", nativeQuery = true)
//    Optional<Chayxana> findByChayxanaDetailId(Long chayxana_detail_id);
//

}