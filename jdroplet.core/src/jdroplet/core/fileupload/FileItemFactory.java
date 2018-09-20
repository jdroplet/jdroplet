package jdroplet.core.fileupload;

public interface FileItemFactory {

    FileItem createItem(
            String fieldName,
            String contentType,
            boolean isFormField,
            String fileName
            );
}