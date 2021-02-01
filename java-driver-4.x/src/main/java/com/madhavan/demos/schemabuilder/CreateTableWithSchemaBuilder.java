/**
 * @author Created by Madhavan Sridharan on Jan 18, 2021 9:08:07 PM.
 */
package com.madhavan.demos.schemabuilder;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;

/**
 * @see <a href="https://docs.datastax.com/en/developer/java-driver/latest/manual/query_builder/schema/">Java Driver Schema Builder</a>
 * @see <a href="https://docs.datastax.com/en/developer/java-driver/latest/manual/core/dse/geotypes/">Java Driver Geospatial Documentation<a/>
 * 
 * @author Madhavan Sridharan (@msmygit)
 *
 */
public class CreateTableWithSchemaBuilder {
    public static void main(String... args) {
	try (CqlSession session = CqlSession.builder().build()) {
		//CREATE KEYSPACE IF NOT EXISTS test WITH replication = {'class':'NetworkTopologyStrategy', 'dc1':1}
	    session.execute(SchemaBuilder.createKeyspace("test").ifNotExists()
		    .withNetworkTopologyStrategy(ImmutableMap.of("dc1", 1));
	    session.execute(SchemaBuilder.createTable("test", "create_table_with_schema_builder").ifNotExists()
		    .withPartitionKey("pk1", DataTypes.INT)
		    .withColumn("c1_pointtype", DataTypes.custom("org.apache.cassandra.db.marshal.PointType"))
		    .withColumn("c2_linestringtype", DataTypes.custom("org.apache.cassandra.db.marshal.LineStringType"))
		    .withColumn("c2_polygontype", DataTypes.custom("org.apache.cassandra.db.marshal.PolygonType"))
		    .build());
	}
    }
}
