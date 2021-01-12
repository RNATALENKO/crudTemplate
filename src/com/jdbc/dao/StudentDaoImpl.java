package com.jdbc.dao;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.jdbc.batchpreparedstatement.StudentBatchPreparedSatementSetter;
import com.jdbc.pojo.Student;
import com.jdbc.resultsetextractor.StudentResultSetExtractor;
import com.jdbc.rowmappers.StudentRowMapper;

import jdk.jfr.consumer.RecordedStackTrace;


//because it's an object that does db actions, use @repository isntead of @component
//give the name as studentdaoimpl

/*
 * update() method used for DML sql commands
 * execute() method used for DDL sql commands
 * 
 * query() to retrieve list of objects/records
 * queryForObject to retrieve single object/record
 * 
 *  * BeanPropertyRowMapper() injects into pojo only if column name is the same as the pojo's property name, 
 * otherwise you get mapped class not specified, but, you can also use alias column names to rename them to pojo's name
 * String singleSelectedSQL = "SELECT ROLL_NUM as rollNo, STUDENT_NAME as name, STUDENT_ADDRESS as address FROM school.student WHERE ROLL_NUM =?";
 * Student student = jdbcTemplate.queryForObject(singleSelectedSQL, new BeanPropertyRowMapper<Student>(Student.class), rollNo);
 */


/*
 * 
 * notes on transactions: 
 * let's say you perform a batch update, if there's 500 records, and one goes wrong, how do you manage that transaction? 
 * https://www.youtube.com/watch?v=oJK_egOYA4s&t=1754s 30 mins
 * Without transaction management, if one record fails then the rest still update
 * however, we want, if one record fails all fail, and if all succeed all update.
 * 
 * add @Transactional over the dao method, each sql query will be wrapped in a transaction and if one fails, it reverts
 * you can also put it ontop of the class which affects all of the methods
 * 
 * Note: you need to activate transactional bean in beans.xml, add the dtd in namespaces tab, tick tx, for transaction
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
	
	
	/*
	 * 
	 * retrieve operations
	 * when retrieving a record, we will convert the record information into a Student object
	 * 
	 * Mapper class fetches records and makes data available one by one
	 * ResultSet is a class that contains all the records from the SQL query
	 */
	


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
		
		//use query for object to retrieve a single object, attach argument to ? mark as the third argument
		//(sql, rowmapper, arguments)
		Student student = jdbcTemplate.queryForObject(singleSelectedSQL, new StudentRowMapper(), rollNo);
		
		return student;
	}


	//returns a list of student names, using result set extractor instead of row mapper
	//if you mention <Student> it will return Student
	@Override
	public List<Student> findStudentsByName(String name) {
		
		String selectSQL = "SELECT * FROM school.student WHERE STUDENT_NAME =?";
		
		
		List<Student> studentList = jdbcTemplate.query(selectSQL, new StudentResultSetExtractor(), name);
		
		return studentList;
	}


	@Override
	public int updateStudent(Student student) {
		
		String updateSQL = "UPDATE school.student SET STUDENT_ADDRESS=? WHERE ROLL_NUM =? ";
		
		//don't forget can wrap args in Object[] array = {student.getaddress() etc.. also
		int recordsUpdated = jdbcTemplate.update(updateSQL, student.getAddress(), student.getRollNo());
		
		return recordsUpdated;
	}


	@Override
	@Transactional   //specifies if one record fails to update, none update, or if all succeed all are updated, see beans for config details
	public int updateBatch(List<Student> studentList) {
		
		//this is technically the prepared statement, it gets compiled and returned as PreparedStatement object
		String updateSQL = "UPDATE school.student SET STUDENT_ADDRESS=? WHERE ROLL_NUM =? ";
		
		/*
		//convert student list data into List<Object[]>
		List<Object[]> batchArgs = new ArrayList<>();
		
		//iterate through student objects
		for(Student tempStudent : studentList) {
			
			//retrieve data from object, store into array
			//address comes first in argument since it's the first ? within the string
			Object[] studentArray = {tempStudent.getAddress(),tempStudent.getRollNo()};
			
			//add array into list
			batchArgs.add(studentArray);
			
		}
		
	
		int[] updateNum = jdbcTemplate.batchUpdate(updateSQL, batchArgs);
		
		
		return updateNum.length;
		*/
		
		//another way to do this is to use batch prepared statement setter
		//returns an array of integers which hold 1 or 0, if a record successfully updated
		int[] updateNum = jdbcTemplate.batchUpdate(updateSQL, new StudentBatchPreparedSatementSetter(studentList));
		
		

		return updateNum.length;
		
		
	}


	
	
	
	
	
	
	
	
	
	
	


	
	
	
	
	
	
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


