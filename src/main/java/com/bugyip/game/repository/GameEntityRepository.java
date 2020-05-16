package com.bugyip.game.repository;

import com.bugyip.game.entity.GameEntity;
import com.bugyip.game.entity.GameTypeEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameEntityRepository extends CrudRepository<GameEntity, Long> {
}
