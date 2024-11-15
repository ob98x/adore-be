package com.userservice.global;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class FileManager {
    private final Storage storage;
    private static final String bucketName = "adore-bucket";

    public String uploadImage(MultipartFile file) throws IOException {
        log.info("[ Global - File Manager ]: 파일 업로드 요청");
        String uuid = UUID.randomUUID().toString();
        String ext = file.getContentType();

        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, uuid)
                .setContentType(ext)
                .build();
        Blob blob = storage.create(blobInfo, file.getBytes());
        log.info("[ Global - File Manager ]: 파일 업로드 완료 blob: {}", blob);
        return "https://storage.googleapis.com/" + bucketName + "/" + uuid;
    }
}
