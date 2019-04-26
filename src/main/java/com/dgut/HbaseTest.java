package com.dgut;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;


/**
 * @Author: wt
 * @Date: 2019/4/26 14:33
 */
public class HbaseTest {
    private static Connection connection;
    private static Configuration configuration;
    public static Admin admin;

    /**
     * 创建表
     *
     * @param tableName
     * @param types
     */
    public static void createTable(String tableName, String[] types) {
        init();
        TableName tableName1 = TableName.valueOf(tableName);

        try {
            if (admin.tableExists(tableName1)) {
                System.out.println("Table is exit!");
                admin.disableTable(tableName1);
                admin.deleteTable(tableName1);
            }

            HTableDescriptor hTableDescriptor = new HTableDescriptor(tableName1);

            for (String type : types) {
                HColumnDescriptor hColumnDescriptor = new HColumnDescriptor(type);
                hTableDescriptor.addFamily(hColumnDescriptor);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 插入记录
     *
     * @param tablename
     * @param row
     * @param fields
     * @param value
     */
    public static void addRecord(String tablename, String row, String[] fields, String[] value) {
        init();
        try {
            Table table = connection.getTable(TableName.valueOf(tablename));

            for (int i = 0; i != fields.length; i++) {
                Put put = new Put(row.getBytes());
                String[] cols = fields[i].split(":");
                put.addColumn(cols[0].getBytes(), cols[1].getBytes(), value[i].getBytes());
                table.put(put);
            }

            table.close();
            close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * scan
     */
    public static void scanTable(String tablename, String column) {
        init();
        try {
            Table table = connection.getTable(TableName.valueOf(tablename));
            Scan scan = new Scan();
            scan.addFamily(Bytes.toBytes(column));
            ResultScanner results = table.getScanner(scan);

            for (Result result = results.next(); result != null; result = results.next()) {
                showCell(result);
            }

            table.close();
            close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showCell(Result result) {
        Cell[] cells = result.rawCells();
        for (Cell cell : cells) {
            System.out.println(String.format("RowName: %s; Timestamp: %s; Column Family %s; Row name: %s; Value: .", new String(CellUtil.cloneRow(cell)), cell.getTimestamp(), new String(CellUtil.cloneRow(cell)), CellUtil.cloneValue(cell)));
        }
    }

    /**
     * 修改
     */
    public static void modifyData(String tablename, String row, String column, String value) {
        init();
        long time = 0;
        try {
            Table table = connection.getTable(TableName.valueOf(tablename));
            Put put = new Put(row.getBytes());
            Scan scan = new Scan();
            ResultScanner results = table.getScanner(scan);
            for (Result result : results) {
                for (Cell cell : result.getColumnCells(row.getBytes(), column.getBytes())) {
                    time = cell.getTimestamp();
                }
            }
            put.addColumn(row.getBytes(), column.getBytes(), time, value.getBytes());
            table.put(put);
            table.close();
            ;
            close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除列
     */
    public static void deleteRow(String tablename, String row) {
        init();
        try {
            Table table = connection.getTable(TableName.valueOf(tablename));
            Delete delete = new Delete(row.getBytes());
            table.delete(delete);
            table.close();
            close();
        } catch (IOException e) {

        }
    }

    /**
     * 建立连接
     */
    public static void init() {
        configuration = HBaseConfiguration.create();
        configuration.set("hbase.rootdir", "hdfs://172.28.7.26:9000/hbase");

        try {
            connection = ConnectionFactory.createConnection(configuration);
            admin = connection.getAdmin();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭连接
     */
    public static void close() {
        try {
            if (admin != null) {
                admin.close();
            }
            if (connection != null) {
                connection.close();
            }
            if (configuration != null) {
                configuration.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String[] types = {"type1", "type2"};
        createTable("table_test", types);
    }
}
