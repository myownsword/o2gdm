package com.tp.o2gdm.o2gdmcore.service;

//import net.sf.json.JSONArray;

import java.util.List;
import java.util.Map;

public interface O2gdmService {

    public List<Map<String,String>> getAllTableList();

    public String o2gdmRun() throws Exception;
}