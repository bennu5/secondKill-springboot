/*
 * Copyright 1995-2021 bennu5.
 *
 * Create date:2021/3/25
 *
 * Class name:SeckillController
 */
package com.bennu.seckill.web;

import com.bennu.seckill.dto.Exposer;
import com.bennu.seckill.dto.SeckillExecution;
import com.bennu.seckill.dto.SeckillResult;
import com.bennu.seckill.entity.Seckill;
import com.bennu.seckill.entity.SuccessKilled;
import com.bennu.seckill.enums.SeckillStatEnum;
import com.bennu.seckill.exception.RepeatKillException;
import com.bennu.seckill.exception.SeckillCloseException;
import com.bennu.seckill.mq.SecKillSender;
import com.bennu.seckill.service.SecondKillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 功能描述
 * url:资源/{id}/细分 /seckill/list
 *
 * @author bennu5
 * @version v1.0
 * @date 2021/3/25
 **/
@Slf4j
@RestController
@RequestMapping(value = "/seckill")
public class SeckillController {
    private final SecondKillService secondKillService;
    private final SecKillSender secKillSender;


    @Autowired
    public SeckillController(SecondKillService secondKillService, SecKillSender secKillSender) {
        this.secondKillService = secondKillService;
        this.secKillSender = secKillSender;
    }

    @GetMapping(value = "/execution")
    public SeckillResult<SeckillExecution> seckillReqReceiver(@RequestParam("secKillId") Long secKillId, @RequestParam("phoneNumber") Long phoneNumber) {
        SeckillExecution seckillExecution;
        SuccessKilled successKilled = new SuccessKilled();
        successKilled.setSeckillId(secKillId);
        successKilled.setUserPhone(phoneNumber);
        secKillSender.send(successKilled);
        seckillExecution = new SeckillExecution(secKillId, SeckillStatEnum.PROCESSING);
        return new SeckillResult<>(true, seckillExecution);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model) {
        // 获取列表页
        List<Seckill> list = secondKillService.getSeckillList();
        model.addAttribute("list", list);
        return "list";
    }

    @RequestMapping(value = "/{seckillId}/detail", method = RequestMethod.GET)
    public String detail(@PathVariable("seckillId") Long seckillId, Model model) {
        if (seckillId == null) {
            return "redirect:/seckill/list";
        }
        Seckill seckill = secondKillService.getById(seckillId);
        if (seckill == null) {
            return "forward:/seckill/list";
        }
        model.addAttribute("seckill", seckill);
        return "detail";
    }

    @RequestMapping(value = "/{seckillId}/exposer", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<Exposer> exposer(@PathVariable Long seckillId) {
        SeckillResult<Exposer> result;
        try {
            Exposer exposer = secondKillService.exportSeckillUrl(seckillId);
            result = new SeckillResult<>(true, exposer);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result = new SeckillResult<>(false, e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/{seckillId}/{md5}/execution", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId") Long seckillId, @PathVariable("md5") String md5, @CookieValue(value = "userPhone", required = false) Long userPhone) {
        SeckillExecution seckillExecution;
        if (userPhone == null) {
            return new SeckillResult<>(false, "用户未登录");
        }
        try {
            seckillExecution = secondKillService.executeSeckill(seckillId, userPhone);
            return new SeckillResult<>(true, seckillExecution);
        } catch (RepeatKillException e1) {
            seckillExecution = new SeckillExecution(seckillId, SeckillStatEnum.REPEAT_KILL);
            return new SeckillResult<>(true, seckillExecution);
        } catch (SeckillCloseException e2) {
            seckillExecution = new SeckillExecution(seckillId, SeckillStatEnum.END);
            return new SeckillResult<>(true, seckillExecution);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            seckillExecution = new SeckillExecution(seckillId, SeckillStatEnum.INNER_ERROR);
            return new SeckillResult<>(true, seckillExecution);
        }
    }

    @RequestMapping(value = "/time/now", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<Long> time() {
        Date now = new Date();
        return new SeckillResult<>(true, now.getTime());
    }

}
