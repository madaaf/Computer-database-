package com.excilys.aflak.dao.inter;

public interface IDAOCrud<T> {

	/**
	 * CREATE
	 * 
	 * @param object
	 * @return id object created
	 */
	default public Long create(T obj) {
		throw new IllegalAccessError("error implementation create");
	}

	/**
	 * READ - find object
	 * 
	 * @param id
	 * @return object find
	 */
	default public T find(Long id) {
		throw new IllegalAccessError("error implementation find");
	}

	/**
	 * UPDATE - update object
	 * 
	 * @param obj
	 * @return object updated
	 */
	default public boolean update(T obj) {
		throw new IllegalAccessError("error implementation update");
	}

	/**
	 * DELETE - delete object
	 * 
	 * @param id
	 * @return boolean
	 */
	default public boolean delete(Long id) {
		throw new IllegalAccessError("error implementation delete");
	}

}
