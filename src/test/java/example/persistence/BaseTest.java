/*
 * Copyright 2015 Stefan Heimberg <kontakt@stefanheimberg.ch>.
 * 
 * All rights reserved.
 */
package example.persistence;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Stefan Heimberg <kontakt@stefanheimberg.ch>
 */
public abstract class BaseTest {

    public abstract EntityManager getEM();

    public abstract EntityManagerFactory getEMF();

    public abstract EntityTransaction getET();

    public abstract void clearCaches();

    @Test(expected = PersistenceException.class)
    public void assert_new_book_inserted_on_merge() throws Throwable {
        final TypedQuery<Book> qFindAll = getEM().createNamedQuery(Book.FIND_ALL, Book.class);

        assertEquals(0, qFindAll.getResultList().size());
        clearCaches();

        final Book book = new Book();
        book.setId(null);
        book.setTitle("test book");

        getET().begin();
        getEM().merge(book);
        getET().commit();
    }

    @Test
    public void assert_not_existing_book_inserted_on_merge() {
        final TypedQuery<Book> qFindAll = getEM().createNamedQuery(Book.FIND_ALL, Book.class);

        assertEquals(0, qFindAll.getResultList().size());
        clearCaches();

        final Book book = new Book();
        book.setId(1l);
        book.setTitle("test book");

        getET().begin();
        getEM().merge(book);
        getET().commit();
        clearCaches();

        assertEquals(1, qFindAll.getResultList().size());

        getET().begin();
        getEM().remove(getEM().getReference(Book.class, 1l));
        getET().commit();
    }

    @Test
    public void assert_existing_book_updated_on_merge() {
        final TypedQuery<Book> qFindAll = getEM().createNamedQuery(Book.FIND_ALL, Book.class);

        assertEquals(0, qFindAll.getResultList().size());
        clearCaches();

        final Book book1 = new Book();
        book1.setId(999l);
        book1.setTitle("test book");

        getET().begin();
        getEM().persist(book1);
        getET().commit();
        clearCaches();

        final List<Book> resultList1 = qFindAll.getResultList();
        assertEquals(1, resultList1.size());
        assertEquals("test book", resultList1.get(0).getTitle());
        clearCaches();

        final Book book2 = new Book();
        book2.setId(999l);
        book2.setTitle("test book 2");

        getET().begin();
        getEM().merge(book2);
        getET().commit();
        clearCaches();

        final List<Book> resultList2 = qFindAll.getResultList();
        assertEquals(1, resultList2.size());
        assertEquals("test book 2", resultList2.get(0).getTitle());
        clearCaches();

        getET().begin();
        getEM().remove(getEM().getReference(Book.class, 999l));
        getET().commit();
    }

    @Test
    public void assert_new_person_inserted_on_merge() {
        final TypedQuery<Person> qFindAll = getEM().createNamedQuery(Person.FIND_ALL, Person.class);

        assertEquals(0, qFindAll.getResultList().size());
        clearCaches();

        final Person person = new Person();
        person.setId(null);
        person.setName("test person");

        getET().begin();
        getEM().merge(person);
        getET().commit();
        clearCaches();

        final List<Person> resultList = qFindAll.getResultList();
        assertEquals(1, resultList.size());
        final Long id = resultList.get(0).getId();

        getET().begin();
        getEM().remove(getEM().getReference(Person.class, id));
        getET().commit();
    }

    @Test
    public void assert_not_existing_person_inserted_on_merge() {
        final TypedQuery<Person> qFindAll = getEM().createNamedQuery(Person.FIND_ALL, Person.class);

        assertEquals(0, qFindAll.getResultList().size());
        clearCaches();

        final Person person = new Person();
        person.setId(999l);
        person.setName("test person");

        getET().begin();
        getEM().merge(person);
        getET().commit();
        clearCaches();

        final List<Person> resultList = qFindAll.getResultList();
        assertEquals(1, resultList.size());
        final Long id = resultList.get(0).getId();
        // INFO: hibernate generiert die id immer NEU, auch wenn diese im entity zuvor gesetzt wurde
        //assertEquals(999l, id.longValue());

        getET().begin();
        getEM().remove(getEM().getReference(Person.class, id));
        getET().commit();
    }

    @Test
    public void assert_existing_person_updated_on_merge() {
        final TypedQuery<Person> qFindAll = getEM().createNamedQuery(Person.FIND_ALL, Person.class);

        assertEquals(0, qFindAll.getResultList().size());
        clearCaches();

        final Person person1 = new Person();
        person1.setName("test person");

        getET().begin();
        getEM().persist(person1);
        getET().commit();
        clearCaches();

        final List<Person> resultList1 = qFindAll.getResultList();
        assertEquals(1, resultList1.size());
        assertEquals("test person", resultList1.get(0).getName());
        final Long id = resultList1.get(0).getId();
        clearCaches();

        final Person person2 = new Person();
        person2.setId(id);
        person2.setName("test person 2");

        getET().begin();
        getEM().merge(person2);
        getET().commit();
        clearCaches();

        final List<Person> resultList2 = qFindAll.getResultList();
        assertEquals(1, resultList2.size());
        assertEquals("test person 2", resultList2.get(0).getName());

        clearCaches();

        getET().begin();
        getEM().remove(getEM().getReference(Person.class, id));
        getET().commit();
    }

}
