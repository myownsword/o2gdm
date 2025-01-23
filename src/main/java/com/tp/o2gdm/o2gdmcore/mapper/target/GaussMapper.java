package com.tp.o2gdm.o2gdmcore.mapper.target;

import com.tp.o2gdm.o2gdmcore.config.GaussSqlProvider;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface GaussMapper {

    // 清空目标表
    @Delete("DELETE FROM ${tableName}")
    void clearTable(@Param("tableName") String tableName);

    // 批量插入数据
    @InsertProvider(type = GaussSqlProvider.class, method = "buildInsertSql")
    void insertBatchData(@Param("tableName") String tableName,
                         @Param("dataList") List<Map<String, Object>> dataList);

    // 查询是否存在该表
    @Select("SELECT COUNT(1) FROM pg_tables WHERE UPPER(schemaname) = UPPER(#{schemaName}) and UPPER(tablename) = UPPER(#{tableName})")
    int isTableExist(@Param("tableName") String tableName, @Param("schemaName") String schemaName);
}
