package test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/*
--------------------------------------------------------
--  SQL CREATE Table TIMESTAMP_FORMAT
--------------------------------------------------------
DROP TABLE TIMESTAMP_FORMAT;
Create Table TIMESTAMP_FORMAT(			
  NAME                  VARCHAR2(50),     
  COL_DATE                  DATE,             
  COL_TIMESTAMP             TIMESTAMP      
);


Insert into TIMESTAMP_FORMAT( NAME ,COL_DATE , COL_TIMESTAMP )values
	('TO_TIMESTAMP����G',TO_DATE('2018-01-01 11:22:33','yyyy-mm-dd hh24:mi:ss'),TO_TIMESTAMP('2018-01-01 11:22:33','yyyy-mm-dd hh24:mi:ss'));
Insert into TIMESTAMP_FORMAT( NAME ,COL_DATE , COL_TIMESTAMP  )values
	('TO_TIMESTAMP������G',TO_DATE('2018-01-01 11:22','yyyy-mm-dd hh24:mi'),TO_TIMESTAMP('2018-01-01 11:22','yyyy-mm-dd hh24:mi'));
Insert into TIMESTAMP_FORMAT( NAME ,COL_DATE ,COL_TIMESTAMP  )values
	('CURRENT_TIMESTAMP���G',sysdate,CURRENT_TIMESTAMP);
Insert into TIMESTAMP_FORMAT( NAME ,COL_DATE ,COL_TIMESTAMP  )values
	('sysdate���G',sysdate,sysdate);

commit;

*/


public class TestTimestamp {
	String driver = "oracle.jdbc.driver.OracleDriver";
	String url = "jdbc:oracle:thin:@localhost:1521:XE";
	String userid = "CA103";
	String passwd = "123456";
	
	private static final String GET_ONE_STMT = 
			"SELECT NAME ,COL_DATE,COL_TIMESTAMP FROM TIMESTAMP_FORMAT WHERE NAME='TO_TIMESTAMP����G'";
	//�ۭq�ɶ���INSERT�y�y
	private static final String INSERT_STMT =
			"Insert into TIMESTAMP_FORMAT( NAME ,COL_DATE , COL_TIMESTAMP )"+
					"values(?,?,?)";
	//�t�ήɶ���INSERT�y�y
//	private static final String INSERT_STMT =
//			"Insert into TIMESTAMP_FORMAT( NAME ,COL_DATE , COL_TIMESTAMP )"+
//					"values(?,?,CURRENT_TIMESTAMP)";
	
	public void insert(TimestampVO timestampVO) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(INSERT_STMT);
				
			pstmt.setString(1,timestampVO.getName());
			pstmt.setDate(2, timestampVO.getCol_date());
			//�Y�Ψt�ήɶ���INSERT�y�y ��3�ӭn����
			pstmt.setTimestamp(3, timestampVO.getCol_timestamp());
			pstmt.executeUpdate();


		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. "
					+ e.getMessage());
		} catch (SQLException se) {
			se.printStackTrace();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}		
	}
	
	
	public List<TimestampVO> getAll() {
		List<TimestampVO> list=new ArrayList<>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(GET_ONE_STMT);
			rs = pstmt.executeQuery();

			while (rs.next()) {		
				TimestampVO timestampVO = new TimestampVO();
				timestampVO.setName(rs.getString("NAME"));
				timestampVO.setCol_date(rs.getDate("COL_DATE"));
				timestampVO.setCol_timestamp(rs.getTimestamp("COL_TIMESTAMP"));				
				list.add(timestampVO);
			}

		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. "
					+ e.getMessage());
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
		return list;
	}
	
	
	
	public static void main(String args[]) {		
		TestTimestamp dao=new TestTimestamp();
		//�s�W-���w�ɶ�
		TimestampVO timestampVO=new TimestampVO();
		timestampVO.setName("insert ���w�ɶ�");
		timestampVO.setCol_date(java.sql.Date.valueOf("2018-02-02"));
		timestampVO.setCol_timestamp(java.sql.Timestamp.valueOf("2018-03-03 13:35:57"));
		dao.insert(timestampVO);
		
		//�s�W-�t�ήɶ� (�Ψt�ήɶ���INSERT�y�y)
//		TimestampVO timestampVO1=new TimestampVO();
//		timestampVO1.setName("insert �t�ήɶ�");
//		timestampVO1.setCol_date(java.sql.Date.valueOf("2018-02-02"));
//		dao.insert(timestampVO1);
		
		
		
		//�d��
		List<TimestampVO> list=dao.getAll();
		for(TimestampVO tsVO:list) {
			System.out.println("========================"+tsVO.getName()+"========================");
			
			Date date=tsVO.getCol_date();
			System.out.println("tsVO.getCol_date() = "+date);
			System.out.println();
			
			System.out.println("---DATE SimpleDateFormat---");
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");		
			System.out.println(sdf.format(date));
			System.out.println();
			
			Timestamp ts= tsVO.getCol_timestamp();
			System.out.println("tsVO.getCol_timestamp() = "+ts);
			System.out.println();
			
			System.out.println("---TIMESTAMP SimpleDateFormat---");
			SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");		
			System.out.println("yyyy-MM-dd HH:mm:ss     = "+sdf1.format(ts));   
			SimpleDateFormat sdf2=new SimpleDateFormat("yyyy/MM/dd HH:mm");		
			System.out.println("yyyy-MM-dd HH:mm        = "+sdf2.format(ts));
			SimpleDateFormat sdf3=new SimpleDateFormat("yyyy�~MM��dd�� a hh��mm�� ");
			System.out.println("yyyy�~MM��dd�� a hh��mm�� = "+sdf3.format(ts));
			SimpleDateFormat sdf4=new SimpleDateFormat("yyyy�~ ");
			System.out.println("yyyy�~ = "+sdf4.format(ts));
			System.out.println();
			
			System.out.println("---printf---");
			System.out.printf("�ɶ� :%1$tY�~ %1$tm�� %1$td�� %1$tH�� %1$tM��%1$tS��  %n", ts);
			System.out.printf("�ɶ� :%1$tY�~ %1$tb %1$te��  %1$tp %1$tl��  %1$tM�� %1$tS��  %n", ts);
			System.out.println();
			
			System.out.println("---Calendar---");
			Calendar cal=new GregorianCalendar();
			cal.setTime(ts);
			int year=cal.get(Calendar.YEAR);
			int month=cal.get(Calendar.MONTH)+1;
			int day=cal.get(Calendar.DATE);
			int hour=cal.get(Calendar.HOUR_OF_DAY);
			int min=cal.get(Calendar.MINUTE);
			int second=cal.get(Calendar.SECOND);
			System.out.println(year+"�~"+month+"��"+day+"��"+hour+"��"+min+"��"+second+"��");
				
			System.out.println("\n\n");
		}
	}
	
	

}



class TimestampVO {
	private String name;
	private Date col_date;
	private Timestamp col_timestamp;
	public TimestampVO() {
		super();
	}
	public TimestampVO(String name, Date col_date, Timestamp col_timestamp) {
		super();
		this.name = name;
		this.col_date = col_date;
		this.col_timestamp = col_timestamp;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getCol_date() {
		return col_date;
	}
	public void setCol_date(Date col_date) {
		this.col_date = col_date;
	}
	public Timestamp getCol_timestamp() {
		return col_timestamp;
	}
	public void setCol_timestamp(Timestamp col_timestamp) {
		this.col_timestamp = col_timestamp;
	}
	
}
