package ce.inu.ikta;

import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import com.google.api.services.vision.v1.model.TextAnnotation;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by 김광현 on 2018-05-06.
 */

public class CloudVisionAPI {
    private final String key = "AIzaSyDc1knQBG7FXcQfObsKwtPiAQUak6Dw8Co";
    private Vision vision = null;
    private static CloudVisionAPI cloudVisionAPI = null;
    private CloudVisionAPI() {
        Vision.Builder visionBuilder = new Vision.Builder(new NetHttpTransport(), new AndroidJsonFactory(), null);
        visionBuilder.setVisionRequestInitializer(new VisionRequestInitializer(key));
        vision = visionBuilder.build();
    }
    public static CloudVisionAPI Initializer() {
        if(cloudVisionAPI == null) {
            return new CloudVisionAPI();
        } else return cloudVisionAPI;
    }

    public String request(Bitmap bitmap) {
        //이미지로 변환
        Image image = BitmapToImage(bitmap);

        //OCR 설정
        Feature desiredFeature = new Feature();
        desiredFeature.setType("TEXT_DETECTION");
        AnnotateImageRequest request = new AnnotateImageRequest();
        request.setFeatures(Arrays.asList(desiredFeature));

        //이미지 설정
        request.setImage(image);

        //이미지 일괄 요청
        BatchAnnotateImagesRequest batchRequest = new BatchAnnotateImagesRequest();
        batchRequest.setRequests(Arrays.asList(request));

        BatchAnnotateImagesResponse batchResponse = null;
        try {
            batchResponse = vision.images().annotate(batchRequest).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(batchResponse == null) {
            return "BatchResponse Failed!";
        }else {
            final TextAnnotation text = batchResponse.getResponses()
                    .get(0).getFullTextAnnotation();
            if(text == null) {
                return "식을 인식하는데 실패했습니다.";
            } else {
                return text.getText();
            }
        }
    }
    private Image BitmapToImage(Bitmap bitmap) {
        Image image = new Image();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,90,byteArrayOutputStream);
        byte[] imageByte = byteArrayOutputStream.toByteArray();
        image.encodeContent(imageByte);
        return image;
    }

}