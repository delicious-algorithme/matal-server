package matal.jwt.repository;


import matal.jwt.entity.BlackList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlackListRepository extends JpaRepository<BlackList, Long> {

    boolean existsByInvalidRefreshToken(String token);
}
