package inflearn_clone.springboot.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;

public class CommonFileUtil {

    @Value("${file.upload-dir}")
    private static String UPLOAD_DIR;
    // 단일 파일 업로드 메서드
    public static String uploadFile(MultipartFile file) throws IOException {
        File uploadDir = new File(UPLOAD_DIR);

        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        String originalFileName = file.getOriginalFilename();
        if (originalFileName != null && !originalFileName.isEmpty()) {
            String uniqueFileName = generateUniqueFileName(originalFileName);
            String fullPath = uploadDir.getPath() + File.separator + uniqueFileName;
            File destinationFile = new File(fullPath);
            file.transferTo(destinationFile);
            // 업로드된 파일의 경로 반환
            return fullPath;
        } else {
            throw new IllegalArgumentException("업로드할 파일이 없습니다.");
        }
    }

    // 다중 파일 업로드 메서드
    public static List<String> uploadFiles(List<MultipartFile> files) throws IOException {
        List<String> uploadedFilePaths = new ArrayList<>();
        File uploadDir = new File(UPLOAD_DIR);

        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        for (MultipartFile file : files) {
            String originalFileName = file.getOriginalFilename();
            if (originalFileName != null && !originalFileName.isEmpty()) {
                String uniqueFileName = generateUniqueFileName(originalFileName);
                String fullPath = uploadDir.getPath() + File.separator + uniqueFileName;
                File destinationFile = new File(fullPath);
                file.transferTo(destinationFile);
                uploadedFilePaths.add(fullPath);
            }
        }
        return uploadedFilePaths;
    }

    // 파일 삭제 메서드
    public static boolean deleteFile(String filePath) {
        if (filePath == null || filePath.isEmpty()) return false;

        File file = new File(filePath);
        return file.exists() && file.delete();
    }

    // 파일명 변경 메서드
    public static String renameFile(String oldFilePath, String newFileName) throws IOException {
        File oldFile = new File(oldFilePath);

        String uniqueNewFileName = generateUniqueFileName(newFileName);
        String newFullPath = UPLOAD_DIR + uniqueNewFileName;
        File newFile = new File(newFullPath);

        if (oldFile.exists() && oldFile.renameTo(newFile)) {
            return newFullPath; // 새 파일 경로 반환
        } else {
            throw new IOException("파일명을 변경할 수 없습니다.");
        }
    }

    // 고유 파일명 생성 메서드
    private static String generateUniqueFileName(String originalFileName) {
        String extension = "";
        int index = originalFileName.lastIndexOf(".");
        if (index > 0) {
            extension = originalFileName.substring(index);
        }
        return UUID.randomUUID().toString() + extension;
    }
}
