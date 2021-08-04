/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.data.mgt.enums;

import com.alibaba.fastjson.JSONObject;
import com.supermap.data.conversion.*;
import com.supermap.gaf.data.mgt.conversion.ExportSettingParser;

/**
 * 数据导出类型
 *
 * @author wxl
 * @since 2021-8-3
 */
public enum ExportSettingTypeEnum implements ExportSettingParser {

    BMP { // 需要提前单独处理WorldFilePath
        @Override
        public ExportSetting parseExportSetting(JSONObject exportSettingJO) {
            return super.parse(exportSettingJO, ExportSettingBMP.class,
                    (exportSettingBMP, basePartJO) -> {});
        }
    },
    CSV {
        @Override
        public ExportSetting parseExportSetting(JSONObject exportSettingJO) {
            return super.parse(exportSettingJO, ExportSettingCSV.class,
                    (exportSettingCSV, basePartJO) -> {});
        }
    },
    //DXF,
    E00 {
        @Override
        public ExportSetting parseExportSetting(JSONObject exportSettingJO) {
            return super.parse(exportSettingJO, ExportSettingE00.class,
                    (exportSettingE00, basePartJO) -> {});
        }
    },
    EGC {
        @Override
        public ExportSetting parseExportSetting(JSONObject exportSettingJO) {
            return super.parse(exportSettingJO, ExportSettingEGC.class,
                    (exportSettingEGC, basePartJO) -> {});
        }
    },
    FILE_GDB_VECTOR {
        @Override
        public ExportSetting parseExportSetting(JSONObject exportSettingJO) {
            return super.parse(exportSettingJO, ExportSettingFileGDBVector.class,
                    (exportSettingFileGDBVector, basePartJO) -> {});
        }
    },
    GEO_JSON {
        @Override
        public ExportSetting parseExportSetting(JSONObject exportSettingJO) {
            return super.parse(exportSettingJO, ExportSettingGeoJson.class,
                    (exportSettingGeoJson, basePartJO) -> {});
        }
    },
    GIF { // 需要提前单独处理WorldFilePath
        @Override
        public ExportSetting parseExportSetting(JSONObject exportSettingJO) {
            return super.parse(exportSettingJO, ExportSettingGIF.class,
                    (exportSettingGIF, basePartJO) -> {});
        }
    },
    GJB {
        @Override
        public ExportSetting parseExportSetting(JSONObject exportSettingJO) {
            return super.parse(exportSettingJO, ExportSettingGJB.class,
                    (exportSettingGJB, basePartJO) -> {});
        }
    },
    JPG { // 需要提前单独处理WorldFilePath
        @Override
        public ExportSetting parseExportSetting(JSONObject exportSettingJO) {
            return super.parse(exportSettingJO, ExportSettingGJB.class,
                    (exportSettingGJB, basePartJO) -> {});
        }
    },
    KML {
        @Override
        public ExportSetting parseExportSetting(JSONObject exportSettingJO) {
            return super.parse(exportSettingJO, ExportSettingKML.class,
                    (exportSettingKML, basePartJO) -> {});
        }
    },
    KMZ {
        @Override
        public ExportSetting parseExportSetting(JSONObject exportSettingJO) {
            return super.parse(exportSettingJO, ExportSettingKMZ.class,
                    (exportSettingKMZ, basePartJO) -> {});
        }
    },
    MODEL_X {
        @Override
        public ExportSetting parseExportSetting(JSONObject exportSettingJO) {
            return super.parse(exportSettingJO, ExportSettingModelX.class,
                    (exportSettingKMZModelX, basePartJO) -> {});
        }
    },
    //PERSONAL_GDB_VECTOR,
    PNG { // 需要提前处理WorldFilePath
        @Override
        public ExportSetting parseExportSetting(JSONObject exportSettingJO) {
            return super.parse(exportSettingJO, ExportSettingPNG.class,
                    (exportSettingPNG, basePartJO) -> {});
        }

    },
    SIT {
        @Override
        public ExportSetting parseExportSetting(JSONObject exportSettingJO) {
            return super.parse(exportSettingJO, ExportSettingSIT.class,
                    (exportSettingSIT, basePartJO) -> {});
        }
    },
    TAB { // 需要提前单独处理StyleMappingTableFile 路径
        @Override
        public ExportSetting parseExportSetting(JSONObject exportSettingJO) {
            return super.parse(exportSettingJO, ExportSettingTAB.class,
                    (exportSettingTAB, basePartJO) -> {});
        }
    },
    TEMS_BUILDING_VECTOR {
        @Override
        public ExportSetting parseExportSetting(JSONObject exportSettingJO) {
            return super.parse(exportSettingJO, ExportSettingTEMSBuildingVector.class,
                    (exportSettingTEMSBuildingVector, basePartJO) -> {});
        }
    },
    TEMS_CLUTTER {
        @Override
        public ExportSetting parseExportSetting(JSONObject exportSettingJO) {
            return super.parse(exportSettingJO, ExportSettingTEMSClutter.class,
                    (exportSettingTEMSClutter, basePartJO) -> {});
        }
    },
    TEMS_TEXT_LABELS {
        @Override
        public ExportSetting parseExportSetting(JSONObject exportSettingJO) {
            return super.parse(exportSettingJO, ExportSettingTEMSTextLabels.class,
                    (exportSettingTEMSTextLabels, basePartJO) -> {});
        }
    },
    TEMS_VECTOR {
        @Override
        public ExportSetting parseExportSetting(JSONObject exportSettingJO) {
            return super.parse(exportSettingJO, ExportSettingTEMSVector.class,
                    (exportSettingTEMSVector, basePartJO) -> {});
        }
    },
    TIF {
        @Override
        public ExportSetting parseExportSetting(JSONObject exportSettingJO) {
            return super.parse(exportSettingJO, ExportSettingTIF.class,
                    (exportSettingTIF, basePartJO) -> {});
        }
    },
    VCT {
        @Override
        public ExportSetting parseExportSetting(JSONObject exportSettingJO) {
            return super.parse(exportSettingJO, ExportSettingVCT.class,
                    (exportSettingVCT, basePartJO) -> {});
        }
    };


}
