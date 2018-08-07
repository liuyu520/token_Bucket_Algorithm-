package com.kunlunsoft.ratelimit;

import lombok.Data;

/***
 * 限流器实现 <br />
 * @author wuhao
 */
@Data
public class LimitRate {
    private int limit_rate;
    private float last_count;
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

    public boolean tryConsume(long current_time) {
        if (test(current_time)) {
            last_count--;
            return true;
        }
        return false;
    }

}