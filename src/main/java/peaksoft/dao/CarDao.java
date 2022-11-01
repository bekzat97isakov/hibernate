package peaksoft.dao;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import peaksoft.model.Car;
import peaksoft.model.Person;
import peaksoft.util.Util;
import java.util.ArrayList;
import java.util.List;

public class CarDao {
    private SessionFactory sessionFactory = Util.getSessionFactory();

    public void saveCar(Car car){
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.save(car);
            session.getTransaction().commit();
        }
    }

    public Car getCarById(Long id) {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            Car car = session.get(Car.class, id);
            session.getTransaction().commit();
            return car;
        } catch (HibernateException e) {
            throw new RuntimeException();
        }
    }

    public List<Car> getCarsByPersonId(Long id) {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            List<Person> personList = session.
                    createQuery("select c from Person c where c.id = :id", Person.class)
                    .setParameter("id", id).list();

            Person person = personList.get(0);
            List<Car> instructors = new ArrayList<>(person.getCars());
            session.getTransaction().commit();
            return instructors;
        } catch (HibernateException e) {
            throw new RuntimeException();
        }
    }

    public List<Car> getCarsByPersonName(String name) {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            List<Person> personList = session.createQuery("select u from Person u").list();

            Person person = null;
            for (Person i : personList) {
                if (i.getName().equals(name)){
                    List<Person> personList2 = session.createQuery("select c from Person c where c.id = :id", Person.class).setParameter("id", i.getId()).list();
                    person = personList2.get(0);
                }
            }
            List<Car> instructors = new ArrayList<>(person.getCars());
            session.getTransaction().commit();
            return instructors;
        } catch (HibernateException e) {
            throw new RuntimeException();
        }
    }

    public List<Car> getAllCars() {
        try{
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            List<Car> cars = session.createQuery("select u from Car u").list();
            session.getTransaction().commit();
            session.close();
            return cars;
        } catch (HibernateException e) {
            throw new RuntimeException();
        }
    }

    public void deleteCarById(Long id) {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            Car car = session.get(Car.class, id);
            session.delete(car);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            throw new RuntimeException();
        }
    }
}
