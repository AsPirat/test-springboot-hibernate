package com.example.mvc_thymeleaf.repository;

import com.example.mvc_thymeleaf.entity.Statistics;
import com.example.mvc_thymeleaf.service.SearchReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StatisticsRepository extends JpaRepository<Statistics, Long> {
    //SELECT `id_resource`, sum(`weight`) FROM `Statistics` WHERE `id_key_word` in (1,2) group by `id_resource`
    @Query("select new com.example.mvc_thymeleaf.service.SearchReport(stat.resource, sum(stat.weight)) " +
            "from Statistics stat where stat.stat_dict.key_word in (:list_key_words) " +
            "and stat.resource.discipline.id_discipline in (:list_id_disc) group by stat.resource.id_resource order by sum(stat.weight) desc")
    List<SearchReport> calculateRelevance(@Param("list_key_words") List<String> id_words, @Param("list_id_disc") List<Long> id_disc);

}
