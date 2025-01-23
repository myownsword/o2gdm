package com.tp.o2gdm.o2gdmcore.config;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class GaussSqlProvider {

    public String buildInsertSql(Map<String, Object> params) {
        String tableName = (String) params.get("tableName");
        List<Map<String, Object>> dataList = (List<Map<String, Object>>) params.get("dataList");

        if (dataList == null || dataList.isEmpty()) {
            return "";
        }

        // 获取所有列名
        Set<String> columns = dataList.get(0).keySet();
        StringBuilder sql = new StringBuilder();
//        sql.append("INSERT INTO ").append(tableName).append(" (");
//        sql.append(String.join(", ", columns)).append(") VALUES ");
        sql.append("INSERT INTO ").append(tableName).append(" (");
        sql.append(columns.stream().map(column -> "\"" + column.toLowerCase() + "\"").collect(Collectors.joining(", ")));
        sql.append(") VALUES ");

        // 构建每行的数据
        for (int i = 0; i < dataList.size(); i++) {
            Map<String, Object> data = dataList.get(i);
            sql.append("(");
            for (String column : columns) {
                sql.append("#{dataList[").append(i).append("].").append(column).append("}, ");
            }
            sql.setLength(sql.length() - 2); // 去掉最后的逗号
            sql.append("), ");
        }
        sql.setLength(sql.length() - 2); // 去掉最后的逗号

        return sql.toString();
    }
}
