package com.supermap.gaf.commontypes.metadata;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.http.util.Asserts;

import javax.annotation.Nullable;
import java.sql.Connection;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * The type Field metadata info.
 *
 * @author:yw
 * @Date 2021 /3/17
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel("字段元数据信息")
public class FieldMetadataInfo {
    @ApiModelProperty(value = "字段名")
    private String fieldName;
    @ApiModelProperty(value = "小写驼峰字段名")
    private String fieldLowCamelName;
    @ApiModelProperty(value = "大写驼峰字段名")
    private String fieldUpperCamelName;
    @ApiModelProperty(value = "sql类型")
    private String sqlType;
    @ApiModelProperty(value = "jdbc类型")
    private String jdbcDataType;
    @ApiModelProperty(value = "java类型")
    private String javaType;
    @JSONField(name = "isNullable")
    @ApiModelProperty(value = "是否不能为空")
    private boolean isNullable;
    @ApiModelProperty(value = "注释")
    private String remarks;
    @ApiModelProperty(value = "默认值")
    private String defaultValue;
    @ApiModelProperty(value = "字段长度。对于数值数据，这是最大的精度。对于字符数据，这是以字符为单位的长度。对于datetime数据类型，这是String表示的字符长度(假设小数秒组件允许的最大精度)。对于二进制数据，这是以字节为单位的长度。对于ROWID数据类型，这是以字节为单位的长度\"")
    private Integer columnSize;
    @JSONField(name = "isPrimaryKey")
    @ApiModelProperty(value = "是否主键")
    private boolean isPrimaryKey;
    @ApiModelProperty(value = "主键序号")
    private Integer pkSeq;
    @JSONField(name = "isAutoincrement")
    @ApiModelProperty(value = "是否自增")
    private boolean isAutoincrement;
    @ApiModelProperty("小数位数")
    private Integer scale;


    public boolean isValueCharType(){
        String sqlTypeLowCase = getSqlType().toLowerCase();
        return sqlTypeLowCase.contains("char") || sqlTypeLowCase.contains("varying") || sqlTypeLowCase.contains("text");
    }

    /**
     * 生成数据类型ddl片段。如：varchar(10)
     *
     * @param connection the connection
     * @return string string
     */
    public String genDataTypeDDLFragment(Connection connection) {
        Asserts.notBlank(getSqlType(),"类型");
        Set<String> hasLength = new HashSet<>();
        hasLength.addAll(Arrays.asList("CHARACTER", "VARYING", "VARCHAR", "CHAR"));
        Set<String> hasPrecisionAndScale = new HashSet<>();
        hasPrecisionAndScale.addAll(Arrays.asList("NUMERIC"));
        String hasLengthTp = "%s(%s)";
        String hasPrecisionAndScaleTp = "%s(%s,%s)";
        String sqlType = getSqlType();
        if (getColumnSize() != null && hasLength.contains(sqlType.toUpperCase())) {
            return String.format(hasLengthTp, sqlType, "" + getColumnSize());
        } else if (getColumnSize() != null && hasPrecisionAndScale.contains(sqlType.toUpperCase())) {
            if (getScale() == null) {
                return String.format(hasLengthTp, sqlType, "" + getColumnSize());
            } else {
                return String.format(hasPrecisionAndScaleTp, sqlType, "" + getColumnSize(), "" + getScale());
            }
        } else {
            return sqlType;
        }
    }


    /**
     * 生成null约束ddl片段。如：NOT NULL
     *
     * @param connection the connection
     * @return the string
     */
    public String genNULLConstraintDDLFragment(Connection connection) {
        if (isNullable()) {
            return " NULL ";
        } else {
            return " NOT NULL ";
        }
    }

    /**
     * 生成默认值约束ddl片段。如：DEFAULT 'value'
     *
     * @param connection the connection
     * @return the string
     */
    public String genDefaultConstraintDDLFragment(Connection connection) {

        if (getDefaultValue() != null) {
            if(isValueCharType()){
                return String.format(" DEFAULT '%s' ", getDefaultValue());
            }else{
                return String.format(" DEFAULT %s ", getDefaultValue());
            }
        } else {
            return "";
        }
    }

    /**
     * 生成添加主键约束ddl片段。如：ADD PRIMARY KEY(field1,field2)
     *
     * @param connection the connection
     * @param keys       the keys
     * @return the string
     */
    public String genAddPrimaryKeyDDLFragment(Connection connection, @Nullable String... keys) {
        String tp = "ADD PRIMARY KEY(%s)";
        if (keys == null || keys.length == 0) {
            return String.format(tp, String.join(",", getFieldName()));
        } else {
            return String.format(tp, String.join(",", keys));
        }
    }

