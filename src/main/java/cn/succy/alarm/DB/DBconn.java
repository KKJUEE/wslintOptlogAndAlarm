package cn.succy.alarm.DB;

import java.sql.*;

import cn.succy.alarm.config.ConfigManager;
import cn.succy.alarm.config.ProviderConfig;
/**
 * 数据库操作封装 
 * @author ranzhonggeng
 *
 */
public class DBconn {
	static ProviderConfig config = ConfigManager.getConfig(ProviderConfig.class);
    static String url = config.getJdbcUrl(); 
    static String username = config.getJdbcUsername(); 
    static String password = config.getJdbcPassword(); 
    static Connection  conn = null;	 //定义一个MYSQL链接对象
    static ResultSet rs = null;
    static PreparedStatement ps =null;
    public static void init(){
        try {
            Class.forName("com.mysql.jdbc.Driver");	//MYSQL驱动
            conn = DriverManager.getConnection(url,username,password);
        } catch (Exception e) {
            System.out.println("init [SQL驱动程序初始化失败！]");
            e.printStackTrace();
        }
    }
    public static int addUpdDel(String sql){
        int i = 0;
        try {
            PreparedStatement ps =  conn.prepareStatement(sql);
            i =  ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("sql数据库增删改异常");
            e.printStackTrace();
        }
        
        return i;
    }
    public static ResultSet selectSql(String sql){
        try {
            ps =  conn.prepareStatement(sql);
            rs =  ps.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println("sql数据库查询异常");
            e.printStackTrace();
        }
        return rs;
    }
    public static void closeConn(){
        try {
            conn.close();
        } catch (SQLException e) {
            System.out.println("sql数据库关闭异常");
            e.printStackTrace();
        }
    }
}

