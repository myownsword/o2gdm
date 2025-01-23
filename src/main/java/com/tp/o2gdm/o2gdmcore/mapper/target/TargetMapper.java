package com.tp.o2gdm.o2gdmcore.mapper.target;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface TargetMapper {

    Integer insertTableData(@Param("tabelName") String tabelName,@Param("list") List<Map<String,String>> list);

}
