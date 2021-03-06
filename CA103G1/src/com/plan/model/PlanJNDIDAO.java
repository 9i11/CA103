package com.plan.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

//DAO與JNDIDAO相同，只差在類別名稱不同。
//plan_start_date  ,  plan_end_date  , plan_create_time  時間表示方法還未健全。
//沒有刪除方法，使用計畫名稱關鍵字找到該計畫之方法尚未完成。

public class PlanJNDIDAO implements PlanDAO_interface {

	private static DataSource ds = null;
	static {
		try {
			Context ctx = new InitialContext();
			ds = (DataSource) ctx.lookup("java:comp/env/jdbc/TestDB");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	private static final String INSERT_STMT = "INSERT INTO plan(plan_id,mem_id,plan_name,plan_vo,plan_cover,plan_start_date,plan_ens_date,sptype_id,plan_view,plan_privacy,plan_create_time,plan_status) VALUES (plan_seq.NEXTVAL,?,?,?,?,?,?,?,?,?,?,?)";

	// plan_start(end)_date 暫定可供修改。
	private static final String UPDATE = "UPDATE plan set plan_name=?, plan_vo=?, plan_cover=?,plan_start_date=? ,plan_end_date=? , sptype_id=?, plan_privacy=?, plan_status=? where plan_id = ? ";

	private static final String GET_ONE_STMT = "SELECT plan_id,mem_id,plan_name,plan_vo,plan_cover,to_char(plan_start_date,'yyyy-mm-dd')plan_start_date,to_char(plan_end_date,'yyyy-mm-dd')plan_end_date,sptype_id,plan_view,plan_privacy,to_char(plan_create_time,'yyyy-mm-dd')plan_create_time,plan_status FROM plan where plan_id = ?";

	// 自訂方法(尚未完成)
	private static final String GET_ONE_STMT2 = "SELECT plan_id,mem_id,plan_name,plan_vo,plan_cover,to_char(plan_start_date,'yyyy-mm-dd')plan_start_date,to_char(plan_end_date,'yyyy-mm-dd')plan_end_date,sptype_id,plan_view,plan_privacy,to_char(plan_create_time,'yyyy-mm-dd'),plan_create_time,plan_status FROM plan where plan_name =?";

	private static final String GET_ALL_STMT = "SELECT plan_id,mem_id,plan_name,plan_vo,plan_cover,to_char(plan_start_date ,'yyyy-mm-dd')plan_start_date,to_char(plan_end_date,'yyyy-mm-dd')plan_end_date,sptype_id,plan_view,plan_privacy,to_char(plan_create_time,'yyyy-mm-dd')plan_create_time,plan_status FROM plan order by plan_id ";

	@Override
	public void insert(PlanVO planVO) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_STMT);

			pstmt.setString(1, planVO.getPlan_id());
			pstmt.setString(2, planVO.getMem_id());
			pstmt.setString(3, planVO.getPlan_name());
			pstmt.setString(4, planVO.getPlan_vo());
			pstmt.setBytes(5, planVO.getPlan_cover());
			pstmt.setTimestamp(6, planVO.getPlan_start_date());
			pstmt.setTimestamp(7, planVO.getPlan_end_date());
			pstmt.setString(8, planVO.getSptype_id());
			pstmt.setInt(9, planVO.getPlan_view());
			pstmt.setString(10, planVO.getPlan_privacy());
			pstmt.setDate(11, planVO.getPlan_create_time());
			pstmt.setString(12, planVO.getPlan_status());

			pstmt.executeUpdate();
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace();
				}
			}

			if (con != null) {
				try {
					con.close();
				} catch (SQLException se) {
					se.printStackTrace();
				}
			}

		}
	}

	@Override
	public void update(PlanVO planVO) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(UPDATE);

			pstmt.setString(1, planVO.getPlan_name());
			pstmt.setString(2, planVO.getPlan_vo());
			pstmt.setBytes(3, planVO.getPlan_cover());
			pstmt.setTimestamp(4, planVO.getPlan_start_date());
			pstmt.setTimestamp(5, planVO.getPlan_end_date());
			pstmt.setString(6, planVO.getSptype_id());
			pstmt.setString(7, planVO.getPlan_privacy());
			pstmt.setString(8, planVO.getPlan_status());

			pstmt.executeUpdate();

		} catch (SQLException se) {
			throw new RuntimeException("A database error occured." + se.getMessage());
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace();
				}
			}

			if (con != null) {
				try {
					con.close();
				} catch (SQLException se) {
					se.printStackTrace();
				}
			}
		}

	}

	@Override
	public PlanVO findByPrimaryKey(String plan_id) {

		PlanVO planVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ONE_STMT);

			pstmt.setString(1, plan_id);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				planVO = new PlanVO();
				planVO.setPlan_id(rs.getString("plan_id"));
				planVO.setMem_id(rs.getString("mem_id"));
				planVO.setPlan_name(rs.getString("plan_name"));
				planVO.setPlan_vo(rs.getString("plan_vo"));
				planVO.setPlan_cover(rs.getBytes("plan_cover"));
				planVO.setPlan_start_date(rs.getTimestamp("plan_start_date"));
				planVO.setPlan_end_date(rs.getTimestamp("plan_end_date"));
				planVO.setSptype_id(rs.getString("sptype_id"));
				planVO.setPlan_view(rs.getInt("plan_view"));
				planVO.setPlan_privacy(rs.getString("plan_privacy"));
				planVO.setPlan_create_time(rs.getDate("plan_create_time"));
				planVO.setPlan_status(rs.getString("plan_status"));
			}

		} catch (SQLException se) {
			throw new RuntimeException("A database error occurred. " + se.getMessage());
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}

			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace();
				}
			}

			if (con != null) {
				try {
					con.close();
				} catch (SQLException se) {
					se.printStackTrace();
				}
			}
		}

		return planVO;
	}

	// 子串比對找出關鍵字，未完成。
	@Override
	public PlanVO findPlanByKeyWord(String plan_name) {

//		PlanVO planVO = null;
//		String keyWord = null;
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		
//		try {
//			con = ds.getConnection();
//			pstmt = con.prepareStatement(GET_ONE_STMT2);
//		
//			
//			
//		}catch(SQLException se) {
//			throw new RuntimeException("A database error occurred. " + se.getMessage());
//		}			
		return null;
	}

	@Override
	public List<PlanVO> getAll() {
		List<PlanVO> list = new ArrayList<PlanVO>();
		PlanVO planVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ALL_STMT);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				planVO = new PlanVO();
				planVO.setPlan_id(rs.getString("plan_id"));
				planVO.setMem_id(rs.getString("mem_id"));
				planVO.setPlan_name(rs.getString("plan_name"));
				planVO.setPlan_vo(rs.getString("plan_vo"));
				planVO.setPlan_cover(rs.getBytes("plan_cover"));
				planVO.setPlan_start_date(rs.getTimestamp("plan_start_date"));
				planVO.setPlan_end_date(rs.getTimestamp("plan_end_date"));
				planVO.setSptype_id(rs.getString("sptype_id"));
				planVO.setPlan_view(rs.getInt("plan_view"));
				planVO.setPlan_privacy(rs.getString("plan_privacy"));
				planVO.setPlan_create_time(rs.getDate("plan_creat_time"));
				planVO.setPlan_status(rs.getString("plan_status"));
				list.add(planVO);
			}

		} catch (SQLException se) {
			throw new RuntimeException("A database error occurred. " + se.getMessage());
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException se) {
					se.printStackTrace();
				}
			}

			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace();
				}
			}

			if (con != null) {
				try {
					con.close();
				} catch (SQLException se) {
					se.printStackTrace();
				}
			}

		}
		return list;
	}

}
