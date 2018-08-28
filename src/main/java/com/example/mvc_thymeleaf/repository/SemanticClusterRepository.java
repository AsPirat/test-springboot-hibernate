package com.example.mvc_thymeleaf.repository;

import com.example.mvc_thymeleaf.entity.SemanticCluster;
import com.example.mvc_thymeleaf.service.ClusterReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SemanticClusterRepository extends JpaRepository<SemanticCluster, Long> {

    @Query("select distinct stat.stat_dict.id from Statistics stat")
    List<Long> findIdUniqueWords();

    @Query("select  new com.example.mvc_thymeleaf.service.ClusterReport(stat.resource.id_resource, stat.weight) from Statistics stat where stat.stat_dict.id=:id_word")
    List<ClusterReport> findWeightWords(@Param("id_word") Long word);

    @Query("select distinct sc.second_dict.key_word from SemanticCluster sc where sc.first_dict.key_word=:key_term")
    List<String> extendQueryWord(@Param("key_term") String key_term);

    @Query("from SemanticCluster sc order by sc.id asc")
    List<SemanticCluster> sortCluster();

}
