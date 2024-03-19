package com.telcel.gsa.dsaf.dao;

import java.io.Serializable;
import java.util.List;

import com.telcel.gsa.dsaf.util.CfeException;

/**
 * 
 * @author VI5XXAA
 *
 * @param <O>
 * @param <K>
 */
public interface AbstractDao<O,K extends Serializable> {
    
	
	void save(O entity)throws CfeException;
	void saveOrUpdate(O entity)throws CfeException;
	void update(O entity)throws CfeException;
	O getById(K id)throws CfeException;
	@SuppressWarnings("rawtypes")
	void saveList(List entity);
}
