package com.kedacom.flinksql.catalog;

import org.apache.flink.annotation.VisibleForTesting;
import org.apache.flink.table.catalog.*;
import org.apache.flink.table.catalog.exceptions.*;
import org.apache.flink.table.catalog.stats.CatalogColumnStatistics;
import org.apache.flink.table.catalog.stats.CatalogTableStatistics;
import org.apache.flink.table.expressions.Expression;
import org.apache.flink.table.factories.Factory;
import org.apache.flink.table.factories.FunctionDefinitionFactory;
import org.apache.flink.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.kedacom.flinksql.catalog.KedaCatalogFactory.DEFAULT_DB;
import static org.apache.flink.util.Preconditions.checkArgument;
import static org.apache.flink.util.Preconditions.checkNotNull;

// catalog 是一个通用的catalog，里面应该包含 科达数据地图的rest服务地址，或者 保存数据地图的数据库的信息
//
public class KedaCatalog extends AbstractCatalog {

    private static final Logger LOG = LoggerFactory.getLogger(KedaCatalog.class);
    private String strmetaurl;
    public KedaCatalog(String catalogName, String defaultDatabase, String metaurl) {
        super(catalogName, defaultDatabase == null ? DEFAULT_DB : defaultDatabase);

        checkArgument(!StringUtils.isNullOrWhitespaceOnly(metaurl), "kedacatalog metaurl cannot be null or empty");
        strmetaurl = metaurl;

        LOG.info("Created KedaCatalog '{}' {} {}", catalogName, defaultDatabase, metaurl);
    }


    @Override
    public Optional<Factory> getFactory() {
        return Optional.empty();
    }

    @Override
    public Optional<FunctionDefinitionFactory> getFunctionDefinitionFactory() {
        return Optional.empty();
    }

    @Override
    public void open() throws CatalogException {
        // open the meta url, to connect the metaurl
        LOG.info("Connected to keda metastore");

        if (!databaseExists(getDefaultDatabase())) {
            throw new CatalogException(String.format("Configured default database %s doesn't exist in catalog %s.",
                    getDefaultDatabase(), getName()));
        }
    }

    @Override
    public void close() throws CatalogException {
        if ("" != null) {
            LOG.info("Close connection to Keda metastore");
        }
    }

    @Override
    public List<String> listDatabases() throws CatalogException {
        return null;
    }

    @Override
    public CatalogDatabase getDatabase(String databaseName) throws DatabaseNotExistException, CatalogException {
        Map<String, String> properties = new HashMap<>();
        // add more properties
        return new CatalogDatabaseImpl(properties, "database comment");
    }

    @Override
    public boolean databaseExists(String databaseName) throws CatalogException {
        return false;
    }

    @Override
    public void createDatabase(String name, CatalogDatabase database, boolean ignoreIfExists) throws DatabaseAlreadyExistException, CatalogException {
        checkArgument(!StringUtils.isNullOrWhitespaceOnly(name), "databaseName cannot be null or empty");
        checkNotNull(database, "database cannot be null");

        throw new CatalogException(String.format("Not support to create database %s", name));
    }

    @Override
    public void dropDatabase(String name, boolean ignoreIfNotExists, boolean cascade) throws DatabaseNotExistException, DatabaseNotEmptyException, CatalogException {

    }

    @Override
    public void alterDatabase(String name, CatalogDatabase newDatabase, boolean ignoreIfNotExists) throws DatabaseNotExistException, CatalogException {

    }

    @Override
    public List<String> listTables(String databaseName) throws DatabaseNotExistException, CatalogException {
        return null;
    }

    @Override
    public List<String> listViews(String databaseName) throws DatabaseNotExistException, CatalogException {
        return null;
    }

    @Override
    public CatalogBaseTable getTable(ObjectPath tablePath) throws TableNotExistException, CatalogException {
        return null;
    }

    @Override
    public boolean tableExists(ObjectPath tablePath) throws CatalogException {
        return false;
    }

    @Override
    public void dropTable(ObjectPath tablePath, boolean ignoreIfNotExists) throws TableNotExistException, CatalogException {

    }

    @Override
    public void renameTable(ObjectPath tablePath, String newTableName, boolean ignoreIfNotExists) throws TableNotExistException, TableAlreadyExistException, CatalogException {

    }

    @Override
    public void createTable(ObjectPath tablePath, CatalogBaseTable table, boolean ignoreIfExists) throws TableAlreadyExistException, DatabaseNotExistException, CatalogException {

    }

