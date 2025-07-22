package com.Polimeras.Service;

import com.itextpdf.html2pdf.HtmlConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class PdfService {
    public byte[] createHtmlPDF(String htmlContent) {
        String pdfPath = "D:\\Harinathreddy\\My Projects\\Ecommerce\\Polimeras-Grocery\\src\\main\\resources\\uploads\\order.pdf";
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            HtmlConverter.convertToPdf(htmlContent, outputStream);
            try (FileOutputStream fileOutputStream = new FileOutputStream(pdfPath)) {
                outputStream.writeTo(fileOutputStream);
            }
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("PDF generation failed", e);
        }
    }
}
