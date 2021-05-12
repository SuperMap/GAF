package com.supermap.gaf.data.mgt.test;


import com.supermap.data.*;
import com.supermap.data.conversion.DataExport;
import com.supermap.data.conversion.ExportSettingCSV;
import com.supermap.data.conversion.ExportSettings;

import java.util.*;
import java.util.function.Consumer;

public class MyTests {

    public static Datasource openDataSource(Workspace workspace,String udbxFilePath ) {
        DatasourceConnectionInfo datasourceConnectionInfo = new DatasourceConnectionInfo(udbxFilePath,UUID.randomUUID().toString(),null) ;
        datasourceConnectionInfo.setEngineType(EngineType.UDBX);
        return workspace.getDatasources().open(datasourceConnectionInfo);
    }

    public static void traverse(DatasetVector datasetVector, Consumer<Recordset> consumer) {
        Recordset recordset = datasetVector.getRecordset(false, CursorType.DYNAMIC);
        Recordset.BatchEditor batch = recordset.getBatch();
        batch.begin();
        recordset.moveFirst();
        while (!recordset.isEOF()){
            consumer.accept(recordset);
            FieldInfos fieldInfos = recordset.getFieldInfos();
            recordset.moveNext();
        }
        batch.update();
        recordset.close();
        recordset.dispose();
    }

    public static void main(String[] args) {
        Workspace workspace = new Workspace();
        Datasource dsbg2021 = openDataSource(workspace,"F:\\图谱demo数据\\阿坝县\\阿坝县(513231)2019-2020年度国土调查数据库和更新成果\\阿坝县(513231)2021年度国土变更调查数据库更新成果\\阿坝县(513231)2021年度国土变更调查数据库更新成果.udbx");
        Set<String> set = new HashSet<>();
        traverse(((DatasetVector) dsbg2021.getDatasets().get("DLTBGX")), recordset -> {
            String bsm = recordset.getString("BSM");
            set.add(bsm);
        });

        Datasource ds2021 = openDataSource(workspace,"F:\\图谱demo数据\\阿坝县\\阿坝县(513231)2019-2020年度国土调查数据库和更新成果\\阿坝县(513231)2021年度国土调查数据库\\阿坝县(513231)2021年度国土调查数据库.udbx");
        traverse(((DatasetVector) ds2021.getDatasets().get("DLTB")), recordset -> {
            int sjnf = recordset.getInt32("SJNF");
            if (Integer.valueOf(2020).equals(sjnf)) {
                String bsm = recordset.getString("BSM");
                if (!set.contains(bsm)) {
                    recordset.setInt32("SJNF",2019);
                }
            }
        });

    }

