package com.example.watch_selling.dtos;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailInvoiceInformation {
    private String contact;

    private String invoiceNumber;

    private String ownerEmail;

    private String orderId;

    private String total;

    private String totalAfterTax;

    private LocalDateTime createDate;

    private String taxCode;

    public static String formatAsVND(Double value) {
        if (value == null) {
            return "";
        }

        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00 VND");
        return decimalFormat.format(value);
    }

    public String toHtmlBody() {
        StringBuilder sb = new StringBuilder();

        sb.append("<!DOCTYPE html>");
        sb.append("<html lang=\"en\">");
        sb.append("<head>");
        sb.append("<meta charset=\"UTF-8\">");
        sb.append("<title>Invoice Information</title>");
        sb.append("<style>");
        sb.append("th {text-align: left; font-weight: bold; padding-right: 20px;}");
        sb.append("</style>");
        sb.append("</head>");
        sb.append("<body>");
        sb.append("<h1>Invoice Information</h1>");
        sb.append("<table style=\"border-collapse: collapse;\">");
        sb.append("<tr>");
        sb.append("<th>Invoice Number</th>");
        sb.append("<td>" + invoiceNumber + "</td>");
        sb.append("</tr>");
        sb.append("<tr>");
        sb.append("<th>Owner Email</th>");
        sb.append("<td>" + ownerEmail + "</td>");
        sb.append("</tr>");
        sb.append("<tr>");
        sb.append("<th>Order ID</th>");
        sb.append("<td>" + orderId + "</td>");
        sb.append("</tr>");
        sb.append("<tr>");
        sb.append("<th>Total</th>");
        sb.append("<td>" + total + "</td>");
        sb.append("</tr>");
        sb.append("<tr>");
        sb.append("<th>Total After Tax</th>");
        sb.append("<td>" + totalAfterTax + "</td>");
        sb.append("</tr>");
        sb.append("<tr>");
        sb.append("<th>Create Date</th>");
        sb.append("<td>" + createDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + "</td>");
        sb.append("</tr>");
        sb.append("<tr>");
        sb.append("<th>Tax Code</th>");
        sb.append("<td>" + taxCode + "</td>");
        sb.append("</tr>");
        sb.append("</table>");
        sb.append("<br>");
        sb.append("<h3>Thank you for ordering with us!</h3>\n");
        sb.append("<br>");
        sb.append("<p>For support, please contact us at " + this.contact + "!</p>\n");
        sb.append("</body>");
        sb.append("</html>");

        return sb.toString();
    }
}
