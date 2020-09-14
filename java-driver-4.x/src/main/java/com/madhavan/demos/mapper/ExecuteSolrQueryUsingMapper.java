/**
 * @author Created by Madhavan Sridharan on Sep 13, 2020 7:47:54 PM.
 */
package com.madhavan.demos.mapper;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.CqlSessionBuilder;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;

/**
 * @author Madhavan Sridharan (@msmygit)
 *
 */
public class ExecuteSolrQueryUsingMapper {
    private static final String searchDatacenterName = "olasgp";
    public static void main(String[] args) {
	try (CqlSession session = new CqlSessionBuilder()
		//.withAuthCredentials(System.getProperty("uid"), System.getProperty("pwd"))
                .build()) {
	    createSchema(session);
	    insertSampleData(session);
	    selectDataBySearchIndexColumn(session, 5);
	}
	System.out.println("----DONE----");
    }

    private static void createSchema(CqlSession session) {
	session.execute("CREATE KEYSPACE IF NOT EXISTS test "
		+ "WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '1'}");

	session.execute(
		"CREATE TABLE IF NOT EXISTS test.tbl_sample_mapper_solr_query (id int, value int, PRIMARY KEY (id))");

	SimpleStatement createSearchIndexStmt = SimpleStatement.builder(
		"CREATE SEARCH INDEX IF NOT EXISTS ON test.tbl_sample_mapper_solr_query WITH COLUMNS value AND PROFILES spaceSavingNoJoin")
		.setExecutionProfileName(searchDatacenterName)// Update to Search enabled DC name
		.build();

	session.execute(createSearchIndexStmt);
    }

    private static void insertSampleData(CqlSession session) {
	session.execute("INSERT INTO test.tbl_sample_mapper_solr_query(id,value) VALUES (1,1)");
	session.execute("INSERT INTO test.tbl_sample_mapper_solr_query(id,value) VALUES (2,2)");
	session.execute("INSERT INTO test.tbl_sample_mapper_solr_query(id,value) VALUES (3,3)");
	session.execute("INSERT INTO test.tbl_sample_mapper_solr_query(id,value) VALUES (4,4)");
	session.execute("INSERT INTO test.tbl_sample_mapper_solr_query(id,value) VALUES (5,5)");
    }
    
    private static void selectDataBySearchIndexColumn(CqlSession session, int value) {
	ExampleMapper exampleMapper = ExampleMapper.builder(session)
		//Schema validation adds a small startup overhead, so once your application is stable you may want to disable it
		.withSchemaValidationEnabled(false)
		.build();
	
	ExampleDao exampleDao = exampleMapper.exampleDao(searchDatacenterName, "test", "tbl_sample_mapper_solr_query");
	
	System.out.println("Result is: " + (exampleDao.findById(value)).toString());
	
	System.out.println("Search result is: " + (exampleDao.findByValueSearchIndex("value:" + String.valueOf(value))));
    }
}
