package com.kedacom.flinksql.catalog;

import org.apache.flink.table.catalog.Catalog;
import org.apache.flink.table.descriptors.DescriptorProperties;
import org.apache.flink.table.factories.CatalogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.flink.table.descriptors.CatalogDescriptorValidator.CATALOG_PROPERTY_VERSION;
import static org.apache.flink.table.descriptors.CatalogDescriptorValidator.CATALOG_TYPE;

public class KedaCatalogFactory implements CatalogFactory {
    private static final Logger LOG = LoggerFactory.getLogger(KedaCatalogFactory.class);

    public static final String DEFAULT_DB = "default";
    public static final String KEDACATALOG_DEFAULT_DATABASE = "default-database";
    public static final String KEDACATALOG_KEDA_META_JDBCURL = "keda-meta-jdbcurl";
    public static final String KEDACATALOG_VERSION = "version";


    @Override
    public Catalog createCatalog(String name, Map<String, String> properties) {
        final DescriptorProperties descriptorProperties = new DescriptorProperties(true);
        descriptorProperties.putProperties(properties);

        final String defaultDatabase =
                descriptorProperties.getOptionalString(KEDACATALOG_DEFAULT_DATABASE)
                        .orElse(DEFAULT_DB);
        final String version = descriptorProperties.getOptionalString(KEDACATALOG_VERSION).orElse("0.1");
        final String metaurl = descriptorProperties.getOptionalString(KEDACATALOG_KEDA_META_JDBCURL).orElse(null);
        return new KedaCatalog(name, defaultDatabase, metaurl);
    }

    @Override
    public Map<String, String> requiredContext() {
        Map<String, String> context = new HashMap<>();
        context.put(CATALOG_TYPE, "kedacom"); // hive
        context.put(CATALOG_PROPERTY_VERSION, "1"); // backwards compatibility
        return context;
    }

    @Override
    public List<String> supportedProperties() {
        List<String> properties = new ArrayList<>();

        // default database
        properties.add(KEDACATALOG_DEFAULT_DATABASE);
        properties.add(KEDACATALOG_KEDA_META_JDBCURL);
        properties.add(KEDACATALOG_VERSION);
        return properties;
    }
}
