package com.supermap.desktop.develop.entity;

import com.supermap.desktop.core.Interface.GlobalParameterField;
import com.supermap.desktop.core.implement.GlobalParameterFieldExportInterpreter;
import com.supermap.desktop.core.implement.GlobalParameterFieldImportInterpreter;
import com.supermap.desktop.core.utilties.FileUtilities;
import com.supermap.desktop.core.utilties.PathUtilities;
import com.supermap.desktop.develop.GAFProperties;

public class GafGlobalEnvironments {
    @GlobalParameterField(
            id = "server",
            nodePath = "environments_login",
            attribute = "server"
    )
    private static String server = "http://gaf.xxx";

    @GlobalParameterField(
            id = "alias",
            nodePath = "environments_alias",
            attribute = "name"
    )
    private static String alias;

    public static String GAF_GLOBAL_ENVIRONMENTS_XML;
    private static String userConfigPath;
    private static String globalConfigPath;

    static {
        GAF_GLOBAL_ENVIRONMENTS_XML = "configuration" + PathUtilities.SYSTEM_SEPARATOR + "SuperMap.Desktop.GAFEnvironments.xml";
        userConfigPath = FileUtilities.getAppDataPath() + GAF_GLOBAL_ENVIRONMENTS_XML;
        globalConfigPath = PathUtilities.getFullPathName("../" + GAF_GLOBAL_ENVIRONMENTS_XML, false);
        alias = GAFProperties.getString("String_Alias");
    }

    public static String getUserConfigPath() {
        return userConfigPath;
    }

    public static String getGlobalConfigPath() {
        return globalConfigPath;
    }

    public static String getAlias() {
        return alias;
    }

    public static void setAlias(String alias) {
        GafGlobalEnvironments.alias = alias;
    }

    public static void setUserConfigPath(String userConfigPath) {
        GafGlobalEnvironments.userConfigPath = userConfigPath;
    }

    public static void setGlobalConfigPath(String globalConfigPath) {
        GafGlobalEnvironments.globalConfigPath = globalConfigPath;
    }

    public static String getServer() {
        return server;
    }

    public static void setServer(String server) {
        GafGlobalEnvironments.server = server;
    }

    public static void initResource() {
        GlobalParameterFieldImportInterpreter globalParameterFieldImportInterpreter = new GlobalParameterFieldImportInterpreter();
        globalParameterFieldImportInterpreter.load(GafGlobalEnvironments.class, userConfigPath, "");
    }

    public static void save() {
        (new GlobalParameterFieldExportInterpreter()).save(GafGlobalEnvironments.class, userConfigPath, "");
    }

}
