/*
 * Copyright 2015 Stefan Heimberg <kontakt@stefanheimberg.ch>.
 * 
 * All rights reserved.
 */
package example.persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 *
 * @author Stefan Heimberg <kontakt@stefanheimberg.ch>
 */
public class EclipseLinkTest extends BaseTest {
    
    private static final String PU = "eclipselinkPU";
    
    private static EntityManagerFactory emf;
    
    @BeforeClass
    public static void createEntityManagerFactory() {
        if(null == emf) {
            emf = Persistence.createEntityManagerFactory(PU);
        }
    }
    
    @AfterClass
    public static void destroyEntityManagerFactory() {
        if(null != emf) {
            if(emf.isOpen()) {
                emf.close();
            }
            emf = null;
        }
    }
    
    private EntityManager em;
    private EntityTransaction et;
    
    @Before
    public void createEntityManager() {
        em = emf.createEntityManager();
        et = em.getTransaction();
    }
    
    @After
    public void destroyEntityManager() {
        if(null != et) {
            if(et.isActive()) {
                et.rollback();
            }
            et = null;
        }
        if(null != em) {
            if(em.isOpen()) {
                em.close();
            }
            em = null;
        }
    }

    @Override
    public EntityManager getEM() {
        return em;
    }

    @Override
    public EntityManagerFactory getEMF() {
        return emf;
    }

    @Override
    public EntityTransaction getET() {
        return et;
    }

    @Override
    public void clearCaches() {
        if(null != em) {
            em.clear();
        }
        if(null != emf && null != emf.getCache()) {
            emf.getCache().evictAll();
        }
    }
    
}
