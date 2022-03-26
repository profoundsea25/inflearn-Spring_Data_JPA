package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    // 위에서 JpaRepository 타입에 명시한 클래스 + 메서드 이름이 같으면 NamedQuery는 생략 가능.
    // NamedQuery가 없으면 메서드 이름으로 쿼리 생성 전략을 사용한다.
//    @Query(name = "Member.findByUsername")
    List<Member> findByUsername(@Param("username") String username);

    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMeberDto();

    @Query("select m from Member m where m.username = :name")
    Member findMembers(@Param("name") String username);

    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") List<String> names);

    List<Member> findListByUsername(String name); // 반환 타입 컬렉션
    Member findSingleResultByUsername(String name); // 반환 타입 단건
    Optional<Member> findOptionalByUsername(String name); // 단건 Optional

    // 페이징
    Page<Member> findPageByUsername(String name, Pageable pageable);
    Slice<Member> findSliceByUsername(String name, Pageable pageable);
    List<Member> findListPageableByUsername(String name, Pageable pageable);
    List<Member> findListSortByUsername(String name, Sort sort);
    Page<Member> findByAge(int age, Pageable pageable);

    @Query(value = "select m from Member m",
            countQuery = "select count(m.username) from Member m")
    Page<Member> findMemberAllCountBy(Pageable pageable);

    List<Member> findTop3By();

    // 벌크성 수정 쿼리
    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();

    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    // JPQL + 엔티티 그래프
    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    // 메서드 이름으로 쿼리에서 특히 편리하다.
    @EntityGraph(attributePaths = {"team"})
    List<Member> findMemberEntityGraphByUsername(String username);

    @EntityGraph("Member.all")
    @Query("select m from Member m")
    List<Member> findMemberNamedEntityGraph();

    // 쿼리 힌트 사용
    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findMemberQueryHintByUsername(String username);

    // 쿼리 힌트 Page
    @QueryHints(value = {@QueryHint(name = "org.hibernate.readOnly", value = "true")}, forCounting = true)
    Page<Member> findMemberPageQueryHintByUsername(String name, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findMemberLockPessimisticWriteByUsername(String name);
}
