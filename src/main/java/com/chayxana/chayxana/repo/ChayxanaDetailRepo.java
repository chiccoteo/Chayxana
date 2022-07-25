package com.chayxana.chayxana.repo;

import com.chayxana.chayxana.entity.Chayxana;
import com.chayxana.chayxana.entity.ChayxanaDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public interface ChayxanaDetailRepo extends JpaRepository<ChayxanaDetail, Long> {

    List<ChayxanaDetail> findAllByActiveIsTrue();

    @Query(value = "SELECT ch_d.*\n" +
            "FROM chayxana_detail ch_d\n" +
            " join chayxana_chayxana_details ch_d_d\n" +
            "     on ch_d.id=ch_d_d.chayxana_details_id\n" +
            "join chayxana ch on ch_d_d.chayxana_id = ch.id\n" +
            "WHERE ch.active = true\n" +
            "AND ch.id =:chayxanaId", nativeQuery = true)
    List<ChayxanaDetail> findAllByChayxana_Id(UUID chayxanaId);

                        @Query(value = "SELECT ch.* from chayxana ch  join  chayxana_detail_chayxanas cdc on ch.id = cdc.chayxanas_id where cdc.chayxana_detail_id=:idList", nativeQuery = true)
                        List<Chayxana> findAllByChayxanaDetailId(List<Long> idList);
   //              Shunaqa bularkan Muhammad aka ayti





}
