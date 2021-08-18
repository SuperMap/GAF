package com.supermap.gaf.data.mgt.support;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 临时的自减的序号生成器
 *
 * 序号并不持久化
 * 不能用于分布式
 *
 * @author wxl
 * @since 2021/8/18
 */
public class TempSerialNumberGenerator {
    private AtomicInteger current;
    private int step;

    public TempSerialNumberGenerator() {
        this.current = new AtomicInteger(0);
        this.step = 1;
    }

    public TempSerialNumberGenerator(AtomicInteger initialValue,int step) {
        this.current = initialValue;
        this.step = step;
    }

    public TempSerialNumberGenerator(int initialValue,int step) {
        this.current = new AtomicInteger(initialValue);
        this.step = step;
    }

    /**
     * 序号自增或自减 并获取更新值
     * @return
     */
    public int nextInt() {
        return current.addAndGet(step);
    }


    public AtomicInteger getCurrent() {
        return current;
    }

    public int getStep() {
        return step;
    }

}
