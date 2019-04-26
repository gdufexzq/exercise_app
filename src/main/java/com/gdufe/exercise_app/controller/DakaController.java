package com.gdufe.exercise_app.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gdufe.exercise_app.Token.TokenInfoMap;
import com.gdufe.exercise_app.aop.TokenAuth;
import com.gdufe.exercise_app.entity.*;
import com.gdufe.exercise_app.service.*;
import com.gdufe.exercise_app.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xuzhiquan
 * @since 2019-03-15
 */
@RestController
@RequestMapping("/daka")
public class DakaController {

    @Autowired
    private DakaService dakaService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserService userService;

    @Autowired
    private CoinDetailService coinDetailService;

    @Autowired
    private CoinTotalService coinTotalService;


    /**
     * 判断当前是否已打卡
     * 通过判断数据库中是否已存在当天的记录，如果已存在，说明已打卡
     * 不存在，说明还没打卡
     * 0-没打卡，
     * 1-已打卡
     */
    @RequestMapping("/isDaka")
//    @TokenAuth
    public int isDakas(@RequestParam String token) {
        TokenInfo tokenInfo = (TokenInfo) TokenInfoMap.tokenMap.get(token);
        String openId = tokenInfo.getOpen_id();
        String curDay = DateUtil.longToString(new Date().getTime());
        long[] curDays = DateUtil.getCurDay(curDay);
        long startDay = curDays[0];
        long endDay = curDays[1];

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("openid", openId);
        queryWrapper.ge("create_time", startDay);
        queryWrapper.le("create_time", endDay);
        List list = dakaService.list(queryWrapper);
        if (list != null && list.size() != 0) {
            return 1;
        }
        return 0;
    }

    /**
     * 获取打卡信息
     * @param token
     * @param curYear
     * @param curMonth
     * @param day
     * @return
     */
    @RequestMapping("/dakaInfo")
    public Object[] dakaInfo(@RequestParam String token, String curYear, String curMonth, String day) {
        Object[] speciallist = null;
        TokenInfo tokenInfo = (TokenInfo) TokenInfoMap.tokenMap.get(token);
        String openId = tokenInfo.getOpen_id();
        String yearMonth = "";
        System.out.println(curYear + "-" + curMonth + "-" + day);
        if(curMonth.length() < 2) {
            curMonth = "0" + curMonth;
        }
        if(day.length() < 2) {
            day = "0" + day;
        }
        yearMonth = curYear + "-" + curMonth;

        //这个月的开始结束时间戳
        long[] monthStamp = DateUtil.getLongStamp(curYear, curMonth);

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("openid", openId);
        queryWrapper.ge("cur_time", monthStamp[0]);
        queryWrapper.le("cur_time", monthStamp[1]);
        List<Daka> dakalist = dakaService.list(queryWrapper);

        speciallist = new Object[dakalist.size()];
        for (int i=0; i<dakalist.size(); i++) {
            Daka daka = dakalist.get(i);
            Map specialone = new HashMap<>();
            Long currentDate = daka.getCurTime();
            String date = DateUtil.longToString(currentDate);
            specialone.put("date", date);
            specialone.put("background", "orange");
            specialone.put("text", "");
            speciallist[i] = specialone;
        }
        return speciallist ;
    }

