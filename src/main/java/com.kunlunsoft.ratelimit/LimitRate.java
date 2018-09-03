package com.kunlunsoft.ratelimit;

import lombok.Data;

/***
 * 限流器实现 <br />
 * @author wuhao
 */
@Data
public class LimitRate {
    /***
     * 每秒钟几个令牌
     */
    private int limit_rate;
    /**
     * 当前剩余可用的令牌数量
     */
    private float last_count;
    /***
     * 最近一次获取令牌的时间
     */
    private long last_time;

    public LimitRate(int limit_rate) {
        this.limit_rate = limit_rate;
        this.last_count = 0;
        this.last_time = -1;
    }

    public boolean test(long current_time) {
        if (last_count >= 1) {
            return true;
        }
        last_count += (current_time - last_time) * limit_rate / 1000f;
        if (last_count > limit_rate) {
            last_count = limit_rate;
        }
        last_time = current_time;
        return last_count >= 1;
    }

    /***
     * 消费令牌
     * @param current_time
     * @return
     */
    public boolean tryConsume(long current_time) {
        if (test(current_time)) {
            last_count--;
            return true;
        }
        return false;
    }

}