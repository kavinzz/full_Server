package Component;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;
import java.util.Vector;

import ui.ServerMain;

import com.ibm.icu.text.SimpleDateFormat;

import config.DataPropMananger;
import data.JmglDutyRecord;




public class JmglDutySqlMessageHandler {
	
	private static JmglDutySqlMessageHandler instance;
	
	private static String DB_DRIVER = "com.mysql.jdbc.Driver";
	private static String DB_CONN = "jdbc:mysql://"+DataPropMananger.SQL_SERVER_IP+"/cts_sql?useUnicode=true&characterEncoding=UTF-8"; 
	private static String DB_USER = "root";
	private static String DB_PWD = "root";
	private static Connection conn;
	private static Statement stmt;
	
	public static synchronized JmglDutySqlMessageHandler getInstance(){
		if(instance == null) instance = new JmglDutySqlMessageHandler();
		return instance;
	}
	
	private JmglDutySqlMessageHandler(){
		try {
			Class.forName(DB_DRIVER).newInstance();
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException e) {
			ServerMain.setLableText("MYSQL Initallizing ERROR:" + e.getMessage(),getClass());
		}
	}
	
	public ResultSet getQuerrySqlRs(String sql){
		ResultSet rs = null;
		try{
			conn = DriverManager.getConnection(DB_CONN, DB_USER, DB_PWD);
			System.out.print("DB connecting ... \n");
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			rs = stmt.executeQuery(sql);
		}
		catch(Exception e){
			ServerMain.setLableText("MYSQL Querry ERROR:" + e.getMessage(),getClass());
		}
		return rs;
	}
	
	public boolean isSingleExecSqlSuccess(String sql){
		boolean isSuccess = false;
		
		try {
			conn = DriverManager.getConnection(DB_CONN, DB_USER, DB_PWD);
			System.out.print("DB connecting ... \n");
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			isSuccess = stmt.execute(sql);
		} catch (SQLException e) {
			ServerMain.setLableText("MYSQL SingleExec ERROR:" + e.getMessage(),getClass());
		}
		
		return isSuccess;
	}
	
	public boolean isMutiExecSqlSuccess(String[] sql){
		boolean isSuccess = false;
		
		try {
			conn = DriverManager.getConnection(DB_CONN, DB_USER, DB_PWD);
			System.out.print("DB connecting ... \n");
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			for(int i = 0;i<sql.length;i++){
				stmt.addBatch(sql[i]);
			}
			isSuccess = (stmt.executeBatch().length != 0);
		} catch (SQLException e) {
			ServerMain.setLableText("MYSQL SingleExec ERROR:" + e.getMessage(),getClass());
		}
		
		return isSuccess;
	}
	
