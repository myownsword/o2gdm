package com.tp.o2gdm.o2gdmcore.service.impl;

import com.google.common.collect.Lists;
import com.tp.o2gdm.o2gdmcore.mapper.source.OracleMapper;
import com.tp.o2gdm.o2gdmcore.mapper.target.GaussMapper;
import com.tp.o2gdm.o2gdmcore.service.O2gdmService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
public class O2gdmServiceImpl implements O2gdmService {

    private static Logger log = LoggerFactory.getLogger(O2gdmServiceImpl.class);

    @Autowired
    private com.tp.o2gdm.o2gdmcore.mapper.source.SourceMapper sourceMapper;

    @Autowired
    private com.tp.o2gdm.o2gdmcore.mapper.target.TargetMapper targetMapper;

    @Autowired
    private OracleMapper oracleMapper;

    @Autowired
    private GaussMapper gaussMapper;

    @Value("${spring.datasource.schemaName:tp_sems}")
    private String schemaName;

    private static final int BATCH_SIZE = 1000; // 批处理大小


    public List<Map<String,String>> getAllTableList() {
        return sourceMapper.getAllTableList();
    }

    public List<Map<String,String>> getTableDataList(String tableName) {
        return sourceMapper.getTableDataList(tableName);
    }

    public void o2gdmRun2() throws Exception {
        log.info("执行数据迁移更新");
        //获取fizz网关路由信息

        StopWatch stopWatch = new StopWatch();

        stopWatch.start("批量更新新数据迁移");
        List<Map<String,String>> allTableList = getAllTableList();
        log.info("数据库表数："+allTableList.size());
        stopWatch.stop();

        // 清除已同步数据
        stopWatch.start("清除已同步数据新数据迁移");
        deleteTableData();
        stopWatch.stop();
        //更新到路由表

        // 同步表数据
        stopWatch.start("同步表数据");
        for(Map<String,String> tableInfo : allTableList) {
            String tableName = tableInfo.get("TABLE_NAME");

            List<Map<String, String>> tableDataList = getTableDataList(tableName);

            insertData(tableName, tableDataList);
        }
        stopWatch.stop();

        log.info("同步表数据：" + stopWatch.getLastTaskTimeMillis() + "毫秒");
        log.info("所有任务耗时：" + stopWatch.getTotalTimeMillis() + "毫秒");
        //打印每个任务执行时间，以及占总时间百分比
        log.info(stopWatch.prettyPrint());
        log.info(stopWatch.toString());

        log.info("执行数据迁移更新结束");

    }

    public void insertData(String tableName, List<Map<String,String>> tableDataList){

        List<List<Map<String, String>>> partition= Lists.partition(tableDataList, 500);
        Integer insertTempRes=0;
        for (List<Map<String, String>> list:partition){
            if(list!=null && list.size()!=0) {

                // TODO
                insertTempRes += targetMapper.insertTableData(tableName, list);

            }
        }
        log.info("插入新数据迁移实际总数："+insertTempRes);
    }

    public  void deleteTableData() {

    }

    public String o2gdmRun() throws Exception {
        log.info("执行数据迁移更新");
        //获取fizz网关路由信息

        StopWatch stopWatch = new StopWatch();

        stopWatch.start("批量更新新数据迁移");
        migrateData();
        stopWatch.stop();

        for (StopWatch.TaskInfo task : stopWatch.getTaskInfo()) {
            System.out.println(task.getTaskName() + ": " + task.getTimeSeconds() + " 秒");
        }
        double totalTimeSeconds = stopWatch.getTotalTimeSeconds();
        log.info("所有任务耗时：" + totalTimeSeconds + "秒");
        //打印每个任务执行时间，以及占总时间百分比
        log.info(stopWatch.prettyPrint());
        log.info(stopWatch.toString());

        log.info("执行数据迁移更新结束");

        return String.valueOf(totalTimeSeconds);

    }

    public void migrateData() {
        List<String> tableNames = oracleMapper.getAllTableNames(); // 获取所有表名
        for (String tableName : tableNames) {
            try {
                // 查询是否存在该表
                int count = gaussMapper.isTableExist(tableName, schemaName);
                if (count == 0) {
                    log.warn("Not exsit table: " + tableName);
                    continue; // 不存在则继续下一个表
                }
                gaussMapper.clearTable(tableName); // 先清空目标表
                migrateTableData(tableName);
            } catch (Exception e) {
                logError(tableName, e); // 记录出错表
            }
        }
    }

    private void migrateTableData(String tableName) {
        int offset = 0;
        List<Map<String, Object>> batchData;
        do {
            batchData = oracleMapper.getTableData(tableName, offset, BATCH_SIZE);
            if (!batchData.isEmpty()) {
                gaussMapper.insertBatchData(tableName, batchData);
                offset += BATCH_SIZE;
            }
        } while (batchData.size() == BATCH_SIZE);
    }

    private void logError(String tableName, Exception e) {
        // 将失败的表名和异常信息记录
        log.error("Failed to migrate table: " + tableName, e);
    }
}

