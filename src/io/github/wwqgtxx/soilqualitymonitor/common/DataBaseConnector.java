package io.github.wwqgtxx.soilqualitymonitor.common;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wwq on 2016/5/29.
 */
public class DataBaseConnector {
    private SessionFactory sessionFactory;
    private DataBaseConnector(){}

    public static DataBaseConnector getDataBaseConnecter() {
        return dataBaseConnecter;
    }

    private static final DataBaseConnector dataBaseConnecter = new DataBaseConnector();

    public boolean initSessionFactory(){
        // A SessionFactory is set up once for an application!
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
            return true;
        }
        catch (Exception e) {
            // The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
            // so destroy it manually.
            StandardServiceRegistryBuilder.destroy( registry );
            return false;
        }
    }

    public boolean closeSessionFactory(){
        if ( sessionFactory != null ) {
            try{
                sessionFactory.close();
                return true;
            }catch (HibernateException e){
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean saveOrUpdate(Object ...entitys){
        if(sessionFactory!=null){
            Session session = sessionFactory.openSession();
            try{
                Transaction transaction = session.beginTransaction();
                for (Object entity:entitys) {
                    session.saveOrUpdate(entity);
                }
                transaction.commit();
                return true;
            }catch (HibernateException e){
                e.printStackTrace();
            }
            finally {
                session.close();
            }
        }
        return false;
    }

    public <T> List<T> getAllByClass(Class<T> entityClass){
        List result = null;
        if(sessionFactory!=null){
            Session session = sessionFactory.openSession();
            try{
                //Transaction transaction = session.beginTransaction();
                result = session.createQuery( "from "+entityClass.getName() ).list();
                //transaction.commit();
            }catch (HibernateException e){
                e.printStackTrace();
            }
            finally {
                session.close();
            }
        }
        return result;
    }

    public <T> T getByNaturalId(Class<T> entityClass,Object naturalIdValue){
        T result = null;
        if(sessionFactory!=null){
            Session session = sessionFactory.openSession();
            try{
                //Transaction transaction = session.beginTransaction();
                result = session.bySimpleNaturalId( entityClass).load(naturalIdValue);
                //transaction.commit();
            }catch (HibernateException e){
                e.printStackTrace();
            }
            finally {
                session.close();
            }
        }
        return result;
    }
    public <T> T getById(Class<T> entityClass,Serializable IdValue){
        T result = null;
        if(sessionFactory!=null){
            Session session = sessionFactory.openSession();
            try{
                //Transaction transaction = session.beginTransaction();
                result = session.byId( entityClass).load(IdValue);
                //transaction.commit();
            }catch (HibernateException e){
                e.printStackTrace();
            }
            finally {
                session.close();
            }
        }
        return result;
    }



}
