package com.supermap.gaf.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.sqlserver.jdbc.SQLServerConnection;
import com.supermap.gaf.commontypes.metadata.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.io.InputStream;
import java.sql.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author:yw
 * @Date 2021/3/17
 **/

/**
 * ALTER TABLE public.tenant_server_config ADD test varchar(21) NULL DEFAULT a;
 * COMMENT ON COLUMN public.tenant_server_config.test IS 'ss';
 */
public class JdbcUtils {
    private static Logger log = LogUtil.getLocLogger(JdbcUtils.class);
    private static final String SQLSERVER_SELECT_TABLE_REMARKS_SQL_TP = "SELECT\n" +
            "A.name,\n" +
            "C.value\n" +
            "FROM sys.tables A\n" +
            "inner JOIN sys.extended_properties C ON C.major_id = A.object_id  and minor_id=0\n" +
            "WHERE A.name = ?";
    private static final String SQLSERVER_SELECT_COLUMN_REMARKS_SQL_TP = "SELECT\n" +
            "B.name AS column_name,\n" +
            "C.value\n" +
            "FROM sys.tables A\n" +
            "INNER JOIN sys.columns B ON B.object_id = A.object_id\n" +
            "LEFT JOIN sys.extended_properties C ON C.major_id = B.object_id AND C.minor_id = B.column_id\n" +
            "WHERE A.name = ?";
    private static final String add_field = "ALTER TABLE public.tenant_server_config ADD test varchar(21) NULL DEFAULT a;\n" +
            "COMMENT ON COLUMN public.tenant_server_config.test IS 'ss';";
    private static Map<String, String> TYPE_MAP;

