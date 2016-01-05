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
import data.CriminalRecord;




public class JmglCriminalSqlMessageHandler {
	
	private static JmglCriminalSqlMessageHandler instance;
	
	private static String DB_DRIVER = "com.mysql.jdbc.Driver";
	private static String DB_CONN = "jdbc:mysql://"+DataPropMananger.SQL_SERVER_IP+"/cts_sql?useUnicode=true&characterEncoding=UTF-8"; 
	private static String DB_USER = "root";
	private static String DB_PWD = "root";
	private static Connection conn;
	private static Statement stmt;
	
	public static synchronized JmglCriminalSqlMessageHandler getInstance(){
		if(instance == null) instance = new JmglCriminalSqlMessageHandler();
		return instance;
	}
	
	private JmglCriminalSqlMessageHandler(){
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
	
	public boolean execInsertCriminalRecord(Vector<CriminalRecord> criminalRecords){
		boolean isSuccess = false;
		
		try {
			conn = DriverManager.getConnection(DB_CONN, DB_USER, DB_PWD);
			System.out.print("DB connecting ... \n");
			conn.setAutoCommit(false);
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			
			String sql = "";
			sql = "Insert into criminal_records(uuid,state,state_reason,destination,starter,other_place,w_watcher,w_dwatcher,rec_time) values (?,?,?,?,?,?,?,?,?);";
			PreparedStatement ps = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			
			Iterator<CriminalRecord> iterator = criminalRecords.iterator();
			while (iterator.hasNext()) {
				CriminalRecord cr = iterator.next();
				
				UUID uuid = UUID.randomUUID();
				
				
				String sql1 = "Insert into criminal_info set "
						+ "uuid = '" + uuid + "',name = '" + cr.getName() + "',num = " +cr.getNum() +",sex = " 
						+ cr.getSex() + ",company = " + cr.getCompany() +",isMuti = "+ (cr.isMuti()?1:0) +";";
				
				stmt.addBatch(sql1);
				
				ps.setString(1, uuid.toString());
				ps.setInt(2, cr.getState());
				ps.setString(3, cr.getSt_reason());
				ps.setInt(4, cr.getDestination());
				ps.setInt(5, cr.getStarter());
				ps.setString(6, cr.getOther_place());
				ps.setBytes(7,cr.getWatherHW());
				ps.setBytes(8,cr.getDoorHW());
				ps.setTimestamp(9, new Timestamp(System.currentTimeMillis()));
				ps.addBatch();
				System.out.print(ps.toString());
			}
			if(stmt.executeBatch().length != 0) isSuccess = (ps.executeBatch().length != 0);
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
	
	public boolean execInsertInOutRecord(CriminalRecord cr){
		boolean isSuccess = false;
		try {
			conn = DriverManager.getConnection(DB_CONN, DB_USER, DB_PWD);
			System.out.print("DB connecting ... \n");
			conn.setAutoCommit(false);

			if(cr.isMuti()){
				stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
				String sql1 = "update criminal_info set num = " +cr.getNum()+" where uuid = '" + cr.getUuid()+"';";
				
				stmt.addBatch(sql1);
			}
			
			String sql = "";
			sql = "Insert into criminal_records(uuid,state,state_reason,destination,starter,other_place,w_watcher,w_dwatcher,rec_time) values (?,?,?,?,?,?,?,?,?);";
			PreparedStatement ps = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);;
			ps.setString(1, cr.getUuid());
			ps.setInt(2, cr.getState());
			ps.setString(3, cr.getSt_reason());
			ps.setInt(4, cr.getDestination());
			ps.setInt(5, cr.getStarter());
			ps.setString(6, cr.getOther_place());
			ps.setBytes(7,cr.getWatherHW());
			ps.setBytes(8,cr.getDoorHW());
			ps.setTimestamp(9, new Timestamp(System.currentTimeMillis()));
			ps.addBatch();
			
			System.out.print(ps.toString());
			
			if(cr.isMuti()){
				if(stmt.executeBatch().length != 0) isSuccess = (ps.executeBatch().length != 0);
			}
			else isSuccess = (ps.executeBatch().length != 0);
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
	
	public boolean execInsertInOutRecord(Vector<CriminalRecord> criminalRecords){
		boolean isSuccess = false;
		try {
			conn = DriverManager.getConnection(DB_CONN, DB_USER, DB_PWD);
			System.out.print("DB connecting ... \n");
			conn.setAutoCommit(false);
			
			String sql = "";
			sql = "Insert into criminal_records(uuid,state,state_reason,destination,starter,other_place,w_watcher,w_dwatcher,rec_time) values (?,?,?,?,?,?,?,?,?);";
			PreparedStatement ps = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);;
			
			Iterator<CriminalRecord> iterator = criminalRecords.iterator();
			while(iterator.hasNext()){
				CriminalRecord cr = iterator.next();
				ps.setString(1, cr.getUuid());
				ps.setInt(2, cr.getState());
				ps.setString(3, cr.getSt_reason());
				ps.setInt(4, cr.getDestination());
				ps.setInt(5, cr.getStarter());
				ps.setString(6, cr.getOther_place());
				ps.setBytes(7,cr.getWatherHW());
				ps.setBytes(8,cr.getDoorHW());
				ps.setTimestamp(9, new Timestamp(System.currentTimeMillis()));
				ps.addBatch();
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
	
	public boolean isCriInfoUpdateSuccess(CriminalRecord cr,int handler){
		boolean isSuccess = false;
		String updateColString = "";
		String beforValue = "";
		String aftherValue = "";
		String updateSql = "";
		String insertSql = "";
		try {
			conn = DriverManager.getConnection(DB_CONN, DB_USER, DB_PWD);
			System.out.print("DB connecting ... \n");
			conn.setAutoCommit(false);
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			ResultSet rs = stmt.executeQuery("select * from criminal_info where uuid = '"+cr.getUuid()+"'");
			if(rs != null) rs.next();
			if(!cr.isMuti()){
				if(!rs.getString("name").trim().equals(cr.getName())){
					updateColString = "姓名变更(criminal_info.name)";
					beforValue = rs.getString("name").trim();
					aftherValue = cr.getName();
					updateSql = "update criminal_info set name = '"+cr.getName()+"' where uuid = '"+cr.getUuid()+"';";
					stmt.addBatch(updateSql);
				}
			}
			else{
				if(rs.getInt("num") != cr.getNum()){
					updateColString = "人数变更(criminal_info.num)";
					beforValue = Integer.toString(rs.getInt("num"));
					aftherValue = Integer.toString(cr.getNum());
					updateSql = "update criminal_info set num = "+cr.getNum()+" where uuid = '"+cr.getUuid()+"';";
					stmt.addBatch(updateSql);
				}
			}
			if(rs.getInt("company") != cr.getCompany()){
				updateColString = "所属地变更(criminal_info.company)";
				beforValue = Integer.toString(rs.getInt("company"));
				aftherValue = Integer.toString(cr.getCompany());
				updateSql = "update criminal_info set company = "+cr.getCompany()+" where uuid = '"+cr.getUuid()+"';";
				stmt.addBatch(updateSql);
			}
			else if(rs.getBoolean("isFinish") != cr.isFinish()){
				updateColString = "是否完成变更(criminal_info.isFinish)";
				beforValue = Integer.toString(rs.getInt("isFinish"));
				aftherValue = cr.isFinish()?"1":"0";
				updateSql = "update criminal_info set isFinish = "+ (cr.isFinish()?1:0) +" where uuid = '"+cr.getUuid()+"';";
				stmt.addBatch(updateSql);
			}
			else if(rs.getBoolean("isDel") != cr.isDel()){
				updateColString = "是否删除变更(criminal_info.isDel)";
				beforValue = Integer.toString(rs.getInt("isDel"));
				aftherValue = cr.isDel()?"1":"0";
				updateSql = "update criminal_info set isDel = "+ (cr.isDel()?1:0) +" where uuid = '"+cr.getUuid()+"';";
				stmt.addBatch(updateSql);
			}
			
			insertSql = "insert into criinfochange_log(changeCol,beforValue,afterValue,handler)values('"
					+updateColString+"','"+beforValue+"','"+aftherValue+"',"+handler+");";
			stmt.addBatch(insertSql);
			
			isSuccess = (stmt.executeBatch().length != 0);
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
	
	public synchronized Vector<CriminalRecord> getAllData(int company,int starter){
		Vector<CriminalRecord> criminalRecords = new Vector<CriminalRecord>();
		String sql = "select * from criminal_info,criminal_records where criminal_info.isDel = 0 and criminal_info.uuid = criminal_records.uuid";
		/*if(my_company !=0) sql = sql + "  and criminal_info.q_state = 0 and (criminal_info.company = " + my_company + " or criminal_records.destination = " + my_company
										+ " or criminal_records.starter = "+ my_company + " or criminal_records.send_to = " + my_company + ")";*/
		//查得到完成记录
		if(company != 0){
			if(starter < 10) starter = starter * 10 + 1;
			sql = sql + " AND criminal_info.uuid in (SELECT uuid from criminal_info where company = " + company +
								" UNION SELECT uuid from criminal_records where criminal_records.starter = "+ starter +
								" or criminal_records.destination = "+ company + " GROUP BY uuid)";
		}
		sql = sql + " ORDER BY criminal_records.rec_time;";

		ResultSet rs = JmglCriminalSqlMessageHandler.getInstance().getQuerrySqlRs(sql);
		System.out.print(sql + "\n");
		try {
			if(rs != null){
				while (rs.next()) {
					CriminalRecord cRecords = new CriminalRecord();
					cRecords.setUuid(rs.getString("uuid"));
					cRecords.setName(rs.getString("name"));
					cRecords.setNum(rs.getInt("num"));
					cRecords.setSex(rs.getInt("sex"));
					cRecords.setCompany(rs.getInt("company"));
					cRecords.setState(rs.getInt("state"));
					cRecords.setSt_reason(rs.getString("state_reason"));
					cRecords.setDestination(rs.getInt("destination"));
					cRecords.setStarter(rs.getInt("starter"));
					cRecords.setWatherHW(rs.getBytes("w_watcher"));
					cRecords.setDoorHW(rs.getBytes("w_dwatcher"));
					cRecords.setOther_place(rs.getString("other_place"));
					cRecords.setSt_time(rs.getString("rec_time"));
					cRecords.setMuti((rs.getInt("isMuti")==0)?false:true);
					cRecords.setFinish(rs.getBoolean("isFinish"));
					criminalRecords.add(cRecords);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return criminalRecords;
	}
	
	public void closeSqlHander(){
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
				stmt = null;
				conn = null;
			} catch (SQLException e) {
				System.out.print("MYSQL Close ERROR:" + e.getMessage());
			}
	}
}
