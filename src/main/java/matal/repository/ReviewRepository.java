package matal.repository;

import matal.entity.Review;
import matal.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {


}
