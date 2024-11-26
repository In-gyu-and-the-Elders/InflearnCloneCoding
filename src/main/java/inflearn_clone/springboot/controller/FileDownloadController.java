package inflearn_clone.springboot.controller;

import inflearn_clone.springboot.utils.CommonFileUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Controller
@RequestMapping("/file")
public class FileDownloadController {
    @GetMapping("/download")
    public ResponseEntity<Resource> download(@RequestParam String fileName, HttpServletResponse response) throws IOException {
        // 파일 다운로드
        Resource fileResource = CommonFileUtil.downloadFile(fileName);

        // 파일 이름을 원래 이름으로 설정 (특수문자 처리 등)
        String originalFileName = fileResource.getFilename();

        // 응답 헤더 설정 (파일 다운로드를 유도)
        response.setContentType("application/octet-stream");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + originalFileName + "\"");

        // 파일 내용 스트림을 ResponseOutputStream으로 복사
        try (InputStream inputStream = fileResource.getInputStream();
             OutputStream outputStream = response.getOutputStream()) {

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
        } catch (IOException e) {
            throw new IOException("파일 다운로드 중 오류 발생: " + e.getMessage(), e);
        }

        return ResponseEntity.ok().build();
    }

}
