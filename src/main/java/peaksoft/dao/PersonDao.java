package peaksoft.dao;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import peaksoft.model.Car;
import peaksoft.model.Person;
import peaksoft.util.Util;
import java.util.List;

public class PersonDao {
    private SessionFactory sessionFactory = Util.getSessionFactory();

    public void savePerson(Person person){
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.save(person);
            session.getTransaction().commit();
        }
    }

    public Person getPerson(Long id){
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            Person person = session.get(Person.class, id);
            session.getTransaction().commit();
            return person;
        }
    }

    public List<Person> getAllPerson() {
        try{
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            List<Person> personList = session.createQuery("select u from Person u").list();
            session.getTransaction().commit();
            session.close();
            return personList;
        } catch (HibernateException e) {
            throw new RuntimeException();
        }
    }

    public void updatePerson(Long id, Person person) {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            Person newPerson = session.get(Person.class, id);
            newPerson.setName(person.getName());
            newPerson.setAge(person.getAge());
            session.saveOrUpdate(newPerson);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            throw new RuntimeException();
        }
    }

    public void deletePersonId(Long id) {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            Person person = session.get(Person.class, id);

            for (Car i: person.getCars()) {
                i.setPerson(null);
            }

            session.delete(person);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            throw new RuntimeException();
        }
    }

    public Person getPersonByName(String name) {
        try(Session session = sessionFactory.openSession();){
            session.beginTransaction();
            List<Person> persons = session.createQuery("select u from Person u").list();
            session.getTransaction().commit();
            for (Person i: persons) {
                if (i.getName().equals(name)){
                    return i;
                }
            }
            return null;
        } catch (HibernateException e) {
            throw new RuntimeException();
        }
    }
}
