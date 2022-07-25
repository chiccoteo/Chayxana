package com.chayxana.chayxana.repo;

import com.chayxana.chayxana.entity.Room;
import com.chayxana.chayxana.payload.CheckedRoomsOneMonth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@EnableJpaRepositories
public interface RoomRepo extends JpaRepository<Room, UUID> {

    @Query(value = "select  count(room.id) \n" +
            " from room \n" +
            " join chayxana ch on ch.id = room.chayxana_id \n" +
            " where ch.id =:chayxana_id ", nativeQuery = true)
    int getRoomNumberByChayxanaId(@Param("chayxana_id") UUID chayxana_id);

    List<Room> findAllByChayxana_Id(UUID chayxana_id);

    @Query(value = "with datee as(\n" +
            "    select onemonth from generate_series(\n" +
            "       make_date(\n" +
            "               cast(\n" +
            "                   (case when :givenMonth < extract(month from  current_date)\n" +
            "                             then date_part('year', (now() + interval '1 year'))\n" +
            "                                           else date_part('year', now()) end\n" +
            "                                                )as integer) ,\n" +
            "                   :givenMonth,\n" +
            "                    1) ,\n" +
            "        make_date(\n" +
            "               cast(\n" +
            "                     (case when :givenMonth < extract(month from  current_date )\n" +
            "                        then date_part('year', (now() + interval '1 year'))\n" +
            "                                                else date_part('year', now()) end\n" +
            "                                               ) as integer),\n" +
            "                  :givenMonth ,\n" +
            "                   (SELECT cast(date_part(  'days',\n" +
            "                                     (date_trunc('month',\n" +
            "                                             make_date(\n" +
            "                                                   cast( (case when :givenMonth < extract(month from  current_date)\n" +
            "                                                      then date_part('year', (now() + interval '1 year'))\n" +
            "                                                                 else date_part('year', now()) end\n" +
            "                                                           )as integer)\n" +
            "                                                                   ,\n" +
            "                                                             :givenMonth, 1)\n" +
            "                                                       ) + interval '1 month - 1 day')\n" +
            "                                               ) as integer)\n" +
            "                                   )\n" +
            "                               ),\n" +
            "                           '1 DAY') as onemonth\n" +
            "\n" +
            ")\n" +
            "select d1.onemonth as  timestamps, cast((case when se.status_id = 1  then true else false end ) as boolean) as check\n" +
            "from orders se\n" +
            "join room r on r.id = se.room_id and r.id = :roomId\n" +
            "right outer join datee d1 on cast(se.order_time as date )  = date_trunc('day', d1.onemonth)\n" +
            "group by  d1.onemonth, se.status_id\n" +
            "order by d1.onemonth", nativeQuery = true)
    List<CheckedRoomsOneMonth> checkRoomIdIsBronByMonth(@Param("roomId") UUID roomId,
                                                        @Param("givenMonth") int givenMonth);



    @Query(value = "select r.* \n" +
            "from room r \n" +
            "join chayxana c on c.id = r.chayxana_id \n" +
            "where r.chayxana_id = :chayxanaId and r.max_person >= :num and r.active ", nativeQuery = true)
    List<Room> getByUserSize(@Param("chayxanaId") UUID chayxanaId,
                             @Param("num") int num);


}
