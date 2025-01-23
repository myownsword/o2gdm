package com.tp.o2gdm.o2gdmcore.mapper.source;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface SourceMapper {

    List<Map<String,String>> getAllTableList();

    List<Map<String,String>> getTableDataList(@Param("tableName") String tableName);

}
