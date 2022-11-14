package tech.jannotti.billing.core.persistence.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import tech.jannotti.billing.core.persistence.model.AbstractModel;

@NoRepositoryBean
public interface AbstractRepository<T extends AbstractModel, U extends Serializable> extends JpaRepository<T, U> {

}