    /**
     * 添加打卡记录
     * @param token
     * @return
     */
    @RequestMapping("/dakaAddInfo")
    private List dakaAddInfo(@RequestParam String token){
        List res = new ArrayList();
        TokenInfo tokenInfo = (TokenInfo) TokenInfoMap.tokenMap.get(token);
        String openId = tokenInfo.getOpen_id();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("open_id", openId);
        List<User> list = userService.list(queryWrapper);
        User user = list.get(0);

        //获取当前的时间
        Daka daka = new Daka();
        daka.setUserName(user.getUserName());
        daka.setCurTime(new Date().getTime());
        daka.setDaka(1);
        daka.setCreateTime(new Date().getTime());
        daka.setModifyTime(new Date().getTime());
        daka.setOpenid(openId);
        System.out.println(daka);
        dakaService.save(daka);

        //每日签到所得到的金币，存入coin_detail表
        long qiandaoTime = new Date().getTime();
        CoinDetail coinDetail = new CoinDetail();
        coinDetail.setCoin(10L);
        coinDetail.setBeizhu("签到");
        coinDetail.setCreateTime(qiandaoTime);
        coinDetail.setModifyTime(qiandaoTime);
        coinDetail.setOpenId(openId);
        coinDetail.setUserName(user.getUserName());

        coinDetailService.save(coinDetail);
        Map res1 = new HashMap();
        res1.put("day", 10);
        res.add(res1);
        /**
         * 添加或更新add总表
         */
        QueryWrapper totalWrapper = new QueryWrapper();
        totalWrapper.eq("open_id", openId);
        List list1 = coinTotalService.list(totalWrapper);
        CoinTotal coinTotalById = null;
        if(list1.size() != 0) {
            coinTotalById = (CoinTotal) list1.get(0);
        }
        if (coinTotalById == null) {
            //添加
            CoinTotal coinTotal = new CoinTotal();
            coinTotal.setOpenId(openId);
            coinTotal.setCoinTotal(10L);
            coinTotal.setUserName(user.getUserName());
            coinTotal.setCreateTime(qiandaoTime);
            coinTotal.setModifyTime(qiandaoTime);
            coinTotalService.save(coinTotal);
        }else {
            coinTotalById.setCoinTotal(coinTotalById.getCoinTotal() + 10L);

            coinTotalService.saveOrUpdate(coinTotalById);
        }

        /**
         *
         * 判断一下打卡时间是否是周日，如果是周日，判断一下用户上周是否连续打卡
         */
        try {
            if(DateUtil.dayForWeek(qiandaoTime) == 0 ) {

                long monStamp = DateUtil.getMonStamp(qiandaoTime);

                //判断用户上周是否连续打卡
                QueryWrapper countWrapper = new QueryWrapper();
                countWrapper.eq("beizhu", "签到");
                countWrapper.eq("open_id", openId);
                countWrapper.ge("create_time",monStamp);
                countWrapper.le("create_time", qiandaoTime);
                int count = coinDetailService.count(countWrapper);
                if(count == 7) {
                    CoinDetail coinDetail_1 = new CoinDetail();
                    coinDetail_1.setCoin(100L);
                    coinDetail_1.setBeizhu("累计7天");
                    coinDetail_1.setCreateTime(qiandaoTime);
                    coinDetail_1.setModifyTime(qiandaoTime);
                    coinDetail_1.setOpenId(openId);
                    coinDetail_1.setUserName(user.getUserName());

                    coinDetailService.save(coinDetail_1);

                    //7天额外奖励,更新总表
                    coinTotalById.setCoinTotal(coinTotalById.getCoinTotal() + 100L);
                    coinTotalService.saveOrUpdate(coinTotalById);

                    Map res2 = new HashMap();
                    res2.put("week", 100);
                    res.add(res2);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        /**
         * 判断当前月累计打卡天数
         *
         */
        String yearmonthday = DateUtil.longToString(qiandaoTime);
        String year = yearmonthday.substring(0,4);
        String month = yearmonthday.substring(5,7);
        long[] longStamp = DateUtil.getLongStamp(year, month);
        QueryWrapper monthWrapper = new QueryWrapper();
        monthWrapper.eq("beizhu", "签到");
        monthWrapper.eq("open_id", openId);
        monthWrapper.ge("create_time", longStamp[0]);
        monthWrapper.le("create_time", longStamp[1]);
        int monthCount = coinDetailService.count(monthWrapper);
        //判断当前月的天数
        if(!month.equals(02)) {
           int day = Integer.valueOf(DateUtil.months[Integer.valueOf(month)]);
            if(monthCount == day) {
                CoinDetail coinDetail_2 = new CoinDetail();
                coinDetail_2.setCoin(200L);
                coinDetail_2.setBeizhu("累计一个月");
                coinDetail_2.setCreateTime(qiandaoTime);
                coinDetail_2.setModifyTime(qiandaoTime);
                coinDetail_2.setOpenId(openId);
                coinDetail_2.setUserName(user.getUserName());

                coinDetailService.save(coinDetail_2);

                //一个月,更新总表
                coinTotalById.setCoinTotal(coinTotalById.getCoinTotal() + 200L);
                coinTotalService.saveOrUpdate(coinTotalById);
                Map res3 = new HashMap();
                res3.put("month", 200);
                res.add(res3);
            }
        } else {
            boolean leap = DateUtil.isLeap(year);
            if(leap) {
                if(monthCount == 29) {
                    CoinDetail coinDetail_2 = new CoinDetail();
                    coinDetail_2.setCoin(200L);
                    coinDetail_2.setBeizhu("累计一个月");
                    coinDetail_2.setCreateTime(qiandaoTime);
                    coinDetail_2.setModifyTime(qiandaoTime);
                    coinDetail_2.setOpenId(openId);
                    coinDetail_2.setUserName(user.getUserName());

                    coinDetailService.save(coinDetail_2);

                    //一个月,更新总表
                    coinTotalById.setCoinTotal(coinTotalById.getCoinTotal() + 200L);
                    coinTotalService.saveOrUpdate(coinTotalById);
                    Map res3 = new HashMap();
                    res3.put("month", 200);
                    res.add(res3);
                }else {
                    if ( monthCount == 28) {
                        CoinDetail coinDetail_2 = new CoinDetail();
                        coinDetail_2.setCoin(200L);
                        coinDetail_2.setBeizhu("累计一个月");
                        coinDetail_2.setCreateTime(qiandaoTime);
                        coinDetail_2.setModifyTime(qiandaoTime);
                        coinDetail_2.setOpenId(openId);
                        coinDetail_2.setUserName(user.getUserName());

                        coinDetailService.save(coinDetail_2);

                        //一个月,更新总表
                        coinTotalById.setCoinTotal(coinTotalById.getCoinTotal() + 200L);
                        coinTotalService.saveOrUpdate(coinTotalById);
                        //传回前端
                        Map res3 = new HashMap();
                        res3.put("month", 200);
                        res.add(res3);
                    }
                }
            }
        }
        return res;
    }

}

