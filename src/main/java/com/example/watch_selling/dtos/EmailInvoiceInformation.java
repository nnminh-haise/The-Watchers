package com.example.watch_selling.dtos;

import java.text.DecimalFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailInvoiceInformation {
    private InvoiceDetail invoiceDetail;

    public static String formatAsVND(Double value) {
        if (value == null) {
            return "";
        }

        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00 VND");
        return decimalFormat.format(value);
    }

    public String toHtmlBody() {
        Double totalPrice = 0.0;

        StringBuilder sb = new StringBuilder();

        sb.append("<!DOCTYPE html>");
        sb.append("<html lang=\"en\">");
        sb.append("  <head>");
        sb.append("    <meta charset=\"UTF-8\" />");
        sb.append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />");
        sb.append("    <title>Invoice detail</title>");
        sb.append("    <style>");
        sb.append("      body {");
        sb.append("        font-family: Arial, sans-serif;");
        sb.append("        background-color: #f8f8f8;");
        sb.append("        margin: 0;");
        sb.append("        padding: 20px;");
        sb.append("      }");
        sb.append("      .invoice-container {");
        sb.append("        background-color: #fff;");
        sb.append("        padding: 20px;");
        sb.append("        border-radius: 8px;");
        sb.append("        box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);");
        sb.append("        max-width: 800px;");
        sb.append("        margin: auto;");
        sb.append("      }");
        sb.append("      .invoice-header {");
        sb.append("        text-align: center;");
        sb.append("        margin-bottom: 20px;");
        sb.append("      }");
        sb.append("      .invoice-header h1 {");
        sb.append("        margin: 0;");
        sb.append("        font-size: 24px;");
        sb.append("      }");
        sb.append("      .invoice-info,");
        sb.append("      .order-info,");
        sb.append("      .receiver-info {");
        sb.append("        margin-bottom: 20px;");
        sb.append("      }");
        sb.append("      .invoice-info table,");
        sb.append("      .order-info table,");
        sb.append("      .receiver-info table,");
        sb.append("      .order-details table {");
        sb.append("        width: 100%;");
        sb.append("        border-collapse: collapse;");
        sb.append("      }");
        sb.append("      .invoice-info th,");
        sb.append("      .order-info th,");
        sb.append("      .receiver-info th,");
        sb.append("      .order-details th,");
        sb.append("      .invoice-info td,");
        sb.append("      .order-info td,");
        sb.append("      .receiver-info td,");
        sb.append("      .order-details td {");
        sb.append("        padding: 8px;");
        sb.append("        border: 1px solid #ddd;");
        sb.append("        text-align: left;");
        sb.append("      }");
        sb.append("      .order-details {");
        sb.append("        margin-top: 20px;");
        sb.append("      }");
        sb.append("      .order-details th,");
        sb.append("      .order-details td {");
        sb.append("        text-align: center;");
        sb.append("      }");
        sb.append("      .order-details th {");
        sb.append("        background-color: #f0f0f0;");
        sb.append("      }");
        sb.append("      .order-details td.name-column {");
        sb.append("        text-align: left;");
        sb.append("      }");
        sb.append("      .total {");
        sb.append("        font-weight: bold;");
        sb.append("      }");
        sb.append("      .footer {");
        sb.append("        text-align: center;");
        sb.append("        margin-top: 20px;");
        sb.append("        padding-top: 20px;");
        sb.append("        border-top: 1px solid #ddd;");
        sb.append("      }");
        sb.append("      .footer p {");
        sb.append("        margin: 5px 0;");
        sb.append("      }");
        sb.append("    </style>");
        sb.append("  </head>");
        sb.append("  <body>");
        sb.append("    <div class=\"invoice-container\">");
        sb.append("      <div class=\"invoice-header\">");
        sb.append("        <h1>Invoice Detail</h1>");
        sb.append("        <p>Invoice Number: " + this.invoiceDetail.getInvoiceNumber() + "</p>");
        sb.append("        <p>Date: " + this.invoiceDetail.getCreateDate() + "</p>");
        sb.append("        <p>Tax code: " + this.invoiceDetail.getTaxCode() + "</p>");
        sb.append("      </div>");
        sb.append("      <div class=\"order-info\">");
        sb.append("        <h2>Order details</h2>");
        sb.append("        <table>");
        sb.append("          <tr>");
        sb.append("            <th>Order Date</th>");
        sb.append("            <td>" + this.invoiceDetail.getCreateDate() + "</td>");
        sb.append("          </tr>");
        sb.append("          <tr>");
        sb.append("            <th>Receiver Name</th>");
        sb.append("            <td>" + this.invoiceDetail.getReceiverName() + "</td>");
        sb.append("          </tr>");
        sb.append("          <tr>");
        sb.append("            <th>Receiver Address</th>");
        sb.append("            <td>" + this.invoiceDetail.getReceiverAddress() + "</td>");
        sb.append("          </tr>");
        sb.append("          <tr>");
        sb.append("            <th>Receiver Phone Number</th>");
        sb.append("            <td>" + this.invoiceDetail.getReceiverPhoneNumber() + "</td>");
        sb.append("          </tr>");
        sb.append("          <tr>");
        sb.append("            <th>Estimate Delivery Date</th>");
        sb.append("            <td>" + this.invoiceDetail.getEstimateDeliveryDate() + "</td>");
        sb.append("          </tr>");
        sb.append("        </table>");
        sb.append("      </div>");
        sb.append("      <div class=\"order-details\">");
        sb.append("        <h2>Products</h2>");
        sb.append("        <table>");
        sb.append("          <thead>");
        sb.append("            <tr>");
        sb.append("              <th>Name</th>");
        sb.append("              <th>Quantity</th>");
        sb.append("              <th>Price</th>");
        sb.append("              <th>Total</th>");
        sb.append("            </tr>");
        sb.append("          </thead>");
        sb.append("          <tbody>");
        for (var detail : this.invoiceDetail.getOrderDetails()) {
            Double totalPerProduct = detail.getPrice() * detail.getQuantity();
            totalPrice += totalPerProduct;

            sb.append(" <tr>");
            sb.append(" <td class=\"name-column\">" + detail.getWatch().getName() + "</td>");
            sb.append(" <td>" + detail.getQuantity() + "</td>");
            sb.append(" <td>" + formatAsVND(detail.getPrice()) + "</td>");
            sb.append(" <td>" + formatAsVND(totalPerProduct) + "</td>");
            sb.append(" </tr>");
        }
        sb.append("          </tbody>");
        sb.append("        </table>");
        sb.append("      </div>");
        sb.append("      <div class=\"total\">");
        sb.append("        <p>Total Price: " + formatAsVND(totalPrice) + "</p>");
        sb.append("        <p>Total Price After Tax (8%): " + formatAsVND(totalPrice * 1.08) + "</p>");
        sb.append("      </div>");
        sb.append("      <div class=\"payment\">");
        sb.append("        <h2>Payment</h2>");
        sb.append("        <p>");
        sb.append("          The payment information must follow below format for checking system.");
        sb.append("        </p>");
        sb.append("        <p><strong>[Invoice number] [Account email] [Used tax code]</strong></p>");
        sb.append("        <p>Example: 2024319000 nnminh.sam.1803@gmail.com 123421241</p>");
        sb.append("        <h3>Methods</h3>");
        sb.append("        <p><strong>Momo:</strong></p>");
        sb.append("        <p>Phone Number: (+84) 70 409 8399</p>");
        sb.append("        <p><strong>E-Banking:</strong></p>");
        sb.append("        <p><strong>Bank</strong>: Vietcombank</p>");
        sb.append("        <p><strong>Bank Number</strong>: 1016677697</p>");
        sb.append("        <p><strong>Bank</strong>: Agribank</p>");
        sb.append("        <p><strong>Bank Number</strong>: 1904220082018</p>");
        sb.append("        <p>");
        sb.append("          <strong");
        sb.append("            >Or you can contact the phone number or email below for your payment");
        sb.append("            support!</strong");
        sb.append("          >");
        sb.append("        </p>");
        sb.append("      </div>");
        sb.append("      <div class=\"footer\">");
        sb.append("        <p>Thank you for shopping with us!</p>");
        sb.append("        <p>If you have any questions, please contact us at:</p>");
        sb.append("        <p>Email: nnminh.sam.1803@gmail.com</p>");
        sb.append("        <p>Phone: (+84) 0704098399</p>");
        sb.append("      </div>");
        sb.append("    </div>");
        sb.append("  </body>");
        sb.append("</html>");

        return sb.toString();
    }
}
