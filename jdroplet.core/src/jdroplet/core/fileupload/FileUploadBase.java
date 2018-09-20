package jdroplet.core.fileupload;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import jdroplet.core.HttpRequest;
import jdroplet.exceptions.fileupload.FileUploadException;
import jdroplet.exceptions.fileupload.InvalidContentTypeException;
import jdroplet.exceptions.fileupload.SizeLimitExceededException;
import jdroplet.exceptions.fileupload.UnknownSizeException;
import jdroplet.util.fileupload.ParameterParser;


public abstract class FileUploadBase {
	
	public static final String CONTENT_TYPE = "Content-type";
	public static final String CONTENT_DISPOSITION = "Content-disposition";
	public static final String FORM_DATA = "form-data";
	public static final String ATTACHMENT = "attachment";
	public static final String MULTIPART = "multipart/";
	public static final String MULTIPART_FORM_DATA = "multipart/form-data";
	public static final String MULTIPART_MIXED = "multipart/mixed";
	public static final int MAX_HEADER_SIZE = 1024;
	
	private long sizeMax = -1;
	private String headerEncoding;
		
	public abstract FileItemFactory getFileItemFactory();
	
	public long getSizeMax() {
		return sizeMax;
	}

	public void setSizeMax(long sizeMax) {
		this.sizeMax = sizeMax;
	}

	public String getHeaderEncoding() {
		return headerEncoding;
	}
	
	public void setHeaderEncoding(String headerEncoding) {
		this.headerEncoding = headerEncoding;
	}

	public static final boolean isMultipartContent(HttpRequest request) {
        String contentType = request.getContentType();
        if (contentType == null) {
            return false;
        }
        if (contentType.toLowerCase().startsWith(MULTIPART)) {
            return true;
        }
        return false;
    }
	
	protected Map<String, String> parseHeaders(String headerPart) {
        Map<String, String> headers = new HashMap<String, String>();
        char[] buffer = new char[MAX_HEADER_SIZE];
        boolean done = false;
        int j = 0;
        int i;
        String header, headerName, headerValue;
        try {
            while (!done) {
                i = 0;
                // Copy a single line of characters into the buffer,
                // omitting trailing CRLF.
                while (i < 2
                        || buffer[i - 2] != '\r' || buffer[i - 1] != '\n') {
                    buffer[i++] = headerPart.charAt(j++);
                }
                header = new String(buffer, 0, i - 2);
                if (header.equals("")) {
                    done = true;
                } else {
                    if (header.indexOf(':') == -1) {
                        // This header line is malformed, skip it.
                        continue;
                    }
                    headerName = header.substring(0, header.indexOf(':'))
                        .trim().toLowerCase();
                    headerValue =
                        header.substring(header.indexOf(':') + 1).trim();
                    if (getHeader(headers, headerName) != null) {
                        // More that one heder of that name exists,
                        // append to the list.
                        headers.put(headerName,
                                    getHeader(headers, headerName) + ','
                                        + headerValue);
                    } else {
                        headers.put(headerName, headerValue);
                    }
                }
            }
        } catch (IndexOutOfBoundsException e) {
            // Headers were malformed. continue with all that was
            // parsed.
        }
        return headers;
    }
		
