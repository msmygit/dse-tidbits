/**
 * @author Created by Madhavan Sridharan on Sep 13, 2020 9:30:33 PM.
 */
package com.madhavan.demos.mapper;

import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Delete;
import com.datastax.oss.driver.api.mapper.annotations.Insert;
import com.datastax.oss.driver.api.mapper.annotations.Query;
import com.datastax.oss.driver.api.mapper.annotations.Select;

/**
 * @author Madhavan Sridharan (@msmygit)
 *
 */
@Dao
public interface ExampleDao {
    @Select
    Example findById(int id);
    
    @Query("SELECT * FROM ${keyspaceId}.${tableId} WHERE solr_query = :solr_query")
    Example findByValueSearchIndex(String solr_query);

    @Select(customWhereClause = "solr_query = :solr_query")
    Example findByValueSearchIndex1(String solr_query);

    @Insert
    void save(Example id);

    @Delete
    void delete(Example example);
}
