package ui;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Vector;

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;

import Component.JmglCarSqlMessageHandler;
import Component.JmglCriminalSqlMessageHandler;
import Component.JmglDutySqlMessageHandler;
import Component.JmglVistorSqlMessageHandler;


import Component.ZHZXTelSqlMessageHandler;
import config.DataPropMananger;
import data.DataPackage;

import org.eclipse.swt.widgets.Text;


public class ServerMain {

	protected Shell shlZhzxserver;
	private ServerSocket serverSocket;
	private ServerThread serverThread;
	private final static boolean isDebug = false;
	private static Text stateText;
	
	private int serverVer = 20160105;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			ServerMain window = new ServerMain();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shlZhzxserver.open();
		shlZhzxserver.layout();
		shlZhzxserver.addDisposeListener(new DisposeListener() {
			
			@Override
			public void widgetDisposed(DisposeEvent arg0) {
				// TODO Auto-generated method stub
				closeServer();
				System.exit(0);
			}
		});
		while (!shlZhzxserver.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlZhzxserver = new Shell(SWT.CLOSE);
		shlZhzxserver.setSize(450, 176);
		shlZhzxserver.setText("ZHZX_Server  " + serverVer);
		
		stateText = new Text(shlZhzxserver, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		stateText.setEditable(false);
		stateText.setBounds(10, 10, 415, 125);
		
		setLableText("服务器启动中...",this.getClass());
		
		try {
			serverStart(16003);
		} catch (BindException e) {
			// TODO Auto-generated catch block
			if(isDebug)e.printStackTrace();
			else setLableText(e.getMessage(),this.getClass());
		}
	}
	
	public static void setLableText(final String str,final Class cls){
		Display.getDefault().asyncExec(new Runnable() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				// TODO Auto-generated method stub
				stateText.append(new Timestamp(System.currentTimeMillis()).toLocaleString()+ str + " :: (" +cls.getName()+ ")\n");
			}
		});
	}
	
	private String getNetVersion(){
		
		String netVer = "1";
		
		BufferedReader br = null;
		InputStreamReader isr = null;
		InputStream is = null;
		URL url = null;
		try {
			url = new URL(DataPropMananger.WEB_URL);
			is = url.openStream();
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
			netVer = br.readLine();
		} catch (IOException e) {
			setLableText("获取版本号失败，请检查网页访问连接是否正常",getClass());
		}finally{
			try {
				br.close();
				isr.close();
				is.close();
				url = null;
			} catch (IOException e) {
				setLableText("关闭连接失败，请重启服务器",getClass());
			}
		}
		return netVer;
	}
	
	private void serverStart(int port) throws java.net.BindException {
		try {
			serverSocket = new ServerSocket(port);
			serverThread = new ServerThread(serverSocket);
			serverThread.start();
		} catch (BindException e) {
			throw new BindException("端口号已被占用，请换一个！");
		} catch (Exception e1) {
			e1.printStackTrace();
			throw new BindException("启动服务器异常！");
		}
		setLableText("服务器已启动...",getClass());
	}
	
	private void closeServer() {
		if (serverThread != null)
			serverThread.stop();
		
		if (serverSocket != null)
			try {
				serverSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				if(isDebug)e.printStackTrace();
				else setLableText(e.getMessage(),getClass());
			}// 关闭服务器端连接
	}
	
	class ServerThread extends Thread {
		private ServerSocket serverSocket;

		// 服务器线程的构造方法
		public ServerThread(ServerSocket serverSocket) {
			this.serverSocket = serverSocket;
		}

		public void run() {
			while (true) {// 不停的等待客户端的链接
				try {
					Socket socket = serverSocket.accept();
			
					ClientThread client = new ClientThread(socket);
					client.start();// 开启对此客户端服务的线程
					
				} catch (IOException e) {
					if(isDebug)e.printStackTrace();
					else setLableText(e.getMessage(),getClass());
				}
			}
		}
	}

	// 为一个客户端服务的线程
	class ClientThread extends Thread {
		private Socket socket;
		private ObjectInputStream reader;
		private ObjectOutputStream writer;

		public ObjectInputStream getReader() {
			return reader;
		}

		public ObjectOutputStream getWriter() {
			return writer;
		}

		// 客户端线程的构造方法
		public ClientThread(Socket socket) {
			try {
				this.socket = socket;
				reader = new ObjectInputStream(socket.getInputStream());
				writer = new ObjectOutputStream(socket.getOutputStream());
			} catch (IOException e) {
				if(isDebug)e.printStackTrace();
				else setLableText(e.getMessage(),getClass());
			}
		}

		
		public void run() {// 不断接收客户端的消息，进行处理。
			DataPackage data = null;
			while (true) {
				try {
					data = (DataPackage) reader.readObject();// 接收客户端消息
					System.out.print(data.getCmd() +"--"+data.getType());
					if (data.getCmd() == DataPropMananger.CMD_SYSTEM)// 下线命令
					{
						if(data.getType() == DataPropMananger.SYS_CLOSE) closeThread();
						
					} else if(data.getCmd() == DataPropMananger.CMD_JMGL){
						jmglDataHander(data);// 客户端消息处理
					} else if(data.getCmd() == DataPropMananger.CMD_ZHZX){
						ZhzxDataHandler(data);
					}
				} catch (IOException | ClassNotFoundException e) {
					if(isDebug)e.printStackTrace();
					else{
						setLableText(e.getMessage(),getClass());
						closeThread();
						return;
					}
				}
			}
		}
		
		public void closeThread(){
			
			// 断开连接释放资源
			try {
				JmglCriminalSqlMessageHandler.getInstance().closeSqlHander();
				this.reader.close();
				this.writer.close();
				this.socket.close();
				this.reader = null;
				this.writer = null;
				this.socket = null;
				this.stop();
				//this.destroy();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				if(isDebug)e.printStackTrace();
				else setLableText(e.getMessage(),getClass());
			}
			
		}

		// 转发消息
		public void jmglDataHander(DataPackage data) {
			if(!checkVersion(data)) return;
			int jmglType = data.getType() % 200;
			if(jmglType < 20)
			{
				switch(data.getType()){
				case DataPropMananger.CRIMINAL_ADD_RECORD:
					System.out.print("Server recieve CRIMINAL_ADD_RECORD cmd \n");
					if(JmglCriminalSqlMessageHandler.getInstance().execInsertCriminalRecord(data.getCriminalData())){
						sendDataToClient(data.getCmd(),DataPropMananger.CRIMINAL_ADD_RECORD, data.getClientNum(), JmglCriminalSqlMessageHandler.getInstance().getAllData(getCompanyNum(data.getClientNum()),data.getClientNum()));
					}
					break;
				case DataPropMananger.CRIMINAL_ADD_IN_OUT:
					System.out.print("Server recieve CRIMINAL_ADD_IN_OUT cmd \n");
					if(data.getDataSize() > 1){
						if(JmglCriminalSqlMessageHandler.getInstance().execInsertInOutRecord(data.getCriminalData())){
							sendDataToClient(data.getCmd(),DataPropMananger.CRIMINAL_ADD_IN_OUT, data.getClientNum(), JmglCriminalSqlMessageHandler.getInstance().getAllData(getCompanyNum(data.getClientNum()),data.getClientNum()));
						}
					}
					else{
						if(JmglCriminalSqlMessageHandler.getInstance().execInsertInOutRecord(data.getCriminalData().get(0))){
							sendDataToClient(data.getCmd(),DataPropMananger.CRIMINAL_ADD_IN_OUT, data.getClientNum(), JmglCriminalSqlMessageHandler.getInstance().getAllData(getCompanyNum(data.getClientNum()),data.getClientNum()));
						}
					}
					
					break;
				case DataPropMananger.CRIMINAL_CHANGE_RECORD:
					System.out.print("Server recieve CRIMINAL_CHANGE_RECORD cmd \n");
					if(JmglCriminalSqlMessageHandler.getInstance().isCriInfoUpdateSuccess(data.getCriminalData().get(0),data.getClientNum())){
						sendDataToClient(data.getCmd(),DataPropMananger.CRIMINAL_CHANGE_RECORD, data.getClientNum(), JmglCriminalSqlMessageHandler.getInstance().getAllData(getCompanyNum(data.getClientNum()),data.getClientNum()));
					}
					break;
				case DataPropMananger.CRIMINAL_UPDATE:
					System.out.print("Server recieve CRIMINAL_UPDATE cmd \n");
					sendDataToClient(data.getCmd(),DataPropMananger.CRIMINAL_UPDATE,data.getClientNum(),JmglCriminalSqlMessageHandler.getInstance().getAllData(getCompanyNum(data.getClientNum()),data.getClientNum()));
					break;
				default:
					System.out.print("Server recieve a message:" + data + " \n");
					break;
				}
			}
			else if(jmglType > 20 && jmglType < 30){
				switch(data.getType()){
				case DataPropMananger.VISITOR_ADD_RECORD:
					System.out.print("Server recieve VISITOR_ADD_RECORD cmd \n");
					if(JmglVistorSqlMessageHandler.getInstance().execInsertVisitorRecord(data.getVisitorData())){
						sendDataToClient(data.getCmd(),DataPropMananger.VISITOR_ADD_RECORD, data.getClientNum(), JmglVistorSqlMessageHandler.getInstance().getAllData(data.getClientNum()));
					}
					break;
				case DataPropMananger.VISITOR_CHANGE_RECORD:
					System.out.print("Server recieve VISITOR_CHANGE_RECORD cmd \n");
					if(JmglVistorSqlMessageHandler.getInstance().isVisitorUpdateSuccess(data.getVisitorData().get(0),data.getClientNum())){
						sendDataToClient(data.getCmd(),DataPropMananger.VISITOR_CHANGE_RECORD, data.getClientNum(), JmglVistorSqlMessageHandler.getInstance().getAllData(data.getClientNum()));
					}
					break;
				case DataPropMananger.VISITOR_UPDATE:
					System.out.print("Server recieve CRIMINAL_UPDATE cmd \n");
					sendDataToClient(data.getCmd(),DataPropMananger.VISITOR_UPDATE,data.getClientNum(),JmglVistorSqlMessageHandler.getInstance().getAllData(data.getClientNum()));
					break;
				default:
					System.out.print("Server recieve a message:" + data + " \n");
					break;
				}
			}
			else if(jmglType >30 && jmglType < 40){
				switch(data.getType()){
				case DataPropMananger.CAR_ADD_RECORD:
					System.out.print("Server recieve CAR_ADD_RECORD cmd \n");
					if(JmglCarSqlMessageHandler.getInstance().execInsertCarRecord(data.getCarData())){
						sendDataToClient(data.getCmd(),DataPropMananger.CAR_ADD_RECORD, data.getClientNum(), JmglCarSqlMessageHandler.getInstance().getAllData(data.getClientNum()));
					}
					break;
				case DataPropMananger.CAR_CHANGE_RECORD:
					System.out.print("Server recieve VISITOR_CHANGE_RECORD cmd \n");
					if(JmglCarSqlMessageHandler.getInstance().isCarUpdateSuccess(data.getCarData().get(0),data.getClientNum())){
						sendDataToClient(data.getCmd(),DataPropMananger.CAR_CHANGE_RECORD, data.getClientNum(), JmglCarSqlMessageHandler.getInstance().getAllData(data.getClientNum()));
					}
					break;
				case DataPropMananger.CAR_UPDATE:
					System.out.print("Server recieve CRIMINAL_UPDATE cmd \n");
					sendDataToClient(data.getCmd(),DataPropMananger.CAR_UPDATE,data.getClientNum(),JmglCarSqlMessageHandler.getInstance().getAllData(data.getClientNum()));
					break;
				default:
					System.out.print("Server recieve a message:" + data + " \n");
					break;
				}
			}
			else if(jmglType >40){
				switch(data.getType()){
				case DataPropMananger.JM_DUTY_GET_CURRENT:
					System.out.print("Server recieve JM_DUTY_GET_CURRENT cmd \n");
					sendDataToClient(data.getCmd(),DataPropMananger.JM_DUTY_GET_CURRENT, data.getClientNum(), JmglDutySqlMessageHandler.getInstance().getCurrentData(data.getClientNum()));
					break;
				case DataPropMananger.JM_DUTY_CHANGE_DUTY:
					System.out.print("Server recieve JM_DUTY_CHANGE_DUTY cmd \n");
					if(JmglDutySqlMessageHandler.getInstance().isDutyChangeSuccess(data.getJmDutyData())){
						sendDataToClient(data.getCmd(),DataPropMananger.JM_DUTY_CHANGE_DUTY, data.getClientNum(), JmglDutySqlMessageHandler.getInstance().getCurrentData(data.getClientNum()));
					}
					break;
				case DataPropMananger.JM_DUTY_CHANGE_INFO:
					System.out.print("Server recieve JM_DUTY_CHANGE_INFO cmd \n");
					if(JmglDutySqlMessageHandler.getInstance().isDutyInfoChangeSuccess(data.getJmDutyData().get(0))){
						sendDataToClient(data.getCmd(),DataPropMananger.JM_DUTY_CHANGE_INFO, data.getClientNum(), null);
					}
					break;
				case DataPropMananger.JM_DUTY_CHANGE_NOTICE:
					System.out.print("Server recieve JM_DUTY_CHANGE_NOTICE cmd \n");
					if(JmglDutySqlMessageHandler.getInstance().isDutyNoticeChangeSuccess(data.getJmDutyData().get(0))){
						sendDataToClient(data.getCmd(),DataPropMananger.JM_DUTY_CHANGE_NOTICE, data.getClientNum(),null);
					}
					break;
				case DataPropMananger.JM_DUTY_CHANGE_SAFE:
					System.out.print("Server recieve JM_DUTY_CHANGE_SAFE cmd \n");
					if(JmglDutySqlMessageHandler.getInstance().isDutySafeChangeSuccess(data.getJmDutyData().get(0))){
						sendDataToClient(data.getCmd(),DataPropMananger.JM_DUTY_CHANGE_SAFE, data.getClientNum(), null);
					}
					break;
				case DataPropMananger.JM_DUTY_GET_HISTORY:
					System.out.print("Server recieve JM_DUTY_GET_HISTORY cmd \n");
					sendDataToClient(data.getCmd(),DataPropMananger.JM_DUTY_GET_HISTORY, data.getClientNum(), JmglDutySqlMessageHandler.getInstance().getHistoryData(data.getJmDutyData().get(0)));	
					break;
				default:
					System.out.print("Server recieve a message:" + data + " \n");
					break;
				}
			}
		}
		
		private void ZhzxDataHandler(DataPackage data){
			if(!checkVersion(data)) return;
			int zhzxType = data.getType() % 300;
			if(zhzxType < 20){
				switch (data.getType()) {
				case DataPropMananger.ZHZX_TEL_GET_ALL:
					System.out.print("Server recieve ZHZX_TEL_GET_ALL cmd \n");
					sendDataToClient(data.getCmd(),DataPropMananger.ZHZX_TEL_GET_ALL, data.getClientNum(), ZHZXTelSqlMessageHandler.getInstance().getAllData(data.getTelData().get(0)));
					break;
				case DataPropMananger.ZHZX_TEL_ADD:
					System.out.print("Server recieve ZHZX_TEL_ADD cmd \n");
					if(ZHZXTelSqlMessageHandler.getInstance().execInsertTelRecord(data.getTelData().get(0))){
						sendDataToClient(data.getCmd(),DataPropMananger.ZHZX_TEL_ADD, data.getClientNum(), null);
					}
					break;
				case DataPropMananger.ZHZX_TEL_UPDATE_NEXT:
					System.out.print("Server recieve ZHZX_TEL_UPDATE_NEXT cmd \n");
					if(ZHZXTelSqlMessageHandler.getInstance().isUpdateNextInfoSuccess(data.getTelData().get(0))){
						sendDataToClient(data.getCmd(),DataPropMananger.ZHZX_TEL_UPDATE_NEXT, data.getClientNum(), null);
					}
					break;
				case DataPropMananger.ZHZX_TEL_UPDATE_BACK:
					System.out.print("Server recieve ZHZX_TEL_UPDATE_BACK cmd \n");
					if(ZHZXTelSqlMessageHandler.getInstance().isUpdateBackInfoSuccess(data.getTelData().get(0))){
						sendDataToClient(data.getCmd(),DataPropMananger.ZHZX_TEL_UPDATE_BACK, data.getClientNum(), null);
					}
					break;
				case DataPropMananger.ZHZX_TEL_DEL:
					System.out.print("Server recieve ZHZX_TEL_DEL cmd \n");
					if(ZHZXTelSqlMessageHandler.getInstance().isDelRecordSuccess(data.getTelData().get(0))){
						sendDataToClient(data.getCmd(),DataPropMananger.ZHZX_TEL_DEL, data.getClientNum(), null);
					}
					break;
				default:
					System.out.print("Server recieve a message:" + data + " \n");
					break;
				}
			}
		}
		
		private boolean checkVersion(DataPackage data){
			if(!data.getVersion().trim().equals(getNetVersion().trim())){
				setLableText("错误的客户端版本链接！" +data.getClientNum() +" : " + socket.getInetAddress() + " \r\n",getClass());
				sendDataToClient(DataPropMananger.CMD_SYSTEM,DataPropMananger.SYS_CLIENT_ERROR, data.getClientNum(), null);
				return false;
			}
			return true;
		}
			
		private void sendDataToClient(int cmd,int type,int cNum, Vector<?> cRecords){
			if(writer != null){
				try {
					System.out.print("Server send a message:" + cmd +" :: "+type + " \n");
					writer.writeObject(new DataPackage(getNetVersion(),cmd,type,cNum, cRecords));
					writer.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					if(isDebug)e.printStackTrace();
					else setLableText(e.getMessage(),getClass());
				}
			}
		}
		
		private int getCompanyNum(int num){
			int reNum = num;
			if(reNum > 10) reNum = num /10;
			return reNum;
		}
		
	}

}