    public static void main1(String[] args) {

        Workspace workspace = new Workspace();
        final Long[] i = {513231211000072000L};
        Map<String,String> map = new HashMap<>(16);
        Datasource dsbg2020 = openDataSource(workspace,"F:\\图谱demo数据\\阿坝县\\阿坝县(513231)2019-2020年度国土调查数据库和更新成果\\阿坝县(513231)2020年度国土变更调查数据库更新成果\\阿坝县(513231)2020年度国土变更调查数据库更新成果.udbx");
        traverse((DatasetVector) dsbg2020.getDatasets().get("DLTBGXGC"),recordset -> {
            String bgqtbbsm = recordset.getString("BGQTBBSM");
            String bghtbbsm = recordset.getString("BGHTBBSM");

            if (Objects.equals(bghtbbsm, bgqtbbsm)) {
                String tmp = String.valueOf(++i[0]);
                recordset.setString("BGHTBBSM", tmp);
                map.put(bghtbbsm,tmp);
            }
        });

        traverse(((DatasetVector) dsbg2020.getDatasets().get("DLTBGX")), recordset -> {
            String bsm = recordset.getString("BSM");
            if (map.containsKey(bsm)) {
                recordset.setString("BSM", map.get(bsm));
            }
        });
        Datasource ds2020 = openDataSource(workspace,"F:\\图谱demo数据\\阿坝县\\阿坝县(513231)2019-2020年度国土调查数据库和更新成果\\阿坝县(513231)2020年度国土调查数据库\\阿坝县(513231)2020年度国土调查数据库.udbx");
        traverse(((DatasetVector) ds2020.getDatasets().get("DLTB")), recordset -> {
            String bsm = recordset.getString("BSM");
            if (map.containsKey(bsm)) {
                recordset.setString("BSM", map.get(bsm));
            }
        });

        Datasource dsbg2021 = openDataSource(workspace,"F:\\图谱demo数据\\阿坝县\\阿坝县(513231)2019-2020年度国土调查数据库和更新成果\\阿坝县(513231)2021年度国土变更调查数据库更新成果\\阿坝县(513231)2021年度国土变更调查数据库更新成果.udbx");
        final Long[] j = {513231211000073000L};
        Map<String, String> map1 = new HashMap<>();
        traverse(((DatasetVector) dsbg2021.getDatasets().get("DLTBGXGC")), recordset -> {
            String bgqtbbsm = recordset.getString("BGQTBBSM");
            if (map.containsKey(bgqtbbsm)) {
                String bghtbbsm = recordset.getString("BGHTBBSM");
                if (Objects.equals(bghtbbsm,bgqtbbsm)) {
                    String tmp = String.valueOf(++j[0]);
                    recordset.setString("BGHTBBSM", tmp);
                    map1.put(bghtbbsm,tmp);
                }
                recordset.setString("BGQTBBSM", map.get(bgqtbbsm));
            }
        });

        traverse(((DatasetVector) dsbg2021.getDatasets().get("DLTBGX")), recordset -> {
            String bsm = recordset.getString("BSM");
            if (map1.containsKey(bsm)) {
                recordset.setString("BSM", map1.get(bsm));
            }
        });

        Datasource ds2021 = openDataSource(workspace,"F:\\图谱demo数据\\阿坝县\\阿坝县(513231)2019-2020年度国土调查数据库和更新成果\\阿坝县(513231)2021年度国土调查数据库\\阿坝县(513231)2021年度国土调查数据库.udbx");
        traverse(((DatasetVector) ds2021.getDatasets().get("DLTB")), recordset -> {
            String bsm = recordset.getString("BSM");
            if (map1.containsKey(bsm)) {
                recordset.setString("BSM", map1.get(bsm));
            }
        });



        Map<String, String> map2 = new HashMap<>();
        final Long[] k = {513231211000074000L};
        traverse(((DatasetVector) dsbg2021.getDatasets().get("DLTBGXGC")), recordset -> {
            String bgqtbbsm = recordset.getString("BGQTBBSM");
            String bghtbbsm = recordset.getString("BGHTBBSM");
            if (Objects.equals(bghtbbsm, bgqtbbsm)) {
                String tmp = String.valueOf(++k[0]);
                recordset.setString("BGHTBBSM", tmp);
                map2.put(bghtbbsm,tmp);
            }
        });

        traverse(((DatasetVector) dsbg2021.getDatasets().get("DLTBGX")), recordset -> {
            String bsm = recordset.getString("BSM");
            if (map2.containsKey(bsm)) {
                recordset.setString("BSM", map2.get(bsm));
            }
        });

        traverse(((DatasetVector) ds2021.getDatasets().get("DLTB")), recordset -> {
            String bsm = recordset.getString("BSM");
            if (map2.containsKey(bsm)) {
                recordset.setString("BSM", map2.get(bsm));
            }
        });



    }


//    public static void main(String[] args) {
//        DatasourceConnectionInfo dciMain = new DatasourceConnectionInfo("F:\\图谱demo数据\\阿坝县\\四川省阿坝藏族羌族自治州阿坝县(513231)-2020-2021\\四川省阿坝藏族羌族自治州阿坝县(513231)\\四川省阿坝藏族羌族自治州阿坝县(513231).udb","myetst",null) ;
//        Workspace workspace = new Workspace();
//        Datasource dsMain = workspace.getDatasources().open(dciMain);
//        DatasetVector datasetVector = (DatasetVector) dsMain.getDatasets().get("L_DLTB");
//        Recordset recordset = datasetVector.getRecordset(false, CursorType.DYNAMIC);
//        recordset.moveFirst();
//        int i = 1;
//        while (!recordset.isEOF()){
//            String dlbm = recordset.getString("DLBM");
//            boolean odd = i % 2 != 0;
//            if (dlbm.equals("0305") || dlbm.equals("0307") || (dlbm.equals("0401") && !odd) || (dlbm.equals("0402") && odd) || dlbm.equals("0701") || dlbm.equals("0702") || (dlbm.equals("0508") && odd) || (dlbm.equals("05H1") && odd)
//                || dlbm.equals("08H1") || dlbm.equals("08H2") || dlbm.equals("09") || dlbm.equals("1003") || dlbm.equals("1006") || dlbm.equals("1101") || dlbm.equals("1109") || dlbm.equals("1202") || (dlbm.equals("1206") && !odd) || dlbm.equals("1207")) {
//                recordset.delete();
//            }
//            i++;
//            recordset.moveNext();
//        }
//        recordset.close();
//        recordset.dispose();
//
//        long startBSM = 513231211000065000L;
//        Recordset recordset2 = datasetVector.getRecordset(false, CursorType.DYNAMIC);
//        Recordset.BatchEditor batch = recordset2.getBatch();
//        batch.begin();
//        recordset2.moveFirst();
//        while (!recordset2.isEOF()){
//            String dlbm = recordset2.getString("DLBM");
//            recordset2.setString("BSM", String.valueOf((++startBSM)));
//            if (dlbm.equals("0103")) {
//                recordset2.setString("DLBM","0101");
//                recordset2.setString("DLMC", "水田");
//            } else if (dlbm.equals("0301")) {
//                recordset2.setString("DLBM","0305");
//                recordset2.setString("DLMC", "灌木林地");
//            } else if (dlbm.equals("0401")) {
//                recordset2.setString("DLBM","0403");
//                recordset2.setString("DLMC", "人工牧草地");
//            } else if (dlbm.equals("0402")) {
//                recordset2.setString("DLBM","0201");
//                recordset2.setString("DLMC", "果园");
//            } else if (dlbm.equals("0508")) {
//                recordset2.setString("DLBM","0701");
//                recordset2.setString("DLMC", "城镇住宅用地");
//                recordset2.setString("CZCSXM", "202");
//            }  else if (dlbm.equals("05H1")) {
//                recordset2.setString("DLBM","0701");
//                recordset2.setString("DLMC", "城镇住宅用地");
//                recordset2.setString("CZCSXM", "202");
//            }  else if (dlbm.equals("0601")) {
//                recordset2.setString("DLBM","0508");
//                recordset2.setString("DLMC", "物流仓储用地");
//                recordset2.setString("CZCSXM", "203");
//            }  else if (dlbm.equals("0602")) {
//                recordset2.setString("DLBM","0508");
//                recordset2.setString("DLMC", "物流仓储用地");
//                recordset2.setString("CZCSXM", "203");
//            } else if (dlbm.equals("0809")) {
//                recordset2.setString("DLBM","0810");
//                recordset2.setString("DLMC", "公园与绿地");
//            } else if (dlbm.equals("1004")) {
//                recordset2.setString("DLBM","1003");
//                recordset2.setString("DLMC", "公路用地");
//                recordset2.setString("CZCSXM", null);
//            } else if (dlbm.equals("1005")) {
//                recordset2.setString("DLBM","1003");
//                recordset2.setString("DLMC", "公路用地");
//                recordset2.setString("CZCSXM", null);
//            } else if (dlbm.equals("1106")) {
//                recordset2.setString("DLBM","1101");
//                recordset2.setString("DLMC", "河流水面");
//            } else if (dlbm.equals("1201")) {
//                recordset2.setString("DLBM","1109");
//                recordset2.setString("DLMC", "水工建筑用地");
//                recordset2.setString("CZCSXM", null);
//            } else if (dlbm.equals("1206")) {
//                recordset2.setString("DLBM","0401");
//                recordset2.setString("DLMC", "天然牧草地");
//            }
//            recordset2.moveNext();
//        }
//        batch.update();
//        recordset2.close();
//        recordset2.dispose();
//    }

//    public static void main(String[] args) {
//
//        String datasetName= "DLTB";
//        DataExport dataExport = new DataExport();
//        ExportSettingCSV exportSettingCSV = new ExportSettingCSV();
//        exportSettingCSV.setIsExportFieldName(true);
//        ExportSettings exportSettings = new ExportSettings();
//        exportSettings.add(exportSettingCSV);
//        dataExport.setExportSettings(exportSettings);
//        exportSettingCSV.setIgnoreFieldNames(new String[]{"SmID","SmGeometry"});
//        exportSettingCSV.setTargetFileCharset(Charset.UTF8);
//        exportSettingCSV.setTargetFilePath("E:\\Program\\idesktop10.1.1\\DLTB_export.csv");
//        Datasource datasource = new Datasource(EngineType.UDBX);
//        DatasourceConnectionInfo connectionInfo = new DatasourceConnectionInfo();
//        connectionInfo.setServer("E:\\tmp\\阿坝县(513231)2019-2020年度国土调查数据库和更新成果\\阿坝县(513231)2019年度国土调查数据库\\阿坝县(513231)2019年度国土调查数据库.udbx");
////                connectionInfo.setAlias(conn.getString("dsName"));
////                connectionInfo.setDatabase(conn.getString("dbName"));
////                connectionInfo.setUser(conn.getString("userName"));
////                connectionInfo.setPassword(conn.getString("password"));
////        datasource.open(connectionInfo);
////        Datasets datasets = datasource.getDatasets();
////        for(int i=0;i<datasets.getCount();++i){
////            Dataset dataset = datasets.get(i);
////            String name = dataset.getName();
////            if(datasetName.equals(name)){
////                exportSettingCSV.setSourceData(dataset);
////            }
////        }
//        //dataExport.run();
//    }

}
