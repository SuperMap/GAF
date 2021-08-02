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


/**
 * @author wxl
 * @since 2021-7-30
 */
public enum ImportSettingTypeEnum implements ImportSettingParser {


    AI_BIN_GRID {
        @Override
        public ImportSetting parseImportSetting(String jsonStr) {
            return super.parse(jsonStr, ImportSettingAiBinGrid.class,
                    (importSettingAiBinGrid, jsonObject) -> {},
                    ImportDataInfoAiBinGrid.class,
                    (importDataInfoAiBinGrid, jsonObject) -> {});
        }
    },
    BIL {
        @Override
        public ImportSetting parseImportSetting(String jsonStr) {
            return super.parse(jsonStr, ImportSettingBIL.class,
                    (importSettingBIL, jsonObject) -> {},
                    ImportDataInfoBIL.class,
                    (importDataInfoBIL, jsonObject) -> {});
        }
    },
    BIP {
        @Override
        public ImportSetting parseImportSetting(String jsonStr) {
            return super.parse(jsonStr, ImportSettingBIP.class,
                    (importSettingBIL, jsonObject) -> {},
                    ImportDataInfoBIP.class,
                    (importDataInfoBIP, jsonObject) -> {});
        }
    },
    //BMP,
    BSQ {
        @Override
        public ImportSetting parseImportSetting(String jsonStr) {
            return super.parse(jsonStr, ImportSettingBSQ.class,
                    (importSettingBSQ, jsonObject) -> {},
                    ImportDataInfoBSQ.class,
                    (importDataInfoBSQ, jsonObject) -> {});
        }
    },
    //COVERAGE,
    CSV {
        @Override
        public ImportSetting parseImportSetting(String jsonStr) {
            return super.parse(jsonStr, ImportSettingCSV.class,
                    (importSettingCSV, jsonObject) -> {},
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
        public ImportSetting parseImportSetting(String jsonStr) {
            return super.parse(jsonStr, ImportSettingDBF.class,
                    (importSettingDBF, jsonObject) -> {},
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
        public ImportSetting parseImportSetting(String jsonStr) {
            return super.parse(jsonStr, ImportSettingE00.class,
                    (importSettingE00, jsonObject) -> {},
                    ImportDataInfoE00.class,
                    (importDataInfoE00, jsonObject) -> {
                        super.parseTargetFieldInfos(jsonObject, fieldInfos -> importDataInfoE00.setTargetFieldInfos(fieldInfos.toArray()));
                    });
        }
    },
    ECW {
        @Override
        public ImportSetting parseImportSetting(String jsonStr) {
            return super.parse(jsonStr, ImportSettingECW.class,
                    (importSettingECW, jsonObject) -> {},
                    ImportDataInfoECW.class,
                    (importDataInfoECW, jsonObject) -> {});
        }
    },
    //FILE_GDB_VECTOR,
    GBDEM {
        @Override
        public ImportSetting parseImportSetting(String jsonStr) {
            return super.parse(jsonStr, ImportSettingGBDEM.class,
                    (importSettingGBDEM, jsonObject) -> {},
                    ImportDataInfoGBDEM.class,
                    (importDataInfoGBDEM, jsonObject) -> {});
        }
    },
    GEO_3D_ML {
        @Override
        public ImportSetting parseImportSetting(String jsonStr) {
            return super.parse(jsonStr, ImportSettingGeo3DML.class,
                    (importSettingGeo3DML, jsonObject) -> {},
                    null,
                    (importDataInfoGeo3DML, jsonObject) -> {});
        }

    },
    GEO_JSON {
        @Override
        public ImportSetting parseImportSetting(String jsonStr) {
            return super.parse(jsonStr, ImportSettingGeoJson.class,
                    (importSettingGeoJson, jsonObject) -> {},
                    ImportDataInfoGeoJson.class,
                    (importDataInfoGeoJson, jsonObject) -> {
                        super.parseTargetFieldInfos(jsonObject, fieldInfos -> importDataInfoGeoJson.setTargetFieldInfos(fieldInfos.toArray()));
                    });
        }
    },
    GIF { // 需要后续单独处理WorldFilePath, 获取真实路径
        @Override
        public ImportSetting parseImportSetting(String jsonStr) {
            return super.parse(jsonStr, ImportSettingGIF.class,
                    (importSettingGIF, jsonObject) -> {},
                    ImportDataInfoGIF.class,
                    (importDataInfoGIF, jsonObject) -> {});
        }
    },
    //GJB,
    //GML,
    GPKG {
        @Override
        public ImportSetting parseImportSetting(String jsonStr) {
            return super.parse(jsonStr, ImportSettingGPKG.class,
                    (importSettingGPKG, jsonObject) -> {},
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
        public ImportSetting parseImportSetting(String jsonStr) {
            return super.parse(jsonStr, ImportSettingGRD.class,
                    (importSettingGRD, jsonObject) -> {},
                    ImportDataInfoGRD.class,
                    (importDataInfoGRD, jsonObject) -> {});
        }
    },
    GRIB {
        @Override
        public ImportSetting parseImportSetting(String jsonStr) {
            return super.parse(jsonStr, ImportSettingGRIB.class,
                    (importSettingGRIB, jsonObject) -> {},
                    ImportDataInfoGRIB.class,
                    (importDataInfoGRIB, jsonObject) -> {});
        }
    },
    IMG {
        @Override
        public ImportSetting parseImportSetting(String jsonStr) {
            return super.parse(jsonStr, ImportSettingIMG.class,
                    (importSettingIMG, jsonObject) -> {},
                    ImportDataInfoIMG.class,
                    (importDataInfoIMG, jsonObject) -> {});
        }
    },
    JP2 {
        @Override
        public ImportSetting parseImportSetting(String jsonStr) {
            return super.parse(jsonStr, ImportSettingJP2.class,
                    (importSettingJP2, jsonObject) -> {},
                    ImportDataInfoJP2.class,
                    (importDataInfoJP2, jsonObject) -> {});
        }
    },
    JPG {
        @Override
        public ImportSetting parseImportSetting(String jsonStr) {
            return super.parse(jsonStr, ImportSettingJPG.class,
                    (importSettingJPG, jsonObject) -> {},
                    ImportDataInfoJPG.class,
                    (importDataInfoJPG, jsonObject) -> {});
        }
    },
    KML {
        @Override
        public ImportSetting parseImportSetting(String jsonStr) {
            return super.parse(jsonStr, ImportSettingKML.class,
                    (importSettingKML, jsonObject) -> {},
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
        public ImportSetting parseImportSetting(String jsonStr) {
            return super.parse(jsonStr, ImportSettingKMZ.class,
                    (importSettingKMZ, jsonObject) -> {},
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
        public ImportSetting parseImportSetting(String jsonStr) {
            return super.parse(jsonStr, ImportSettingLIDAR.class,
                    (importSettingLIDAR, jsonObject) -> {},
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
        public ImportSetting parseImportSetting(String jsonStr) {
            return super.parse(jsonStr, ImportSettingMIF.class,
                    (importSettingMIF, jsonObject) -> {},
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
        public ImportSetting parseImportSetting(String jsonStr) {
            return super.parse(jsonStr, ImportSettingModel3DS.class,
                    (importSettingModel3DS, jsonObject) -> {},
                    null,
                    (nullClazz, jsonObject) -> {});
        }
    },
    //MODEL_DXF,
    //MODEL_FBX,
    //MODEL_FLT,
    MODEL_OSG {
        @Override
        public ImportSetting parseImportSetting(String jsonStr) {
            return super.parse(jsonStr, ImportSettingModelOSG.class,
                    (importSettingModelOSG, jsonObject) -> {},
                    null,
                    (nullClazz, jsonObject) -> {});
        }
    },
    MODEL_X {
        @Override
        public ImportSetting parseImportSetting(String jsonStr) {
            return super.parse(jsonStr, ImportSettingModelX.class,
                    (importSettingModelX, jsonObject) -> {},
                    null,
                    (nullClazz, jsonObject) -> {});
        }
    },
    MR_SID {
        @Override
        public ImportSetting parseImportSetting(String jsonStr) {
            return super.parse(jsonStr, ImportSettingMrSID.class,
                    (importSettingMrSID, jsonObject) -> {},
                    ImportDataInfoMrSID.class,
                    (importDataInfoMrSID, jsonObject) -> {});
        }
    },
    ORANGE_TAB {
        @Override
        public ImportSetting parseImportSetting(String jsonStr) {
            return super.parse(jsonStr, ImportSettingOrangeTab.class,
                    (importSettingOrangeTab, jsonObject) -> {},
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
        public ImportSetting parseImportSetting(String jsonStr) {
            return super.parse(jsonStr, ImportSettingPNG.class,
                    (importSettingPNG, jsonObject) -> {},
                    ImportDataInfoPNG.class,
                    (importDataInfoPNG, jsonObject) -> {});
        }
    },
    RAW {
        @Override
        public ImportSetting parseImportSetting(String jsonStr) {
            return super.parse(jsonStr, ImportSettingRAW.class,
                    (importSettingRAW, jsonObject) -> {},
                    ImportDataInfoRAW.class,
                    (importDataInfoRAW, jsonObject) -> {});
        }
    },
    SCV {
        @Override
        public ImportSetting parseImportSetting(String jsonStr) {
            return super.parse(jsonStr, ImportSettingSCV.class,
                    (importSettingSCV, jsonObject) -> {},
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
        public ImportSetting parseImportSetting(String jsonStr) {
            return super.parse(jsonStr, ImportSettingSHP.class,
                    (importSettingSHP, jsonObject) -> {},
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
        public ImportSetting parseImportSetting(String jsonStr) {
            return super.parse(jsonStr, ImportSettingSIT.class,
                    (importSettingSIT, jsonObject) -> {},
                    ImportDataInfoSIT.class,
                    (importDataInfoSIT, jsonObject) -> {});
        }
    },
    SKP {
        @Override
        public ImportSetting parseImportSetting(String jsonStr) {
            return super.parse(jsonStr, ImportSettingSKP.class,
                    (importSettingSKP, jsonObject) -> {},
                    null,
                    (nullClazz, jsonObject) -> {});
        }
    },
    TAB {
        @Override
        public ImportSetting parseImportSetting(String jsonStr) {
            return super.parse(jsonStr, ImportSettingTAB.class,
                    (importSettingTAB, jsonObject) -> {},
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
        public ImportSetting parseImportSetting(String jsonStr) {
            return super.parse(jsonStr, ImportSettingTEMSBuildingVector.class,
                    (importSettingTEMSBuildingVector, jsonObject) -> {},
                    ImportDataInfoTEMSBuildingVector.class,
                    (importDataInfoTEMSBuildingVector, jsonObject) -> {
                        super.parseTargetFieldInfos(jsonObject, fieldInfos -> importDataInfoTEMSBuildingVector.setTargetFieldInfos(fieldInfos.toArray()));
                    });
        }
    },
    TEMS_CLUTTER {
        @Override
        public ImportSetting parseImportSetting(String jsonStr) {
            return super.parse(jsonStr, ImportSettingTEMSClutter.class,
                    (importSettingTEMSClutter, jsonObject) -> {},
                    ImportDataInfoTEMSClutter.class,
                    (importDataInfoTEMSClutter, jsonObject) -> {});
        }
    },
    TEMS_TEXT_LABELS {
        @Override
        public ImportSetting parseImportSetting(String jsonStr) {
            return super.parse(jsonStr, ImportSettingTEMSTextLabels.class,
                    (importSettingTEMSTextLabels, jsonObject) -> {},
                    ImportDataInfoTEMSTextLabels.class,
                    (importDataInfoTEMSTextLabels, jsonObject) -> {
                        super.parseTargetFieldInfos(jsonObject, fieldInfos -> importDataInfoTEMSTextLabels.setTargetFieldInfos(fieldInfos.toArray()));
                    });
        }
    },
    TEMS_VECTOR {
        @Override
        public ImportSetting parseImportSetting(String jsonStr) {
            return super.parse(jsonStr, ImportSettingTEMSVector.class,
                    (importSettingTEMSVector, jsonObject) -> {},
                    ImportDataInfoTEMSVector.class,
                    (importDataInfoTEMSVector, jsonObject) -> {
                        super.parseTargetFieldInfos(jsonObject, fieldInfos -> importDataInfoTEMSVector.setTargetFieldInfos(fieldInfos.toArray()));
                    });
        }
    },
    TIF { // WorldFilePath需要单独处理 为正确的真实路径
        @Override
        public ImportSetting parseImportSetting(String jsonStr) {
            return super.parse(jsonStr, ImportSettingTIF.class,
                    (importSettingTIF, jsonObject) -> {},
                    ImportDataInfoTIF.class,
                    (importDataInfoTIF, jsonObject) -> {});
        }
    },
    USGSDEM {
        @Override
        public ImportSetting parseImportSetting(String jsonStr) {
            return super.parse(jsonStr, ImportSettingUSGSDEM.class,
                    (importSettingUSGSDEM, jsonObject) -> {},
                    ImportDataInfoUSGSDEM.class,
                    (importDataInfoUSGSDEM, jsonObject) -> {});
        }
    },
    VCT {
        @Override
        public ImportSetting parseImportSetting(String jsonStr) {
            return super.parse(jsonStr, ImportSettingVCT.class,
                    (importSettingVCT, jsonObject) -> {},
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
        public ImportSetting parseImportSetting(String jsonStr) {
            return super.parse(jsonStr, ImportSettingVRT.class,
                    (importSettingVRT, jsonObject) -> {},
                    ImportDataInfoVRT.class,
                    (importDataInfoVRT, jsonObject) -> {});
        }
    },
    WOR { // 需要单独处理 workspace
        @Override
        public ImportSetting parseImportSetting(String jsonStr) {
            return super.parse(jsonStr, ImportSettingWOR.class,
                    (importSettingWOR, jsonObject) -> {},
                    ImportDataInfoWOR.class,
                    (importDataInfoWOR, jsonObject) -> {});
        }
    };

}

