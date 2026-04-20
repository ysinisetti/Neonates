package com.neonates.service;

import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.sas.BlobSasPermission;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;
import com.neonates.Enum.DocumentCategory;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.OffsetDateTime;

@Slf4j
@Service
@ConditionalOnProperty(name = "azure.blob.enabled", havingValue = "true")
public class AzureBlobService {

    private final BlobServiceClient blobServiceClient;

    public AzureBlobService(
            @Value("${azure.storage.account-name}") String accountName) {

        String endpoint =  "https://" + accountName + ".blob.core.windows.net";
        log.info("Initializing Azure Blob Service with endpoint: {}", endpoint);
        System.out.println("Using Azure Credential...");
        this.blobServiceClient = new BlobServiceClientBuilder()
                .endpoint(endpoint)
                .credential(new DefaultAzureCredentialBuilder().build())
                .buildClient();
    }

    private BlobContainerClient getContainer(DocumentCategory category) {
        System.out.println("category.getContainerName() "+ category.getContainerName());
        return blobServiceClient
                .getBlobContainerClient(category.getContainerName());
    }

    public String upload(DocumentCategory category, String blobName, MultipartFile file) throws IOException {
        log.info("The hit is in upload in AzureBlob Service for blob: {}", blobName);
        try {
            BlobClient blobClient = getContainer(category)
                    .getBlobClient(blobName);

            blobClient.upload(file.getInputStream(), file.getSize(), true);
            return blobClient.getBlobUrl();
        } catch (Exception e) {
            log.error("Error uploading to Azure Blob: {}", e.getMessage(), e);
            throw new IOException("Failed to upload file to Azure", e);
        }
    }

    public void delete(DocumentCategory category, String blobName) {
        log.info("Deleting blob: {} from category: {}", blobName, category);
        getContainer(category)
                .getBlobClient(blobName)
                .delete();
    }

    public byte[] download(DocumentCategory category, String blobName) {
        log.info("Downloading blob: {} from category: {}", blobName, category);
        BlobClient blobClient = getContainer(category)
                .getBlobClient(blobName);

        if (!blobClient.exists()) {
            log.error("File not found in Azure Blob: {}", blobName);
            throw new RuntimeException("File not found in Azure Blob");
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        blobClient.download(outputStream);

        return outputStream.toByteArray();
    }

    public String generateUploadSasUrl(DocumentCategory category, String blobName) {
        log.info("Generating SAS URL for blob: {}", blobName);
        BlobClient blobClient = getContainer(category)
                .getBlobClient(blobName);

        BlobSasPermission permission = new BlobSasPermission()
                .setCreatePermission(true)
                .setWritePermission(true);

        BlobServiceSasSignatureValues values =
                new BlobServiceSasSignatureValues(
                        OffsetDateTime.now().plusMinutes(10),
                        permission
                );

        return blobClient.getBlobUrl() + "?" +
                blobClient.generateSas(values);
    }
}
