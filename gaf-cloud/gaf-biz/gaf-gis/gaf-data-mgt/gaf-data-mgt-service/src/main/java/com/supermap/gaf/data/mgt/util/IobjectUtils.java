package com.supermap.gaf.data.mgt.util;

import com.supermap.data.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The type Iobject utils.
 */
public class IobjectUtils {
    private static final String GeoCoordSys_GeoDatumPlane = "大地基准面";
    private static final String GeoCoordSys_ReferenceSpheroid = "参考椭球体";
    private static final String GeoSpheroid_Axis = "椭球长半轴";
    private static final String GeoSpheroid_Flatten = "椭球扁率";
    private static final String CenterBasisMeridian = "中央子午线";
    private static final String Projection_ProjectionType = "投影方式";
    private static final String CenterMeridian = "中央经线";
    private static final String CentralParallel = "原点纬线";
    private static final String StandardParallel1 = "标准纬线(1)";
    private static final String StandardParallel2 = "标准纬线(2)";
    private static final String FalseEasting = "水平偏移量";
    private static final String FalseNorthing = "垂直偏移量";
    private static final String ScaleFactor = "比例因子";
    private static final String Azimuth = "方位角";
    private static final String FirstPointLongitude = "第一经纬线";
    private static final String SecondPointLongitude = "第二经纬线";

    /**
     * Gets dataset properties.
     *
     * @param dataset the dataset
     * @return the dataset properties
     */
    public static Map<String, Object> getDatasetProperties(Dataset dataset) {
        Map<String, Object> re = new HashMap<>();
        re.put("type", dataset.getType().name());
        re.put("name", dataset.getName());
        re.put("tableName", dataset.getTableName());
        re.put("bounds", getDatasetBounds(dataset));
        return re;
    }

    /**
     * Gets dataset bounds.
     *
     * @param dataset the dataset
     * @return the dataset bounds
     */
    public static Map<String, Object> getDatasetBounds(Dataset dataset) {

        Map<String, Object> re = new HashMap<>();
        Rectangle2D bounds = dataset.getBounds();
        re.put("left", Double.toString(bounds.getLeft()));
        re.put("top", Double.toString(bounds.getTop()));
        re.put("right", Double.toString(bounds.getRight()));
        re.put("bottom", Double.toString(bounds.getBottom()));
        return re;
    }

    /**
     * Gets prj coord sys propertes.
     *
     * @param dataset the dataset
     * @return the prj coord sys propertes
     */
    public static Map<String, Object> getPrjCoordSysPropertes(Dataset dataset) {
        Map<String, Object> re = new HashMap<>();
        if (!DatasetType.TABULAR.equals(dataset.getType())) {
            PrjCoordSys prjCoordSys = dataset.getPrjCoordSys();
            re.put("name", prjCoordSys.getName());
            re.put("coordUnit", prjCoordSys.getCoordUnit().toString());
            re.put("detail", getPrjCoordSysDetails(prjCoordSys));
        }
        return re;
    }

    /**
     * Gets prj coord sys details.
     *
     * @param prj the prj
     * @return the prj coord sys details
     */
    public static Map<String, Object> getPrjCoordSysDetails(PrjCoordSys prj) {
        Map<String, Object> re = new LinkedHashMap<>();
        if (prj.getType() != PrjCoordSysType.PCS_NON_EARTH) {
            re.put("EPSG_Code", prj.getEPSGCode());
            re.put(GeoCoordSys_GeoDatumPlane, prj.getGeoCoordSys().getGeoDatum().getName());
            re.put(GeoCoordSys_ReferenceSpheroid, prj.getGeoCoordSys().getGeoDatum().getGeoSpheroid().getName());
            re.put(GeoSpheroid_Axis, prj.getGeoCoordSys().getGeoDatum().getGeoSpheroid().getAxis());
            re.put(GeoSpheroid_Flatten, prj.getGeoCoordSys().getGeoDatum().getGeoSpheroid().getFlatten());
            re.put(CenterBasisMeridian, prj.getGeoCoordSys().getGeoPrimeMeridian().getLongitudeValue());
            if (prj.getType() != PrjCoordSysType.PCS_EARTH_LONGITUDE_LATITUDE) {
                re.put(Projection_ProjectionType, prj.getProjection().getType().name());
                re.put(CenterMeridian, prj.getPrjParameter().getCentralMeridian());
                re.put(CentralParallel, prj.getPrjParameter().getCentralParallel());
                re.put(StandardParallel1, prj.getPrjParameter().getStandardParallel1());
                re.put(StandardParallel2, prj.getPrjParameter().getStandardParallel2());
                re.put(FalseEasting, prj.getPrjParameter().getFalseEasting());
                re.put(FalseNorthing, prj.getPrjParameter().getFalseNorthing());
                re.put(ScaleFactor, prj.getPrjParameter().getScaleFactor());
                re.put(Azimuth, prj.getPrjParameter().getAzimuth());
                re.put(FirstPointLongitude, prj.getPrjParameter().getFirstPointLongitude());
                re.put(SecondPointLongitude, prj.getPrjParameter().getSecondPointLongitude());
            }
        }
        return re;
    }


    /**
     * Is modify field enable boolean.
     *
     * @param dataset the dataset
     * @return the boolean
     */
    public static boolean isModifyFieldEnable(Dataset dataset){
        EngineType engineType = dataset.getDatasource().getEngineType();
        return dataset instanceof DatasetVector && (engineType == EngineType.ORACLEPLUS || engineType == EngineType.SQLPLUS || engineType == EngineType.POSTGRESQL || engineType == EngineType.KINGBASE || engineType == EngineType.MYSQL);
    }

    /**
     * Modify filed.
     *
     * @param dataset      the dataset
     * @param fieldName    the field name
     * @param newFieldInfo the new field info
     */
    public static void modifyFiled(Dataset dataset,String fieldName,FieldInfo newFieldInfo){
        if(!isModifyFieldEnable(dataset)){
            throw new IllegalArgumentException("该数据集不支持修改字段操作");
        }
        FieldInfos fieldInfos = ((DatasetVector)dataset).getFieldInfos();
        fieldInfos.modify(fieldName, newFieldInfo);
    }

    /**
     * Add field.
     *
     * @param dataset      the dataset
     * @param newFieldInfo the new field info
     */
    public static void addField(Dataset dataset,FieldInfo newFieldInfo){
        if(!(dataset instanceof DatasetVector)){
            throw new IllegalArgumentException("该数据集类型不支持增加字段操作");
        }
        FieldInfos fieldInfos = ((DatasetVector)dataset).getFieldInfos();
        fieldInfos.add(newFieldInfo);
    }

    /**
     * Remove field.
     *
     * @param dataset   the dataset
     * @param fieldName the field name
     */
    public static void removeField(Dataset dataset,String fieldName){
        if(!(dataset instanceof DatasetVector)){
            throw new IllegalArgumentException("该数据集类型不支持删除字段操作");
        }
        FieldInfos fieldInfos = ((DatasetVector)dataset).getFieldInfos();
        fieldInfos.remove(fieldName);
    }




}
