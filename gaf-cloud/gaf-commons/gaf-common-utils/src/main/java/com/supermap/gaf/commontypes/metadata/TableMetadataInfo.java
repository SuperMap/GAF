package com.supermap.gaf.commontypes.metadata;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author:yw
 * @Date 2021/3/17
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class TableMetadataInfo {
    private String tableName;
    private String remarks;
    private List<PkMetadataInfo> pkMetadataInfos;
    private List<FieldMetadataInfo> fieldMetadataInfos;
}
