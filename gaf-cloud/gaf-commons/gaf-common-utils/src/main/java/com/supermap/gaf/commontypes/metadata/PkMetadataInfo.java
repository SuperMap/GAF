package com.supermap.gaf.commontypes.metadata;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel("主键元数据信息")
public class PkMetadataInfo {
    private String columnName;
    private String pkName;
    private Integer seq;
}
