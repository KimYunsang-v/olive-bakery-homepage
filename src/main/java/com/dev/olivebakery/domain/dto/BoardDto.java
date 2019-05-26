package com.dev.olivebakery.domain.dto;


import com.dev.olivebakery.domain.entity.Board;
import com.dev.olivebakery.domain.entity.Member;
import com.dev.olivebakery.domain.enums.BoardType;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BoardDto {
    @Getter @NoArgsConstructor
    public static class GetPosts {
        private Long boardId;
        private LocalDateTime insertTime;
        private LocalDateTime updateTime;
        private String title;
        private String context;
        private boolean isNotice;
        private boolean isSecret;
        private String userId;

        @Builder
        public GetPosts(Long boardId, LocalDateTime insertTime, LocalDateTime updateTime, String title, String context, boolean isNotice, boolean isSecret, String userId) {
            this.boardId = boardId;
            this.insertTime = insertTime;
            this.updateTime = updateTime;
            this.title = title;
            this.context = context;
            this.isNotice = isNotice;
            this.isSecret = isSecret;
            this.userId = userId;
        }
    }

    @Getter @NoArgsConstructor
    public static class GetPostDetails {
        private GetPosts posts;
        private List<CommentDto.GetComment> comments = new ArrayList<>();

        @Builder
        public GetPostDetails(GetPosts posts, List<CommentDto.GetComment> comments) {
            this.posts = posts;
            this.comments = comments;
        }

        public void setComments(List<CommentDto.GetComment> comments) {
            this.comments = comments;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SavePost {
        private String context;
        private String title;
        private String userId;
        private BoardType boardType;
        @ApiModelProperty(notes = "true 또는 false로 보내야함")
        private String isNotice;
        @ApiModelProperty(notes = "true 또는 false로 보내야함")
        private String isSecret;

        @Builder
        public SavePost(String context, String title, String userId, BoardType boardType, String isSecret, String isNotice) {
            this.context = context;
            this.title = title;
            this.userId = userId;
            this.boardType = boardType;
            this.isNotice = isNotice;
            this.isSecret = isSecret;
        }

        public Board toEntity(Member member) {
            return Board.builder()
                    .context(context)
                    .title(title)
                    .member(member)
                    .boardType(boardType)
                    .isNotice(isNotice.matches("true"))
                    .isSecret(isSecret.matches("true"))
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UpdatePost {
        private Long boardId;
        private String context;
        private String title;
        @ApiModelProperty(notes = "true 또는 false로 보내야함")
        private String isNotice;
        @ApiModelProperty(notes = "true 또는 false로 보내야함")
        private String isSecret;

        @Builder
        public UpdatePost(Long boardId, String context, String title, String isSecret, String isNotice) {
            this.boardId = boardId;
            this.context = context;
            this.title = title;
            this.isNotice = isNotice;
            this.isSecret = isSecret;
        }
    }
}
