package com.mehmetalivargun.snippets.repoistory;


import com.mehmetalivargun.snippets.model.Code;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface  CodeRepository extends JpaRepository<Code, UUID> {
    List<Code> findFirst10ByTimeAndViewsOrderByDateDesc(long time, long views);
}
