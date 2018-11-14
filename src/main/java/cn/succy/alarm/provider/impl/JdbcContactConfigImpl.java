package cn.succy.alarm.provider.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.succy.alarm.config.ConfigManager;
import cn.succy.alarm.config.ProviderConfig;

/**
 * 保存联系人配置实现类
 * @author ranzhonggeng
 *
 */
public class JdbcContactConfigImpl {
	private static final Logger logger = LoggerFactory.getLogger(JdbcContactProviderImpl.class);
	
	public static void main(String arg[]) {
        try {
            Connection con = null; //定义一个MYSQL链接对象
            Class.forName("com.mysql.jdbc.Driver").newInstance(); //MYSQL驱动
            ProviderConfig config = ConfigManager.getConfig(ProviderConfig.class);
            con = DriverManager.getConnection(config.getJdbcUrl(), config.getJdbcUsername(), config.getJdbcPassword()); //链接本地MYSQL
            System.out.print("get sql");
            
            Statement stmt; //创建声明
            stmt = con.createStatement();

            //新增一条数据
            
            //获取接口数据
//            stmt.executeUpdate("INSERT INTO contact(contact_id,name,email,phone,wechat) values(?,?,?,?,?)");
            //testing...
            stmt.executeUpdate("INSERT INTO contact(contact_id,name,email,phone,wechat) values('3','rzg','1445159429@qq.com','18382174181','RanZhongGeng')");
            ResultSet res = stmt.executeQuery("select LAST_INSERT_ID()");
            int ret_id;
            if (res.next()) {
                ret_id = res.getInt(1);
                System.out.print(ret_id);
            }
            
            System.out.print("insert sql success");
        } catch (Exception e) {
            System.out.print("MYSQL ERROR:" + e.getMessage());
        }

    }
	

    //创建Statement对象
//    Statement stmt = (Statement) conn.createStatement();
//    private ProviderConfig config = ConfigManager.getConfig(ProviderConfig.class);
//    private DataSource dataSource = new SimpleDataSource(config.getJdbcUrl(), config.getJdbcUsername(), config.getJdbcPassword());
//    private SqlRunner runner = SqlRunner.create(dataSource);
//  //创建Statement对象
//    Statement stmt = (Statement) dataSource.createStatement();
//    String sql="insert into contact(contact_id,name,email,phone,wechat) values(?,?,?,?,?)";
    //暴露接口
    
    
//    runner.execute(sql, );
}
