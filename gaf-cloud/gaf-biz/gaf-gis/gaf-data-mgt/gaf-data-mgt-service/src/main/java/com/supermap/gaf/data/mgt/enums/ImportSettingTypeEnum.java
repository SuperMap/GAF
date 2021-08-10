/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.data.mgt.enums;

import com.alibaba.fastjson.JSONObject;
import com.supermap.data.FieldType;
import com.supermap.data.conversion.*;
import com.supermap.gaf.data.mgt.conversion.ImportSettingParser;

import java.util.function.Consumer;


/**
 * @author wxl
 * @since 2021-7-30
 */
public enum ImportSettingTypeEnum implements ImportSettingParser {


    AI_BIN_GRID {
        @Override
        public ImportSetting parseImportSetting(JSONObject importSettingJO, Consumer<ImportSetting> consumer) {
            return super.parse(importSettingJO, ImportSettingAiBinGrid.class,
                    (importSettingAiBinGrid, jsonObject) -> consumer.accept(importSettingAiBinGrid),
                    ImportDataInfoAiBinGrid.class,
                    (importDataInfoAiBinGrid, jsonObject) -> {});
        }
    },
    BIL {
        @Override
        public ImportSetting parseImportSetting(JSONObject importSettingJO, Consumer<ImportSetting> consumer) {
            return super.parse(importSettingJO, ImportSettingBIL.class,
                    (importSettingBIL, jsonObject) -> consumer.accept(importSettingBIL),
                    ImportDataInfoBIL.class,
                    (importDataInfoBIL, jsonObject) -> {});
        }
    },
    BIP {
        @Override
        public ImportSetting parseImportSetting(JSONObject importSettingJO, Consumer<ImportSetting> consumer) {
            return super.parse(importSettingJO, ImportSettingBIP.class,
                    (importSettingBIP, jsonObject) -> consumer.accept(importSettingBIP),
                    ImportDataInfoBIP.class,
                    (importDataInfoBIP, jsonObject) -> {});
        }
    },
    //BMP,
    BSQ {
        @Override
        public ImportSetting parseImportSetting(JSONObject importSettingJO, Consumer<ImportSetting> consumer) {
            return super.parse(importSettingJO, ImportSettingBSQ.class,
                    (importSettingBSQ, jsonObject) -> consumer.accept(importSettingBSQ),
                    ImportDataInfoBSQ.class,
                    (importDataInfoBSQ, jsonObject) -> {});
        }
    },
    //COVERAGE,
    CSV {
        @Override
        public ImportSetting parseImportSetting(JSONObject importSettingJO, Consumer<ImportSetting> consumer) {
            return super.parse(importSettingJO, ImportSettingCSV.class,
                    (importSettingCSV, jsonObject) -> consumer.accept(importSettingCSV),
                    ImportDataInfoCSV.class,
                    (importDataInfoCSV, jsonObject) -> {
                        super.parseChangeFieldName(jsonObject, importDataInfoCSV::changeFieldName);

                        JSONObject changeFieldTypeJO = jsonObject.getJSONObject("changeFieldType");
                        if (changeFieldTypeJO != null && !changeFieldTypeJO.isEmpty()) {
                            String oldName = changeFieldTypeJO.getString("oldFieldName");
                            String newFieldTypeStr = changeFieldTypeJO.getString("newFieldType");
                            FieldType newFieldTye = (FieldType) FieldType.parse(FieldType.class, newFieldTypeStr);
                            importDataInfoCSV.changeFieldType(oldName, newFieldTye);
                        }
                        super.parseExchangeFieldOrder(jsonObject, importDataInfoCSV::exchangeFieldOrder);

                        super.parseImportFieldState(jsonObject,importDataInfoCSV::setImportFieldState);

                        super.parseTargetFieldInfos(jsonObject, importDataInfoCSV::setTargetFieldInfos);
                    });
        }
    },
    DBF {
        @Override
        public ImportSetting parseImportSetting(JSONObject importSettingJO, Consumer<ImportSetting> consumer) {
            return super.parse(importSettingJO, ImportSettingDBF.class,
                    (importSettingDBF, jsonObject) -> consumer.accept(importSettingDBF),
                    ImportDataInfoDBF.class,
                    (importDataInfoDBF, jsonObject) -> {
                        super.parseTargetFieldInfos(jsonObject, fieldInfos -> importDataInfoDBF.setTargetFieldInfos(fieldInfos.toArray()));
                    });
        }
    },
    //DGN,
    //DWG,
    //DXF,
    E00 { //SpatialIndexInfo
        @Override
        public ImportSetting parseImportSetting(JSONObject importSettingJO, Consumer<ImportSetting> consumer) {
            return super.parse(importSettingJO, ImportSettingE00.class,
                    (importSettingE00, jsonObject) -> consumer.accept(importSettingE00),
                    ImportDataInfoE00.class,
                    (importDataInfoE00, jsonObject) -> {
                        super.parseTargetFieldInfos(jsonObject, fieldInfos -> importDataInfoE00.setTargetFieldInfos(fieldInfos.toArray()));
                    });
        }
    },
    ECW {
        @Override
        public ImportSetting parseImportSetting(JSONObject importSettingJO, Consumer<ImportSetting> consumer) {
            return super.parse(importSettingJO, ImportSettingECW.class,
                    (importSettingECW, jsonObject) -> consumer.accept(importSettingECW),
                    ImportDataInfoECW.class,
                    (importDataInfoECW, jsonObject) -> {});
        }
    },
    //FILE_GDB_VECTOR,
    GBDEM {
        @Override
        public ImportSetting parseImportSetting(JSONObject importSettingJO, Consumer<ImportSetting> consumer) {
            return super.parse(importSettingJO, ImportSettingGBDEM.class,
                    (importSettingGBDEM, jsonObject) -> consumer.accept(importSettingGBDEM),
                    ImportDataInfoGBDEM.class,
                    (importDataInfoGBDEM, jsonObject) -> {});
        }
    },
    GEO_3D_ML {
        @Override
        public ImportSetting parseImportSetting(JSONObject importSettingJO, Consumer<ImportSetting> consumer) {
            return super.parse(importSettingJO, ImportSettingGeo3DML.class,
                    (importSettingGeo3DML, jsonObject) -> consumer.accept(importSettingGeo3DML),
                    null,
                    (importDataInfoGeo3DML, jsonObject) -> {});
        }

    },
    GEO_JSON {
        @Override
        public ImportSetting parseImportSetting(JSONObject importSettingJO, Consumer<ImportSetting> consumer) {
            return super.parse(importSettingJO, ImportSettingGeoJson.class,
                    (importSettingGeoJson, jsonObject) -> consumer.accept(importSettingGeoJson),
                    ImportDataInfoGeoJson.class,
                    (importDataInfoGeoJson, jsonObject) -> {
                        super.parseTargetFieldInfos(jsonObject, fieldInfos -> importDataInfoGeoJson.setTargetFieldInfos(fieldInfos.toArray()));
                    });
        }
    },
    GIF { // 需要后续单独处理WorldFilePath, 获取真实路径
        @Override
        public ImportSetting parseImportSetting(JSONObject importSettingJO, Consumer<ImportSetting> consumer) {
            return super.parse(importSettingJO, ImportSettingGIF.class,
                    (importSettingGIF, jsonObject) -> consumer.accept(importSettingGIF),
                    ImportDataInfoGIF.class,
                    (importDataInfoGIF, jsonObject) -> {});
        }
    },
    //GJB,
    //GML,
    GPKG {
        @Override
        public ImportSetting parseImportSetting(JSONObject importSettingJO, Consumer<ImportSetting> consumer) {
            return super.parse(importSettingJO, ImportSettingGPKG.class,
                    (importSettingGPKG, jsonObject) -> consumer.accept(importSettingGPKG),
                    ImportDataInfoGPKG.class,
                    (importDataInfoGPKG, jsonObject) -> {
                        super.parseChangeFieldName(jsonObject, importDataInfoGPKG::changeFieldName);
                        super.parseExchangeFieldOrder(jsonObject,importDataInfoGPKG::exchangeFieldOrder);
                        super.parseImportFieldState(jsonObject,importDataInfoGPKG::setImportFieldState);
                        super.parseTargetFieldInfos(jsonObject, fieldInfos -> importDataInfoGPKG.setTargetFieldInfos(fieldInfos.toArray()));
                    });
        }
    },
    GRD {
        @Override
        public ImportSetting parseImportSetting(JSONObject importSettingJO, Consumer<ImportSetting> consumer) {
            return super.parse(importSettingJO, ImportSettingGRD.class,
                    (importSettingGRD, jsonObject) -> consumer.accept(importSettingGRD),
                    ImportDataInfoGRD.class,
                    (importDataInfoGRD, jsonObject) -> {});
        }
    },
    GRIB {
        @Override
        public ImportSetting parseImportSetting(JSONObject importSettingJO, Consumer<ImportSetting> consumer) {
            return super.parse(importSettingJO, ImportSettingGRIB.class,
                    (importSettingGRIB, jsonObject) -> consumer.accept(importSettingGRIB),
                    ImportDataInfoGRIB.class,
                    (importDataInfoGRIB, jsonObject) -> {});
        }
    },
    IMG {
        @Override
        public ImportSetting parseImportSetting(JSONObject importSettingJO, Consumer<ImportSetting> consumer) {
            return super.parse(importSettingJO, ImportSettingIMG.class,
                    (importSettingIMG, jsonObject) -> consumer.accept(importSettingIMG),
                    ImportDataInfoIMG.class,
                    (importDataInfoIMG, jsonObject) -> {});
        }
    },
    JP2 {
        @Override
        public ImportSetting parseImportSetting(JSONObject importSettingJO, Consumer<ImportSetting> consumer) {
            return super.parse(importSettingJO, ImportSettingJP2.class,
                    (importSettingJP2, jsonObject) -> consumer.accept(importSettingJP2),
                    ImportDataInfoJP2.class,
                    (importDataInfoJP2, jsonObject) -> {});
        }
    },
    JPG {
        @Override
        public ImportSetting parseImportSetting(JSONObject importSettingJO, Consumer<ImportSetting> consumer) {
            return super.parse(importSettingJO, ImportSettingJPG.class,
                    (importSettingJPG, jsonObject) -> consumer.accept(importSettingJPG),
                    ImportDataInfoJPG.class,
                    (importDataInfoJPG, jsonObject) -> {});
        }
    },
    KML {
        @Override
        public ImportSetting parseImportSetting(JSONObject importSettingJO, Consumer<ImportSetting> consumer) {
            return super.parse(importSettingJO, ImportSettingKML.class,
                    (importSettingKML, jsonObject) -> consumer.accept(importSettingKML),
                    ImportDataInfoKML.class,
                    (importDataInfoKML, jsonObject) -> {
                        super.parseChangeFieldName(jsonObject,importDataInfoKML::changeFieldName);
                        super.parseExchangeFieldOrder(jsonObject,importDataInfoKML::exchangeFieldOrder);
                        super.parseImportFieldState(jsonObject,importDataInfoKML::setImportFieldState);
                        super.parseTargetFieldInfos(jsonObject, fieldInfos -> importDataInfoKML.setTargetFieldInfos(fieldInfos.toArray()));
                    });
        }
    },
    KMZ {
        @Override
        public ImportSetting parseImportSetting(JSONObject importSettingJO, Consumer<ImportSetting> consumer) {
            return super.parse(importSettingJO, ImportSettingKMZ.class,
                    (importSettingKMZ, jsonObject) -> consumer.accept(importSettingKMZ),
                    ImportDataInfoKMZ.class,
                    (importDataInfoKMZ, jsonObject) -> {
                        super.parseChangeFieldName(jsonObject,importDataInfoKMZ::changeFieldName);
                        super.parseExchangeFieldOrder(jsonObject,importDataInfoKMZ::exchangeFieldOrder);
                        super.parseImportFieldState(jsonObject,importDataInfoKMZ::setImportFieldState);
                        super.parseTargetFieldInfos(jsonObject, fieldInfos -> importDataInfoKMZ.setTargetFieldInfos(fieldInfos.toArray()));
                    });
        }
    },
    LIDAR {
        @Override
        public ImportSetting parseImportSetting(JSONObject importSettingJO, Consumer<ImportSetting> consumer) {
            return super.parse(importSettingJO, ImportSettingLIDAR.class,
                    (importSettingLIDAR, jsonObject) -> consumer.accept(importSettingLIDAR),
                    ImportDataInfoLIDAR.class,
                    (importDataInfoLIDAR, jsonObject) -> {
                        super.parseChangeFieldName(jsonObject,importDataInfoLIDAR::changeFieldName);
                        super.parseExchangeFieldOrder(jsonObject,importDataInfoLIDAR::exchangeFieldOrder);
                        super.parseImportFieldState(jsonObject,importDataInfoLIDAR::setImportFieldState);
                        super.parseTargetFieldInfos(jsonObject, fieldInfos -> importDataInfoLIDAR.setTargetFieldInfos(fieldInfos.toArray()));
                    });
        }
    },
    //MAPGIS,
    MIF {
        @Override
        public ImportSetting parseImportSetting(JSONObject importSettingJO, Consumer<ImportSetting> consumer) {
            return super.parse(importSettingJO, ImportSettingMIF.class,
                    (importSettingMIF, jsonObject) -> consumer.accept(importSettingMIF),
                    ImportDataInfoMIF.class,
                    (importDataInfoMIF, jsonObject) -> {
                        super.parseChangeFieldName(jsonObject,importDataInfoMIF::changeFieldName);
                        super.parseExchangeFieldOrder(jsonObject,importDataInfoMIF::exchangeFieldOrder);
                        super.parseImportFieldState(jsonObject,importDataInfoMIF::setImportFieldState);
                        super.parseTargetFieldInfos(jsonObject, fieldInfos -> importDataInfoMIF.setTargetFieldInfos(fieldInfos.toArray()));
                    });
        }
    },
    MODEL_3D_S {
        @Override
        public ImportSetting parseImportSetting(JSONObject importSettingJO, Consumer<ImportSetting> consumer) {
            return super.parse(importSettingJO, ImportSettingModel3DS.class,
                    (importSettingModel3DS, jsonObject) -> consumer.accept(importSettingModel3DS),
                    null,
                    (nullClazz, jsonObject) -> {});
        }
    },
    //MODEL_DXF,
    //MODEL_FBX,
    //MODEL_FLT,
    MODEL_OSG {
        @Override
        public ImportSetting parseImportSetting(JSONObject importSettingJO, Consumer<ImportSetting> consumer) {
            return super.parse(importSettingJO, ImportSettingModelOSG.class,
                    (importSettingModelOSG, jsonObject) -> consumer.accept(importSettingModelOSG),
                    null,
                    (nullClazz, jsonObject) -> {});
        }
    },
    MODEL_X {
        @Override
        public ImportSetting parseImportSetting(JSONObject importSettingJO, Consumer<ImportSetting> consumer) {
            return super.parse(importSettingJO, ImportSettingModelX.class,
                    (importSettingModelX, jsonObject) -> consumer.accept(importSettingModelX),
                    null,
                    (nullClazz, jsonObject) -> {});
        }
    },
    MR_SID {
        @Override
        public ImportSetting parseImportSetting(JSONObject importSettingJO, Consumer<ImportSetting> consumer) {
            return super.parse(importSettingJO, ImportSettingMrSID.class,
                    (importSettingMrSID, jsonObject) -> consumer.accept(importSettingMrSID),
                    ImportDataInfoMrSID.class,
                    (importDataInfoMrSID, jsonObject) -> {});
        }
    },
    ORANGE_TAB {
        @Override
        public ImportSetting parseImportSetting(JSONObject importSettingJO, Consumer<ImportSetting> consumer) {
            return super.parse(importSettingJO, ImportSettingOrangeTab.class,
                    (importSettingOrangeTab, jsonObject) -> consumer.accept(importSettingOrangeTab),
                    ImportDataInfoOrangeTab.class,
                    (importDataInfoOrangeTab, jsonObject) -> {
                        super.parseChangeFieldName(jsonObject,importDataInfoOrangeTab::changeFieldName);
                        super.parseExchangeFieldOrder(jsonObject,importDataInfoOrangeTab::exchangeFieldOrder);
                        super.parseImportFieldState(jsonObject,importDataInfoOrangeTab::setImportFieldState);
                        super.parseTargetFieldInfos(jsonObject, fieldInfos -> importDataInfoOrangeTab.setTargetFieldInfos(fieldInfos.toArray()));
                    });
        }
    },
    //OSM,
    //PERSONAL_GDB_VECTOR,
    PNG { // 注意 WorldFilePath 需要单独处理解析为实际路径
        @Override
        public ImportSetting parseImportSetting(JSONObject importSettingJO, Consumer<ImportSetting> consumer) {
            return super.parse(importSettingJO, ImportSettingPNG.class,
                    (importSettingPNG, jsonObject) -> consumer.accept(importSettingPNG),
                    ImportDataInfoPNG.class,
                    (importDataInfoPNG, jsonObject) -> {});
        }
    },
    RAW {
        @Override
        public ImportSetting parseImportSetting(JSONObject importSettingJO, Consumer<ImportSetting> consumer) {
            return super.parse(importSettingJO, ImportSettingRAW.class,
                    (importSettingRAW, jsonObject) -> consumer.accept(importSettingRAW),
                    ImportDataInfoRAW.class,
                    (importDataInfoRAW, jsonObject) -> {});
        }
    },
    SCV {
        @Override
        public ImportSetting parseImportSetting(JSONObject importSettingJO, Consumer<ImportSetting> consumer) {
            return super.parse(importSettingJO, ImportSettingSCV.class,
                    (importSettingSCV, jsonObject) -> consumer.accept(importSettingSCV),
                    ImportDataInfoSCV.class,
                    (importDataInfoSCV, jsonObject) -> {
                        super.parseChangeFieldName(jsonObject,importDataInfoSCV::changeFieldName);
                        super.parseExchangeFieldOrder(jsonObject,importDataInfoSCV::exchangeFieldOrder);
                        super.parseImportFieldState(jsonObject,importDataInfoSCV::setImportFieldState);
                        super.parseTargetFieldInfos(jsonObject, fieldInfos -> importDataInfoSCV.setTargetFieldInfos(fieldInfos.toArray()));
                    });
        }
    },
    //SDE_VECTOR,
    SHP {
        @Override
        public ImportSetting parseImportSetting(JSONObject importSettingJO, Consumer<ImportSetting> consumer) {
            return super.parse(importSettingJO, ImportSettingSHP.class,
                    (importSettingSHP, jsonObject) -> consumer.accept(importSettingSHP),
                    ImportDataInfoSHP.class,
                    (importDataInfoSHP, jsonObject) -> {
                        super.parseChangeFieldName(jsonObject,importDataInfoSHP::changeFieldName);
                        super.parseExchangeFieldOrder(jsonObject,importDataInfoSHP::exchangeFieldOrder);
                        super.parseImportFieldState(jsonObject,importDataInfoSHP::setImportFieldState);
                        super.parseTargetFieldInfos(jsonObject, fieldInfos -> importDataInfoSHP.setTargetFieldInfos(fieldInfos.toArray()));
                    });
        }
    },
    SIT {
        @Override
        public ImportSetting parseImportSetting(JSONObject importSettingJO, Consumer<ImportSetting> consumer) {
            return super.parse(importSettingJO, ImportSettingSIT.class,
                    (importSettingSIT, jsonObject) -> consumer.accept(importSettingSIT),
                    ImportDataInfoSIT.class,
                    (importDataInfoSIT, jsonObject) -> {});
        }
    },
    SKP {
        @Override
        public ImportSetting parseImportSetting(JSONObject importSettingJO, Consumer<ImportSetting> consumer) {
            return super.parse(importSettingJO, ImportSettingSKP.class,
                    (importSettingSKP, jsonObject) -> consumer.accept(importSettingSKP),
                    null,
                    (nullClazz, jsonObject) -> {});
        }
    },
    TAB {
        @Override
        public ImportSetting parseImportSetting(JSONObject importSettingJO, Consumer<ImportSetting> consumer) {
            return super.parse(importSettingJO, ImportSettingTAB.class,
                    (importSettingTAB, jsonObject) -> consumer.accept(importSettingTAB),
                    ImportDataInfoTAB.class,
                    (importDataInfoTAB, jsonObject) -> {
                        super.parseChangeFieldName(jsonObject,importDataInfoTAB::changeFieldName);
                        super.parseExchangeFieldOrder(jsonObject,importDataInfoTAB::exchangeFieldOrder);
                        super.parseImportFieldState(jsonObject,importDataInfoTAB::setImportFieldState);
                        super.parseTargetFieldInfos(jsonObject, fieldInfos -> importDataInfoTAB.setTargetFieldInfos(fieldInfos.toArray()));
                    });
        }
    },
    TEMS_BUILDING_VECTOR {
        @Override
        public ImportSetting parseImportSetting(JSONObject importSettingJO, Consumer<ImportSetting> consumer) {
            return super.parse(importSettingJO, ImportSettingTEMSBuildingVector.class,
                    (importSettingTEMSBuildingVector, jsonObject) -> consumer.accept(importSettingTEMSBuildingVector),
                    ImportDataInfoTEMSBuildingVector.class,
                    (importDataInfoTEMSBuildingVector, jsonObject) -> {
                        super.parseTargetFieldInfos(jsonObject, fieldInfos -> importDataInfoTEMSBuildingVector.setTargetFieldInfos(fieldInfos.toArray()));
                    });
        }
    },
    TEMS_CLUTTER {
        @Override
        public ImportSetting parseImportSetting(JSONObject importSettingJO, Consumer<ImportSetting> consumer) {
            return super.parse(importSettingJO, ImportSettingTEMSClutter.class,
                    (importSettingTEMSClutter, jsonObject) -> consumer.accept(importSettingTEMSClutter),
                    ImportDataInfoTEMSClutter.class,
                    (importDataInfoTEMSClutter, jsonObject) -> {});
        }
    },
    TEMS_TEXT_LABELS {
        @Override
        public ImportSetting parseImportSetting(JSONObject importSettingJO, Consumer<ImportSetting> consumer) {
            return super.parse(importSettingJO, ImportSettingTEMSTextLabels.class,
                    (importSettingTEMSTextLabels, jsonObject) -> consumer.accept(importSettingTEMSTextLabels),
                    ImportDataInfoTEMSTextLabels.class,
                    (importDataInfoTEMSTextLabels, jsonObject) -> {
                        super.parseTargetFieldInfos(jsonObject, fieldInfos -> importDataInfoTEMSTextLabels.setTargetFieldInfos(fieldInfos.toArray()));
                    });
        }
    },
    TEMS_VECTOR {
        @Override
        public ImportSetting parseImportSetting(JSONObject importSettingJO, Consumer<ImportSetting> consumer) {
            return super.parse(importSettingJO, ImportSettingTEMSVector.class,
                    (importSettingTEMSVector, jsonObject) -> consumer.accept(importSettingTEMSVector),
                    ImportDataInfoTEMSVector.class,
                    (importDataInfoTEMSVector, jsonObject) -> {
                        super.parseTargetFieldInfos(jsonObject, fieldInfos -> importDataInfoTEMSVector.setTargetFieldInfos(fieldInfos.toArray()));
                    });
        }
    },
    TIF { // WorldFilePath需要单独处理 为正确的真实路径
        @Override
        public ImportSetting parseImportSetting(JSONObject importSettingJO, Consumer<ImportSetting> consumer) {
            return super.parse(importSettingJO, ImportSettingTIF.class,
                    (importSettingTIF, jsonObject) -> consumer.accept(importSettingTIF),
                    ImportDataInfoTIF.class,
                    (importDataInfoTIF, jsonObject) -> {});
        }
    },
    USGSDEM {
        @Override
        public ImportSetting parseImportSetting(JSONObject importSettingJO, Consumer<ImportSetting> consumer) {
            return super.parse(importSettingJO, ImportSettingUSGSDEM.class,
                    (importSettingUSGSDEM, jsonObject) -> consumer.accept(importSettingUSGSDEM),
                    ImportDataInfoUSGSDEM.class,
                    (importDataInfoUSGSDEM, jsonObject) -> {});
        }
    },
    VCT {
        @Override
        public ImportSetting parseImportSetting(JSONObject importSettingJO, Consumer<ImportSetting> consumer) {
            return super.parse(importSettingJO, ImportSettingVCT.class,
                    (importSettingVCT, jsonObject) -> consumer.accept(importSettingVCT),
                    ImportDataInfoVCT.class,
                    (importDataInfoVCT, jsonObject) -> {
                        super.parseChangeFieldName(jsonObject,importDataInfoVCT::changeFieldName);
                        super.parseExchangeFieldOrder(jsonObject,importDataInfoVCT::exchangeFieldOrder);
                        super.parseImportFieldState(jsonObject,importDataInfoVCT::setImportFieldState);
                        super.parseTargetFieldInfos(jsonObject, fieldInfos -> importDataInfoVCT.setTargetFieldInfos(fieldInfos.toArray()));
                    });
        }
    },
    VRT {
        @Override
        public ImportSetting parseImportSetting(JSONObject importSettingJO, Consumer<ImportSetting> consumer) {
            return super.parse(importSettingJO, ImportSettingVRT.class,
                    (importSettingVRT, jsonObject) -> consumer.accept(importSettingVRT),
                    ImportDataInfoVRT.class,
                    (importDataInfoVRT, jsonObject) -> {});
        }
    };
    //WOR { // 需要单独处理 workspace
    //    @Override
    //    public ImportSetting parseImportSetting(JSONObject importSettingJO, Consumer<ImportSetting> consumer) {
    //        return super.parse(importSettingJO, ImportSettingWOR.class,
    //                (importSettingWOR, jsonObject) -> consumer.accept(importSettingWOR),
    //                ImportDataInfoWOR.class,
    //                (importDataInfoWOR, jsonObject) -> {});
    //    }
    //};

}

