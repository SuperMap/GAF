package com.supermap.gaf.authority.service.p3sync;

/**
 * @author : yd
 * @since : 2020-11-26
 * 第三方资源同步服务
 */
public interface P3SyncService {

    /**
     * 根据同步规则分发同步实现
     *
     * @param ruleId        规则标识
     * @param search        模糊查询值，pull方法可使用
     * @param gafResourceId gaf原始资源id，push方法可使用
     * @return 结果
     */
    Object dispatch(String ruleId, String search, String gafResourceId);


}
