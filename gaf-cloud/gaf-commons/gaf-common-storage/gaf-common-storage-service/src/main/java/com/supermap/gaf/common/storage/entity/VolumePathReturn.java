package com.supermap.gaf.common.storage.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VolumePathReturn {
    private String path;
    private String publicUrl;
}
