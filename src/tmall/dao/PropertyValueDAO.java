package tmall.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import tmall.util.DBUtil;
import tmall.bean.Product;
import tmall.bean.Property;
import tmall.bean.PropertyValue;

public class PropertyValueDAO {
	//获取属性值条数
	public int getTotal() {
		int total = 0 ;
		try (Connection c = DBUtil.getConnection();Statement s = c.createStatement();){
			String sql = "select count(*) from PropertyValue";
			
			ResultSet rs = s.executeQuery(sql);
			while(rs.next()) {
				total = rs.getInt(1);
			}
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return total;
	}
	
	//添加属性值
	public void add(PropertyValue bean) {
		String sql = "insert into propertyvalue values(null,?,?,?)";
		try (Connection c = DBUtil.getConnection();PreparedStatement ps = c.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);){
			ps.setInt(1, bean.getProduct().getId());
			ps.setInt(2, bean.getProperty().getId());
			ps.setString(3, bean.getValue());
			ps.execute();
			
			ResultSet rs = ps.getGeneratedKeys();
			while(rs.next()) {
				int id  = rs.getInt(1);
				bean.setId(id);
			}
		} catch (SQLException e) {
			// TODO: handle exception
		}
	}
	
	//更新属性信息
	public void update(PropertyValue bean) {
		String sql = "update propertyvalue set pid= ?,ptid= ?,value= ? where id = ?";
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);){
			ps.setInt(1, bean.getProduct().getId());
			ps.setInt(2, bean.getProperty().getId());
			ps.setString(3, bean.getValue());
			ps.setInt(4, bean.getId());
			ps.execute();
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	//删除属性信息
	
	public void delete(int id) {
		try (Connection c = DBUtil.getConnection();Statement s = c.createStatement();){
			String sql = "delete from propertyvalue where id = " + id;
			s.execute(sql);
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	//根据id 获取属性值信息
	public PropertyValue get(int id) {
		PropertyValue bean = new PropertyValue();
		
		try (Connection c = DBUtil.getConnection();Statement s = c.createStatement();){
			String sql = "select * from PropertyValue where id = " + id;
			
			ResultSet rs = s.executeQuery(sql);
			
			while(rs.next()) {
				int pid = rs.getInt("pid");
				int ptid = rs.getInt("ptid");
				String value = rs.getString("value");
				
				Product product = new ProductDAO().get(pid);
				Property property = new PropertyDAO().get(ptid);
				bean.setProduct(product);
				bean.setProperty(property);
				bean.setValue(value);
				bean.setId(id);
				
				
			}
		} catch (SQLException e) {
			// TODO: handle exception
		}
		return bean;
	}
	//据属性id和产品id，获取一个PropertyValue对象
	public PropertyValue get(int ptid , int pid) {
		PropertyValue bean = null;
		try (Connection c = DBUtil.getConnection();Statement s = c.createStatement();){
			String sql = "select * from propertyvalue where ptid = " + ptid + " and pid = " + pid;
			
			ResultSet rs = s.executeQuery(sql);
			
			while(rs.next()) {
				bean = new PropertyValue();
				int id = rs.getInt("id");
				String value = rs.getString("value");
				
				 Product product = new ProductDAO().get(pid);
	             Property property = new PropertyDAO().get(ptid);
	             bean.setProduct(product);
	             bean.setProperty(property);
	             bean.setValue(value);
	             bean.setId(id);
				
			}
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return bean;
	}
	
	//调用下面查询方法
	 public List<PropertyValue> list() {
	        return list(0, Short.MAX_VALUE);
	    }
	 
	 public List<PropertyValue> list(int start, int count) {
	        List<PropertyValue> beans = new ArrayList<PropertyValue>();
	  
	        String sql = "select * from propertyvalue order by id desc limit ?,? ";
	  
	        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
	  
	            ps.setInt(1, start);
	            ps.setInt(2, count);
	  
	            ResultSet rs = ps.executeQuery();
	  
	            while (rs.next()) {
	                PropertyValue bean = new PropertyValue();
	                int id = rs.getInt(1);
	 
	                int pid = rs.getInt("pid");
	                int ptid = rs.getInt("ptid");
	                String value = rs.getString("value");
	                 
	                Product product = new ProductDAO().get(pid);
	                Property property = new PropertyDAO().get(ptid);
	                bean.setProduct(product);
	                bean.setProperty(property);
	                bean.setValue(value);
	                bean.setId(id);          
	                beans.add(bean);
	            }
	        } catch (SQLException e) {
	  
	            e.printStackTrace();
	        }
	        return beans;
	    }

	// 初始化某个产品对应的属性值PropertyValue，初始化逻辑：
	// 1. 根据分类获取所有的属性 
	// 2. 遍历每一个属性
	// 2.1 根据属性和产品，获取属性值 
	// 2.2 如果属性值不存在，就创建一个属性值对象
	 
	 	public void init(Product p) {
	 		List<Property> pts = new PropertyDAO().list(p.getCategory().getId());
	 		
	 		for(Property pt: pts) {
	 			PropertyValue pv = get(pt.getId(),p.getId());
	 			if(null==pv) {
	 				pv = new PropertyValue();
	 				pv.setProduct(p);
	 				pv.setProperty(pt);
	 				this.add(pv);//调用本类中的add函数，往数据库插入一条property数据
	 			}
	 		}
	 		
	 	}
	 	//
	 	//查询某个产品下所有的属性值
	 	  public List<PropertyValue> list(int pid) {
	 	        List<PropertyValue> beans = new ArrayList<PropertyValue>();
	 	         
	 	        String sql = "select * from PropertyValue where pid = ? order by ptid desc";
	 	  
	 	        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
	 	  
	 	            ps.setInt(1, pid);
	 	  
	 	            ResultSet rs = ps.executeQuery();
	 	  
	 	            while (rs.next()) {
	 	                PropertyValue bean = new PropertyValue();
	 	                int id = rs.getInt(1);
	 	 
	 	                int ptid = rs.getInt("ptid");
	 	                String value = rs.getString("value");
	 	                 
	 	                Product product = new ProductDAO().get(pid);
	 	                Property property = new PropertyDAO().get(ptid);
	 	                bean.setProduct(product);
	 	                bean.setProperty(property);
	 	                bean.setValue(value);
	 	                bean.setId(id);          
	 	                beans.add(bean);
	 	            }
	 	        } catch (SQLException e) {
	 	  
	 	            e.printStackTrace();
	 	        }
	 	        return beans;
	 	    }
	 	  
	 	}
	













