    /**
     * 生成删除字段默认值ddl片段。如：ALTER COLUMN field1 DROP DEFAULT
     *
     * @param connection the connection
     * @return the string
     */
    public String genDropDefaultDDLFragment(Connection connection) {
        String tp = "ALTER COLUMN %s DROP DEFAULT";
        return String.format(tp, getFieldName());
    }

    /**
     * 设置字段默认值ddl片段。如：ALTER COLUMN field1 SET DEFAULT '123456'
     *
     * @param connection the connection
     * @return the string
     */
    public String genSetDefaultDDLFragment(Connection connection) {
        String tp = "ALTER COLUMN %s SET DEFAULT %s";
        if(isValueCharType()){
            tp = "ALTER COLUMN %s SET DEFAULT '%s'";
        }
        if (getDefaultValue() == null) {
            return genDropDefaultDDLFragment(connection);
        } else {
            return String.format(tp, getFieldName(), getDefaultValue());
        }
    }

    /**
     * 设置字段注释ddl语句。如：COMMENT ON COLUMN table1.field1 IS '我解释';
     *
     * @param connection the connection
     * @param tableName  the table name
     * @return the string
     */
    public String genCommentDDL(Connection connection, String tableName) {
        String re = null;
        if (getRemarks() != null) {
            String tp = "COMMENT ON COLUMN %s.%s IS '%s';";
            re = String.format(tp, tableName, getFieldName(), getRemarks());
        }
        return re;
    }

    /**
     * 删除UNIQUE，PRIMARY KEY，FOREIGN KEY或CHECK约束ddl语句片段。如：DROP CONSTRAINT pk_name;
     *
     * @param connection      the connection
     * @param constraintName the constraint names
     * @return the string
     */
    public String genDropConstraintDDLFragment(Connection connection, String constraintName) {
        String tp = "DROP CONSTRAINT %s";
        return String.format(tp, String.join(" ", constraintName));
    }

    /**
     * 设置删除字段ddl片段语句。如：DROP COLUMN field1;
     *
     * @param connection the connection
     * @return the string
     */
    public String genDropColumnDDLFragment(Connection connection) {
        String tp = "DROP COLUMN %s";
        return String.format(tp, getFieldName());
    }

    /**
     * 重命名字段ddl语句。如：ALTER TABLE table1 RENAME COLUMN old TO new;
     *
     * @param connection      the connection
     * @param tableName       the table name
     * @param originFieldName the origin field name
     * @return the string
     */
    public String genRenameDDL(Connection connection, String tableName, String originFieldName) {
        String tp = "ALTER TABLE %s RENAME COLUMN %s TO %s;";
        return String.format(tp, tableName, originFieldName, getFieldName());
    }

    /**
     * 修改字段类型ddl片段语句。如：ALTER COLUMN field1 TYPE varchar(10) USING field1::varchar
     *
     * @param connection the connection
     * @return the string
     */
    public String genModifyTypeDDLFragment(Connection connection) {
        Asserts.notBlank(getSqlType(),"类型");
        Asserts.notBlank(getFieldName(),"字段名");
        String tp = "ALTER COLUMN %s TYPE %s USING %s::%s";
        String typeDDLFragment = genDataTypeDDLFragment(connection);
        return String.format(tp, getFieldName(), typeDDLFragment, getFieldName(), getSqlType());
    }

    /**
     * 修改null约束ddl片段语句。如：ALTER COLUMN DROP NOT NULL
     *
     * @param connection the connection
     * @return the string
     */
    public String genModifyNullConstraintDDLFragment(Connection connection) {
        String tp = "ALTER COLUMN %s %s";
        String nullDDLFragment = null;
        if (isNullable()) {
            nullDDLFragment = " DROP NOT NULL ";
        } else {
            nullDDLFragment = " SET NOT NULL ";
        }
        return String.format(tp, getFieldName(), nullDDLFragment);
    }

    /**
     * 修改字段默认值ddl片段语句。如：ALTER COLUMN field SET DEFAULT 'xxx'
     *
     * @param connection the connection
     * @return the string
     */
    public String genModifyDefaultConstraintDDLFragment(Connection connection) {
        String tp = "ALTER COLUMN ${columnName} SET ${defaultValue}".replaceAll("\\$\\{[^\\}]+\\}", "%s");
        String defaultDDLFragment = genDefaultConstraintDDLFragment(connection);
        return String.format(tp, getFieldName(), defaultDDLFragment);
    }

    /**
     * 修改字段默认值ddl片段语句。如：ALTER COLUMN field SET DEFAULT 'xxx'
     *
     * @param connection the connection
     * @return the string
     */
    public String genAddColumnDDLFragment(Connection connection) {
        String tp = "ADD ${columnName} ${dataType} ${nullable} ${default}".replaceAll("\\$\\{[^\\}]+\\}", "%s");
        return String.format(tp, getFieldName(), genDataTypeDDLFragment(connection),
                genNULLConstraintDDLFragment(connection), genDefaultConstraintDDLFragment(connection));
    }
}
