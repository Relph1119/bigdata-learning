package com.teapot.clickhouse.dao;

import com.clickhouse.client.ClickHouseException;
import com.clickhouse.client.ClickHouseRequest;
import com.clickhouse.client.ClickHouseRequestManager;
import com.clickhouse.client.ClickHouseResponse;
import com.clickhouse.data.ClickHouseRecord;
import com.clickhouse.jdbc.SqlExceptionUtils;
import com.teapot.clickhouse.config.ClickHouseConfig;
import org.springframework.stereotype.Component;

import java.io.UncheckedIOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class CustomDao {
    public String customQueryId() throws SQLException {
        String sql = "select 1";
        String queryId = "my-query-id";
        String result;
        try (Connection conn = ClickHouseConfig.getConnection(); Statement stmt = conn.createStatement()) {
            stmt.unwrap(ClickHouseRequest.class).manager(new ClickHouseRequestManager() {
                private final AtomicInteger id = new AtomicInteger(0);

                @Override
                public String createQueryId() {
                    return "my-query-" + id.incrementAndGet();
                }
            });
            try {
                ClickHouseResponse resp = stmt.unwrap(ClickHouseRequest.class).query(sql, queryId).executeAndWait();
                ClickHouseRecord record = resp.firstRecord();
                result = record.getValue("1").asString();
            } catch (ClickHouseException | UncheckedIOException e) {
                throw SqlExceptionUtils.handle(e);
            }
        }
        return result;
    }
}