package com.dev.olivebakery.service;

import com.dev.olivebakery.domain.dto.BoardDto;
import com.dev.olivebakery.domain.dto.CommentDto;
import com.dev.olivebakery.domain.entity.Board;
import com.dev.olivebakery.domain.entity.Comment;
import com.dev.olivebakery.domain.entity.Member;
import com.dev.olivebakery.domain.enums.BoardType;
import com.dev.olivebakery.domain.enums.MemberRole;
import com.dev.olivebakery.exception.UserDefineException;
import com.dev.olivebakery.repository.BoardRepository;
import com.dev.olivebakery.repository.CommentRepository;
import com.dev.olivebakery.security.JwtProvider;
import com.dev.olivebakery.service.signService.SignService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final SignService signService;
    private final JwtProvider jwtProvider;

    public BoardService(BoardRepository boardRepository, CommentRepository commentRepository, SignService signService, JwtProvider jwtProvider) {
        this.boardRepository = boardRepository;
        this.commentRepository = commentRepository;
        this.signService = signService;
        this.jwtProvider = jwtProvider;
    }

    public Board findBoardById(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new UserDefineException("해당 번호의 게시글은 존재하지 않습니다."));
    }

    /**
     *
     * @param boardType: 게시판 or QnA
     * @param pageNum: 페이지 번호
     *
     */
    public Page<BoardDto.GetPosts> getPosts(BoardType boardType, int pageNum) {
        return boardRepository.getPosts(boardType,pageNum);
    }

    public List<BoardDto.GetPosts> getNoticePosts(){
        return boardRepository.getNoticePosts();
    }

    public BoardDto.GetPostDetails getPost(String bearerToken, Long boardId){
        String userId = jwtProvider.getUserEmailByToken(bearerToken);//사용자 아이디 받기
        List<MemberRole> roles = jwtProvider.getUserRolesByToken(bearerToken); //사용자 권한 받기

        BoardDto.GetPostDetails postDetails = boardRepository.getPostDetails(boardId);
        if(postDetails.getPosts().isSecret()) { // 게시물이 비밀글일 경우
            if (postDetails.getPosts().getUserId().equals(userId) || roles.contains(MemberRole.ADMIN))
                return postDetails; // 게시물의 작성자가 해당 열람요청자와 같거나, 관리자 권한을 가지고 있을경우 허용
            else
                throw new UserDefineException("해당 게시물을 열람하실 수 없습니다.");
        }
        return postDetails;
    }

    public void saveBoard(BoardDto.SavePost savePostDto) {
        Member member = signService.findById(savePostDto.getUserId());
        boardRepository.save(savePostDto.toEntity(member));
    }

    public void updateBoard(BoardDto.UpdatePost updatePostDto) {
        Board board = findBoardById(updatePostDto.getBoardId());
        board.updateBoard(updatePostDto);
        boardRepository.save(board);
    }

    public void deleteBoard(Long boardId) {
        Board board = findBoardById(boardId);
        boardRepository.delete(board);
    }

    public void saveComment(CommentDto.SaveComment comment) {
        signService.findById(comment.getUserId());  //validation check
        Board board = findBoardById(Long.valueOf(comment.getBoardId()));
        commentRepository.save(comment.toEntity(board));
    }

    public void updateComment(CommentDto.UpdateComment updateComment) {
        signService.findById(updateComment.getUserId());    //Validation Check
        Comment comment = commentRepository.findByUserIdAndUpdateTime(updateComment.getUserId(), updateComment.getUpdateTime())
                                                .orElseThrow(() -> new UserDefineException("해당 댓글이 존재하지 않습니다."));

        commentRepository.save(comment.update(updateComment.getContent()));
    }

    public void deleteComment(CommentDto.DeleteComment deleteComment) {
        signService.findById(deleteComment.getUserId());    //Validation Check
        Comment comment = commentRepository.findByUserIdAndUpdateTime(deleteComment.getUserId(), deleteComment.getUpdateTime())
                .orElseThrow(() -> new UserDefineException("해당 댓글이 존재하지 않습니다."));
        commentRepository.delete(comment);
    }
}