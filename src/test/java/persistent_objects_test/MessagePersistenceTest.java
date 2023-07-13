package persistent_objects_test;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.junit.jupiter.api.Test;
import persistent_objects.Message;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;


public class MessagePersistenceTest {

    public static SessionFactory createSessionFactory(){

        Configuration configuration = new Configuration();
        configuration.configure().addAnnotatedClass(Message.class);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(
                configuration.getProperties()).build();

        SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);

        return sessionFactory;

    }

    @Test
    public void messageTest(){

        try (SessionFactory sessionFactory = createSessionFactory();
             Session session = sessionFactory.openSession()){

            session.beginTransaction();

            Message message = new Message();
            message.setText("hello kamar, you handsome");

            Message greeting = new Message();
            greeting.setText("hello kenyans");

            session.persist(message);
            session.persist(greeting);

            session.getTransaction().commit();

            CriteriaQuery<Message> criteriaQuery = session.getCriteriaBuilder().createQuery(Message.class);
            criteriaQuery.from(Message.class);

            List<Message> messages = session.createQuery(criteriaQuery).getResultList();

            messages.forEach(System.out::println);
        }
    }
}
