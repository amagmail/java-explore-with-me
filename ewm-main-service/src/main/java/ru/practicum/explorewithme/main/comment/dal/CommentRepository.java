package ru.practicum.explorewithme.main.comment.dal;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explorewithme.main.comment.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c from Comment c " +
            "where c.eventId = ?1 and c.accepted = true " +
            "order by c.createdDate desc")
    List<Comment> getEventComments(Long eventId, Pageable pageable);

    @Query("select c from Comment c " +
            "where c.eventId = ?1 and c.userId = ?2 " +
            "order by c.createdDate desc")
    List<Comment> getEventUserComments(Long eventId, Long userId, Pageable pageable);

    @Query("select c from Comment c " +
            "where c.accepted = false " +
            "order by c.createdDate asc")
    List<Comment> getCommentsAdmin(Pageable pageable);

}
