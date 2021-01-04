package com.jdbc.dao;

import com.jdbc.pojo.Student;
import java.util.*;



/*
 * DDL - definition language create, drop, truncate and delete   database/schemas/tables/user i.e. structure e.g truncate, create schema, drop table, alter table etc..
 * 
 *DML - manipulation langauges DELETE, INSERT, UPDATE records within a table
 */

public interface StudentDao {
	
	void insert(Student student);
	
	void batchInsert(List<Student> students);
	
	/*adding more Crud operations
	 * Note: don't add a truncate or cleaning method to the dao, add it to the impl
	 * 
	 * */ 
	
	boolean deleteRecord(int rollNo); //if delete happens return true, else return false
	
	int deleteByNameOrAddress(String name, String Address); //method will delete ALL records where name = ? OR address ?, and returns number of records deleted
	
	
	
	/*
	 * read methods
	 */
	List<Student> retrieveAllRecords();
	Student retrieveRecord(int rollNo);
	

}
