package com.yh.web.service.impl;

import com.yh.web.dao.BoardDao;
import com.yh.web.dto.board.Board;
import com.yh.web.dto.board.BoardDetail;
import com.yh.web.dto.board.BoardFile;
import com.yh.web.service.BoardService;
import com.yh.web.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class BoardServiceImpl implements BoardService {
    private final BoardDao boardDao;
    private final FileService fileService;

    public BoardServiceImpl(BoardDao boardDao, FileService fileService) {
        log.info("BoardServiceImpl Init...");
        this.boardDao = boardDao;
        this.fileService = fileService;
    }

    /**
     * 해당 글번호가 존재하는지 조회
     * @param articleNo  글벊호
     * @return 존재하면 1 없으면 0
     */
    @Override
    public int searchArticleNo(int articleNo) {
        return boardDao.selectBoardCountByArticleNo(articleNo);
    }

    /**
     * @param field  검색타입
     * @param query  검색 하고자 하는 내용
     * @param page   페이지 숫자
     * @return       게시글 리스트
     */
    @Transactional(readOnly = true)
    @Override
    public Map<String,Object> getBoardList(String field, String query, int page) {
        Map<String,Object> map = new HashMap<>();
        map.put("field",field);
        map.put("query",query);
        int start = 1 + (page-1) * listNum;
        int end = page * listNum;
        map.put("start",start);
        map.put("end",end);

        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("list",boardDao.selectBoardList(map));
        resultMap.put("count",boardDao.selectBoardListCount(map));

        return resultMap;
    }

    @Transactional(readOnly = true)
    @Override
    public BoardDetail getBoardDetail(int articleNo, boolean isModify) {
        if(!isModify){
            boardDao.updateBoardHitByArticleNo(articleNo);
        }
        return boardDao.selectBoardDetailByArticleNo(articleNo);
    }

    /**
     * @return  다음에 삽입할 글번호
     */
    @Override
    public int getNextArticleNo() {
        return boardDao.selectNextSequence();
    }

    /**
     * @param articleNo  부모글번호
     * @return   부모 글번호에 대한 그룹번호
     */
    @Override
    public int getGrpNo(int articleNo) {
        return boardDao.selectGrpNoByArticleNo(articleNo);
    }

    /**
     * @param board  글정보
     * @param mf     파일
     */
    @Transactional
    @Override
    public int addBoard(Board board, MultipartFile mf) {
        int result;
        result = boardDao.insertBoard(board);//글 추가하기

        if(result != 0) { // 글을 성공적으로 추가했을경우
            if(mf.getSize() != 0) {  // 첨부된 파일이 있는지 확인
                String folderName = FileService.boardPath + File.separator + board.getArticleNo(); //  board/articleNo

                String safeName = fileService.upload(mf,folderName); //파일 업로드

                if(safeName != null){ //파일 업로드가 됐다면
                    BoardFile boardFile = new BoardFile();
                    boardFile.setArticleNo(board.getArticleNo());           //글번호
                    boardFile.setFileName(safeName);                         //변경된 파일이름
                    boardFile.setFileSize(mf.getSize());                    //파일 사이즈
                    boardFile.setOriginalFileName(mf.getOriginalFilename()); //원본 파일이름
                    boardDao.insertBoardFile(boardFile);   //파일 정보 db에 추가
                }
            }
        }
        return result;
    }

    @Transactional
    @Override
    public int modifyBoard(Board board, MultipartFile mf, boolean isDelete) {

        //먼저 글 수정하기
        int result = boardDao.updateBoard(board);
        if(result != 0){ //글 수정이 정상적으로 완료
            if(mf.getSize() != 0){ //파일 첨부된경우
                //먼저 기존 파일삭제 진행
                String folderName = FileService.boardPath + File.separator + board.getArticleNo();  //board/글번호
                String fileName = boardDao.selectBoardFileNameByArticleNo(board.getArticleNo()); //기존 파일 이름 얻기
                if(fileName != null){ //기존파일이 있다면 삭제하고 수정
                    boolean delete = fileService.deleteFile(folderName, fileName);
                    if(delete){ //삭제가 되었으면
                        String safeName =  fileService.upload(mf,folderName);  //새로운 파일 업로드

                        BoardFile boardFile = new BoardFile();
                        boardFile.setArticleNo(board.getArticleNo());
                        boardFile.setFileName(safeName);
                        boardFile.setFileSize(mf.getSize());
                        boardFile.setOriginalFileName(mf.getOriginalFilename());
                        boardDao.updateBoardFile(boardFile);  //파일정보 업데이트
                    }
                }else{ // 기존파일이 없는경우 새로 추가
                    String safeName =  fileService.upload(mf,folderName);  //새로운 파일 업로드
                    BoardFile boardFile = new BoardFile();
                    boardFile.setArticleNo(board.getArticleNo());
                    boardFile.setFileName(safeName);
                    boardFile.setFileSize(mf.getSize());
                    boardFile.setOriginalFileName(mf.getOriginalFilename());
                    boardDao.insertBoardFile(boardFile);  //파일정보 추가
                }
            }else if(isDelete){ //수정 없이 파일 삭제 인경우
                String fileName = boardDao.selectBoardFileNameByArticleNo(board.getArticleNo());
                String folderName = FileService.boardPath + File.separator + board.getArticleNo();  //board/글번호
                boolean delete = fileService.deleteFile(folderName, fileName);
                if(delete) { //삭제가 되었으면 db값 삭제
                    boardDao.deleteBoardFileByArticleNo(board.getArticleNo());
                }
            }
        }
        return result;
    }

    /**
     * @param articleNo  글번호
     * @return           정상적으로 삭제 했으면 1 자식을 가지고 있다면 44 실패 했다면 0
     */
    @Transactional
    @Override
    public int deleteBoard(int articleNo) {
        int child = boardDao.selectChildCount(articleNo);
        if(child != 0)
            return 44;
        if(fileService.deleteFolder(FileService.boardPath + File.separator + articleNo)) //폴더 삭제가 된경우
            return boardDao.deleteBoardByArticleNo(articleNo);  //글 삭제 시도
        return 0;
    }

    /**
     * @param articleNo 글번호
     * @param userName  유저 아이디
     * @return 추천수를 올렸으면 1 이미 올린 상태면 0
     */
    @Transactional
    @Override
    public int upRecommend(int articleNo, String userName) {
        //isAlreadyExistsID?
        Map<String, Object> map = new HashMap<>();
        map.put("articleNo", articleNo);
        map.put("id", userName);
        boolean isExists = boardDao.isAlreadyExistsID(map);
        
        if(isExists){ //이미 추천한 아이디 이므로 0리턴
            return 0;
        }else{ //추천수 올리고 결과 리턴
            return boardDao.insertRecommend(map);
        }
    }

    @Override
    public List<String> selectTitleLastFive() {
        return boardDao.selectTitleLastFive();
    }

    @Override
    public int updateBoardPubByArticleNo(int articleNo) {
        return boardDao.updateBoardPubByArticleNo(articleNo);
    }
}
