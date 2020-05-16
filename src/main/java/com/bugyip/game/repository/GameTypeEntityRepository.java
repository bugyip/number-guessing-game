package com.bugyip.game.repository;

import com.bugyip.game.entity.GameTypeEntity;
import com.bugyip.game.enums.DifficultyType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameTypeEntityRepository extends CrudRepository<GameTypeEntity, Long> {

    GameTypeEntity findByDifficultyType(DifficultyType difficulty);
}
