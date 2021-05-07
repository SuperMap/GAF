package com.supermap.gaf.data.graph.utils;

import com.supermap.gaf.data.graph.config.Neo4jProperties;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.driver.*;


/**
 * @author : duke
 * @since 2021/5/7 2:24 PM
 */
@Slf4j
public class Neo4jAutoCloseable implements AutoCloseable {
    private final Driver driver;



    public Neo4jAutoCloseable(Neo4jProperties neo4jProperties) {
        driver = GraphDatabase.driver( neo4jProperties.getUri(), AuthTokens.basic( neo4jProperties.getUsername(), neo4jProperties.getPassword() ) );
    }

    @Override
    public void close() {
        driver.close();
    }



    public void execute(String cql) {
        try ( Session session = driver.session() ) {
            int res = session.writeTransaction(tx->{
                    Result result = tx.run(cql);
                    return result.list().size();
                });
            log.info("Neo4j执行CQL：【{}】", cql);
            log.info("Neo4j执行结果：结果记录【{}】条", res);
        }
    }



}
