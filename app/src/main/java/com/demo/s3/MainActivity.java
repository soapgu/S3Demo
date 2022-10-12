package com.demo.s3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.minio.messages.Bucket;
import io.minio.messages.Item;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        MinioClient minioClient =
                MinioClient.builder()
                        .endpoint("http://172.16.30.171:30008")
                        .credentials("13RRHHTjgxRrBV0H", "4SGr4GFuSY8sarLCOvcU3B6m8OgHmFBK")
                        .build();
        TextView textView = this.findViewById(R.id.tv_message);
            //boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket("asiatrip").build());
            //Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder().bucket("test").build());

            //List<Bucket> bucketList = minioClient.listBuckets();
            /*
            String output = "";
            for (Result<Item> each : results
                 ) {
                output += each.get().objectName() + "  ";
            }

            textView.setText(  output );
             */
            new Thread( ()->{
                InputStream stream =
                        null;
                try {
                    stream = minioClient.getObject(
                            GetObjectArgs.builder().bucket("test").object("ppp.pdf").build());
                } catch (ErrorResponseException e) {
                    e.printStackTrace();
                } catch (InsufficientDataException e) {
                    e.printStackTrace();
                } catch (InternalException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (InvalidResponseException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (ServerException e) {
                    e.printStackTrace();
                } catch (XmlParserException e) {
                    e.printStackTrace();
                }
                File targetFile = new File(this.getExternalFilesDir(null), "ppp.pdf");
                try {
                    copy(stream,targetFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                runOnUiThread(()->textView.setText( "Copy OK!" ));
            } ).start();

    }

    public static void copy(InputStream inputStream, File output) throws IOException {
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(output);
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } finally {
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        }
    }
}