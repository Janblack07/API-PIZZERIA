package com.example.apipizzeria.Configuration.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

@Service
public class CloudinaryStorageService implements CloudStorageService {

    private static final Set<String> ALLOWED = Set.of(
            MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE, "image/webp"
    );

    private final Cloudinary cloudinary;
    private final CloudinaryConfig props;

    public CloudinaryStorageService(Cloudinary cloudinary, CloudinaryConfig props) {
        this.cloudinary = cloudinary;
        this.props = props;
    }

    @Override
    public Map<String, Object> upload(MultipartFile file, String folder) {
        if (file == null || file.isEmpty()) throw new IllegalArgumentException("EMPTY_FILE");
        if (!ALLOWED.contains(file.getContentType())) throw new IllegalArgumentException("UNSUPPORTED_MIME");

        try {
            String target = (folder == null || folder.isBlank()) ? props.getDefaultFolder() : folder;
            return cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", target,
                            "resource_type", "image",
                            "overwrite", true,
                            "invalidate", true
                    )
            );
        } catch (IOException e) {
            throw new IllegalStateException("UPLOAD_FAILED", e);
        }
    }

    @Override
    public void delete(String publicId) {
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.asMap("invalidate", true));
        } catch (Exception e) {
            throw new IllegalStateException("DELETE_FAILED", e);
        }
    }

    @Override
    public String buildUrl(String publicId, Integer width, Integer height, boolean crop, boolean autoFormat) {
        Transformation t = new Transformation();
        if (width != null) t = t.width(width);
        if (height != null) t = t.height(height);
        if (crop) t = t.crop("fill");
        if (autoFormat) t = t.fetchFormat("auto").quality("auto");
        return cloudinary.url().transformation(t).secure(true).generate(publicId);
    }
}