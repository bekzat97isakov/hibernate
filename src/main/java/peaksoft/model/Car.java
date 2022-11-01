package peaksoft.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import peaksoft.util.Util;
import static jakarta.persistence.CascadeType.*;

@Entity
@Table(name = "cars")
@NoArgsConstructor
@Getter
@Setter
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String carModel;

    @ManyToOne(cascade = {MERGE, PERSIST, PERSIST, DETACH}, fetch = FetchType.EAGER)
    private Person person;

    public Car(String carModel, Long personId) {
        this.carModel = carModel;
        this.person = convertPerson(personId);
    }

    public Person convertPerson(Long id){
        SessionFactory sessionFactory = Util.getSessionFactory();
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            Person person = session.get(Person.class, id);
            session.getTransaction().commit();
            return person;
        } catch (HibernateException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", carModel='" + carModel + '\'' +
                '}';
    }
}
