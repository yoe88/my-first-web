package com.yh.web.service.impl;

import com.yh.web.Utils;
import com.yh.web.dao.BoardDao;
import com.yh.web.dto.board.Board;
import com.yh.web.dto.board.BoardDetail;
import com.yh.web.dto.board.BoardFile;
import com.yh.web.dto.board.BoardList;
import com.yh.web.service.BoardService;
import com.yh.web.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
     * @param articleNo  글번호
     * @return 존재하면 1 없으면 0
     */
    @Override
    public int findArticleNo(long articleNo) {
        return boardDao.findBoardByArticleNo(articleNo);
    }

    /**
     * @param field  검색타입
     * @param query  검색 하고자 하는 내용
     * @param page   페이지 숫자
     * @return       게시글 리스트
     */
    @Override
    public List<BoardList> getBoardList(String field, String query, long page) {
        Map<String,Object> map = new HashMap<>();
        map.put("field",field);
        map.put("query",query);
        long start = 1 + (page-1) * listNum;
        long end = page * listNum;
        map.put("start",start);
        map.put("end",end);

        return boardDao.selectBoardList(map);
    }

    /**
     * @param field  필드 타입
     * @param query  검색 내용
     * @return       검색된 글 리스트
     */
    @Override
    public long getBoardListCount(String field, String query) {
        Map<String,Object> map = new HashMap<>();
        map.put("field",field);
        map.put("query",query);

        return boardDao.selectBoardListCount(map);
    }

    /**
     * @param articleNo  번호
     * @param isModify   수정 하려고 하는지
     * @return          번호에 해당하는 정보
     */
    @Transactional(readOnly = true)
    @Override
    public BoardDetail getBoardDetail(long articleNo, boolean isModify) {
        if(!isModify){
            boardDao.updateBoardHitByArticleNo(articleNo);
        }
        BoardDetail boardDetail = boardDao.selectBoardDetailByArticleNo(articleNo);
        if(boardDetail != null){
            if(boardDetail.getProfileImage() != null){
                String profileImageName = Utils.urlEncode(boardDetail.getProfileImage());
                boardDetail.setProfileImage(profileImageName);
            }
            if(boardDetail.getFileName() != null) {
                String fileName = Utils.urlEncode(boardDetail.getFileName());
                boardDetail.setFileName(fileName);
                String encodeOriginalFileName = Utils.urlEncode(boardDetail.getOriginalFileName());
                boardDetail.setEncodeOriginalFileName(encodeOriginalFileName);
            }
        }
        return boardDetail;
    }

    /**
     * @param board  추가 할 글정보
     * @param mf     추가 할 단일 파일
     */
    @Transactional
    @Override
    public int addBoard(Board board, MultipartFile mf) {
        int result;

        long articleNo = boardDao.selectNextSequence();  //다음 글번호 얻기, 시퀀스
        board.setArticleNo(articleNo);  //글번호 설정

        if(board.getParent() != 0){   //답글 쓰기 인 경우,  부모글번호가 존재할때
            long grpNo = boardDao.selectGrpNoByArticleNo(board.getParent());  //부모 글번호에 대한 그룹번호 얻어오기
            board.setGrpNo(grpNo);  //그룹번호 설정
        }else {
            board.setGrpNo(articleNo);  // 답글쓰기가 아닌경우 글번호와 동일하게 설정
        }
        result = boardDao.insertBoard(board);//글 추가하기

        if(result != 0) { // 글을 성공적으로 추가했을경우
            if(mf.getSize() != 0) {  // 첨부된 파일이 있는지 확인
                String folderPath = FileService.boardPath + SE + board.getArticleNo(); //  c/upload/board/articleNo

                String safeName = fileService.upload(mf,folderPath); //파일 업로드

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

    /**
     * @param board     수정할 글 정보
     * @param mf         파일
     * @param isDelete   파일을 삭제하는건지?
     */
    @Transactional
    @Override
    public int modifyBoard(Board board, MultipartFile mf, boolean isDelete) {
        //먼저 글 수정하기
        int result = boardDao.updateBoard(board);
        if(result != 0){ //글 수정이 정상적으로 완료
            String fileName = boardDao.selectBoardFileNameByArticleNo(board.getArticleNo()); //기존 파일 이름 얻기
            // C:\ upload\board\no
            String folderPath = FileService.boardPath + SE + board.getArticleNo();
            // C:\ upload\board\no\fileName
            String filePath = folderPath + SE + fileName; //파일 경로
            
            if(mf.getSize() != 0) { //파일 첨부된경우
                //새로운 파일 업로드
                BoardFile boardFile = new BoardFile();
                boardFile.setArticleNo(board.getArticleNo());
                String safeName = fileService.upload(mf, folderPath);
                boardFile.setFileName(safeName);
                boardFile.setFileSize(mf.getSize());
                boardFile.setOriginalFileName(mf.getOriginalFilename());

                if (fileName != null) { //기존 파일이 있다면 삭제
                    boolean delete = fileService.deleteFile(filePath);
                    if (delete) { //삭제가 되었으면
                        boardDao.updateBoardFile(boardFile);  //파일정보 업데이트
                    }
                } else { // 기존파일이 없는경우 새로 추가
                    boardDao.insertBoardFile(boardFile);  //파일정보 추가
                }
            }

            if(isDelete){ //파일 삭제 인경우
                boolean delete = fileService.deleteFile(filePath);
                if(delete) { //삭제 됐으면 db값 삭제
                    boardDao.deleteBoardFileByArticleNo(board.getArticleNo());
                }
            }
        }
        return result;
    }

    /**
     * @param articleNo  글번호
     * @return          정상적으로 삭제 했으면 1 자식을 가지고 있다면 44 실패 했다면 0
     */
    @Transactional
    @Override
    public int deleteBoard(long articleNo) {
        long child = boardDao.selectChildCount(articleNo);
        if(child != 0)
            return 44;
        if(fileService.deleteFolder(FileService.boardPath + SE + articleNo)) //폴더 삭제가 된경우
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
    public int upRecommend(long articleNo, String userName) {
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

    /**
     * @param articleNo   글 번호
     * @param pub         번호에 해당하는 글 공개 수정
     * @return            수정 됐으면 1 실패 0
     */
    @Override
    public boolean updateBoardPubByArticleNo(long articleNo, Integer pub) {
        Map<String, Object> map = new HashMap<>();
        map.put("articleNo",articleNo);
        map.put("pub",pub);

        int result = boardDao.updateBoardPubByArticleNo(map);
        return result == 1;
    }
}
