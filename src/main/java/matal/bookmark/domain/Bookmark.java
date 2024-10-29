package matal.bookmark.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import matal.member.domain.Member;
import matal.store.domain.Store;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "BOOKMARK")
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookmarkId;

    @OneToOne
    private Member member;

    @OneToOne
    private Store store;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;

    @Builder
    public Bookmark(Long bookmarkId,
                    Member member,
                    Store store,
                    LocalDateTime createdDate,
                    LocalDateTime modifiedDate
    ) {
        this.bookmarkId = bookmarkId;
        this.member = member;
        this.store = store;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }
}
