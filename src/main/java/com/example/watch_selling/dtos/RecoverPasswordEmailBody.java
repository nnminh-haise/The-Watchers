package com.example.watch_selling.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecoverPasswordEmailBody {
    private String contact;

    private String accountEmail;

    private String newPassword;

    public String toHtmlBody() {
        StringBuilder sb = new StringBuilder();

        sb.append("<!DOCTYPE html>");
        sb.append("<html lang=\"en\">");
        sb.append("<head>");
        sb.append("<meta charset=\"UTF-8\">");
        sb.append("<title>Password recovery</title>");
        sb.append("</head>");
        sb.append("<body>");
        sb.append("<h1>Password recovery</h1>");
        sb.append("<br>");
        sb.append("<p>Email: " + this.accountEmail + "</P>");
        sb.append("<p>New password: " + this.newPassword + "</P>");
        sb.append("<br>");
        sb.append("<p>For support, please contact us at " + this.contact + "!</p>\n");
        sb.append("</body>");
        sb.append("</html>");

        return sb.toString();
    }
}