    @Override
    public void alterTable(ObjectPath tablePath, CatalogBaseTable newTable, boolean ignoreIfNotExists) throws TableNotExistException, CatalogException {

    }

    //------------------------------------------------------------------------------------------------------
    @Override
    public List<CatalogPartitionSpec> listPartitions(ObjectPath tablePath) throws TableNotExistException, TableNotPartitionedException, CatalogException {
        return null;
    }

    @Override
    public List<CatalogPartitionSpec> listPartitions(ObjectPath tablePath, CatalogPartitionSpec partitionSpec) throws TableNotExistException, TableNotPartitionedException, CatalogException {
        return null;
    }

    @Override
    public List<CatalogPartitionSpec> listPartitionsByFilter(ObjectPath tablePath, List<Expression> filters) throws TableNotExistException, TableNotPartitionedException, CatalogException {
        return null;
    }

    @Override
    public CatalogPartition getPartition(ObjectPath tablePath, CatalogPartitionSpec partitionSpec) throws PartitionNotExistException, CatalogException {
        return null;
    }

    @Override
    public boolean partitionExists(ObjectPath tablePath, CatalogPartitionSpec partitionSpec) throws CatalogException {
        return false;
    }

    @Override
    public void createPartition(ObjectPath tablePath, CatalogPartitionSpec partitionSpec, CatalogPartition partition, boolean ignoreIfExists) throws TableNotExistException, TableNotPartitionedException, PartitionSpecInvalidException, PartitionAlreadyExistsException, CatalogException {

    }

    @Override
    public void dropPartition(ObjectPath tablePath, CatalogPartitionSpec partitionSpec, boolean ignoreIfNotExists) throws PartitionNotExistException, CatalogException {

    }

    @Override
    public void alterPartition(ObjectPath tablePath, CatalogPartitionSpec partitionSpec, CatalogPartition newPartition, boolean ignoreIfNotExists) throws PartitionNotExistException, CatalogException {

    }
    //------------------------------------------------------------------------- function
    @Override
    public List<String> listFunctions(String dbName) throws DatabaseNotExistException, CatalogException {
        return null;
    }

    @Override
    public CatalogFunction getFunction(ObjectPath functionPath) throws FunctionNotExistException, CatalogException {
        return null;
    }

    @Override
    public boolean functionExists(ObjectPath functionPath) throws CatalogException {
        return false;
    }

    @Override
    public void createFunction(ObjectPath functionPath, CatalogFunction function, boolean ignoreIfExists) throws FunctionAlreadyExistException, DatabaseNotExistException, CatalogException {

    }

    @Override
    public void alterFunction(ObjectPath functionPath, CatalogFunction newFunction, boolean ignoreIfNotExists) throws FunctionNotExistException, CatalogException {

    }

    @Override
    public void dropFunction(ObjectPath functionPath, boolean ignoreIfNotExists) throws FunctionNotExistException, CatalogException {

    }

    //------------------------------------------------------------ staticstics
    @Override
    public CatalogTableStatistics getTableStatistics(ObjectPath tablePath) throws TableNotExistException, CatalogException {
        return null;
    }

    @Override
    public CatalogColumnStatistics getTableColumnStatistics(ObjectPath tablePath) throws TableNotExistException, CatalogException {
        return null;
    }

    @Override
    public CatalogTableStatistics getPartitionStatistics(ObjectPath tablePath, CatalogPartitionSpec partitionSpec) throws PartitionNotExistException, CatalogException {
        return null;
    }

    @Override
    public CatalogColumnStatistics getPartitionColumnStatistics(ObjectPath tablePath, CatalogPartitionSpec partitionSpec) throws PartitionNotExistException, CatalogException {
        return null;
    }

    @Override
    public void alterTableStatistics(ObjectPath tablePath, CatalogTableStatistics tableStatistics, boolean ignoreIfNotExists) throws TableNotExistException, CatalogException {

    }

    @Override
    public void alterTableColumnStatistics(ObjectPath tablePath, CatalogColumnStatistics columnStatistics, boolean ignoreIfNotExists) throws TableNotExistException, CatalogException, TablePartitionedException {

    }

    @Override
    public void alterPartitionStatistics(ObjectPath tablePath, CatalogPartitionSpec partitionSpec, CatalogTableStatistics partitionStatistics, boolean ignoreIfNotExists) throws PartitionNotExistException, CatalogException {

    }

    @Override
    public void alterPartitionColumnStatistics(ObjectPath tablePath, CatalogPartitionSpec partitionSpec, CatalogColumnStatistics columnStatistics, boolean ignoreIfNotExists) throws PartitionNotExistException, CatalogException {

    }
}
