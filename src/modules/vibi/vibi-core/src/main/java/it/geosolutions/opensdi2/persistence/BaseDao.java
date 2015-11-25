package it.geosolutions.opensdi2.persistence;

import com.googlecode.genericdao.dao.jpa.GenericDAOImpl;
import com.googlecode.genericdao.search.Search;
import com.googlecode.genericdao.search.jpa.JPASearchProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.io.Serializable;

@Transactional(value = "opensdiTransactionManager")
@Repository
public abstract class BaseDao<T, ID extends Serializable> extends GenericDAOImpl<T, ID> implements GenericVibiDao<T, ID> {

    private final static Logger LOGGER = LoggerFactory.getLogger(BaseDao.class);

    @Override
    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        super.setEntityManager(entityManager);
    }

    @Override
    @Autowired
    public void setSearchProcessor(JPASearchProcessor searchProcessor) {
        super.setSearchProcessor(searchProcessor);
    }

    public boolean removeByPK(String[] names, Serializable... pkObjects) {
        T found = searchByPK(names, pkObjects);
        if (found != null) {
            // Remove
            boolean ret = remove((T) found);
            return ret;
        }
        return false;
    }

    public boolean removeByPK(Serializable... pkObjects) {
        return removeByPK(getPKNames(), pkObjects);
    }

    /**
     * Obtain a entity by its pk
     *
     * @return entity found or null if not found
     */
    public T searchByPK(Serializable... pkObjects) {
        return searchByPK(getPKNames(), pkObjects);
    }

    /**
     * Obtain a entity by its pk
     *
     * @return entity found or null if not found
     */
    public T searchByPK(String[] names, Serializable... pkObjects) {
        T found = null;
        // Check if correct pkObjects
        if (names != null && pkObjects != null && pkObjects.length == names.length) {
            Search search = new Search(persistentClass);

            int index = 0;
            for (String name : names) {
                Object value = pkObjects[index++];
                if (value != null) {
                    search.addFilterEqual(name, value);
                }
            }
            try {
                found = searchUnique(search);
            } catch (NonUniqueResultException e) {
                LOGGER.error(
                        "Non unique result searching for  "
                                + persistentClass.getSimpleName() + " - " + search,
                        e);
            } catch (Exception e) {
                LOGGER.error(
                        "Result not found for  "
                                + persistentClass.getSimpleName() + " - " + search,
                        e);
                // return null
            }
        }
        // Return found object
        return found;
    }

    /**
     * @return the actual type of the entity managed by the concrete DAO
     */
    protected abstract Class getEntityType();

    /**
     * Returns the field identified as unique, that represent the composite primary key in the Relational model.
     * <p/>
     * This method should replace the hardcoded list in the old DAO objects.
     *
     * @return
     */
    protected String[] getUniqueFields() {

        Class clazz = getEntityType();

        Table tableAnnotation = (Table) getEntityType().getAnnotation(Table.class);
        if (tableAnnotation == null) {
            // No Table annotation on this class, return an 0 length array
            return new String[0];
        }
        UniqueConstraint[] uc = tableAnnotation.uniqueConstraints();
        if (uc.length == 0) {
            // No unique constraints on this entity, return an 0 length array
            return new String[0];
        } else if (uc.length != 1) {
            // TODO: remove this for VIBI
            throw new IllegalStateException("Annotation 'Table' for entity Fertilizer has more than 1 unique constraint definition... this should never happen...");
        }
        return uc[0].columnNames().clone();
    }

}
