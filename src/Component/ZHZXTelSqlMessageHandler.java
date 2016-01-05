package Component;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import ui.ServerMain;
import config.DataPropMananger;
import data.TelRecord;




public class ZHZXTelSqlMessageHandler {
	
	private static ZHZXTelSqlMessageHandler instance;
	
	private static String DB_DRIVER = "com.mysql.jdbc.Driver";
	private static String DB_CONN = "jdbc:mysql://"+DataPropMananger.SQL_SERVER_IP+"/cts_sql?useUnicode=true&characterEncoding=UTF-8"; 
	private static String DB_USER = "root";
	private static String DB_PWD = "root";
	private static Connection conn;
	private static Statement stmt;
	
	public static synchronized ZHZXTelSqlMessageHandler getInstance(){
		if(instance == null) instance = new ZHZXTelSqlMessageHandler();
		return instance;
	}
	
	private ZHZXTelSqlMessageHandler(){
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
			System.out.print(sql +" \n");
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
	
	public synchronized Vector<TelRecord> getAllData(TelRecord telRecord){
		Vector<TelRecord> TelRecords = new Vector<TelRecord>();
		String sql = "select * from tel_records where starter = "+ telRecord.getStarter() +" and isDel = 0 order by st_time desc limit " + telRecord.getLines() + ";";
		

		ResultSet rs = ZHZXTelSqlMessageHandler.getInstance().getQuerrySqlRs(sql);
		System.out.print(sql + "\n");
		try {
			if(rs != null){
				while (rs.next()) {
					TelRecord tRecords = new TelRecord();
					tRecords.setName(rs.getString("name"));
					tRecords.setNumber(rs.getString("number"));
					tRecords.setCompany(rs.getString("company"));
					tRecords.setSt_reason(rs.getString("reason"));
					tRecords.setStarter(rs.getInt("starter"));
					tRecords.setTime(rs.getString("time"));
					tRecords.setDuty_index(rs.getInt("duty_index"));
					tRecords.setNext_info(rs.getString("next_info"));
					tRecords.setBack_info(rs.getString("back_info"));
					tRecords.setSt_time(rs.getTimestamp("st_time"));
					TelRecords.add(tRecords);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return TelRecords;
	}
	
	public boolean execInsertTelRecord(TelRecord cr){
		boolean isSuccess = false;
		
		try {
			conn = DriverManager.getConnection(DB_CONN, DB_USER, DB_PWD);
			System.out.print("DB connecting ... \n");
			conn.setAutoCommit(false);
			
			String sql = "";
			sql = "Insert into tel_records(time,name,number,company,reason,duty_index,starter) values"
					+ " (?,?,?,?,?,?,?);";
			PreparedStatement ps = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);;
				
			ps.setString(1, cr.getTime());
			ps.setString(2, cr.getName());
			ps.setString(3, cr.getNumber());
			ps.setString(4, cr.getCompany());
			ps.setString(5, cr.getSt_reason());
			ps.setInt(6, cr.getDuty_index());
			ps.setInt(7, cr.getStarter());
			ps.addBatch();
			System.out.print(ps.toString());
			
			isSuccess = (ps.executeBatch().length != 0);
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
	
	public boolean isUpdateNextInfoSuccess(TelRecord telRecord){
		String sqlString = "update tel_records set next_info = '" +telRecord.getNext_info()+"' where starter = "+telRecord.getStarter()+" and st_time = '" +telRecord.getSt_time()+ "';";
		return !isSingleExecSqlSuccess(sqlString);
	}
	
	public boolean isUpdateBackInfoSuccess(TelRecord telRecord){
		String sqlString = "update tel_records set back_info = '" +telRecord.getBack_info()+"' where starter = "+telRecord.getStarter()+" and st_time = '" +telRecord.getSt_time()+ "';";
		return !isSingleExecSqlSuccess(sqlString);
	}
	
	public boolean isDelRecordSuccess(TelRecord telRecord){
		String sqlString = "update tel_records set isDel = 1 where starter = "+telRecord.getStarter()+ " and st_time = '"+telRecord.getSt_time()+"';";
		return !isSingleExecSqlSuccess(sqlString);
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
