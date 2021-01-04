package com.jdbc.dao;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Repository;

import com.jdbc.pojo.Student;
import com.jdbc.rowmappers.StudentRowMapper;

import jdk.jfr.consumer.RecordedStackTrace;


//because it's an object that does db actions, use @repository isntead of @component
//give the name as studentdaoimpl

/*
 * update() method used for DML sql commands
 * execute() method used for DDL sql commands
 * 
 */

@Repository("dao")
public class StudentDaoImpl implements StudentDao {
	
	
	//injects the jdbctemplate bean to here
	@Autowired
	private JdbcTemplate jdbcTemplate; /* = new JdbcTemplate(getDataSource()); set to bean id= jdbctemplate */
	

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}


	@Override
	public void insert(Student student) {
		
		//extract pojo data and store in array
		Object[] args = {student.getRollNo(), student.getName(), student.getAddress()};
		
		//sql query to perform
		String insertSql = "INSERT INTO school.student VALUES (?,?,?)";
		
		//execute the update with data and sql, return number of records that were stored
		int numberRecordsInserted = jdbcTemplate.update(insertSql, args);
		System.out.println("records inserted: " + numberRecordsInserted);
		
		
	}
	
	
	@Override
	public void batchInsert(List<Student> students) {
		
		String batchInsertSQL = "INSERT INTO school.student VALUES (?,?,?)";
		
		//list to store object arrays
		ArrayList<Object[]> ObjectList = new ArrayList<Object[]>();
		
		//we have to fetch the data of the list and store them
		for(Student temp: students) {
			Object[] studentObject = {temp.getRollNo(), temp.getName(), temp.getAddress()}; //store student data in array
			ObjectList.add(studentObject); //add array to a list
		}
		
		//add list to table
		int[] numberUpdated = jdbcTemplate.batchUpdate(batchInsertSQL, ObjectList);
		
		System.out.println("number in batch added: " + numberUpdated.length);
		
	}
	
	
	@Override
	public boolean deleteRecord(int rollNo) {
		
		String deleteSQL = "DELETE FROM school.student WHERE ROLL_NUM = ?";
		int successful = jdbcTemplate.update(deleteSQL, rollNo);
		if(successful == 1) {
			System.out.println("records deleted: 1:");
			return true; 
		}
		
		System.out.println("records deleted: 0");
		return false; 
	}


	@Override
	public int deleteByNameOrAddress(String name, String address) {
		String deleteNameOrAddressSQL = "DELETE FROM school.student WHERE STUDENT_NAME=? OR STUDENT_ADDRESS=?";
		int numberDeleted = jdbcTemplate.update(deleteNameOrAddressSQL , name,address); //no need to create Object[] args ={} ...args converts for you
		System.out.println("Number of records deleted by name OR address: " + numberDeleted);
		return numberDeleted;
	}
	
	
	public void deleteAll() {
		
		//truncate deletes all records but keeps all columns/indexes/constraints
		String cleanupSQL = "TRUNCATE TABLE school.student;";
		jdbcTemplate.execute(cleanupSQL);
		System.out.println("table records cleaned");
		
	}


	@Override
	public List<Student> retrieveAllRecords() {
		
		//sql string to query for all records
		String selectSQL = "SELECT * FROM school.student";
		
		
		//use .query() to execute a retrieval with a rowmapper
		List<Student> studentList = jdbcTemplate.query(selectSQL, new StudentRowMapper());
		
		
		return studentList;
	}


	@Override
	public Student retrieveRecord(int rollNo) {
		
		String singleSelectedSQL = "SELECT * FROM school.student WHERE ROLL_NUM =?";
		
		Student student = jdbcTemplate.query
		
		
		return null;
	}


	
	
	/*
	 * 
	 * retrieve operations
	 * when retrieving a record, we will convert the record information into a Student object
	 * 
	 * Mapper class fetches records and makes data available one by one
	 * ResultSet is a class that contains all the records from the SQL query
	 */
	
	
	
	
	
	
	
	
	


	
	
	
	
	
	
	/* turned into bean, id = datasource
	public DataSource getDataSource() {
		
		String url = "jdbc:mysql://localhost:3306/school";
		String username = "root";
		String password = "Bricks64";
		
		DataSource dataSource = new DriverManagerDataSource(url, username, password);
		
		return dataSource;
	}
	*/

}
