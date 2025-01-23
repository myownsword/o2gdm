package com.tp.o2gdm.o2gdmcore.controller;

import com.tp.o2gdm.o2gdmcore.service.impl.O2gdmServiceImpl;
//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/o2gdm")
public class O2gdmController {

    @Autowired
    private O2gdmServiceImpl o2gdmServiceImpl;
    private static Logger log = LoggerFactory.getLogger(O2gdmController.class);

    /**
     * o2gdmRun
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/o2gdmRun" , method= RequestMethod.GET)
    public String o2gdmRun(HttpServletRequest request) {
        log.info("数据迁移更新调用开始");
        String s = "";
        try {
            s = o2gdmServiceImpl.o2gdmRun();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        log.info("数据迁移更新调用结束");
        return "执行数据迁移完成! 执行时间:" + s + "秒";
    }
}
