package peaksoft.dao;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import peaksoft.model.Person;
import peaksoft.model.SocialMedia;
import peaksoft.util.Util;
import java.util.List;

public class SocialMediaDao {
    private SessionFactory sessionFactory = Util.getSessionFactory();

    public void saveMedia(SocialMedia socialMedia){
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.save(socialMedia);
            session.getTransaction().commit();
        }
    }

    public SocialMedia getSocialMedia(Long id){
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            SocialMedia socialMedia = session.get(SocialMedia.class, id);
            session.getTransaction().commit();
            return socialMedia;
        }
    }

    public List<SocialMedia> getAllMedia() {
        try{
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            List<SocialMedia> socialMediaList = session.createQuery("select u from SocialMedia u").list();
            session.getTransaction().commit();
            session.close();
            return socialMediaList;
        } catch (HibernateException e) {
            throw new RuntimeException();
        }
    }

    public void updateSocialMedia(Long id, SocialMedia socialMedia) {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            SocialMedia newMedia = session.get(SocialMedia.class, id);
            newMedia.setName(socialMedia.getName());
            session.saveOrUpdate(newMedia);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            throw new RuntimeException();
        }
    }

    public void deleteSocialMedia(Long id) {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            SocialMedia socialMedia = session.get(SocialMedia.class, id);

            for (Person i: socialMedia.getPersonList()) {
                i.setSocialMediaList(null);
            }

            session.delete(socialMedia);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            throw new RuntimeException();
        }
    }

    public SocialMedia getSocialMediaByName(String name) {
        try(Session session = sessionFactory.openSession();){
            session.beginTransaction();
            List<SocialMedia> socialMedia = session.createQuery("select u from SocialMedia u").list();
            session.getTransaction().commit();
            for (SocialMedia i: socialMedia) {
                if (i.getName().equals(name)){
                    return i;
                }
            }
            return null;
        } catch (HibernateException e) {
            throw new RuntimeException();
        }
    }

    public void assignPersonToMedia(Long id, Long mediaId) {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            Person person = session.get(Person.class, id);
            SocialMedia socialMedia = session.get(SocialMedia.class, mediaId);
            person.getSocialMediaList().add(socialMedia);
            socialMedia.getPersonList().add(person);
            session.persist(person);
            session.persist(socialMedia);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            throw new RuntimeException();
        }
    }
}
