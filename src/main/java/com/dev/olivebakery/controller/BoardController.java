package com.dev.olivebakery.controller;

import com.dev.olivebakery.domain.dto.BoardDto;
import com.dev.olivebakery.domain.dto.CommentDto;
import com.dev.olivebakery.domain.enums.BoardType;
import com.dev.olivebakery.service.BoardService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * TODO
 * 1. 게시물 하나씩 가져오기.
 * 2. 게시물
 */

@RestController
@RequestMapping(value = "/olive/board")
public class BoardController {

    private BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @ApiOperation(value = "게시물 리스트", notes = "게시판과 QnA의 게시물들 불러오기", response = BoardDto.GetPosts.class, responseContainer = "list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "게시판인지 QnA인지", required = true),
            @ApiImplicitParam(name = "num", value = "페이지 번호", required = true)
    })
    @GetMapping("/{type}/page/{num}")
    public Page<BoardDto.GetPosts> getPosts(@PathVariable("type") BoardType boardType, @PathVariable("num") int pageNum) {
        return boardService.getPosts(boardType, pageNum);
    }

    @ApiOperation(value = "공지사항 리스트", notes = "게시판 공지사항 불러오기")
    @GetMapping("/notice")
    public List<BoardDto.GetPosts> getNoticePosts(){
        return boardService.getNoticePosts();
    }

    @ApiOperation(value = "게시물 정보", notes = "게시물(게시판, QnA) 하나 불러오기")
    @GetMapping("/id/{boardId}")
    public BoardDto.GetPostDetails getPostDetails(@RequestHeader(name = "Authorization") String token, @PathVariable("boardId") Long boardId){
        return boardService.getPost(token, boardId);
    }

    @ApiOperation(value = "게시물 저장하기")
    @PostMapping
    public void savePost(@RequestBody BoardDto.SavePost savePostDto) {
        boardService.saveBoard(savePostDto);
    }

    @ApiOperation("게시물 수정하기")
    @PutMapping
    public void updatePost(@RequestBody BoardDto.UpdatePost updatePostDto) {
        boardService.updateBoard(updatePostDto);
    }

    @ApiOperation("게시물 삭제하기")
    @DeleteMapping("/id/{boardId}")
    public void deletePost(@PathVariable("boardId") Long boardId) {
        boardService.deleteBoard(boardId);
    }

    @ApiOperation("댓글 저장하기")
    @PostMapping("/comment")
    public void saveComment(@RequestBody CommentDto.SaveComment comment){
        boardService.saveComment(comment);
    }

    @ApiOperation("댓글 수정하기")
    @PutMapping("/comment")
    public void updateComment(@RequestBody CommentDto.UpdateComment comment){
        boardService.updateComment(comment);
    }

    @ApiOperation("댓글 삭제하기")
    @DeleteMapping("/comment")
    public void deleteComment(@RequestBody CommentDto.DeleteComment comment){
        boardService.deleteComment(comment);
    }
}
