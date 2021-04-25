/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.data.mgt.util;

import com.supermap.data.Dataset;
import com.supermap.data.Datasource;
import com.supermap.data.Maps;
import com.supermap.data.Workspace;
import com.supermap.mapping.Layer;
import com.supermap.mapping.LayerSettingImage;
import com.supermap.mapping.Map;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: app-landuse
 * @description: 地图工具类
 * @author: lidong
 * @date:2021/3/25
 * @create: 2018/08/22
 */
public class MapUtils {

    /**
     * 保存地图
     *
     * @param toSaveMap
     * @param workspace
     */
    @Deprecated
    public static void saveMap(Workspace workspace, Map toSaveMap) {
        if (toSaveMap != null) {
            int index = workspace.getMaps().indexOf(toSaveMap.getName());
            String mapXml = toSaveMap.toXML();
            String mapName = toSaveMap.getName();
            workspace.getMaps().remove(index);
            workspace.getMaps().insert(index, mapName, mapXml);
            workspace.save();
        }
    }

    /**
     * 保存地图
      * @param workspace
     * @param toSaveMap
     * @param mapName
     */
    public static void saveMap(Workspace workspace, Map toSaveMap, String mapName) {
        if (toSaveMap != null) {
            workspace.getMaps().add(mapName, toSaveMap.toXML(), workspace.getVersion());
            workspace.save();
        }
    }

    public static Map getMap(Workspace workspace) {
        Map map = null;
        if (workspace != null) {
            map = new Map(workspace);
            String mapName = workspace.getMaps().get(0);
            map.open(mapName);
        }
        return map;
    }

    public static Map getMap(Workspace workspace, String mapName) {
        Map map = null;
        if (workspace != null && workspace.getMaps().getCount() > 0) {
            Maps maps = workspace.getMaps();
            if (maps.indexOf(mapName) > -1) {
                map = new Map(workspace);
                map.open(mapName);
            }
        }
        return map;
    }

    public static Map createMap(Workspace workspace, String mapName) {
        if (checkExitedMap(workspace, mapName)) {
            return getMap(workspace, mapName);
        }
        Map newMap = new Map();
        newMap.setName(mapName);
        newMap.setWorkspace(workspace);
        int addResult = workspace.getMaps().add(mapName, newMap.toXML());
        if (addResult > -1) {
            newMap.open(mapName);
        }
        return newMap;
    }

    public static boolean addMap(Workspace workspace, Map newMap) {
        boolean result = false;
        if (checkExitedMap(workspace, newMap.getName())) {
            return false;
        }
        if (workspace != null) {
            Maps maps = workspace.getMaps();
            if (maps.indexOf(newMap.getName()) < 0) {
                result = maps.add(newMap.getName(), newMap.toXML()) > 0;
                workspace.save();
            }
        }
        return result;
    }

    public static boolean deleteMap(Workspace workspace, String mapName) {
        boolean result = false;
        if (workspace != null) {
            Maps maps = workspace.getMaps();
            if (maps.indexOf(mapName) > -1) {
                result = maps.remove(mapName);
                workspace.save();
            }
        }
        return result;
    }

    public static boolean updateMap(Workspace workspace, String oldMapName, Map newMap) {
        boolean result = false;
        Map editMap = getMap(workspace, oldMapName);
        if (editMap == null) {
            return false;
        }
        if (checkValidMap(newMap)) {
            deleteMap(workspace, editMap.getName());
            result = addMap(workspace, newMap);
        }
        return result;
    }

    public static boolean addLayers(Map dataMap, Map tempMap) {
        boolean result = false;
        if (dataMap != null && tempMap != null) {
            for (int i = 0; i < tempMap.getLayers().getCount(); i++) {
                Layer layer = tempMap.getLayers().get(i);
                if (!dataMap.getLayers().contains(layer.getName())) {
                    // if (!IsContainsLayer(dataMap, layer)) {
                    result = dataMap.getLayers().add(layer);
                }
            }
        }
        return result;
    }

