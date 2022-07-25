package com.chayxana.chayxana.repo;

import com.chayxana.chayxana.entity.Chayxana;
import com.chayxana.chayxana.entity.Order;
import com.chayxana.chayxana.entity.enums.StatusName;
import com.chayxana.chayxana.payload.LastOneMonthDto;
import com.chayxana.chayxana.payload.LastSixMonthResultsDto;
import org.hibernate.annotations.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface OrderRepo extends JpaRepository<Order, UUID> {

//    Page<Order> findAllByRoom_ChayxanaId(UUID room_chayxana_id, Pageable pageable);




    @Query(value = "SELECT cast(ch.id as varchar ) as id,(\n" +
            "                CASE WHEN EXISTS (\n" +
            "                            SELECT room.id\n" +
            "                            FROM room\n" +
            "                            join orders o on room.id = o.room_id\n" +
            "                            WHERE room.chayxana_id = ch.id\n" +
            "                        )\n" +
            "                             THEN (SELECT sum(ors.price)\n" +
            "                                   FROM orders ors\n" +
            "                                   join  room rm on rm.id = ors.room_id\n" +
            "                                   WHERE ors.order_time >= now() - interval '1' MONTH and ch.id = rm.chayxana_id)\n" +
            "                        ELSE 0\n" +
            "                        END)  as price\n" +
            "                from chayxana ch", nativeQuery = true)
    @Modifying
    List<LastOneMonthDto> getSumOrdersLastOneMonth();


    @Query(value ="WITH data1 AS (SELECT date2\n" +
            "               FROM generate_series(cast((\n" +
            "                   now() - interval '14 day') as date),\n" +
            "                                    current_date,\n" +
            "                                    '1 day') AS date2)\n" +
            "select d1.date2 as timestamps,\n" +
            "       (CASE\n" +
            "            WHEN count(se.price) IS NULL THEN\n" +
            "                0\n" +
            "            ELSE\n" +
            "                count(se.price)\n" +
            "           END\n" +
            "           )    as counts\n" +
            "\n" +
            "from orders se\n" +
            "         join chayxana ch on ch.id = :chayxanaId\n" +
            "         join room rm on rm.chayxana_id = ch.id and se.room_id = rm.id\n" +
            "         right outer join data1 d1\n" +
            "                          on cast(se.order_time as date) > date_trunc('day', d1.date2) and cast(se.order_time as date) <= date_trunc('day', d1.date2) + interval '1 day'\n" +
            "group by d1.date2\n" +
            "order by d1.date2 desc",nativeQuery = true)

    @Modifying
    List<LastSixMonthResultsDto> getLastMonthByDayCount(@Param("chayxanaId") UUID chayxanaId);


    @Query(value ="WITH data1 AS (SELECT date2\n" +
            "               FROM generate_series(cast((\n" +
            "                   now() - interval '6 month') as date),\n" +
            "                                    current_date,\n" +
            "                                    '1 month') AS date2)\n" +
            "select d1.date2 as timestamps,\n" +
            "       (CASE\n" +
            "            WHEN count(se.price) IS NULL THEN\n" +
            "                0\n" +
            "            ELSE\n" +
            "                count(se.price)\n" +
            "           END\n" +
            "           )    as counts\n" +
            "\n" +
            "from orders se\n" +
            "         join chayxana ch on ch.id = :chayxanaId\n" +
            "         join room rm on rm.chayxana_id = ch.id and se.room_id = rm.id\n" +
            "         right outer join data1 d1\n" +
            "                          on cast(se.order_time as date) > date_trunc('month', d1.date2) and cast(se.order_time as date) <= date_trunc('month', d1.date2) + interval '1 month'\n" +
            "group by d1.date2\n" +
            "order by d1.date2 desc",nativeQuery = true)

    @Modifying
    List<LastSixMonthResultsDto> getLastMonthByMonthCount(@Param("chayxanaId") UUID chayxanaId);

    @Query(value ="WITH data1 AS (SELECT date2\n" +
            "               FROM generate_series(cast((\n" +
            "                   now() - interval '6 month') as date),\n" +
            "                                    current_date,\n" +
            "                                    '1 week') AS date2)\n" +
            "select d1.date2 as timestamps,\n" +
            "       (CASE\n" +
            "            WHEN count(se.price) IS NULL THEN\n" +
            "                0\n" +
            "            ELSE\n" +
            "                count(se.price)\n" +
            "           END\n" +
            "           )    as counts\n" +
            "\n" +
            "from orders se\n" +
            "         join chayxana ch on ch.id = :chayxanaId\n" +
            "         join room rm on rm.chayxana_id = ch.id and se.room_id = rm.id\n" +
            "         right outer join data1 d1\n" +
            "                          on cast(se.order_time as date) > date_trunc('week', d1.date2) and cast(se.order_time as date) <= date_trunc('week', d1.date2) + interval '1 week'\n" +
            "group by d1.date2\n" +
            "order by d1.date2 desc",nativeQuery = true)

    @Modifying
    List<LastSixMonthResultsDto> getLastMonthByWeekCount(@Param("chayxanaId") UUID chayxanaId);

 //    Page<Order> findAllByRoom_ChayxanaIdOOrderByCreatedAtCreatedAtAsc(UUID chayxanaId, Pageable pageable);

    Page<Order> findAllByRoom_ChayxanaIdOrderByOrderTimeAsc(UUID chayxanaId, Pageable pageable);

    List<Order> findAllByUserId(UUID userId);

//    List<Order> findAllByStatusStatusName_Old();

    List<Order> findAllByStatusStatusName(StatusName statusName);
}