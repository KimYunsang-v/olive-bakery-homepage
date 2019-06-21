package com.dev.olivebakery.repository.implement;

import com.dev.olivebakery.domain.dto.BoardDto;
import com.dev.olivebakery.domain.dto.CommentDto;
import com.dev.olivebakery.domain.entity.*;
import com.dev.olivebakery.domain.enums.BoardType;
import com.dev.olivebakery.repository.custom.BoardRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

@Component
public class BoardRepositoryImpl extends QuerydslRepositorySupport implements BoardRepositoryCustom {
    @PersistenceContext
    EntityManager entityManager;
    private QBoard board = QBoard.board;
    private QMember member = QMember.member;
    private QComment comment = QComment.comment;

    private final int DEFAULT_LIMIT_SIZE = 10;

    public BoardRepositoryImpl() {
        super(Board.class);
    }

    @Override
    public Page<BoardDto.GetPosts> getPosts(BoardType boardType, int pageNum) {
        JPAQuery query = new JPAQuery(entityManager);
        long countResult = query.select(board.count()).from(board).fetchCount();

        JPAQuery<BoardDto.GetPosts> jpaQuery = new JPAQuery<>(entityManager);
        jpaQuery = setQuery(jpaQuery);
        jpaQuery.where(board.boardType.eq(boardType))
                .where(board.isNotice.eq(false))
                .orderBy(board.boardId.desc())
                .offset(--pageNum * DEFAULT_LIMIT_SIZE)
                .limit(DEFAULT_LIMIT_SIZE)
        ;
        List<BoardDto.GetPosts> boards = jpaQuery.fetch();
        return new PageImpl<>(boards, PageRequest.of(pageNum, DEFAULT_LIMIT_SIZE, new Sort(Sort.Direction.DESC, "boardId")), countResult);
    }

    @Override
    public List<BoardDto.GetPosts> getNoticePosts() {
        JPAQuery<BoardDto.GetPosts> jpaQuery = new JPAQuery<>(entityManager);
        jpaQuery = setQuery(jpaQuery);
        jpaQuery.where(board.isNotice.eq(true));
        return jpaQuery.fetch();
    }

    @Override
    public BoardDto.GetPostDetails getPostDetails(Long boardId) {
        JPAQuery<BoardDto.GetPosts> jpaQuery = new JPAQuery<>(entityManager);
        jpaQuery = setQuery(jpaQuery);
        BoardDto.GetPosts post = jpaQuery.where(board.boardId.eq(boardId)).fetchOne();

        List<Comment> comments = jpaQuery.join(board.comments, comment)
                                        .transform(groupBy(board.boardId).as(list(comment))).get(boardId);

        List<CommentDto.GetComment> commentDtoList = new ArrayList<>();
        if(comments != null) {
            comments.forEach(commentTmp ->
                    commentDtoList.add(
                            CommentDto.GetComment.builder()
                                    .insertTime(commentTmp.getInsertTime())
                                    .updateTime(commentTmp.getUpdateTime())
                                    .userName(commentTmp.getUserName())
                                    .content(commentTmp.getContent())
                                    .build()
                    )
            );
        }

        return BoardDto.GetPostDetails.builder()
                .posts(post)
                .comments(commentDtoList)
                .build();
    }

    private JPAQuery<BoardDto.GetPosts> setQuery(JPAQuery<BoardDto.GetPosts> query){
        return query.select(Projections.constructor(BoardDto.GetPosts.class, board.boardId, board.insertTime, board.updateTime, board.title, board.context, board.isNotice, board.isSecret, member.email))
                    .from(board)
                    .join(board.member, member);
    }
}
