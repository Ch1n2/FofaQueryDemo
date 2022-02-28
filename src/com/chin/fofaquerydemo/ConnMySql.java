package com.chin.fofaquerydemo;

import java.sql.*;

public class ConnMySql {

    String product;
    String product_new;
    String flag;

    //MySOL8.0以下版本
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/fofaquery?useSSL=true";
    //数据库的用户名密码
    static final String USER = "root";
    static final String PASS = "123456";

    Connection conn = null;
    Statement stmt = null;

    public String ConnMySqlQuery(String fofaquery) {
        try{
            //注册JDBC驱动
            Class.forName(JDBC_DRIVER);

            //打开连接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            //执行查询
            System.out.println("实例化Statement对象...");
            stmt = conn.createStatement();
            String sql;
//            sql = "SELECT ID, ID_new, product, product_new, company, company_new, sort from fofaquerycompare";
            //模糊匹配
//            sql = "SELECT * FROM fofaquerycompare WHERE product_new LIKE '%大华-DH-SD2304%'";
            //精确匹配
//            sql = "SELECT * FROM fofaquerycompare WHERE product in ('大华-DH-SD2304')";

            sql = "SELECT * FROM fofaquerycompare WHERE binary product='" + fofaquery + "'" + " UNION SELECT * FROM fofaquerycompare WHERE binary product_new='" + fofaquery + "'";
            //用下面这种方法binary无法生效，不能解决查询大小写不敏感的问题
//            sql = String.format("SELECT * FROM fofaquerycompare where binary find_in_set('%s',product) UNION SELECT * FROM fofaquerycompare where binary find_in_set('%s',product_new)",fofaquery,fofaquery);


            ResultSet rs = stmt.executeQuery(sql);


            //展开结果集数据
            while(rs.next()){
                //通过字段检索
                int id = rs.getInt("ID");
                int id_new = rs.getInt("ID_new");
                product = rs.getString("product");
                product_new = rs.getString("product_new");
                String company = rs.getString("company");
                String company_new = rs.getString("company_new");
                String sort = rs.getString("sort");
//                System.out.println("ID:" + id + "，ID-新：" + id_new + "，旧-产品名称：" + product + "，产品名称-新：" + product_new + "，旧-公司名称：" + company + "，公司名称-新：" + company_new + "，分类：" + sort);

                if (product.equals(fofaquery) && !product.equals(product_new)){
                    System.out.println("This is a old product's name.");
                    System.out.println(product);
                    System.out.println(product_new);
                    flag = "This is a old product's name.";
                }else if (product_new.equals(fofaquery) && !product_new.equals(product)){
                    System.out.println("This is a new product's name.");
                    System.out.println(product);
                    System.out.println(product_new);
                    flag = "This is a new product's name.";
                }else if (product.equals(product_new)){
                    System.out.println("The new rules are the same as the old rules.");
                    System.out.println(product);
                    System.out.println(product_new);
                    flag = "The new rules are the same as the old rules.";
                }


            }
            //关闭连接
            rs.close();
            stmt.close();
            conn.close();
        }catch (SQLException se){
            //处理JDBC错误
            se.printStackTrace();
//            System.out.println(se);
            flag = se.toString();
        }catch (Exception e){
            //处理Class.forName错误
            e.printStackTrace();
            return e.toString();
        }finally {
            //关闭资源
            try{
                if (stmt!=null) stmt.close();
            }catch (SQLException se2){
                flag = se2.toString();
                //什么都不做
            }
            try{
                if (conn != null) conn.close();
            }catch (SQLException se){
                se.printStackTrace();
                flag = se.toString();
            }
        }
        System.out.println("Goobye!");
        return flag + "\n" + "旧规则：app=\"" + product + "\"\n新规则：app=\"" +  product_new + "\"";
    }
}