    @Deprecated
    public static boolean addLayer(Map dataMap, Layer layer) {
        boolean addResult = false;
        // Map dataMap = getMap(mapName);
        if (dataMap != null && layer != null) {
            if (!dataMap.getLayers().contains(layer.getName())) {
                addResult = dataMap.getLayers().add(layer);
            }
        }
        return addResult;
    }
    @Deprecated
    public static Layer addLayer(Datasource datasource, Map map, String datasetName, LayerSettingImage layerSettingImage) {
        Layer resultLayer = null;
        String dsAlias = datasource.getAlias();
        String objectLayerName = datasetName + "@" + dsAlias;
        Dataset dataset = datasource.getDatasets().get(datasetName);
        if (map != null) {
            Layer layer = null;
            if (!map.getLayers().contains(objectLayerName)) {
                if (layerSettingImage != null) {
                    layer = map.getLayers().add(dataset, layerSettingImage, true);
                } else {
                    layer = map.getLayers().add(dataset, true);
                }
            }
            if (layer != null && !layer.isDisposed()) {
                layer.setCaption(objectLayerName);
                layer.setSelectable(true);
                layer.setEditable(true);
                layer.setVisible(true);
            }
            resultLayer = layer;
            map.refresh();
        }
        return resultLayer;
    }
    @Deprecated
    public static boolean deleteLayer(Map dataMap, Layer layer) {
        boolean delResult = false;
        // Map dataMap = getMap(mapName);
        if (dataMap != null && layer != null) {
            if (dataMap.getLayers().contains(layer.getName())) {
                delResult = dataMap.getLayers().remove(layer);
            }
        }
        return delResult;
    }

    public static Layer queryByName(Map dataMap, String layerName) {
        Layer resultLayer = null;
        if (dataMap != null && layerName != null) {
            if (dataMap.getLayers().contains(layerName)) {
                resultLayer = dataMap.getLayers().get(layerName);
            }
        }
        return resultLayer;
    }

    /**
     * 地图有效性检查，有效返回true
     *
     * @param map
     * @return
     */
    private static boolean checkValidMap(Map map) {
        return StringUtils.isNotBlank(map.getName());
    }

    /**
     * 检查地图是否已存在，存在返回true
     *
     * @param checkedMapName
     * @return
     */
    private static boolean checkExitedMap(Workspace workspace, String checkedMapName) {
        if (getMap(workspace, checkedMapName) != null) {
            return true;
        }
        return false;
    }

    /**
     * 判断该地图中是否包含该图层，包含返回true，不包含返回false
     *
     * @param map
     * @param tmpLayer
     * @return
     */
    public static boolean isContainsLayer(Map map, Layer tmpLayer) {
        boolean isContain = false;
        String tmpLayerName = tmpLayer.getName();
        // 图层名相同
        if (map.getLayers().contains(tmpLayerName)) {
            Layer dataLayer = map.getLayers().get(tmpLayerName);
            // 判断是否有专题
            if (dataLayer.getTheme() != null && tmpLayer.getTheme() != null) {
                // 判断专题类型是否相同
                if (dataLayer.getTheme().getType().equals(tmpLayer.getTheme().getType())) {
                    isContain = true;
                }
            } else if (dataLayer.getTheme() == null && tmpLayer.getTheme() == null) {
                isContain = true;
            }
        }
        if (!isContain) {
            System.out.println("不一样的图层" + tmpLayer.getName());
        }
        return isContain;
    }

    /**
     * 移除图层
     *
     * @param dataMap
     * @param dsName
     * @return
     */
    @Deprecated
    public static boolean removeLayer(Map dataMap, String dsName) {
        boolean result = false;
        if (dataMap != null && dsName != null) {
            List <Layer> layers = new ArrayList <>();
            for (int j = 0; j < dataMap.getLayers().getCount(); j++) {
                Layer toRemove = dataMap.getLayers().get(j);
                String layerName = toRemove.getName();
                String datasourceName = "";
                if (layerName.contains("#")) {
                    datasourceName = StringUtils.substringBetween(layerName, "@", "#");
                } else {
                    String rgex = "\\@(.*)";
                    datasourceName = StrUtils.getSubStrSimple(layerName, rgex);
                }
                //				String datasourceName = getDatasourceNameFromLayer(toRemove);
                System.out.println("图层名截取数据源名：" + datasourceName);
                if (StringUtils.equals(datasourceName, dsName)) {
                    layers.add(toRemove);
                }
            }
            if (layers.size() > 0) {
                for (Layer layer : layers) {
                    result = dataMap.getLayers().remove(layer);
                }
            }
        }
        return result;
    }

    public static String getDatasourceNameFromLayer(Layer layer) {
        String dsName = "";
        if (layer != null) {
            Dataset dataset = layer.getDataset();
            if (dataset != null) {
                dsName = dataset.getDatasource().getAlias();
            }
        }
        return dsName;
    }
}
