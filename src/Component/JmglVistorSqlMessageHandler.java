package Component;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.UUID;
import java.util.Vector;

import ui.ServerMain;
import config.DataPropMananger;
import data.VisitorRecord;




public class JmglVistorSqlMessageHandler {
	
	private static JmglVistorSqlMessageHandler instance;
	
	private static String DB_DRIVER = "com.mysql.jdbc.Driver";
	private static String DB_CONN = "jdbc:mysql://"+DataPropMananger.SQL_SERVER_IP+"/cts_sql?useUnicode=true&characterEncoding=UTF-8"; 
	private static String DB_USER = "root";
	private static String DB_PWD = "root";
	private static Connection conn;
	private static Statement stmt;
	
	public static synchronized JmglVistorSqlMessageHandler getInstance(){
		if(instance == null) instance = new JmglVistorSqlMessageHandler();
		return instance;
	}
	
	private JmglVistorSqlMessageHandler(){
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
	
	public synchronized Vector<VisitorRecord> getAllData(int i){
		Vector<VisitorRecord> visitorRecords = new Vector<VisitorRecord>();
		String sql = "";
		if(i == 0)
			sql = "select * from visitor_records order by in_time desc limit 30;";
		else 
			sql = "select * from visitor_records where starter = "+i+" OR starter = "+(i*10+1)+" order by in_time desc limit 30;";
		

		ResultSet rs = JmglVistorSqlMessageHandler.getInstance().getQuerrySqlRs(sql);
		System.out.print(sql + "\n");
		try {
			if(rs != null){
				while (rs.next()) {
					VisitorRecord vRecords = new VisitorRecord();
					vRecords.setUuid(rs.getString("uuid"));
					vRecords.setName(rs.getString("name"));
					vRecords.setNum(rs.getInt("num"));
					vRecords.setCompany(rs.getString("company"));
					vRecords.setIdType(rs.getString("id_type"));
					vRecords.setIdNum(rs.getString("id_num"));
					vRecords.setSt_reason(rs.getString("reason"));
					vRecords.setStarter(rs.getInt("starter"));
					vRecords.setWatherHW(rs.getBytes("watcher"));
					vRecords.setDoorHW(rs.getBytes("door_watcher"));
					vRecords.setOut_doorHW(rs.getBytes("out_door_watcher"));
					vRecords.setIn_time(rs.getString("in_time"));
					vRecords.setOut_time(rs.getString("out_time"));
					vRecords.setTools(rs.getString("tools"));
					vRecords.setMuti((rs.getInt("isMuti")==0)?false:true);
					vRecords.setFinish(rs.getBoolean("isFinish"));
					visitorRecords.add(0,vRecords);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return visitorRecords;
	}
	
	public boolean execInsertVisitorRecord(Vector<VisitorRecord> visitorRecords){
		boolean isSuccess = false;
		
		try {
			conn = DriverManager.getConnection(DB_CONN, DB_USER, DB_PWD);
			System.out.print("DB connecting ... \n");
			conn.setAutoCommit(false);
			
			String sql = "";
			sql = "Insert into visitor_records(uuid,name,num,id_type,id_num,company,reason,watcher,door_watcher,in_time,isMuti,starter,tools) values"
					+ " (?,?,?,?,?,?,?,?,?,?,?,?,?);";
			PreparedStatement ps = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);;
			
			Iterator<VisitorRecord> iterator = visitorRecords.iterator();
			while (iterator.hasNext()) {
				VisitorRecord cr = iterator.next();
				
				UUID uuid = UUID.randomUUID();
				
				ps.setString(1, uuid.toString());
				ps.setString(2, cr.getName());
				ps.setInt(3, cr.getNum());
				ps.setString(4, cr.getIdType());
				ps.setString(5, cr.getIdNum());
				ps.setString(6, cr.getCompany());
				ps.setString(7, cr.getSt_reason());
				ps.setBytes(8,cr.getWatherHW());
				ps.setBytes(9,cr.getDoorHW());
				ps.setTimestamp(10, new Timestamp(System.currentTimeMillis()));
				ps.setInt(11, cr.isMuti()?1:0);
				ps.setInt(12, cr.getStarter());
				ps.setString(13, cr.getTools());
				ps.addBatch();
				System.out.print(ps.toString());
			}
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
	
	public boolean isVisitorUpdateSuccess(VisitorRecord cr,int handler){
		 
		 boolean isSuccess = false;
			
			try {
				conn = DriverManager.getConnection(DB_CONN, DB_USER, DB_PWD);
				System.out.print("DB connecting ... \n");
				conn.setAutoCommit(false);
				
				String sql = "";
				sql = "update visitor_records set out_time = ?,out_door_watcher = ?,isFinish = 1 where uuid = '"+cr.getUuid()+"';";
				PreparedStatement ps = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);;
				
				ps.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
				ps.setBytes(2, cr.getOut_doorHW());
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
