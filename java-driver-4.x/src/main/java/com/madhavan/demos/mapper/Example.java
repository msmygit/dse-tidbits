/**
 * @author Created by Madhavan Sridharan on Sep 13, 2020 9:26:51 PM.
 */
package com.madhavan.demos.mapper;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;

/**
 * @author Madhavan Sridharan (@msmygit)
 *
 */
@Entity
@CqlName("tbl_sample_mapper_solr_query")
public class Example {
    @PartitionKey private int id;
    private int value;
    
    public Example() {}
    
    public Example(int id, int value) {
	this.id = id;
	this.value = value;
    }
    
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    public int getValue() {return value;}
    public void setValue(int value) { this.value = value;}

    @Override
    public String toString() {
	return "Example [id=" + id + ", value=" + value + "]";
    }
}