	public synchronized Vector<JmglDutyRecord> getCurrentData(int i){
		Vector<JmglDutyRecord> jmglDutyRecords = new Vector<JmglDutyRecord>();
		JmglDutyRecord dRecords = new JmglDutyRecord();
		String sql = "";
		if(i == 0)
			sql = "select * from duty_jmgl_info order by duty_jmgl_info.start_time desc limit 1;";
		else 
		{
			if(i < 10) i = i * 10 +1;
			sql = "select * from duty_jmgl_info where starter = " + i + " order by duty_jmgl_info.start_time desc limit 1;";
		}
		

		ResultSet rs = JmglDutySqlMessageHandler.getInstance().getQuerrySqlRs(sql);
		System.out.print(sql + "\n");
		try {
			if(rs != null){
				while (rs.next()) {
					dRecords.setUuid(rs.getString("uuid"));
					dRecords.setName(rs.getString("name"));
					dRecords.setStartTime(rs.getString("start_time"));
					dRecords.setInfo(rs.getString("info"));
					dRecords.setEqpOk(rs.getBoolean("eqp_state"));
					dRecords.setEqpInfo(rs.getString("eqp_info"));
					dRecords.setSafeMenNum(rs.getInt("safe_men_num"));
					dRecords.setSafeCarNum(rs.getInt("safe_car_num"));
					dRecords.setClean(rs.getBoolean("isClean"));
					dRecords.setStarter(rs.getInt("starter"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		dRecords.setNotice(getDutyNotice(dRecords.getStarter()));
		
		jmglDutyRecords.add(dRecords);
		
		return jmglDutyRecords;
	}
	
	private String getDutyNotice(int id){
		String info = null;
		String sql = "select * from duty_jmgl_notice where id = " + id + ";";
		

		ResultSet rs2 = JmglDutySqlMessageHandler.getInstance().getQuerrySqlRs(sql);
		System.out.print(sql + "\n");
		
		try {
			if(rs2 != null){
				while (rs2.next()) {
					
					info = rs2.getString("info");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return info;
	}
	
	public boolean isDutyChangeSuccess(Vector<JmglDutyRecord> jmglDutyRecords){
		 if(jmglDutyRecords.size() < 2) return false;
		 boolean isSuccess = false;
			
			try {
				conn = DriverManager.getConnection(DB_CONN, DB_USER, DB_PWD);
				System.out.print("DB connecting ... \n");
				conn.setAutoCommit(false);
				JmglDutyRecord newRecord = jmglDutyRecords.get(0);
				JmglDutyRecord oDutyRecord = jmglDutyRecords.get(1);
				
				String sql1 = "";
				sql1 = "update duty_jmgl_info set next_name = ?,eqp_state = ?,eqp_info = ?,end_time = ?,isClean = ?,isFinish = 1 where uuid = '"+oDutyRecord.getUuid()+"';";
				PreparedStatement ps1 = conn.prepareStatement(sql1,ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);;
				
				
				ps1.setString(1, oDutyRecord.getNextName());
				ps1.setInt(2, oDutyRecord.isEqpOk()?1:0);
				ps1.setString(3, oDutyRecord.getEqpInfo());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
				String time = sdf.format(System.currentTimeMillis());
				ps1.setString(4, time);
				ps1.setInt(5,oDutyRecord.isClean()?1:0);
				ps1.addBatch();
				System.out.print(ps1.toString());
				
				String sql2 = "";
				sql2 = "insert into duty_jmgl_info(uuid,name,start_time,starter,eqp_state,eqp_info) values (?,?,?,?,?,?);";
				
				PreparedStatement ps2 = conn.prepareStatement(sql2,ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);;
				
				UUID uuid = UUID.randomUUID();
				ps2.setString(1, uuid.toString());
				ps2.setString(2, newRecord.getName());
				String time1 = sdf.format(System.currentTimeMillis());
				ps2.setString(3, time1);
				ps2.setInt(4,newRecord.getStarter());
				ps2.setInt(5, oDutyRecord.isEqpOk()?1:0);
				ps2.setString(6, oDutyRecord.getEqpInfo());
				ps2.addBatch();
				System.out.print(ps2.toString());
				
				if(ps1.executeBatch().length != 0) isSuccess = (ps2.executeBatch().length != 0);
				if(isSuccess) conn.commit();
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				try{  
		            //提交失败，执行回滚操作  
		            conn.rollback();  
		  
		        }catch (SQLException ex) {  
		        	e.printStackTrace();  
		            ServerMain.setLableText("复杂插入回滚执行失败!!!",getClass());  
		        }  
		        e.printStackTrace();  
		        ServerMain.setLableText("复杂插入执行失败",getClass());  
		  
		        //插入失败返回标志0  
		        return false;
			}finally{
				try{   
		            if(conn != null)conn.close();  
		              
		        }catch (SQLException e) {  
		            e.printStackTrace();  
		            ServerMain.setLableText("资源关闭失败!!!",getClass());  
		        }  
			}
			
			return isSuccess;
	}
	
	public boolean isDutyInfoChangeSuccess(JmglDutyRecord jmglDutyRecord){
		if(jmglDutyRecord == null) return false;
		
		String sql1 = "";
		sql1 = "update duty_jmgl_info set info = '"+jmglDutyRecord.getInfo()+"' where uuid = '"+jmglDutyRecord.getUuid()+"';";
				
		return !isSingleExecSqlSuccess(sql1);
	}
	
	public boolean isDutyNoticeChangeSuccess(JmglDutyRecord jmglDutyRecord){
		if(jmglDutyRecord == null) return false;
		
		String sql1 = "";
		sql1 = "update duty_jmgl_notice set info = '"+jmglDutyRecord.getNotice()+"' where id = '"+jmglDutyRecord.getStarter()+"';";
				
		return !isSingleExecSqlSuccess(sql1);
	}
	
	public boolean isDutySafeChangeSuccess(JmglDutyRecord jmglDutyRecord){
		if(jmglDutyRecord == null) return false;
		
		String sql1 = "";
		sql1 = "update duty_jmgl_info set safe_men_num = " +jmglDutyRecord.getSafeMenNum()+",safe_car_num = "+jmglDutyRecord.getSafeCarNum() 
				+" where uuid = '"+jmglDutyRecord.getUuid()+"';";
				
		return !isSingleExecSqlSuccess(sql1);
	}
	
	public synchronized Vector<JmglDutyRecord> getHistoryData(JmglDutyRecord dutyRecord){
		
		
		Vector<JmglDutyRecord> jmglDutyRecords = new Vector<JmglDutyRecord>();
		JmglDutyRecord dRecords = new JmglDutyRecord();
		int i = dutyRecord.getStarter();
		if(i < 10 && i != 0) i = i *10 + 1;
		String sql = "";
		if(i == 0) sql = "select * from duty_jmgl_info where st_time>timestamp(?) and st_time<timestamp(?) and isFinish = 1 order by duty_jmgl_info.st_time desc;";
		else sql = "select * from duty_jmgl_info where starter = " + i + " and st_time>timestamp(?) and st_time<timestamp(?) and isFinish = 1 order by duty_jmgl_info.st_time desc;";
		
		try {
			
			conn = DriverManager.getConnection(DB_CONN, DB_USER, DB_PWD);
			System.out.print("DB connecting ... \n");
			
			PreparedStatement ps = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			ps.setLong(1, Long.parseLong(dutyRecord.getStartTime()));
			ps.setLong(2, Long.parseLong(dutyRecord.getEndTime()));
			ps.addBatch();
			System.out.print(ps.toString() + "\n");
			ResultSet rs = ps.executeQuery();
			
			if(rs != null){
				while (rs.next()) {
					dRecords.setUuid(rs.getString("uuid"));
					dRecords.setName(rs.getString("name"));
					dRecords.setStartTime(rs.getString("start_time"));
					dRecords.setEndTime(rs.getString("end_time"));
					dRecords.setInfo(rs.getString("info"));
					dRecords.setEqpOk(rs.getBoolean("eqp_state"));
					dRecords.setEqpInfo(rs.getString("eqp_info"));
					dRecords.setSafeMenNum(rs.getInt("safe_men_num"));
					dRecords.setSafeCarNum(rs.getInt("safe_car_num"));
					dRecords.setClean(rs.getBoolean("isClean"));
					dRecords.setStarter(rs.getInt("starter"));
					jmglDutyRecords.add(dRecords);
				}
			}
			
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return jmglDutyRecords;
	}
	
	public void closeSqlHander(){
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
				stmt = null;
				conn = null;
			} catch (SQLException e) {
				ServerMain.setLableText("MYSQL Close ERROR:" + e.getMessage(),getClass());
			}
	}
}
