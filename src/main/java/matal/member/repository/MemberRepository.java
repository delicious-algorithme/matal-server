package matal.member.repository;

import matal.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional <Member> findByEmail(String email); // 중복 가입 확인
}
