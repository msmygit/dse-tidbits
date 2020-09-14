/**
 * @author Created by Madhavan Sridharan on Sep 13, 2020 9:32:16 PM.
 */
package com.madhavan.demos.mapper;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.mapper.MapperBuilder;
import com.datastax.oss.driver.api.mapper.annotations.DaoFactory;
import com.datastax.oss.driver.api.mapper.annotations.DaoKeyspace;
import com.datastax.oss.driver.api.mapper.annotations.DaoProfile;
import com.datastax.oss.driver.api.mapper.annotations.DaoTable;
import com.datastax.oss.driver.api.mapper.annotations.Mapper;

/**
 * @author Madhavan Sridharan (@msmygit)
 *
 */
@Mapper
public interface ExampleMapper {
    static MapperBuilder<ExampleMapper> builder(CqlSession session) {
	return new ExampleMapperBuilder(session);
    }

    @DaoFactory
    ExampleDao exampleDao(@DaoProfile String profileName);

    @DaoFactory
    ExampleDao exampleDao(@DaoProfile String profileName, @DaoKeyspace String keyspace, @DaoTable String table);
}
