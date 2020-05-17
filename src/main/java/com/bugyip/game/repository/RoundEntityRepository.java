package com.bugyip.game.repository;

import com.bugyip.game.entity.RoundEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoundEntityRepository extends CrudRepository<RoundEntity, Long> {
}
