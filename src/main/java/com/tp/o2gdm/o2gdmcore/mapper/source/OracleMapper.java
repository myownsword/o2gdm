package com.tp.o2gdm.o2gdmcore.mapper.source;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface OracleMapper {

    // 获取 Oracle 数据库中所有表的名称
    @Select("SELECT table_name FROM user_tables")
    List<String> getAllTableNames();

    // 查询表数据（带分页）
//    @Select("SELECT * FROM ${tableName} OFFSET #{offset} ROWS FETCH NEXT #{batchSize} ROWS ONLY")
    @Select("SELECT * FROM ${tableName} where rownum < #{batchSize}")
    List<Map<String, Object>> getTableData(@Param("tableName") String tableName,
                                           @Param("offset") int offset,
                                           @Param("batchSize") int batchSize);
}
