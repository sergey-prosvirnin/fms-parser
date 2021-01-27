package com.prosvirnin.fmsparser.service.impl;

import com.prosvirnin.fmsparser.entity.FmsEntity;
import com.prosvirnin.fmsparser.repository.FmsRepository;
import com.prosvirnin.fmsparser.service.JobService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final FmsRepository fmsRepository;

    public static File newFileZipEntry(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());
        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();
        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }
        return destFile;
    }

    public static void downloadWithJavaNIO(String fileURL, String localFilename) throws IOException {
        URL url = new URL(fileURL);
        try (ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(localFilename);
             FileChannel fileChannel = fileOutputStream.getChannel()) {
            fileChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
            fileOutputStream.close();
        } catch (Exception e) {
            System.out.println("Problem with URL of archive " + e.fillInStackTrace());
        }
    }

    public static void convertFileFromCp1251ToUTF8(String inputFile, String outputFile) {
        int BYTE_ORDER_MARK = 1024;
        Charset windows1252 = Charset.forName("windows-1251");

        try (InputStream in = new FileInputStream(inputFile);
             Reader reader = new InputStreamReader(in, windows1252);
             OutputStream out = new FileOutputStream(outputFile);
             Writer writer = new OutputStreamWriter(out, StandardCharsets.UTF_8)) {
            writer.write(BYTE_ORDER_MARK);
            char[] buffer = new char[1024];
            int read;
            while ((read = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, read);
            }
        } catch (IOException ioe) {
            System.out.println("Problem with convert charset file from cp1251 to UTF-8" + ioe.getMessage());
        }
    }

    @Override
    public int getFmsListFileFromCSV(String fmsZipUrl) {
        try {
            //Download ZIP file
            downloadWithJavaNIO(fmsZipUrl, "test.zip");

            //Unpacked ZIP file
            final String fileZip = "test.zip";
            final File destDir = new File("fms/unzipTest");
            final byte[] buffer = new byte[1024];
            final ZipInputStream zis = new ZipInputStream(new FileInputStream(fileZip));
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                final File newFile = newFileZipEntry(destDir, zipEntry);
                if (zipEntry.isDirectory()) {
                    if (!newFile.isDirectory() && !newFile.mkdirs()) {
                        throw new IOException("Failed to create directory " + newFile);
                    }
                } else {
                    File parent = newFile.getParentFile();
                    if (!parent.isDirectory() && !parent.mkdirs()) {
                        throw new IOException("Failed to create directory " + parent);
                    }
                    final FileOutputStream fos = new FileOutputStream(newFile);
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                    fos.close();
                }
                zipEntry = zis.getNextEntry();
            }
            zis.closeEntry();
            zis.close();

            String outputFileFmsCsvFileList = "fms/unzipTest/fms_structure_10012018_by_utf-8.txt";
            convertFileFromCp1251ToUTF8("fms/unzipTest/fms_structure_10012018.txt", outputFileFmsCsvFileList);

            //Parse CSV file
            CSVParser parser = new CSVParser(
                    new FileReader(outputFileFmsCsvFileList),
                    CSVFormat.newFormat('|').withFirstRecordAsHeader());
            int counterForNewRecords = 0;
            for ( CSVRecord record : parser ) {
                FmsEntity fmsEntity = FmsEntity.builder()
                        .version(Long.valueOf(record.get(0)))
                        .name(record.get(1))
                        .build();
                fmsRepository.save(fmsEntity);
                counterForNewRecords++;
            }
            parser.close();

            return counterForNewRecords;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

}
