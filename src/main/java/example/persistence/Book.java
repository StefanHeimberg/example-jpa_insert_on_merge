/*
 * Copyright 2015 Stefan Heimberg <kontakt@stefanheimberg.ch>.
 * 
 * All rights reserved.
 */
package example.persistence;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 *
 * @author Stefan Heimberg <kontakt@stefanheimberg.ch>
 */
@Entity
@NamedQueries(
        @NamedQuery(name = Book.FIND_ALL, query = "SELECT b FROM Book b")
)
public class Book implements Serializable {
    
    public static final String FIND_ALL = "Book.findAll";
    
    @Id
    private Long id;
    
    @Basic(optional = false)
    @Column(nullable = false)
    private String title;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return new StringBuilder(getClass().getSimpleName())
                .append("id=").append(id).append(", ")
                .append("title=").append(title)
                .toString();
    }
    
}
