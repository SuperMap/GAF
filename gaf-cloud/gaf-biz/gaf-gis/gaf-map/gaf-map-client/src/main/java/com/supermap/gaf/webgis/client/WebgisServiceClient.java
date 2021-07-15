package com.supermap.gaf.webgis.client;

import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.webgis.entity.WebgisService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * ClassName:WebgisServiceClient
 * Package:com.supermap.gaf.webgis.client
 *
 * @date:2021/7/15 10:06
 * @author:Yw
 */
@FeignClient(name = "GAF-MAP", contextId = "webgisServiceClient")
public interface WebgisServiceClient {
    /**
     * 根据id集合批量查询webgis服务
     * @param webgisIds
     * @return
     */
    @PostMapping("/map/webgis-services/get-by-webgis-ids")
    MessageResult<List<WebgisService>> getByWebgisIds(List<String> webgisIds);
}