	private byte[] getMultipartBoundary(String contentType) {
		ParameterParser parser = new ParameterParser();
        parser.setLowerCaseNames(true);
        Map<String, String> params = parser.parse(contentType, ';');
        String boundaryStr = (String) params.get("boundary");

        if (boundaryStr == null) {
            return null;
        }
        byte[] boundary;
        try {
            boundary = boundaryStr.getBytes("ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            boundary = boundaryStr.getBytes();
        }
        return boundary;
	}
	
	protected String getFileName(Map<String, String> headers) {
        String fileName = null;
        String cd = getHeader(headers, CONTENT_DISPOSITION);
        if (cd.startsWith(FORM_DATA) || cd.startsWith(ATTACHMENT)) {
            ParameterParser parser = new ParameterParser();
            parser.setLowerCaseNames(true);
            Map<String, String> params = parser.parse(cd, ';');
            if (params.containsKey("filename")) {
                fileName = (String) params.get("filename");
                if (fileName != null) {
                    fileName = fileName.trim();
                } else {
                    fileName = "";
                }
            }
        }
        return fileName;
    }
	
	protected String getFieldName(Map<String, String> headers) {
        String fieldName = null;
        String cd = getHeader(headers, CONTENT_DISPOSITION);
        if (cd != null && cd.startsWith(FORM_DATA)) {
            ParameterParser parser = new ParameterParser();
            parser.setLowerCaseNames(true);
            Map<String, String> params = parser.parse(cd, ';');
            fieldName = (String) params.get("name");
            if (fieldName != null) {
                fieldName = fieldName.trim();
            }
        }
        return fieldName;
    }
	
	protected final String getHeader(Map<String, String> headers, String name) {
		return (String) headers.get(name.toLowerCase());
	}
	
	public List<FileItem> parseRequest(HttpServletRequest request) throws FileUploadException {
		if (request == null) {
            throw new NullPointerException("context parameter");
        }

        ArrayList<FileItem> items = new ArrayList<FileItem>();
        String contentType = request.getContentType();

        if ((null == contentType) || (!contentType.toLowerCase().startsWith(MULTIPART))) {
            throw new InvalidContentTypeException(
                "the request doesn't contain a "
                + MULTIPART_FORM_DATA
                + " or "
                + MULTIPART_MIXED
                + " stream, content type header is "
                + contentType);
        }
        int requestSize = request.getContentLength();

        if (requestSize == -1) {
            throw new UnknownSizeException(
                "the request was rejected because its size is unknown");
        }

        if (sizeMax >= 0 && requestSize > sizeMax) {
            throw new SizeLimitExceededException(
                "the request was rejected because "
                + "its size exceeds allowed range");
        }

        try {
            byte[] boundary = this.getMultipartBoundary(contentType);
            if (boundary == null) {
                throw new FileUploadException(
                        "the request was rejected because "
                        + "no multipart boundary was found");
            }

            InputStream input = request.getInputStream();

            MultipartStream multi = new MultipartStream(input, boundary);
            multi.setHeaderEncoding(headerEncoding);

            boolean nextPart = multi.skipPreamble();
            while (nextPart) {
                Map<String, String> headers = parseHeaders(multi.readHeaders());
                
                String fieldName = getFieldName(headers);
                if (fieldName != null) {
                    String subContentType = getHeader(headers, CONTENT_TYPE);
                    if (subContentType != null && subContentType
                        .toLowerCase().startsWith(MULTIPART_MIXED)) {
                        // Multiple files.
                        byte[] subBoundary = this.getMultipartBoundary(subContentType);
                        multi.setBoundary(subBoundary);
                        boolean nextSubPart = multi.skipPreamble();
                        while (nextSubPart) {
                            headers = parseHeaders(multi.readHeaders());
                            if (getFileName(headers) != null) {
                                FileItem item = createItem(headers, false);
                                OutputStream os = item.getOutputStream();
                                try {
                                    multi.readBodyData(os);
                                } finally {
                                    os.close();
                                }
                                items.add(item);
                            } else {
                                // Ignore anything but files inside
                                // multipart/mixed.
                                multi.discardBodyData();
                            }
                            nextSubPart = multi.readBoundary();
                        }
                        multi.setBoundary(boundary);
                    } else {
                        FileItem item = createItem(headers, getFileName(headers) == null);
                        OutputStream os = item.getOutputStream();
                        try {
                            multi.readBodyData(os);
                        } finally {
                            os.close();
                        }
                        items.add(item);
                    }
                } else {
                    // Skip this part.
                    multi.discardBodyData();
                }
                nextPart = multi.readBoundary();
            }
        } catch (IOException e) {
            throw new FileUploadException(
                "Processing of " + MULTIPART_FORM_DATA
                    + " request failed. " + e.getMessage());
        }

        return items;
	}
		
	protected FileItem createItem(Map<String, String> headers, boolean isFormField)  {
		return getFileItemFactory().createItem(getFieldName(headers),
												getHeader(headers, CONTENT_TYPE),
												isFormField,
												getFileName(headers));
	}
}
