/*
 * Copyright (c) 2008-2018, Hazelcast, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hazelcast.jet.tests.sql.tests;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.jet.JetInstance;

//TODO: Create SQL tests for different InMemoryFormat
//TODO: Create SQL tests for Kafka as a data source
public class SqlObjectMapTest extends AbstractSqlTest {

    private static final String MAP_NAME = "object_sql_map";

    private HazelcastInstance hazelcastInstance;

    public static void main(String[] args) throws Exception{
        new SqlObjectMapTest().run(args);
    }

    @Override
    protected void init(JetInstance client){
        hazelcastInstance = client.getHazelcastInstance();
        dataSetSize = propertyInt("dataSetSize", DEFAULT_DATA_SET_SIZE);
        queryTimeout = propertyInt("queryTimeout", DEFAULT_QUERY_TIMEOUT_MILLIS);
        populateMap(hazelcastInstance, MAP_NAME);
    }

    @Override
    protected void test(JetInstance client, String name){
        runTest(hazelcastInstance, MAP_NAME);
    }

    @Override
    protected void teardown(Throwable t){
        //Not sure what to cleanup in case of sql
        //TODO: Find out
    }
}