    static {
        try (InputStream in = JdbcUtils.class.getClassLoader().getResourceAsStream("typeMapping.json")) {
            ObjectMapper mapper = new ObjectMapper();
            TYPE_MAP = mapper.readValue(in,
                    Map.class);
        } catch (Exception e) {
            log.error("type-mapping.json解析失败");
        }

    }


    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    static class ModifySqls {
        private List<String> rename;
        private List<String> modify;
        private List<String> comment;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    static class AddSqls {
        private List<String> add;
        private List<String> comment;
    }

    public static Connection openConnection(String url, String username, String password) {
        try {
            Properties props = new Properties();
            props.setProperty("user", username);
            props.setProperty("password", password);
            props.setProperty("remarks", "true");
            return DriverManager.getConnection(url, props);
        } catch (SQLException e) {
            throw new IllegalArgumentException("数据库连接失败:" + e.getMessage());
        }
    }

    public static Object sqlProcessor(String url, String username, String password, Function<Connection, Object> process) {
        try (Connection connection = JdbcUtils.openConnection(url, username, password)) {
            connection.setAutoCommit(false);
            try {
                Object re = process.apply(connection);
                connection.commit();
                connection.setAutoCommit(true);
                return re;
            } catch (Exception e) {
                connection.rollback();
                connection.setAutoCommit(true);
                throw e;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean checkConnectionInfo(String url, String username, String password) {
        boolean re = false;
        try (Connection connection = openConnection(url, username, password);) {
            re = true;
        } catch (SQLException e) {
            log.error("数据库连接关闭失败：{}", e.getStackTrace());
        }
        return re;
    }

    public static JdbcConnectionMetadata getJdbcConnectionInfo(Connection connection) {
        try {
            if (connection.isClosed()) {
                throw new IllegalArgumentException("连接已关闭");
            }
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            String databaseType = databaseMetaData.getURL().split(":")[1];
            String database = connection.getCatalog();
            String schema = connection.getSchema();
            String databaseVersion = databaseMetaData.getDatabaseProductVersion();
            return JdbcConnectionMetadata.builder()
                    .database(database)
                    .databaseType(databaseType)
                    .schema(schema)
                    .databaseVersion(databaseVersion)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<TableMetadataInfo> getTableMetadatas(Connection connection, boolean returnFields) {
        return getTableMetadata(connection, null, returnFields, null);
    }

    public static TableMetadataInfo getTableMetadata(Connection connection, String tableName, boolean returnFields) {
        return getTableMetadata(connection, tableName, returnFields, null).get(0);
    }

    public static TableMetadataInfo getTableMetadata(Connection connection, String tableName, String fieldNamePattern) {
        return getTableMetadata(connection, tableName, true, fieldNamePattern).get(0);
    }


    static List<TableMetadataInfo> getTableMetadata(Connection connection, @Nullable String tableNamePattern, boolean returnFields, @Nullable String fieldNamePattern) {
        try {
            List<TableMetadataInfo> re = new ArrayList<>();
            JdbcConnectionMetadata jdbcConnectionInfo = getJdbcConnectionInfo(connection);
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            //获取表信息
            String[] type = {"TABLE"};
            try (ResultSet tableRs = databaseMetaData.getTables(jdbcConnectionInfo.getDatabase(), jdbcConnectionInfo.getSchema(), tableNamePattern, type);) {
                while (tableRs.next()) {
                    TableMetadataInfo item = convert2TableMetadataInfo(tableRs, jdbcConnectionInfo, databaseMetaData, returnFields, fieldNamePattern);
                    re.add(item);
                }
                if (tableNamePattern != null && CollectionUtils.isEmpty(re)) {
                    throw new IllegalArgumentException(String.format("%s表不存在于数据库%s.%s中", tableNamePattern, jdbcConnectionInfo.getDatabase(), jdbcConnectionInfo.getSchema()));
                }
            }
            return re;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static void injectTableRemarks2SqlServer(SQLServerConnection connection, TableMetadataInfo tableMetadataInfo, String tableName) {
        try (PreparedStatement ps = connection.prepareStatement(SQLSERVER_SELECT_TABLE_REMARKS_SQL_TP);) {
            ps.setString(1, tableName);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tableMetadataInfo.setRemarks(rs.getString("value"));
                    break;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void injectColumnRemarks2Sqlserver(SQLServerConnection connection, List<FieldMetadataInfo> fieldMetadatas, String tableName) {
        try (PreparedStatement ps = connection.prepareStatement(SQLSERVER_SELECT_COLUMN_REMARKS_SQL_TP);) {
            ps.setString(1, tableName);
            Map<String, FieldMetadataInfo> fieldMetadataMap = fieldMetadatas.stream().collect(Collectors.toMap(FieldMetadataInfo::getFieldName, item -> item, (s1, s2) -> s1));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String columnName = rs.getString("column_name");
                    String columnRemarks = rs.getString("value");
                    fieldMetadataMap.get(columnName).setRemarks(columnRemarks);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String escapeString(String in) {
        if (StringUtils.isEmpty(in)) return null;
        return in.replaceAll("[\\r\\n|\\r|\\n|\"]+", " ");
    }

    public static void addField(Connection connection, String tableName, FieldMetadataInfo fieldMetadataInfo) {
        List<String> sqls = new ArrayList<>();
        AddSqls addSqls = addFieldsDDLs(connection, tableName, Arrays.asList(fieldMetadataInfo));
        // 添加字段
        sqls.addAll(addSqls.getAdd());
        sqls.addAll(addSqls.getComment());
        // 主键修改
        if (fieldMetadataInfo.isPrimaryKey()) {
            // 复合主键在指定顺序插入主键，或者插在末尾
            StringBuilder alterSql = new StringBuilder("ALTER TABLE ");
            alterSql.append(tableName).append(" ");
            List<String> alterDDLItems = new ArrayList<>();
            TableMetadataInfo tableMetadataInfo = getTableMetadata(connection, tableName, false);
            alterDDLItems.addAll(addPkDDLItems(connection, tableMetadataInfo.getPkMetadataInfos(), fieldMetadataInfo));
            if (!alterDDLItems.isEmpty()) {
                String sql = alterSql.append("\n").append(String.join(",\n", alterDDLItems)).append(";").toString();
                sqls.add(sql);
            }
        }
        runDDL(connection, sqls);
    }

    public static void modifyField(Connection connection, String tableName, String originFieldName, FieldMetadataInfo fieldMetadataInfo) {
        List<String> sqls = new ArrayList<>();
        TableMetadataInfo tableMetadataInfo = getTableMetadata(connection, tableName, originFieldName);

        List<String> alterDDLItems = new ArrayList<>();
        List<PkMetadataInfo> pkMetadataInfos = tableMetadataInfo.getPkMetadataInfos();
        FieldMetadataInfo originFieldMetadataInfo = tableMetadataInfo.getFieldMetadataInfos().get(0);
        ModifySqls modifySqls = modifyFieldsDDLs(connection, tableName, Arrays.asList(originFieldMetadataInfo), Arrays.asList(new UpdateMetadataParam<>(originFieldName, fieldMetadataInfo)));

        List<PkMetadataInfo> newPkMetadataInfos = new ArrayList<>();
        // 主键变化
        for(PkMetadataInfo item:pkMetadataInfos){
            if(!item.getColumnName().equals(fieldMetadataInfo.getFieldName())){
                newPkMetadataInfos.add(item);
            }
        }
        if(fieldMetadataInfo.isPrimaryKey()){
            newPkMetadataInfos.add(new PkMetadataInfo(fieldMetadataInfo.getFieldName(),fieldMetadataInfo.getPkName(),fieldMetadataInfo.getPkSeq()));
        }
        if(!CollectionUtils.isEmpty(pkMetadataInfos) || !CollectionUtils.isEmpty(newPkMetadataInfos)){
            String oldPKsDDLFragment = addPrimaryKeyDDLFragment(connection,pkMetadataInfos,null);
            String newPkDDLFragment = addPrimaryKeyDDLFragment(connection,newPkMetadataInfos,fieldMetadataInfo.getFieldName());
            if(!StringUtils.equals(oldPKsDDLFragment,newPkDDLFragment)){
                if(!CollectionUtils.isEmpty(pkMetadataInfos)){
                    // 删除主键
                    alterDDLItems.add(new FieldMetadataInfo().genDropConstraintDDLFragment(connection,pkMetadataInfos.get(0).getPkName()));
                    // 添加新的主键
                    alterDDLItems.add(newPkDDLFragment);
                }
            }
        }


        // 1.重命名字段
        sqls.addAll(modifySqls.getRename());
        // 2.更改主键
        if (!alterDDLItems.isEmpty()) {
            String tp = "ALTER TABLE %s %s;";
            sqls.add(String.format(tp,tableName,String.join(",\n", alterDDLItems)));
        }
        // 3.更改其他属性
        sqls.addAll(modifySqls.getModify());
        sqls.addAll(modifySqls.getComment());
        runDDL(connection, sqls);
    }

    static void runDDL(Connection connection, List<String> sqls) {
        if (sqls.size() > 0) {
            try (Statement statement = connection.createStatement()) {
//                log.info("DDL：");
                for (String sql : sqls) {
//                    log.info("{}", sql);
                    System.out.println(sql);
                    statement.execute(sql);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void deleteField(Connection connection, String tableName, String fieldName) {
        StringBuilder alterSql = new StringBuilder("ALTER TABLE ");
        alterSql.append(tableName).append(" ");
        List<String> alterDDLItems = new ArrayList<>();
        TableMetadataInfo tableMetadataInfo = getTableMetadata(connection, tableName, false);
        List<PkMetadataInfo> pkMetadataInfos = tableMetadataInfo.getPkMetadataInfos();

        // 处理主键
        alterDDLItems.addAll(deletePkDDLItems(connection, pkMetadataInfos, fieldName));
        // 删除字段
        FieldMetadataInfo fieldMetadataInfo = FieldMetadataInfo.builder().fieldName(fieldName).build();
        alterDDLItems.add(fieldMetadataInfo.genDropColumnDDLFragment(connection));

        runDDL(connection,Arrays.asList(alterSql.append("\n").append(String.join(",\n", alterDDLItems)).append(";").toString()));

    }

    public static void batchDeleteFields(Connection connection, String tableName, Set<String> deletes) {
        TableMetadataInfo tableMetadataInfo = getTableMetadata(connection, tableName, true);
        List<FieldMetadataInfo> fieldMetadataInfos = tableMetadataInfo.getFieldMetadataInfos();
        List<UpdateMetadataParam<FieldMetadataInfo>> fields = fieldMetadataInfos.stream().map(item -> {
            if(deletes.contains(item.getFieldName())){
                return new UpdateMetadataParam<FieldMetadataInfo>(item.getFieldName(), null);
            }else{
                return new UpdateMetadataParam<FieldMetadataInfo>(item.getFieldName(), item);
            }
        }).collect(Collectors.toList());
        batchUpdateFields(connection, tableMetadataInfo, tableName, fields);
    }

    public static void batchUpdateFields(Connection connection, TableMetadataInfo tableMetadataInfo, String tableName, List<UpdateMetadataParam<FieldMetadataInfo>> fields) {
        List<PkMetadataInfo> pkMetadataInfos = tableMetadataInfo.getPkMetadataInfos();
        List<FieldMetadataInfo> fieldMetadataInfos = tableMetadataInfo.getFieldMetadataInfos();
        List<FieldMetadataInfo> added = new ArrayList<>();
        List<PkMetadataInfo> newPks = new ArrayList<>();
        List<String> deleted = new ArrayList<>();
        List<UpdateMetadataParam<FieldMetadataInfo>> updated = new ArrayList<>();
        for (UpdateMetadataParam<FieldMetadataInfo> item : fields) {

            if (StringUtils.isEmpty(item.getOldName())) {
                // 新增
                added.add(item.getMetadata());
                continue;
            } else if (item.getMetadata() == null) {
                // 删除
                deleted.add(item.getOldName());
            } else {
                // 更新
                updated.add(item);
            }
            if(item.getMetadata() != null){
                FieldMetadataInfo field = item.getMetadata();
                // 主键
                if (field.isPrimaryKey()) {
                    newPks.add(new PkMetadataInfo(field.getFieldName(), field.getPkName(), field.getPkSeq()));
                }
            }
        }
        boolean pkChanged = false;
        if (!CollectionUtils.isEmpty(pkMetadataInfos)) {
            if (pkMetadataInfos.size() != newPks.size()) {
                pkChanged = true;
            } else {
                Map<String, PkMetadataInfo> oldPkMap = pkMetadataInfos.stream().collect(Collectors.toMap(PkMetadataInfo::getColumnName, item -> item));
                for (PkMetadataInfo newPk : newPks) {
                    if (!oldPkMap.containsKey(newPk)) {
                        pkChanged = true;
                        break;
                    } else {
                        PkMetadataInfo oldPk = oldPkMap.get(newPk.getColumnName());
                        if (!oldPk.getSeq().equals(newPk.getSeq()) || StringUtils.equals(oldPk.getPkName(), newPk.getPkName())) {
                            pkChanged = true;
                            break;
                        }
                    }
                }
            }
        }
        List<String> sqls = new ArrayList<>();
        ModifySqls modify = modifyFieldsDDLs(connection, tableName, fieldMetadataInfos, updated);
        AddSqls add = addFieldsDDLs(connection, tableName, added);
        List<String> delete = deleteFieldsDDLs(connection, tableName, deleted);
        String tp = "ALTER TABLE %s %s;";
        // 1.删除主键
        if (pkChanged && !CollectionUtils.isEmpty(pkMetadataInfos)) {

            sqls.add(String.format(tp, tableName,new FieldMetadataInfo().genDropConstraintDDLFragment(connection, pkMetadataInfos.get(0).getPkName())));
        }
        // 2.删除字段
        sqls.addAll(delete);
        // 3.先更改字段名称
        sqls.addAll(modify.getRename());
        // 4.增加字段
        sqls.addAll(add.getAdd());
        sqls.addAll(add.getComment());
        // 5.添加主键，在删除和更改其他字段属性之前
        if (pkChanged) {
            String[] pkColumns = newPks.stream()
                    .sorted(Comparator.comparing(PkMetadataInfo::getSeq))
                    .map(PkMetadataInfo::getColumnName).toArray(String[]::new);
            sqls.add(String.format(tp,tableName, new FieldMetadataInfo().genAddPrimaryKeyDDLFragment(connection, pkColumns)));
        }
        // 6.更改字段属性
        sqls.addAll(modify.getModify());
        sqls.addAll(modify.getComment());
        runDDL(connection, sqls);
    }

    static List<String> deleteFieldsDDLs(Connection connection, String tableName, List<String> fieldNames) {
        List<String> re = new ArrayList<>();
        if (CollectionUtils.isEmpty(fieldNames)) {
            return re;
        }
        List<String> alterDDLItems = new ArrayList<>();
        for (String fieldName : fieldNames) {
            // 删除字段
            FieldMetadataInfo fieldMetadataInfo = FieldMetadataInfo.builder().fieldName(fieldName).build();
            alterDDLItems.add(fieldMetadataInfo.genDropColumnDDLFragment(connection));
        }
        String tp = "ALTER TABLE %s %s;";
        String sql = String.format(tp,tableName,String.join(",\n", alterDDLItems));
        re.add(sql);
        return re;
    }

    static AddSqls addFieldsDDLs(Connection connection, String tableName, List<FieldMetadataInfo> fieldMetadataInfos) {
        AddSqls re = new AddSqls();
        if (CollectionUtils.isEmpty(fieldMetadataInfos)) {
            re.setAdd(new ArrayList<>());
            re.setComment(new ArrayList<>());
            return re;
        }
        List<String> commentSqls = new ArrayList<>();
        List<String> alterDDLItems = new ArrayList<>();
        for (FieldMetadataInfo fieldMetadataInfo : fieldMetadataInfos) {
            // 添加字段
            alterDDLItems.add(fieldMetadataInfo.genAddColumnDDLFragment(connection));
            // 添加注释
            String commentDDL = fieldMetadataInfo.genCommentDDL(connection, tableName);
            if (commentDDL != null) {
                commentSqls.add(commentDDL);
            }
        }
        String tp = "ALTER TABLE " + tableName + " %s;";
        String sql = String.format(tp,String.join(",\n", alterDDLItems));
        re.setAdd(Arrays.asList(sql));
        re.setComment(commentSqls);
        return re;
    }

    static ModifySqls modifyFieldsDDLs(Connection connection, String tableName, List<FieldMetadataInfo> originFieldMetadataInfos, List<UpdateMetadataParam<FieldMetadataInfo>> fields) {
        ModifySqls re = new ModifySqls();
        if (CollectionUtils.isEmpty(fields)) {
            re.setModify(new ArrayList<>());
            re.setRename(new ArrayList<>());
            re.setComment(new ArrayList<>());
            return re;
        }
        Map<String, FieldMetadataInfo> originFieldsMap = originFieldMetadataInfos.stream().collect(Collectors.toMap(FieldMetadataInfo::getFieldName, item -> item));
        List<String> alterDDLItems = new ArrayList<>();
        StringBuilder alterSql = new StringBuilder("ALTER TABLE ");
        alterSql.append(tableName).append(" ");
        List<String> renameSqls = new ArrayList<>();
        List<String> commentSqls = new ArrayList<>();
        List<String> modifySqls = new ArrayList<>();
        for (UpdateMetadataParam<FieldMetadataInfo> item : fields) {
            String originFieldName = item.getOldName();
            if (originFieldName == null) {
                continue;
            }
            FieldMetadataInfo fieldMetadataInfo = item.getMetadata();
            FieldMetadataInfo originFieldMetadataInfo = originFieldsMap.get(originFieldName);
            // 名称变化
            if (!item.getOldName().equals(fieldMetadataInfo.getFieldName())) {
                // 修改字段名优先
                renameSqls.add(fieldMetadataInfo.genRenameDDL(connection, tableName, originFieldName));
            }
            // 类型变化
            if (!originFieldMetadataInfo.typeEquals(fieldMetadataInfo)){
                alterDDLItems.add(fieldMetadataInfo.genModifyTypeDDLFragment(connection));
            }
            // NULL约束变化
            if (originFieldMetadataInfo.isNullable() != fieldMetadataInfo.isNullable()) {
                alterDDLItems.add(fieldMetadataInfo.genModifyNullConstraintDDLFragment(connection));
            }
            // Default约束变化
            if (!StringUtils.equals(originFieldMetadataInfo.getDefaultValue(), fieldMetadataInfo.getDefaultValue())) {
                alterDDLItems.add(fieldMetadataInfo.genModifyDefaultConstraintDDLFragment(connection));
            }
            // 注释变化
            if (!StringUtils.equals(originFieldMetadataInfo.getRemarks(), fieldMetadataInfo.getRemarks())) {
                if (fieldMetadataInfo.getRemarks() == null) {
                    fieldMetadataInfo.setRemarks("");
                }
                commentSqls.add(fieldMetadataInfo.genCommentDDL(connection, tableName));
            }
        }
        if(alterDDLItems.size()>0){
            String modifySql = alterSql.append("\n").append(String.join(",\n", alterDDLItems)).append(";").toString();
            modifySqls.add(modifySql);
        }
        re.setModify(modifySqls);
        re.setRename(renameSqls);
        re.setComment(commentSqls);
        return re;
    }



    static List<String> addPkDDLItems(Connection connection, List<PkMetadataInfo> originPkMetadataInfos, FieldMetadataInfo fieldMetadataInfo) {
        List<String> alterDDLItems = new ArrayList<>();
        if (fieldMetadataInfo.isPrimaryKey()) {
            // 复合主键在指定顺序插入主键，或者插在末尾
            List<PkMetadataInfo> newPkMetadataInfos = new ArrayList<>();
            newPkMetadataInfos.addAll(originPkMetadataInfos);
            newPkMetadataInfos.add(new PkMetadataInfo(fieldMetadataInfo.getFieldName(), null, fieldMetadataInfo.getPkSeq() == null ? originPkMetadataInfos.size() + 1 : fieldMetadataInfo.getPkSeq()));

            if (!CollectionUtils.isEmpty(originPkMetadataInfos)) {
                // 删除原有主键
                alterDDLItems.add(fieldMetadataInfo.genDropConstraintDDLFragment(connection, originPkMetadataInfos.get(0).getPkName()));
            }
            String[] pkColumns = newPkMetadataInfos.stream()
                    .sorted(Comparator.comparing(PkMetadataInfo::getSeq).thenComparing((o1, o2) -> {
                        if (o1.getColumnName().equals(fieldMetadataInfo.getFieldName())) {
                            return -1;
                        } else {
                            return 1;
                        }
                    }))
                    .map(PkMetadataInfo::getColumnName).toArray(String[]::new);
            // 新增主键
            alterDDLItems.add(fieldMetadataInfo.genAddPrimaryKeyDDLFragment(connection, pkColumns));
        }
        return alterDDLItems;
    }

    static List<String> deletePkDDLItems(Connection connection, List<PkMetadataInfo> pkMetadataInfos, String deletedField) {
        List<String> alterDDLItems = new ArrayList<>();
        boolean isPk = false;
        String pkName = null;
        for (PkMetadataInfo pkMetadataInfo : pkMetadataInfos) {
            if (pkMetadataInfo.getColumnName().equals(deletedField)) {
                pkName = pkMetadataInfo.getPkName();
                isPk = true;
                pkMetadataInfos.remove(pkMetadataInfo);
                break;
            }
        }
        FieldMetadataInfo fieldMetadataInfo = FieldMetadataInfo.builder().fieldName(deletedField).isPrimaryKey(isPk).build();
        if (isPk) {
            // 删除主键
            alterDDLItems.add(fieldMetadataInfo.genDropConstraintDDLFragment(connection, pkName));
            if (pkMetadataInfos.size() > 0) {
                String[] pkColumns = pkMetadataInfos.stream()
                        .sorted(Comparator.comparing(PkMetadataInfo::getSeq))
                        .map(PkMetadataInfo::getColumnName).toArray(String[]::new);
                // 重建主键
                alterDDLItems.add(fieldMetadataInfo.genAddPrimaryKeyDDLFragment(connection, pkColumns));
            }
        }
        return alterDDLItems;
    }

    static List<String> modifyPk(Connection connection, String originPkName, String[] pkColumns) {
        List<String> alterDDLItems = new ArrayList<>();
        // 删除主键
        alterDDLItems.add(new FieldMetadataInfo().genDropConstraintDDLFragment(connection, originPkName));
        if (pkColumns.length > 0) {
            // 重建主键
            alterDDLItems.add(new FieldMetadataInfo().genAddPrimaryKeyDDLFragment(connection, pkColumns));
        }
        return alterDDLItems;
    }

    static TableMetadataInfo convert2TableMetadataInfo(ResultSet tableRs, JdbcConnectionMetadata jdbcConnectionInfo, DatabaseMetaData databaseMetaData, boolean returnFields, @Nullable String fieldNamePattern) {
        try {
            String tableName = tableRs.getString("TABLE_NAME");
            TableMetadataInfo re = TableMetadataInfo.builder()
                    .tableName(tableName)
                    .remarks(tableRs.getString("REMARKS"))
                    .build();
            Map<String, PkMetadataInfo> pkFields = new HashMap<>();
            List<PkMetadataInfo> pkMetadataInfos = new ArrayList<>();
            try (ResultSet primaryKeyRs = databaseMetaData.getPrimaryKeys(jdbcConnectionInfo.getDatabase(), jdbcConnectionInfo.getSchema(), tableName);) {
                while (primaryKeyRs.next()) {
                    String pkColumnName = primaryKeyRs.getString("COLUMN_NAME");
                    Integer pkSeq = primaryKeyRs.getInt("key_seq");
                    String pkName = primaryKeyRs.getString("pk_name");
                    PkMetadataInfo pkItem = new PkMetadataInfo(pkColumnName, pkName, pkSeq);
                    pkMetadataInfos.add(pkItem);
                    pkFields.put(pkColumnName, pkItem);
                }
                re.setPkMetadataInfos(pkMetadataInfos);
            }
            if (returnFields) {
                //获取字段信息
                Connection connection = databaseMetaData.getConnection();
                ResultSet fieldsRs = databaseMetaData.getColumns(jdbcConnectionInfo.getDatabase(), jdbcConnectionInfo.getSchema(), tableName, fieldNamePattern);
                List<FieldMetadataInfo> list = convert2FieldMetadataInfos(fieldsRs, pkFields, TYPE_MAP);
                if (fieldNamePattern != null && CollectionUtils.isEmpty(list)) {
                    throw new IllegalArgumentException(String.format("%s字段不存在于数据库%s.%s表%s中", fieldNamePattern, jdbcConnectionInfo.getDatabase(), jdbcConnectionInfo.getSchema(), tableName));

                }
                if (connection instanceof SQLServerConnection) {
                    injectTableRemarks2SqlServer((SQLServerConnection) connection, re, tableName);
                    injectColumnRemarks2Sqlserver((SQLServerConnection) connection, list, tableName);
                }
                re.setFieldMetadataInfos(list);
            }
            return re;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    static List<FieldMetadataInfo> convert2FieldMetadataInfos(ResultSet fieldsRs, Map<String, PkMetadataInfo> pkFields, Map<String, String> TYPE_MAP) throws SQLException {
        List<FieldMetadataInfo> re = new ArrayList<>();
        while (fieldsRs.next()) {
            String fieldName = fieldsRs.getString("COLUMN_NAME");
            boolean isPk = pkFields.containsKey(fieldName);
            FieldMetadataInfo fieldMetadata = FieldMetadataInfo.builder()
                    .fieldName(fieldName)
                    .sqlType(fieldsRs.getString("TYPE_NAME"))
                    .jdbcDataType(JDBCType.valueOf(fieldsRs.getInt("DATA_TYPE")).getName())
                    //默认Object类型
                    .javaType("Object")
                    .defaultValue(fieldsRs.getString("COLUMN_DEF"))
                    .remarks(escapeString(fieldsRs.getString("REMARKS")))
                    .isNullable("YES".equals(fieldsRs.getString("IS_NULLABLE")))
                    .columnSize(fieldsRs.getInt("COLUMN_SIZE"))
                    .isAutoincrement("YES".equals(fieldsRs.getString("IS_AUTOINCREMENT")))
                    .scale(fieldsRs.getInt("DECIMAL_DIGITS"))
                    .build();
            String javaType = TYPE_MAP.get(fieldMetadata.getJdbcDataType());
            if (javaType != null) {
                fieldMetadata.setJavaType(javaType);
            }
            if (isPk) {
                fieldMetadata.setPrimaryKey(true);
                fieldMetadata.setPkSeq(pkFields.get(fieldName).getSeq());
                fieldMetadata.setPkName(pkFields.get(fieldName).getPkName());
//                tableMetadataInfo
//                        .setPkJavaType(fieldMetadata.getJavaType())
//                        .setPkSqlType(fieldMetadata.getSqlType())
//                        .setPkJdbcType(fieldMetadata.getJavaType());
            }
            re.add(fieldMetadata);
        }
        return re;
    }

    static String addPrimaryKeyDDLFragment(Connection connection, List<PkMetadataInfo> pks, @Nullable String insertedFieldName){
        String[] pkColumns = null;
        if(pks==null){
            return null;
        }
        Comparator<PkMetadataInfo> comparator = (o1, o2) -> {
            if(o1.getSeq()==null){
                return 1;
            }else if(o2.getSeq()==null){
                return -1;
            }else{
                return o1.getSeq().compareTo(o2.getSeq());
            }
        };
        if(insertedFieldName != null){

            pkColumns = pks.stream()
                    .sorted(comparator.thenComparing((o1, o2) -> {
                        if (o1.getColumnName().equals(insertedFieldName)) {
                            return -1;
                        } else {
                            return 1;
                        }
                    }))
                    .map(PkMetadataInfo::getColumnName).toArray(String[]::new);
        }else{
            pkColumns = pks.stream()
                    .sorted(comparator)
                    .map(PkMetadataInfo::getColumnName).toArray(String[]::new);
        }
       return new FieldMetadataInfo().genAddPrimaryKeyDDLFragment(connection, pkColumns);
    }

}
