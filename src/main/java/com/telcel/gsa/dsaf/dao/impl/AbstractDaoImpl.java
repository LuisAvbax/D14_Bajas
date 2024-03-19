
package com.telcel.gsa.dsaf.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;


import com.telcel.gsa.dsaf.dao.AbstractDao;
import com.telcel.gsa.dsaf.util.CfeException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;


public class AbstractDaoImpl<O, K extends Serializable> implements AbstractDao<O, K>, Serializable{

	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 7922486377753971314L;

	private Class< O > clazz;
	
	@Autowired
	@Qualifier("sessionFactory")
    protected SessionFactory sessionFactory;

	
	public AbstractDaoImpl(Class<O> entityClass) {
		this.clazz = entityClass;
	}
	
	
	
	/**
	 * @return the sessionFactory
	 */
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}



	/**
	 * @param sessionFactory the sessionFactory to set
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	@Transactional(value = "transactionManager")
	public void save(O entity) throws CfeException {
		try {
			this.getCurrentSession().save(entity);
			this.getCurrentSession().flush();
		} catch (Exception e) {
			throw new CfeException(e.getMessage(), e);
		}
		
	}

	@Override
	@Transactional(value = "transactionManager")
	public void saveOrUpdate(O entity) throws CfeException {
		try {
			this.getCurrentSession().saveOrUpdate(entity);
			this.getCurrentSession().flush();
		} catch (Exception e) {
			throw new CfeException(e.getMessage(), e);
		}
		
	}

	@Override
	@Transactional(value = "transactionManager")
	public void update(O entity) throws CfeException {
		try {
			this.getCurrentSession().update( entity );
			this.getCurrentSession().flush();
		} catch (Exception e) {
			throw new CfeException(e.getMessage(), e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(value = "transactionManager")
	public O getById(K id) throws CfeException {
		try {
			O obj = null;
			obj = (O) this.getCurrentSession().get(clazz, id);
			return obj;
		} catch (Exception e) {
			throw new CfeException(e.getMessage(),e);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(value = "transactionManager")
	public void saveList(List entity) {
		try {
			for (Object entitys : entity) {
				sessionFactory.getCurrentSession().saveOrUpdate(entitys);
			}
		} catch (Exception e) {
			sessionFactory.getCurrentSession().getTransaction().rollback();
			throw new CfeException("Error", e);
		}

		
	}
	
	
	protected final Session getCurrentSession(){
		return this.sessionFactory.getCurrentSession();
	}
    

	
	
	
	
}
